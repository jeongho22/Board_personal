package com.example.dy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@Slf4j
@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class DyApplication {

	public static void main(String[] args) {
		log.info("ENV SPRING_DATASOURCE_URL: {}", System.getenv("SPRING_DATASOURCE_URL"));
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
		SpringApplication.run(DyApplication.class, args);
	}

}
