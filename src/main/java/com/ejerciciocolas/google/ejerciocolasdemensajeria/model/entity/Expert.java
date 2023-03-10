package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="experts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Expert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("first_name")
    @Column(name = "first_name")
    private String firstName;

    @JsonAlias("last_name")
    @Column(name = "last_name")
    private String lastName;

    @ToString.Exclude
    //@JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="expert")
    private List<OperationExpertLog> operationExpertLogs;

    //@JsonManagedReference
    @ToString.Exclude
    //@JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy="expert")
    private ExpertPoint expertPoints;

    /*@JsonProperty(required = false)
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @JsonProperty(required = false)
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;*/
}
