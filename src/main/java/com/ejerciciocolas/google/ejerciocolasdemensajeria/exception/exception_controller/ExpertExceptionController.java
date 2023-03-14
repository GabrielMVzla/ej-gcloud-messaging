package com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exception_controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exceptions.BadRequestExpertOperationException;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ErrorDTO;
import com.google.api.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.builders.ResponseBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidateException(MethodArgumentNotValidException exception){
        /*Map<String, String> response = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                    .stream()
                    .forEach(err -> {
                        response.put(err.getField(),  err.getDefaultMessage());
                    });

         */
        Map<String, Object> response = new HashMap<>();

        List<String> errors =  exception.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
//					.map(err -> { return "El campo '" + err.getField() + "' " + err.getDefaultMessage()})
                .collect(Collectors.toList());

        response.put("message", "error");
        response.put("errors", errors);

        return response;
    }
}