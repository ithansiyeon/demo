package com.example.demo.springsecurity;

import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserLog;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userService.getUserByUserId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new SecurityUser(user);
    }

    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void updateLastLoginDate(String loginId) {
        userService.updateLastLoginDate(loginId);
    }

    public void insertUserLog(String ip, boolean isSuccess, String loginId) {
        String isLogin = isSuccess ? "Y" : "N";
        User user = userService.getUserByUserId(loginId);
        UserLog userLog = UserLog.builder().ip(ip).isLoginSuccess(isLogin).user(user).build();
        userService.insertUserLog(userLog);
    }
}

