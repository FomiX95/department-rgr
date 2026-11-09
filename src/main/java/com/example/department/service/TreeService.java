package com.example.department.service;

import com.example.department.dto.Nodes.*;
import com.example.department.entity.Student;
import com.example.department.entity.Teacher;
import com.example.department.repository.StudentRepository;
import com.example.department.repository.SubjectRepository;
import com.example.department.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreeService {
    private final SubjectRepository subjects;
    private final TeacherRepository teachers;
    private final StudentRepository students;

    /** Будує дерево Предмет -> Викладачі -> Студенти */
    @Transactional(readOnly = true)
    public List<SubjectNode> buildTree() {
        log.info("Building department tree");
        Map<Long, List<Student>> studentsByTeacher = students.findAll().stream()
                .collect(Collectors.groupingBy(Student::getTeacherId));
        Map<Long, List<Teacher>> teachersBySubject = teachers.findAll().stream()
                .collect(Collectors.groupingBy(Teacher::getSubjectId));

        return subjects.findAll().stream().map(s -> new SubjectNode(
                s.getId(), s.getName(), s.getDescription(),
                teachersBySubject.getOrDefault(s.getId(), List.of()).stream().map(t -> new TeacherNode(
                        t.getId(), t.getFullName(), t.getJobPosition(),
                        studentsByTeacher.getOrDefault(t.getId(), List.of()).stream()
                                .map(st -> new StudentNode(st.getId(), st.getFullName(), st.getGroupName()))
                                .toList()
                )).toList()
        )).toList();
    }
}
