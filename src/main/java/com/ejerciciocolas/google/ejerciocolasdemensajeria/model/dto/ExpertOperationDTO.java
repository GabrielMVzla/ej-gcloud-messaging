package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

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
    private String operationType;

    @JsonAlias("amount_entered")
    private double amountEntered;

}
