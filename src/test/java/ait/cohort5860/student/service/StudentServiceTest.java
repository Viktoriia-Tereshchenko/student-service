package ait.cohort5860.student.service;

import ait.cohort5860.configuration.ServiceConfiguration;
import ait.cohort5860.student.dao.StudentRepository;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private Student student;

    @Autowired
    private ModelMapper modelMapper;

    @MockitoBean // don't use the database
    private StudentRepository studentRepository;
    private StudentService studentService; // necessary field - the object we are testing

    @BeforeEach // run before each test
    public void setUp() {
        student = new Student(studentId, name, password);
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
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
        // if we test, we should call this method
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
    void updateStudent() {
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


    // addScore

    // findStudentsByName


    // countStudentsByNames

    @Test
    void testCountStudentsByNames() {
        // Arrange
        Set<String> names = new HashSet<>(Arrays.asList("John", "Peter"));
        when(studentRepository.countByNameIn(names)).thenReturn(2L);

        // Act
        Long count = studentService.countStudentsByNames(names);

        // Assert
        assertEquals(2L, count);
    }

    @Override
    public Long countStudentsByNames(Set<String> names) {
        return studentRepository.countByNameIn(names);
        //return studentRepository.countByNameInIgnoreCase(names);
    }








    // findStudentsByExamNameMinScore
}

// Arrange
// Act
// Assert