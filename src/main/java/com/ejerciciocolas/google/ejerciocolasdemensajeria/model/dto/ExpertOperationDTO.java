package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpertOperationDTO {

    @JsonAlias("id_expert")
    private long idExpert;

    @JsonAlias("operation_type")
    @NotBlank(message = " - operation_type: no debe estar vacío")
    @Pattern(regexp="^(?i)(abono|colocaci[oOóÓ\u00D3\u00F3]n)$", message="El valor debe ser (abono o colocación) mayúscula o minúscula.")
    private String operationType;

    @JsonAlias("amount_entered")
    @Min(value = 1, message = "El monto agregado debe ser de al menos 1 en adelante")
    private double amountEntered;

}
