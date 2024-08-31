package ru.yandex.practicum.filmorate.model;


import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Mpa {

    private Integer id;

    private String name;


    public Mpa(int mpaId, String name) {
        this.id = mpaId;
        this.name = name;
    }


}
