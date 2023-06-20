package com.example.demo.repository.user;

import com.example.demo.entity.user.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.demo.entity.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    public final JPAQueryFactory query;

    @Override
    public Page<User> findUserCustom(PageRequest pageRequest) {
        JPAQuery<User> userQuery = query.select(user)
                .from(user)
                .offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());
        for(Sort.Order o : pageRequest.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(user.getType(), user.getMetadata());
            userQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<User> content = userQuery.fetch();

        JPAQuery<Long> countQuery = query.select(user.count()).from(user);

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }
}
