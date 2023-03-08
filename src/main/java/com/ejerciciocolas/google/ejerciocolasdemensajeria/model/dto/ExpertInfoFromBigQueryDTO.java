package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpertInfoFromBigQueryDTO
{
    private long id;
    private String operationType;
    private double amountEntered;
    private long totalPoints;
    private LocalDateTime operationDate;
}
