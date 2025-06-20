package ait.cohort5860.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCredentialsDto {
    private Long id; // wrapper class (null if nothing is entered)
    private String name;
    private String password;
}
