package com.example.ddjtest.service;

import com.example.ddjtest.dto.UserProfileDto;
import com.example.ddjtest.dto.UserProfilePageResponse;
import com.example.ddjtest.entity.User;
import com.example.ddjtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserProfilePageResponse getUserProfiles(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        userPage = getUsersWithSort(sort, pageable);

        // Entity -> DTO 변환
        List<UserProfileDto> userProfiles = userPage.getContent().stream()
                .map(UserProfileDto::from)
                .collect(Collectors.toList());

        // 페이지네이션 정보 포함한 응답 생성
        return UserProfilePageResponse.builder()
                .content(userProfiles)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .hasNext(userPage.hasNext())
                .hasPrevious(userPage.hasPrevious())
                .build();
    }

    // 정렬 조건에 따른 사용자 조회
    private Page<User> getUsersWithSort(String sort, Pageable pageable) {
        return switch (sort) {
            case "name" -> userRepository.findAllOrderByNameAsc(pageable);
            case "view" -> userRepository.findAllOrderByViewCountDesc(pageable);
            case "date" -> userRepository.findAllOrderByCreatedAtDesc(pageable);
            default -> userRepository.findAll(pageable);
        };
    }

    // 특정 사용자 조회 (상세 조회시 조회수 증가)
    @Transactional
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 조회수 증가
        user.increaseViewCount();
        userRepository.save(user);

        return UserProfileDto.from(user);
    }
}