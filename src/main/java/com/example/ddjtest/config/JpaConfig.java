package com.example.ddjtest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // JPA Auditing 활성화 (CreatedDate, LastModifiedDate 자동 설정)
}