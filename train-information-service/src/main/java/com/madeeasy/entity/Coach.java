package com.madeeasy.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Coach {

    @Id
    @Column(unique = true)
    private String coachId;

    @Column(nullable = false, length = 5)
    private String name;
    @Column(nullable = false)
    private int seats;
    @Column(nullable = false)
    private int availableSeats;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_class_id")
    @JsonBackReference
    private TrainClass trainClass;
}
