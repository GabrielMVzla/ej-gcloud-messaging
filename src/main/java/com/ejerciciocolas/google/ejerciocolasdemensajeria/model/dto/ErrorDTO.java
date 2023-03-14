package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorDTO {

    private String code;
    private String message;
    //private Throwable cause;

}
