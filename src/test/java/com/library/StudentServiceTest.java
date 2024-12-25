package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private StudentService studentService;

    @Mock
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentDAO);
    }

    @Test
    void testAddStudent() throws SQLException {
        Student student = new Student(1, "Alice");
        List<Student> studentList = new ArrayList<>();
        when(studentDAO.getAllStudents()).thenReturn(studentList);
        when(studentDAO.getStudentById(1)).thenReturn(student);
        studentService.addStudent(student);

        verify(studentDAO).addStudent(student);
        assertEquals(0, studentList.size());
        assertEquals("Alice", studentDAO.getStudentById(1).getName());
    }

    @Test
    void testUpdateStudent() {

        Student originalStudent = new Student(3, "Alice");
        Student updatedStudent = new Student(3, "Alice Smith");

        when(studentDAO.getStudentById(3)).thenReturn(updatedStudent);

        studentService.updateStudent(3, "Alice Smith");

        assertEquals("Alice Smith", studentDAO.getStudentById(3).getName());
    }

    @Test
    void testDeleteStudent() {
        studentService.deleteStudent(4);
        verify(studentDAO).deleteStudent(4);
    }

    @Test
    void testGetAllStudents() {
        List<Student> mockStudentList = new ArrayList<>();
        mockStudentList.add(new Student(12, "Alice"));
        mockStudentList.add(new Student(13, "Bob"));

        when(studentDAO.getAllStudents()).thenReturn(mockStudentList);
        List<Student> students = studentDAO.getAllStudents();

        assertEquals(2, students.size());
        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
    }
}