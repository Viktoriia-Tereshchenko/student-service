package ait.cohort5860.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCredentialsDto {
    private Long id; // класс обертка(проверка на null, если ничего не ввели)
    private String name;
    private String password;
}
