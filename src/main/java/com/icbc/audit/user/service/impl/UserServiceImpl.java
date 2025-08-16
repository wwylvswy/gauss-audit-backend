package com.icbc.audit.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icbc.audit.user.dao.UserDTO;
import com.icbc.audit.user.entity.UserEntity;
import com.icbc.audit.user.mapper.UserMapper;
import com.icbc.audit.user.service.UserService;
import com.icbc.audit.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

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
    public void addUser(UserDTO dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO dto) {
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(dto, user);
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