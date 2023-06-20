package com.example.demo.repository.user;

import com.example.demo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserRepositoryCustom {
    Page<User> findUserCustom(PageRequest pageRequest);
}
