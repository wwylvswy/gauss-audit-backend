package com.icbc.audit.user.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class RefreshDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String refreshToken;
}
