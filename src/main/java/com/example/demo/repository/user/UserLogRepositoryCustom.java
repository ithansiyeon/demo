package com.example.demo.repository.user;

import com.example.demo.entity.user.UserLog;

import java.util.List;

public interface UserLogRepositoryCustom {
    List<UserLog> findUserLogByUserId(String userId);
}
