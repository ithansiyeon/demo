package com.example.demo.springsecurity;

import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserLog;
import com.example.demo.repository.user.UserLogRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new SecurityUser(user);
    }

    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void updateLastLoginDate(String loginId) {
        User user = userRepository.findByUserId(loginId);
        user.updateLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
    }

    public void insertUserLog(String ip, boolean isSuccess, String loginId) {
        String isLogin = isSuccess ? "Y" : "N";
        User user = userRepository.findByUserId(loginId);
        UserLog userLog = UserLog.builder().ip(ip).isLoginSuccess(isLogin).user(user).build();
        userLogRepository.save(userLog);
    }
}

