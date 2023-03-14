package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="operations_experts_log")
@AllArgsConstructor
@NoArgsConstructor
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
    private double amountEntered;

    @JsonAlias("points_generated")
    @Column(name = "points_generated")
    private long pointsGenerated;

    @JsonProperty(required = false)
    @JoinColumn(name = "operation_date")
    @Temporal(TemporalType.DATE) //para indicar que trabajaremos con Date, tranforma fecha de java a fecha de sql
    private LocalDateTime operationDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_expert")
    private Expert expert;

    @PrePersist
    public void saveDate(){
        operationDate = LocalDateTime.now();
    }
}