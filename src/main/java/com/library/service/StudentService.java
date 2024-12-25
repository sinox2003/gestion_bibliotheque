package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    

    // Ajouter un étudiant
    public void addStudent(Student student) throws SQLException {
        studentDAO.addStudent(student);
    }

    public void displayStudents() throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            System.out.println("ID: " + student.getId() + " | Nom: " + student.getName());
        }
    }

    public Student findStudentById(int id) throws SQLException {
        return studentDAO.getStudentById(id);
    }

    public void updateStudent(int i, String aliceSmith) {
        Student student = studentDAO.getStudentById(i);
        student.setName(aliceSmith);
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int i) {
        studentDAO.deleteStudent(i);
    }
}
