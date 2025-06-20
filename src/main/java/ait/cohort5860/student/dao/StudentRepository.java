package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    //wrap
    Optional<Student> findById(Long id);

    void deleteById(Student student);

    List<Student> findAll();
}
