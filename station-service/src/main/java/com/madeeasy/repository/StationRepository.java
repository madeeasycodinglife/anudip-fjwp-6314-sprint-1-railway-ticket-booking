package com.madeeasy.repository;

import com.madeeasy.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
    Optional<Station> findByStationCode(String stationCode);

    Optional<Station> findByStationName(String stationName);
}
