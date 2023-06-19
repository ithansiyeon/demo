package com.example.demo.repository.user;

import com.example.demo.entity.user.UserLog;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.entity.user.QUser.user;
import static com.example.demo.entity.user.QUserLog.userLog;

@RequiredArgsConstructor
public class UserLogRepositoryImpl {
    private final JPAQueryFactory query;
    public List<UserLog> findUserLogByUserId(String userId) {
        return query.select(userLog)
                .from(userLog)
                .join(userLog.user, user)
                .where(user.userId.eq(userId)).orderBy(userLog.registerDate.desc()).fetch();
    }
}
