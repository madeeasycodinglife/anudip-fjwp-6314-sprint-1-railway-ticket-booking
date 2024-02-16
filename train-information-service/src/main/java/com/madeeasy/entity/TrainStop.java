package com.madeeasy.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class TrainStop {
    @Id
    @Column(unique = true)
    private String stopId;

    @Column(nullable = false, length = 50)
    private String stationName;

    /**
     * 00:00 to 11:59 is considered AM (midnight to noon).
     * 12:00 to 23:59 is considered PM (noon to midnight).
     */

    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class) // Assuming you have a converter for LocalTime
    private LocalTime arrivalTime;

    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class)
    private LocalTime departureTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_id")
    @JsonBackReference
    private Train train;
}
