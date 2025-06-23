package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//@Component
@Repository // // @@Repository = @Component - repository
public class StudentRepositoryImpl implements StudentRepository {

    // ConcurrentHashMap<> - thead-safe
    private Map<Long, Student> students = new ConcurrentHashMap<>();

    @Override
    public Student save(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public void deleteById(Student student) {
        students.remove(student.getId());
    }

    @Override
    public List<Student> findAll() {
        // students.values() - thead-safe
        return new ArrayList<>(students.values());
    }
}
