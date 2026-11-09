package com.example.department.controller;

import com.example.department.entity.Teacher;
import com.example.department.repository.SubjectRepository;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository repo;
    private final SubjectRepository subjects;

    @GetMapping
    public List<Teacher> all(@RequestParam(required = false) Long subjectId) {
        return subjectId == null ? repo.findAll() : repo.findBySubjectId(subjectId);
    }

    @GetMapping("/{id}")
    public Teacher one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher create(@Valid @RequestBody Teacher t) {
        checkSubject(t);
        t.setId(null);
        Teacher saved = repo.save(t);
        log.info("Teacher created: id={}, subjectId={}", saved.getId(), saved.getSubjectId());
        return saved;
    }

    @PutMapping("/{id}")
    public Teacher update(@PathVariable Long id, @Valid @RequestBody Teacher t) {
        Teacher db = one(id);
        checkSubject(t);
        db.setFullName(t.getFullName());
        db.setJobPosition(t.getJobPosition());
        db.setSubjectId(t.getSubjectId());
        log.info("Teacher updated: id={}", id);
        return repo.save(db);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        one(id);
        repo.deleteById(id);
        log.info("Teacher deleted: id={}", id);
    }

    private void checkSubject(Teacher t) {
        if (!subjects.existsById(t.getSubjectId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject not found");
        }
    }
}
