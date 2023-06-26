package com.example.demo.entity.user;

import com.example.demo.entity.menu.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.ibatis.annotations.Many;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Table(name = "users")
@ToString(of = {"id","userName","password","url","authority"})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String userId;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    @ColumnDefault("'N'")
    private String isDel;
    private String url;
    @NotNull
    private String authority;
    LocalDateTime lastLoginDate;
    @OneToMany(mappedBy = "user")
    private List<UserLog> userLogs = new ArrayList<>();
    private String phoneNumber;
    private String isAlert;
    @OneToMany(mappedBy = "regUserIdx")
    private List<Menu> menus1;
    @OneToMany(mappedBy = "modifyUserIdx")
    private List<Menu> menus2;

    @Builder
    public User(String userName, String password, String userId, String isDel, String url, String authority, String phoneNumber, String isAlert) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.isDel = isDel;
        this.url = url;
        this.authority = authority;
        this.phoneNumber = phoneNumber;
        this.isAlert = isAlert;
    }

    public void updateLastLoginDate(LocalDateTime now) {
        this.lastLoginDate = now;
    }
}
