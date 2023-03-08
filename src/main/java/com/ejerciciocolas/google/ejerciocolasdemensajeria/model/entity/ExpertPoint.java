package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("last_points_entered")
    @Column(name = "last_points_entered")
    private Long lastPointsEntered;

    @JsonAlias("total_points")
    @Column(name = "total_points")
    private Long totalPoints;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_expert")
    private Expert expert;
}