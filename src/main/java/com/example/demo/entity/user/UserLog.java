package com.example.demo.entity.user;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_log_idx")
    private Long id;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    private String ip;
    private String isLoginSuccess;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User user;
    @ColumnDefault("'N'")
    private String autoYn;
    private String reasonType;
    private String otherReason;


    @Builder
    public UserLog(String ip, String isLoginSuccess, User user, String autoYn, String reasonType, String otherReason) {
        this.ip = ip;
        this.isLoginSuccess = isLoginSuccess;
        this.user = user;
        this.autoYn = autoYn;
        this.reasonType = reasonType;
        this.otherReason = otherReason;
    }
}
