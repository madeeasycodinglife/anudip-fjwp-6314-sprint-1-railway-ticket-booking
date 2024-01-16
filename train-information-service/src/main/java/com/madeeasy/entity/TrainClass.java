package com.madeeasy.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class TrainClass {

    @Id
    @Column(unique = true)
    private String classId;
    private String className;  // Name of the train class (e.g., AC First Class, Sleeper Class)
    private int totalSeats;    // Total number of seats available in this class

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;       // The train to which this class belongs

    @OneToMany(
            mappedBy = "trainClass",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<Coach> coach;
}
