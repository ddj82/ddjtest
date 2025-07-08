package com.example.ddjtest.controller;

import com.example.ddjtest.dto.UserProfileDto;
import com.example.ddjtest.dto.UserProfilePageResponse;
import com.example.ddjtest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    * 회원 프로필 목록 조회 API
    * page 페이지 번호 (기본값: 0)
    * size 페이지 크기 (기본값: 10)
    * sort 정렬 방식 (name: 이름순, view: 조회수순, date: 등록일순)
    */
    @GetMapping
    public ResponseEntity<UserProfilePageResponse> getUserProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort) {

        UserProfilePageResponse response = userService.getUserProfiles(page, size, sort);
        return ResponseEntity.ok(response);
    }

    // 특정 회원 프로필 상세 조회 API (조회수 증가)
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        UserProfileDto userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }
}