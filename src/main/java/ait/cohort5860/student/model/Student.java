package ait.cohort5860.student.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Document(collection = "students") // name of collection
public class Student {
    @Id // primary key
    private long id;
    @Setter
    private String name;
    @Setter
    private String password;
    private Map<String, Integer> scores = new HashMap<>();

    public Student(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public boolean addScore(String examName, Integer score) {
        return scores.put(examName, score) == null;
        // true - if this is the first score
        // false - if retaking a score
    }
}
