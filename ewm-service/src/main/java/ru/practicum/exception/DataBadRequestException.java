package ru.practicum.exception;

public class DataBadRequestException extends RuntimeException {

    public DataBadRequestException(String message) {
        super(message);
    }
}
