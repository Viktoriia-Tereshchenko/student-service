package ait.cohort5860.student.service;

import ait.cohort5860.student.dao.StudentRepository;
import ait.cohort5860.student.dto.ScoreDto;
import ait.cohort5860.student.dto.StudentCredentialsDto;
import ait.cohort5860.student.dto.StudentDto;
import ait.cohort5860.student.dto.StudentUpdateDto;
import ait.cohort5860.student.dto.exceptions.NotFoundException;
import ait.cohort5860.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// создать объект класса
// поместит внутрь аппликационного контекста
@Component
public class StudentServicImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
        // если там null, то isPresent() вернет false
        // вернет true, если студент уже есть
        if (studentRepository.findById(studentCredentialsDto.getId()).isPresent()) {
            return false;
        }
        Student student = new Student(
                studentCredentialsDto.getId(),
                studentCredentialsDto.getName(),
                studentCredentialsDto.getPassword());
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Long id) {
        // если студента нет, то наша ошибка NotFoundException = 404
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentDto removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        studentRepository.deleteById(student);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentCredentialsDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        student.setName(studentUpdateDto.getName());
        student.setPassword(studentUpdateDto.getPassword());
        studentRepository.save(student);
        return new StudentCredentialsDto(student.getId(), student.getName(), student.getPassword());
    }

    @Override
    public boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        return student.addScore(scoreDto.getExamName(), scoreDto.getScore());
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .collect(Collectors.toList());
    }

    @Override
    public Long countStudentsByNames(Set<String> names) {
        return studentRepository.findAll().stream()
                .filter(s -> names.contains(s.getName()))
                .count();
    }

    @Override
    public List<StudentDto> findStudentsByExamNameMinScore(String examName, Integer minScore) {
        return studentRepository.findAll().stream()
                .filter(s -> s.getScores().containsKey(examName) && s.getScores().get(examName) >= minScore)
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
                .collect(Collectors.toList());
    }
}
