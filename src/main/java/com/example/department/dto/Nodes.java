package com.example.department.dto;

import java.util.List;

public final class Nodes {
    private Nodes() {}
    public record StudentNode(Long id, String fullName, String groupName) {}
    public record TeacherNode(Long id, String fullName, String jobPosition, List<StudentNode> students) {}
    public record SubjectNode(Long id, String name, String description, List<TeacherNode> teachers) {}
}
