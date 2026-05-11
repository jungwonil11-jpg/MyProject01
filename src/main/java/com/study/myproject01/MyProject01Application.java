package com.study.myproject01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 애플리케이션의 진입점(Entry Point) 클래스.
 *
 * <p>{@code @SpringBootApplication}은 아래 세 가지 어노테이션을 합친 메타 어노테이션이다.
 * <ul>
 *   <li>{@code @SpringBootConfiguration} — 현재 클래스를 설정 클래스로 등록</li>
 *   <li>{@code @EnableAutoConfiguration} — classpath의 라이브러리를 분석해 필요한 Bean을 자동 설정</li>
 *   <li>{@code @ComponentScan} — 현재 패키지(com.study.myproject01) 이하를 스캔하여
 *       {@code @Component}, {@code @Service}, {@code @Repository}, {@code @Controller} 등을 Bean으로 등록</li>
 * </ul>
 */
@SpringBootApplication
public class MyProject01Application {

    /**
     * JVM이 가장 먼저 호출하는 메인 메서드.
     *
     * <p>{@link SpringApplication#run(Class, String[])}을 호출하면
     * 내장 Tomcat 서버가 시작되고 Spring IoC 컨테이너(ApplicationContext)가 초기화된다.
     *
     * @param args 커맨드라인 인수 (예: --server.port=9090)
     */
    public static void main(String[] args) {
        SpringApplication.run(MyProject01Application.class, args);
    }

}
