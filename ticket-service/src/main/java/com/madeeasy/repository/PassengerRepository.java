package com.madeeasy.repository;

import com.madeeasy.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
    @Query(
            value = "SELECT MAX(seat_number) FROM passenger " +
                    "WHERE ticket_id IN (SELECT p.ticket_id FROM passenger p JOIN ticket t " +
                    "ON p.ticket_id = t.pnr_number WHERE t.train_number = :train_number)",
            nativeQuery = true
    )
    Integer findByEndingSeatNumberNativeQuery(@Param("train_number") String trainNumber);
}
