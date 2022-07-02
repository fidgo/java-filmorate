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
    private Integer id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 0, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private Integer duration;
}
