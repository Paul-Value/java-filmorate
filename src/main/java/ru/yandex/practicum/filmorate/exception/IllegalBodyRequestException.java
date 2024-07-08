package ru.yandex.practicum.filmorate.exception;

public class IllegalBodyRequestException extends RuntimeException {
    public IllegalBodyRequestException(String message) {
        super(message);
    }
}
