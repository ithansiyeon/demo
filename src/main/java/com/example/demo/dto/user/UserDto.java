package com.example.demo.dto.user;

import com.example.demo.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String userName;
    private String userId;
    private String password;
    private String phoneNumber;
    private String authority;
    private String isAlert;

    public UserDto(Long id, String userName, String userId, String phoneNumber, String authority, String isAlert) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
        this.isAlert = isAlert;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.authority = user.getAuthority();
        this.isAlert = user.getIsAlert();
    }
}
