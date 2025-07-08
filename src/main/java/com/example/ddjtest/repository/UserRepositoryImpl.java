package com.example.ddjtest.repository;

import com.example.ddjtest.entity.QUser;
import com.example.ddjtest.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 이름 가나다순 정렬
    @Override
    public Page<User> findAllOrderByNameAsc(Pageable pageable) {
        QUser user = QUser.user;

        List<User> users = queryFactory
                .selectFrom(user)
                .orderBy(user.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(user.count())
                .from(user)
                .fetchOne();

        return new PageImpl<>(users, pageable, count);
    }

    // 조회수 내림차순 정렬
    @Override
    public Page<User> findAllOrderByViewCountDesc(Pageable pageable) {
        QUser user = QUser.user;

        List<User> users = queryFactory
                .selectFrom(user)
                .orderBy(user.profileViewCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(user.count())
                .from(user)
                .fetchOne();

        return new PageImpl<>(users, pageable, count);
    }

    // 등록 최신순 정렬
    @Override
    public Page<User> findAllOrderByCreatedAtDesc(Pageable pageable) {
        QUser user = QUser.user;

        List<User> users = queryFactory
                .selectFrom(user)
                .orderBy(user.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(user.count())
                .from(user)
                .fetchOne();

        return new PageImpl<>(users, pageable, count);
    }
}