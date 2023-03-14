package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpertOperationDTO {

    @JsonAlias("id_expert")
    @Min(0)
    private long idExpert;

    @JsonAlias("operation_type")
    @NotBlank(message = "campo \"operationType\" no debe estar vacío.")
    private String operationType;

    @JsonAlias("amount_entered")
    private double amountEntered;

}
