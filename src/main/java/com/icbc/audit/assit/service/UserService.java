package com.icbc.audit.assit.service;

// ... (代码与之前版本完全相同)

import com.icbc.audit.assit.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}