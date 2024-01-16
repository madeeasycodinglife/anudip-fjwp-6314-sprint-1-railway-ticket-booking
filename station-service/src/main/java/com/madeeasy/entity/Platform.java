package com.madeeasy.entity;

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
public class Platform {
    @Id
    private String platformId;
    @Column(length = 10, nullable = false)
    private String platformNumber;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "station_code")
    private Station station;
}
