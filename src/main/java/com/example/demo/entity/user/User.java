package com.example.demo.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
@ToString(of = {"userIdx","userName","password","url","authority"})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;
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
    @NotNull
    @ColumnDefault("'N'")
    private String delYn;
    private String url;
    @NotNull
    private String authority;
    LocalDateTime lastLoginDate;
    @OneToMany(mappedBy = "user")
    private List<UserLog> userLogs = new ArrayList<>();

    @Builder
    public User(String userName, String password, String userId, String delYn, String url, String authority) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.delYn = delYn;
        this.url = url;
        this.authority = authority;
    }

    public void updateLastLoginDate(LocalDateTime now) {
        this.lastLoginDate = now;
    }
}
