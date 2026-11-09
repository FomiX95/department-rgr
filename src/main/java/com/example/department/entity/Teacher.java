package com.example.department.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 1 рівень: Викладач */
@Entity
@Table(name = "teachers")
@Getter @Setter @NoArgsConstructor
public class Teacher {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String fullName;
    private String jobPosition;
    @NotNull
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
}
