package com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExpertNotFoundException extends RuntimeException {

    private String code;

    public ExpertNotFoundException(String code) {
        super();
        this.code = code;
    }

    public ExpertNotFoundException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ExpertNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ExpertNotFoundException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
