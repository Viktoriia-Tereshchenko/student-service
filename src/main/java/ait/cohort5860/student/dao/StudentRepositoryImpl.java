package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class StudentRepositoryImpl implements StudentRepository {

    // ConcurrentHashMap<> - потокобезопасная HashMap<>
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
        // students.values() - потокобезопасно
        return new ArrayList<>(students.values());
    }
}
