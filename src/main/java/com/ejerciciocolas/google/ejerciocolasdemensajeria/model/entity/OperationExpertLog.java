package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="operations_experts_log")
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Cacheable(false)
public class OperationExpertLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("operation_type")
    @Column(name = "operation_type")
    private String operationType;

    @JsonAlias("amount_entered")
    @Column(name = "amount_entered")
    private Float amountEntered;

    @JsonProperty(required = false)
    @JoinColumn(name = "operation_date")
    private LocalDateTime operationDate;

    @JsonBackReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_expert")
    private Expert expert;

    public OperationExpertLog(){
        operationDate = LocalDateTime.now();
    }
}