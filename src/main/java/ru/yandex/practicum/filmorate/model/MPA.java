package ru.yandex.practicum.filmorate.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class MPA {
    private Integer id;
    private String name;
}
