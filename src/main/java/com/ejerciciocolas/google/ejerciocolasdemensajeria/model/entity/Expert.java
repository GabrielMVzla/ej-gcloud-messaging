package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//@Entity
@Table(name="experts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Expert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("first_name")
    @Column(name = "first_name")
    private String firstName;

    @JsonAlias("last_name")
    @Column(name = "last_name")
    private String lastName;

    @JsonProperty(required = false)
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @JsonProperty(required = false)
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}
