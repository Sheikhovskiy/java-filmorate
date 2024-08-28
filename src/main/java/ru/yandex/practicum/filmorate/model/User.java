package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// Описание: @NotBlank используется для строк и проверяет, что строка не является null и не пустая (содержит хотя бы один непробельный символ).
// Описание: @NotNull используется для проверки, что значение не является null. Он применяется ко всем типам объектов.
// Описание: @NonNull не является частью стандарта Bean Validation и чаще используется как подсказка для разработчиков
// и инструментов анализа кода, что данное поле или параметр метода не должны быть null. Она не осуществляет реальной валидации во время выполнения.

// Почему нужно указывать @Valid в параметрах метода? -> Активирует Валидацию:
// Без этой аннотации Spring просто принимает объект как есть и не выполняет проверки, НА УРОВНЕ КОНТРОЛЛЕРА ИЛИ СЕРВИСА даже если в объекте указаны аннотации валидации, такие как @NotBlank, @Email, @Size, и так далее.
@Data
@NoArgsConstructor
@Component
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

    private Set<Integer> friends = new HashSet<>();


}
