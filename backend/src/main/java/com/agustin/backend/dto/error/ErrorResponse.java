package com.agustin.backend.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ErrorResponse {

    private String code;
    private String message;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(String code, String message) {
        this.code    = code;
        this.message = message;
    }

    public String getCode()             { return code; }
    public String getMessage()          { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
