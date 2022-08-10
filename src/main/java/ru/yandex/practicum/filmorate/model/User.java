package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private Long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[^\\s]*$")
    private String login;
    @NotNull
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;

}
