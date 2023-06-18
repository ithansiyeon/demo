package com.example.demo.entity.user;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLogIdx;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    private String ip;
    private String isLoginSuccess;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User user;


    @Builder
    public UserLog(String ip, String isLoginSuccess, User user) {
        this.ip = ip;
        this.isLoginSuccess = isLoginSuccess;
        this.user = user;
    }
}
