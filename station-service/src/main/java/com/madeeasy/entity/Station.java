package com.madeeasy.entity;

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
public class Station {

    @Id
    @Column(unique = true, nullable = false, length = 10)
    private String stationCode;
    @Column(unique = true, nullable = false, length = 30)
    private String stationName;
    @Column(nullable = false, length = 30)
    private String location;
    private String description;

    @OneToMany(
            mappedBy = "station",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Platform> platforms;
}