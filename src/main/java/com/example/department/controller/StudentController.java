package com.example.department.controller;

import com.example.department.entity.Student;
import com.example.department.repository.StudentRepository;
import com.example.department.repository.TeacherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository repo;
    private final TeacherRepository teachers;

    @GetMapping
    public List<Student> all(@RequestParam(required = false) Long teacherId) {
        return teacherId == null ? repo.findAll() : repo.findByTeacherId(teacherId);
    }

    @GetMapping("/{id}")
    public Student one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student s) {
        checkTeacher(s);
        s.setId(null);
        Student saved = repo.save(s);
        log.info("Student created: id={}, teacherId={}", saved.getId(), saved.getTeacherId());
        return saved;
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student s) {
        Student db = one(id);
        checkTeacher(s);
        db.setFullName(s.getFullName());
        db.setGroupName(s.getGroupName());
        db.setTeacherId(s.getTeacherId());
        log.info("Student updated: id={}", id);
        return repo.save(db);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        one(id);
        repo.deleteById(id);
        log.info("Student deleted: id={}", id);
    }

    private void checkTeacher(Student s) {
        if (!teachers.existsById(s.getTeacherId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teacher not found");
        }
    }
}
