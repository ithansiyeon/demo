package com.example.demo.service.user;

import com.example.demo.dto.user.UserLogDto;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserLog;
import com.example.demo.repository.user.UserLogRepository;
import com.example.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;

    public User getUserByUserId(String loginId) {
        return userRepository.findByUserId(loginId);
    }

    public List<UserLogDto> getUserLog(String loginId) {
        return userLogRepository.findUserLogByUserId(loginId).stream().map( o -> mapper.map(o,UserLogDto.class)).collect(Collectors.toList());
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateLastLoginDate(String loginId) {
        User user = userRepository.findByUserId(loginId);
        user.updateLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
    }

    public void insertUserLog(UserLog userLog) {
        userLogRepository.save(userLog);
    }
}
