package ait.cohort5860.student.service;

import ait.cohort5860.student.dao.StudentRepository;
import ait.cohort5860.student.dto.ScoreDto;
import ait.cohort5860.student.dto.StudentCredentialsDto;
import ait.cohort5860.student.dto.StudentDto;
import ait.cohort5860.student.dto.StudentUpdateDto;
import ait.cohort5860.student.dto.exceptions.NotFoundException;
import ait.cohort5860.student.model.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// create an object of class
// place inside the application context
//@Component
@Service // @Service = @Component - service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    //@Autowired
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
//        if (studentRepository.findById(studentCredentialsDto.getId()).isPresent()) {
        if (studentRepository.existsById(studentCredentialsDto.getId())) {
            return false;
        }
        Student student = modelMapper.map(studentCredentialsDto, Student.class);
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public StudentDto removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        studentRepository.deleteById(student.getId());
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public StudentCredentialsDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        if (studentUpdateDto.getName() != null) {
            student.setName(studentUpdateDto.getName());
        }
        if (studentUpdateDto.getPassword() != null) {
            student.setPassword(studentUpdateDto.getPassword());
        }
        studentRepository.save(student);
        return modelMapper.map(student, StudentCredentialsDto.class);
    }

    @Override
    public boolean addScore(Long id, ScoreDto scoreDto) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        Boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
        studentRepository.save(student);
        return res;
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        return studentRepository.findByNameIgnoreCase(name)
                .map(s -> modelMapper.map(s, StudentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long countStudentsByNames(Set<String> names) {
        return studentRepository.countByNameIn(names);
        //return studentRepository.countByNameInIgnoreCase(names);
    }

    @Override
    public List<StudentDto> findStudentsByExamNameMinScore(String examName, Integer minScore) {
        return studentRepository.findByExamAndScoreGreaterThan(examName, minScore)
                .map(s -> modelMapper.map(s, StudentDto.class))
                .collect(Collectors.toList());
    }
}
