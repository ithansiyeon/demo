package com.example.demo.repository.user;

import com.example.demo.entity.user.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog,Long> {
}
