package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//@Entity
@Table(name="experts_points")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OperationExpertLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("operation_type")
    @Column(name = "operation_type")
    private Long operationType;

    @JsonAlias("amount_entered")
    @Column(name = "amount_entered")
    private Float amountEntered;

    @JsonProperty(required = false)
    @JoinColumn(name = "operation_date", insertable = false, updatable = false)
    private Timestamp operationDate;

    @JsonAlias("id_expert")
    @JoinColumn(name = "id_expert")
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Long idExpert;
}