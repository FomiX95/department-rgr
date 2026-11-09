package com.example.department;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DepartmentApplication {
    public static void main(String[] args) {
        // обход проблемы: PostgreSQL не знает старое название пояса Europe/Kiev
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(DepartmentApplication.class, args);
    }
}