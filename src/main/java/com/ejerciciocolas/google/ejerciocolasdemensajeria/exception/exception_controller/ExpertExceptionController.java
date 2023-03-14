package com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exception_controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exceptions.BadRequestExpertOperationException;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.builders.ResponseBuilder;

import static com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.ExceptionCustomCodesUtil.*;

@RestControllerAdvice
public class ExpertExceptionController {

    @ExceptionHandler(value = BadRequestExpertOperationException.class)
    public ResponseEntity<ErrorDTO> exception(BadRequestExpertOperationException exception) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}