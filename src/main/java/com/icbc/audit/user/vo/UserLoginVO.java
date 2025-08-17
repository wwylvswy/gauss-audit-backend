package com.icbc.audit.user.vo;

import lombok.Data;
import java.util.List;

@Data
public class UserLoginVO {
    private String avatar;
    private String account;
    private String nickname;
    private List<String> roles;
    private List<String> permissions;
    private String accessToken;
    private String refreshToken;
    private String expires;
}
