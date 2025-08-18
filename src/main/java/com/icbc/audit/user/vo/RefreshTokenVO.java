package com.icbc.audit.user.vo;

import lombok.Data;

@Data
public class RefreshTokenVO {
    private String accessToken;
    private String refreshToken;
    private String expires;
}
