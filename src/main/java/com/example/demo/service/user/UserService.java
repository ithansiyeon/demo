package com.example.demo.service.user;

import com.example.demo.entity.user.User;
import com.example.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getUserByUserId(String loginId) {
        return userRepository.findByUserId(loginId);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
