package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// Описание: @NotBlank используется для проверки строковых полей, чтобы убедиться, что строка не является null и не пустая (то есть не содержит только пробелы).
// Описание: @NotNull используется для проверки, что значение не является null. Он применяется ко всем типам объектов. Является частью спецификации Bean Validation

// Описание: @NonNull С помощью Lombok можно проверить, что поле не принимает значение null. Чтобы сделать это,
// достаточно пометить нужные поля аннотацией @NonNull, и библиотека сгенерирует для них проверку на неинициализированное значение.
// не является частью стандарта Bean Validation и чаще используется как подсказка для разработчиков. Ассоциируется с библиотеками, такими как Lombok


// Почему нужно указывать @Valid в параметрах метода? -> Активирует Валидацию:
// Без этой аннотации Spring просто принимает объект как есть и не выполняет проверки,
// НА УРОВНЕ КОНТРОЛЛЕРА ИЛИ СЕРВИСА даже если в объекте указаны аннотации валидации, такие, как @NotBlank, @Email, @Size, и так далее.

@Data
@NoArgsConstructor
public class User {

    private Integer id;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

}
