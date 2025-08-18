package com.icbc.audit.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icbc.audit.user.dao.LoginDTO;
import com.icbc.audit.user.dao.RefreshDTO;
import com.icbc.audit.user.dao.UserDTO;
import com.icbc.audit.user.dao.UserPostDTO;
import com.icbc.audit.user.entity.UserEntity;
import com.icbc.audit.user.mapper.UserMapper;
import com.icbc.audit.user.service.UserService;
import com.icbc.audit.user.vo.RefreshTokenVO;
import com.icbc.audit.user.vo.UserLoginVO;
import com.icbc.audit.user.vo.UserVO;
import com.icbc.audit.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final List<String> allPermissions = Arrays.asList("*:*:*");
    private final List<String> commonPermissions = Arrays.asList(
            "permission:btn:add", "permission:btn:edit"
    );

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public UserLoginVO login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getAccount, account).eq(UserEntity::getEnabled, 1);

        UserEntity user = userMapper.selectOne(wrapper);
//        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
//            throw new RuntimeException("用户名或密码错误");
//        }

        if (user == null) {
            throw new RuntimeException("用户不存在或未启用");
        }

        // TODO 先使用简单的校验，后续改为加密存储
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        List<String> roles = userMapper.selectRolesByAccount(account);
        if (roles.isEmpty()) {
            throw new RuntimeException("用户未分配角色");
        }

        // 构建权限
        List<String> permissions = roles.contains("admin") ? allPermissions : commonPermissions;

        // 生成 Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("account", account);
        claims.put("roles", roles);
        claims.put("permissions", permissions);

//        System.out.println("generating token for account: " + account);
        String accessToken = jwtUtil.generateAccessToken(claims);

//        System.out.println("generating refresh token for account: " + account);
        String refreshToken = jwtUtil.generateRefreshToken(account);

        // 过期时间
//        System.out.println("Expires in 1 hour for account: " + account);
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(36000);

//        System.out.println("Expire time: " + expireTime);
        String expires = expireTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        UserLoginVO vo = new UserLoginVO();
        vo.setAvatar(user.getAvatar());
        vo.setAccount(user.getAccount());
        vo.setNickname(user.getNickname());
        vo.setRoles(roles);
        vo.setPermissions(permissions);
        vo.setAccessToken(accessToken);
        vo.setRefreshToken(refreshToken);
        vo.setExpires(expires);

        return vo;
    }

    @Override
    public RefreshTokenVO refreshToken(RefreshDTO refreshDTO) {
        String refreshToken = refreshDTO.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("refreshToken不能为空");
        }
        Claims claims;
        try {
            claims = jwtUtil.getClaimsFromToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("refreshToken无效或已过期");
        }
        String account = claims.getSubject();
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getAccount, account).eq(UserEntity::getEnabled, 1);
        UserEntity user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在或未启用");
        }
        List<String> roles = userMapper.selectRolesByAccount(account);
        List<String> permissions = roles.contains("admin") ? allPermissions : commonPermissions;
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("account", account);
        accessClaims.put("roles", roles);
        accessClaims.put("permissions", permissions);
        String newAccessToken = jwtUtil.generateAccessToken(accessClaims);
        String newRefreshToken = jwtUtil.generateRefreshToken(account);
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(36000);
        String expires = expireTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        RefreshTokenVO vo = new RefreshTokenVO();
        vo.setAccessToken(newAccessToken);
        vo.setRefreshToken(newRefreshToken);
        vo.setExpires(expires);
        return vo;
    }

    @Override
    public IPage<UserVO> listUsers(UserDTO dto) {
        Page<UserEntity> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
//        if (dto.getAccount() != null) {
//            wrapper.like(UserEntity::getAccount, dto.getAccount());
//        }
//        if (dto.getDepartment() != null) {
//            wrapper.eq(UserEntity::getDepartment, dto.getDepartment());
//        }
        wrapper.eq(UserEntity::getDeleted, 0);

        Page<UserEntity> userPage = userMapper.selectPage(page, wrapper);
        return userPage.convert(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        });
    }

    @Override
    public UserVO getUser(Long id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserPostDTO dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserPostDTO userPostDTO) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userPostDTO, user);
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        int res = userMapper.deleteById(id);
        if (res == 0) {
            throw new RuntimeException("User not found or already deleted");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableUser(UserDTO dto) {
        UserEntity user = new UserEntity();
        user.setId(dto.getId());
        user.setEnabled(dto.getEnabled());
        userMapper.updateById(user);
    }
}