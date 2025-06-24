package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.stream.Stream;

public interface StudentRepository extends MongoRepository<Student, Long> {
    Stream<Student> findByNameIgnoreCase(String name);

    @Query(value = "{'scores.?0':{'$gt':?1}}") // option 1
    Stream<Student> findByExamAndScoreGreaterThan(String examName, Integer score);

//     @Query(value = "{'scores.?#{#examName}':{'$gt':?#{#score}}}") // option 2
//     Stream<Student> findByExamAndScoreGreaterThan(@Param("examName") String examName, @Param("score") Integer score);

    Long countByNameIn(Set<String> names);
    //Long countByNameInIgnoreCase(Set<String> names);
}
