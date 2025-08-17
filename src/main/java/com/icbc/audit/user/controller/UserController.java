package com.icbc.audit.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.icbc.audit.user.dao.LoginDTO;
import com.icbc.audit.user.dao.UserDTO;
import com.icbc.audit.user.service.UserService;
import com.icbc.audit.user.vo.UserLoginVO;
import com.icbc.audit.user.vo.UserVO;
import com.icbc.audit.web.ApiResponse;
import com.icbc.audit.web.NewApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(
            summary = "用户登录",
            description = "使用账号和密码进行用户登录",
            tags = {"用户管理"}
    )
    public NewApiResponse<UserLoginVO> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("LoginDTO: " + loginDTO.getAccount() + " " + loginDTO.getPassword());
        UserLoginVO data = userService.login(loginDTO);

        return NewApiResponse.ok(data);
    }

    @Operation(
            summary = "分页获取用户",           //
            description = "分页获取用户", // 详细描述
            tags = {"用户管理"}               // 分组标签
    )
    @GetMapping("/list")
    public ApiResponse<IPage<UserVO>> listUsers(UserDTO dto) {
        return ApiResponse.ok(userService.listUsers(dto));
    }

    @GetMapping("/getUserInfo/{id}")
    @Operation(
            summary = "获取用户信息",
            description = "根据用户ID查询详细信息",
            tags = {"用户管理"}
    )
    public ApiResponse<UserVO> getUser(@PathVariable Long id) {
        return ApiResponse.ok(userService.getUser(id));
    }

    @PostMapping("/addUser")
    @Operation(
            summary = "添加新用户",
            description = "添加一个新的用户",
            tags = {"用户管理"}
    )
    public ApiResponse<Void> addUser(@RequestBody UserDTO dto) {
        userService.addUser(dto);
        return ApiResponse.ok();
    }

    @PutMapping("/updateUserInfo/{id}")
    @Operation(
            summary = "更新用户信息",
            description = "根据用户ID更新用户信息",
            tags = {"用户管理"}
    )
    public ApiResponse<Void> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        dto.setId(id);
        userService.updateUser(dto);
        return ApiResponse.ok();
    }

    @DeleteMapping("/deleteUser/{id}")
    @Operation(
            summary = "删除用户",
            description = "根据用户ID删除用户",
            tags = {"用户管理"}
    )
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.ok();
    }

    @PatchMapping("/enable/{id}")
    @Operation(
            summary = "启用/禁用用户",
            description = "根据用户ID启用或禁用用户",
            tags = {"用户管理"}
    )
    public ApiResponse<Void> enableUser(@PathVariable Long id, @RequestParam Boolean enabled) {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setEnabled(enabled);
        userService.enableUser(dto);
        return ApiResponse.ok();
    }
}
