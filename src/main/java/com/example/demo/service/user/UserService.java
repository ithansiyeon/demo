package com.example.demo.service.user;

import com.example.demo.entity.user.User;
import com.example.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    public User getUserByUserId(String loginId) {
        return userRepository.findByUserId(loginId);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateLastLoginDate(String loginId) {
        User user = userRepository.findByUserId(loginId);
        user.updateLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
    }

    public String getUrlByUserId(String loginId) {
        return userRepository.findByUserId(loginId).getUrl();
    }
}
