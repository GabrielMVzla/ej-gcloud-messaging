package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class ExpertPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("last_points_entered")
    @Column(name = "last_points_entered")
    private Long lastPointsEntered;

    @JsonAlias("total_points")
    @Column(name = "total_points")
    private Long totalPoints;

    @JsonAlias("id_expert")
    @JoinColumn(name = "id_expert")
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Long idExpert;
}