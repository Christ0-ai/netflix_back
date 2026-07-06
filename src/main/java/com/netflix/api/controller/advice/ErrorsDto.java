package com.netflix.api.controller.advice;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsDto(
        LocalDateTime timestamp,
        int errorCode,
        List<ErrorValidDto> errors) {
}

record ErrorValidDto(
        String key,
        String errorMessage) {
}
