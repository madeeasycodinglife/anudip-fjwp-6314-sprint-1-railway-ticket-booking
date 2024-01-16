package com.madeeasy.repository;

import com.madeeasy.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    Optional<Ticket> findByPnrNumber(String pnrNumber);


    @Query(
            value = "SELECT MAX(ending_seat_number) FROM Ticket WHERE train_number = :train_number",
            nativeQuery = true
    )
    Integer findByEndingSeatNumberNativeQuery(@Param("train_number") String trainNumber);

    Optional<Ticket> findByTrainNumber(String trainNumber);

    @Query(
            value = "SELECT * FROM Ticket WHERE train_number = :train_number",
            nativeQuery = true
    )
    List<Ticket> findByTrainNumberNativeQuery(@Param("train_number") String trainNumber);
}
