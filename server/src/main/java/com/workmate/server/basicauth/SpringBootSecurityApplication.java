package com.workmate.server.basicauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.workmate.server")
public class SpringBootSecurityApplication {

    public static void main(String[] args) {

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println("password:" + encoder.encode("m123"));//
        SpringApplication.run(SpringBootSecurityApplication.class, args);
    }
}
