package com.example.ddjtest.config;

import com.example.ddjtest.entity.User;
import com.example.ddjtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터 있는지 확인
        if (userRepository.count() == 0) {
            initializeUsers();
        }
    }

    private void initializeUsers() {
        LocalDateTime now = LocalDateTime.now();

        List<User> users = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> {
                    char nameChar = (char) ('a' + i);
                    return User.builder()
                            .name(String.valueOf(nameChar))
                            .profileViewCount(i + 1)
                            .createdAt(now.minusDays(9 - i))
                            .build();
                })
                .collect(Collectors.toList());

        userRepository.saveAll(users);
        System.out.println("사용자 10명 더미데이터 생성");
    }
}