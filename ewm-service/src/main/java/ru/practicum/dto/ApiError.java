package ru.practicum.dto;

import java.util.List;

public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;//enum
    private String timestamp;
}
