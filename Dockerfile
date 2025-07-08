FROM ubuntu:latest

# 환경 변수 설정
ENV MYSQL_ROOT_PASSWORD=1111
ENV MYSQL_DATABASE=ddjtest

# 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y mysql-server openjdk-17-jdk wget unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# MySQL 설정
RUN service mysql start && \
    mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '1111';" && \
    mysql -u root -p1111 -e "CREATE DATABASE ddjtest;" && \
    mysql -u root -p1111 -e "FLUSH PRIVILEGES;"

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 파일들 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# 권한 설정 및 의존성 다운로드
RUN chmod +x ./gradlew

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew build -x test --no-daemon

# 시작 스크립트 생성
RUN echo '#!/bin/bash' > /start.sh && \
    echo 'service mysql start' >> /start.sh && \
    echo 'sleep 10' >> /start.sh && \
    echo 'java -jar /app/build/libs/*-SNAPSHOT.jar' >> /start.sh && \
    chmod +x /start.sh

# 포트 노출
EXPOSE 5064 3306

# 시작 스크립트 실행
CMD ["/start.sh"]