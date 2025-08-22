package com.icbc.audit.assits.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 对应文档 5.3.1 实体 "用户(User)"
 */
@Entity
@Table(name = "app_user") // "user" is a reserved keyword in many SQL dialects
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // In a real app, this should be hashed

    @Column(nullable = false)
    private String role; // e.g., "ADMIN", "USER"
}