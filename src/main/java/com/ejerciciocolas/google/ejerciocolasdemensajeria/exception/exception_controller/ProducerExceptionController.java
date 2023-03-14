package com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exception_controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exceptions.ExpertNotFoundException;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ProducerExceptionController {
    /**
     * Se lanza esta exception personalizada en caso de no encontrar al/(a la) expert@ especificada
     *
     * @param exception ExpertNotFoundException
     * @return ResponseEntity&#60;ErrorDTO>
     */
    @ExceptionHandler(value = ExpertNotFoundException.class)
    public ResponseEntity<ErrorDTO> expertNotFoundException(ExpertNotFoundException exception) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Se realizan las validaciones a los campos con anotaciones de la depencencia Validation @Valid, si encuentra errores se mostrarán
     *
     * @param exception MethodArgumentNotValidException
     * @return Map&#60;String, Object>
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidateException(MethodArgumentNotValidException exception){

        this.handleValidateException(null);
        Map<String, Object> response = new HashMap<>();

        List<String> errors =  exception.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage() )
                .collect(Collectors.toList());

        response.put("message", "error");
        response.put("errors", errors);

        return response;
    }
}