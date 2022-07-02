package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
public class User extends AbstractModel {
    private Integer id;
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
