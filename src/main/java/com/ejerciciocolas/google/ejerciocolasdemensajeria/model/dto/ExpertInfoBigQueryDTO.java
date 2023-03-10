package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpertInfoBigQueryDTO
{
    private long id;
    private String operationType;
    private double amountEntered;
    private long pointsOperation;
    private long acumulatedResidual;
    private long totalPoints;
    private LocalDateTime operationDate;
}
