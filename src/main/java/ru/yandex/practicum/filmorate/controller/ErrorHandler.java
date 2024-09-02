package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, ConditionsNotMetException.class, DuplicatedDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(final Exception e) {
        log.trace("Получен статус 400 Bad Request {}", e.getMessage(), e);

        String errorMessage;

        if (e instanceof ValidationException) {
            errorMessage = "Ошибка: Аргументы метода не прошли валидацию!";
        } else {
            errorMessage = "Ошибка: Валидации!";
        }

        return new ErrorResponse(errorMessage, e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleConstraintException(ConstraintViolationException ex) {
        log.trace("Получен статус 400 Bad Request {}", ex.getMessage(), ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.trace("Получен статус 404 Not Found {}", e.getMessage(), e);

        return new ErrorResponse("Ошибка: Искомый ресурс не найден!", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.trace("Получен статус 500 Internal Server Error {}", e.getMessage(), e);

        return new ErrorResponse("Ошибка: Возникло исключение", "Произошла непредвиденная ошибка: " + e.getMessage());
    }


}
