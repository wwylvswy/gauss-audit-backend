package com.icbc.audit.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String account;
    private String password;
    private String department;
    private String phone;
    private String email;
    private String creator;
    private LocalDateTime createTime;
    private Boolean enabled;
}
