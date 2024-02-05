package com.madeeasy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Passenger {
    @Id
    private String passengerId;

    @Column(length = 30)
    private String passengerName;

    @Column(length = 30)
    private int age;

    @Column(length = 30)
    private int seatNumber;  // Seat number for each passenger

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    @JsonBackReference
    @ToString.Exclude
    private Ticket ticket;
}
