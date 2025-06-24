package ait.cohort5860.student.service;

import ait.cohort5860.configuration.ServiceConfiguration;
import ait.cohort5860.student.dao.StudentRepository;
import ait.cohort5860.student.dto.ScoreDto;
import ait.cohort5860.student.dto.StudentCredentialsDto;
import ait.cohort5860.student.dto.StudentDto;
import ait.cohort5860.student.dto.StudentUpdateDto;
import ait.cohort5860.student.dto.exceptions.NotFoundException;
import ait.cohort5860.student.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// AAA - Arrange, Act, Assert

@ContextConfiguration(classes = {ServiceConfiguration.class})
@SpringBootTest
public class StudentServiceTest {
    private final long studentId = 1000L;
    private final String name = "John";
    private final String password = "1234";
    private final String examName = "Math";
    private final Integer score = 95;

    private Student student;
    private ScoreDto scoreDto;

    @Autowired
    private ModelMapper modelMapper;

    @MockitoBean // don't use the database
    private StudentRepository studentRepository;
    private StudentService studentService; // necessary field - the object we are testing

    @BeforeEach // run before each test
    public void setUp() {
        student = new Student(studentId, name, password);
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
        scoreDto = new ScoreDto(examName, score);
    }

    @Test
    void testAddStudentWhenStudentDoesNotExist() {
        // Arrange
        StudentCredentialsDto dto = new StudentCredentialsDto(studentId, name, password);
        // when we save any student
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        boolean result = studentService.addStudent(dto);

        // Assert
        assertTrue(result);
    }

    @Test
    void testAddStudentWhenStudentExist() {
        // Arrange
        StudentCredentialsDto dto = new StudentCredentialsDto(studentId, name, password);
        when(studentRepository.existsById(dto.getId())).thenReturn(true);

        // Act
        boolean result = studentService.addStudent(dto);

        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testFindStudentWhenStudentExist() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));

        // Act
        StudentDto studentDto = studentService.findStudent(studentId);

        // Assert
        assertNotNull(studentDto);
        assertEquals(studentId, studentDto.getId());
    }

    @Test
    void testFindStudentWhenStudentNotExist() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        // assertThrows(1, 2)
        // 1 - result, 2 - called method
        assertThrows(NotFoundException.class, () -> studentService.findStudent(studentId));
    }

    @Test
    void testRemoveStudent() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));

        // Act
        StudentDto studentDto = studentService.removeStudent(studentId);

        // Assert
        assertNotNull(studentDto);
        assertEquals(studentId, studentDto.getId());
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        String newName = "newName";
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        StudentUpdateDto dto = new StudentUpdateDto(newName, null);

        // Act
        StudentCredentialsDto dto2 = studentService.updateStudent(studentId, dto);

        // Assert
        assertNotNull(dto2);
        assertEquals(studentId, dto2.getId());
        assertEquals(newName, dto2.getName());
        assertEquals(password, dto2.getPassword());
        verify(studentRepository, times(1)).save(any(Student.class));

    }

    @Test
    void testAddScoreWhenStudentExists() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        boolean result = studentService.addScore(studentId, scoreDto);

        // Assert
        assertTrue(result);
        assertEquals(score, student.getScores().get(examName));
        verify(studentRepository).save(student);
    }

    //-----------------------------------------------------------------------
    @Test
    void testAddScoreWhenStudentNotExists() {
        // Arrange
        long id = 2000L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> studentService.addScore(id, scoreDto));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testFindStudentsByNameWhenSomethingIsFound() {
        // Arrange
        when(studentRepository.findByNameIgnoreCase(name)).thenReturn(Stream.of(student));

        // Act
        List<StudentDto> students = studentService.findStudentsByName(name);

        // Assert
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertTrue(students.stream().allMatch(s -> s.getName().equalsIgnoreCase(name)));
    }

    @Test
    void testFindStudentsByNameWhenNothingIsFound() {
        // Arrange
        String name = "Peter";
        when(studentRepository.findByNameIgnoreCase(name)).thenReturn(Stream.empty());

        // Act
        List<StudentDto> students = studentService.findStudentsByName(name);

        // Assert
        assertNotNull(students);
        assertTrue(students.isEmpty());
        verify(studentRepository, times(1)).findByNameIgnoreCase(name);
    }

    @Test
    void testCountStudentsByNames() {
        // Arrange
        Set<String> names = new HashSet<>(List.of("John", "Peter"));
        Long result = 1L;
        when(studentRepository.countByNameIn(names)).thenReturn(result);

        // Act
        Long count = studentService.countStudentsByNames(names);

        // Assert
        assertEquals(result, count);
    }

    @Test
    void testFindStudentsByExamNameMinScoreWhenSomethingIsFound() {
        // Arrange
        Integer minScore = 90;
        when(studentRepository.findByExamAndScoreGreaterThan(examName, minScore)).thenReturn(Stream.of(student));

        // Act
        List<StudentDto> result = studentService.findStudentsByExamNameMinScore(examName, minScore);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(s -> s.getId().equals(studentId)));
    }

    @Test
    void testFindStudentsByExamNameMinScoreWhenNothingIsFound() {
        // Arrange
        Integer minScore = 99;
        when(studentRepository.findByExamAndScoreGreaterThan(examName, minScore)).thenReturn(Stream.empty());

        // Act
        List<StudentDto> result = studentService.findStudentsByExamNameMinScore(examName, minScore);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findByExamAndScoreGreaterThan(examName, minScore);
    }
}
