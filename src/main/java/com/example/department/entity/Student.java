package com.example.department.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 2 рівень: Студент */
@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String fullName;
    private String groupName;
    @NotNull
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;
}
