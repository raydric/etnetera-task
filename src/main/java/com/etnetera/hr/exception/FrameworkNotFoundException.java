package com.etnetera.hr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FrameworkNotFoundException extends RuntimeException {

    public FrameworkNotFoundException(final Long id) {
        super(String.format("JavaScript framework with id '%s' doesn't exist", id));
    }
}
