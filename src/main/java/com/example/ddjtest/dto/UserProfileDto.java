package com.example.ddjtest.dto;

import com.example.ddjtest.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {

    private Long id;
    private String name;
    private Long point;
    private Integer profileViewCount;
    private LocalDateTime createdAt;

    // Entity -> DTO 변환
    public static UserProfileDto from(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .point(user.getPoint())
                .profileViewCount(user.getProfileViewCount())
                .createdAt(user.getCreatedAt())
                .build();
    }
}