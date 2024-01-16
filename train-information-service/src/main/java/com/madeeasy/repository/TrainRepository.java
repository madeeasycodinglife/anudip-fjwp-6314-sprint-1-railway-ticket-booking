package com.madeeasy.repository;

import com.madeeasy.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, String> {
    @Query(
            value = "SELECT * FROM Train WHERE starting_station = :startingStation AND ending_station = :endingStation",
            nativeQuery = true
    )
    List<Train> findTrainsByStations(@Param("startingStation") String startingStation,
                                     @Param("endingStation") String endingStation);

    @Query(
            value = "SELECT * FROM Train WHERE train_number = :trainNumber",
            nativeQuery = true
    )
    Train findTrainByTrainNumber(@Param("trainNumber") String trainNumber);

    @Query(value = "SELECT DISTINCT t.* " +
            "FROM train t " +
            "JOIN train_stop ts1 ON t.train_id = ts1.train_id " +
            "JOIN train_stop ts2 ON t.train_id = ts2.train_id " +
            "WHERE ts1.station_name = :source OR ts2.station_name = :destination",
            nativeQuery = true)
    List<Train> findTrainsBySourceAndDestination(@Param("source") String source, @Param("destination") String destination);

}
