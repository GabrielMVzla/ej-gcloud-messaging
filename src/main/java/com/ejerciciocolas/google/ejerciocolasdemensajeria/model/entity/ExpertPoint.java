package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="experts_points")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ExpertPoint implements Serializable {

    @Id
    private Long id;

    @JsonAlias("last_operation")
    @Column(name = "last_operation")
    private String lastOperation;

    @JsonAlias("last_points_entered")
    @Column(name = "last_points_entered")
    private long lastPointsEntered;

    @JsonAlias("last_amount_entered")
    @Column(name = "last_amount_entered")
    private double lastAmountEntered;

    @JsonAlias("acumulated_residual")
    @Column(name = "acumulated_residual")
    private long acumulatedResidual;

    @JsonAlias("total_points")
    @Column(name = "total_points")
    private long totalPoints;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_expert")
    private Expert expert;
}