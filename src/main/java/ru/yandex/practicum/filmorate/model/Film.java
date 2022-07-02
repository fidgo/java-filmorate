package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Film {
    Integer id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @NotBlank
    @Size(min = 0, max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @NotNull
    @Positive
    Integer duration;
}
