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

    @Query(value = "SELECT * FROM Ticket WHERE seat_class = :seatClass", nativeQuery = true)
    List<Ticket> findAllBySeatClass(@Param("seatClass") String seatClass);

    List<Ticket> findByTrainNumber(String trainNumber);

    @Query(
            value = "SELECT * FROM Ticket WHERE train_number = :train_number",
            nativeQuery = true
    )
    List<Ticket> findByTrainNumberNativeQuery(@Param("train_number") String trainNumber);

    @Query(
            value = "SELECT * FROM Ticket t JOIN passenger p " +
                    "ON t.pnr_number = p.ticket_id WHERE p.seat_number > :seatNumber" +
                    " ORDER BY p.seat_number ASC",
            nativeQuery = true
    )
    List<Ticket> findTicketsBySeatNumberGreaterThan(@Param("seatNumber") int seatNumber);

}
