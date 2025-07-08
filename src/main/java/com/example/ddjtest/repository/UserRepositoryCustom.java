package com.example.ddjtest.repository;

import com.example.ddjtest.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    // 이름 가나다순 정렬
    Page<User> findAllOrderByNameAsc(Pageable pageable);

    // 조회수 내림차순 정렬
    Page<User> findAllOrderByViewCountDesc(Pageable pageable);

    // 등록 최신순 정렬
    Page<User> findAllOrderByCreatedAtDesc(Pageable pageable);
}