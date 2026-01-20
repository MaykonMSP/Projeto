package com.portfolio.insurance.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        String path,
        String error,
        String message,
        List<String> details
) {
}
