package com.icbc.audit.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icbc.audit.user.dao.LoginDTO;
import com.icbc.audit.user.dao.RefreshDTO;
import com.icbc.audit.user.dao.UserDTO;
import com.icbc.audit.user.dao.UserPostDTO;
import com.icbc.audit.user.vo.RefreshTokenVO;
import com.icbc.audit.user.vo.UserLoginVO;
import com.icbc.audit.user.vo.UserVO;

public interface UserService {
    IPage<UserVO> listUsers(UserDTO dto);

    UserVO getUser(Long id);

    void addUser(UserPostDTO userPostDTO);

    void updateUser(UserPostDTO userPostDTO);

    void deleteUser(Long id);

    void enableUser(UserDTO dto);

    UserLoginVO login(LoginDTO loginDTO);

    RefreshTokenVO refreshToken(RefreshDTO refreshDTO);
}
