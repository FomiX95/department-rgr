package com.example.department.controller;

import com.example.department.entity.Subject;
import com.example.department.repository.SubjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectRepository repo;

    @GetMapping
    public List<Subject> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Subject one(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subject create(@Valid @RequestBody Subject s) {
        s.setId(null);
        Subject saved = repo.save(s);
        log.info("Subject created: id={}, name={}", saved.getId(), saved.getName());
        return saved;
    }

    @PutMapping("/{id}")
    public Subject update(@PathVariable Long id, @Valid @RequestBody Subject s) {
        Subject db = one(id);
        db.setName(s.getName());
        db.setDescription(s.getDescription());
        log.info("Subject updated: id={}", id);
        return repo.save(db);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        one(id);
        repo.deleteById(id);
        log.info("Subject deleted: id={}", id);
    }
}
