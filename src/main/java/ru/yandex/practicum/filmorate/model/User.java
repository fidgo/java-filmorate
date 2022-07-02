package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class User {
    Integer id;
    @NotNull
    @Email
    String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[^\\s]*$")
    String login;
    @NotNull
    String name;
    @NotNull
    @Past
    LocalDate birthday;
}
