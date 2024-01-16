package com.madeeasy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
public class Ticket {
    @Id
    @Column(unique = true, length = 30)
    private String pnrNumber;

    @Column(length = 30)
    private String source;

    @Column(length = 30)
    private String destination;

    @Column(length = 10)
    private String seatClass;

    @Column(length = 10)
    private String coach;

    @Column(length = 30)
    private int staringSeatNumber;

    @Column(length = 30)
    private int endingSeatNumber;

    @Column(length = 30)
    private String trainNumber;

    @Column(length = 30)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class) // Assuming you have a converter for LocalTime
    private LocalTime arrivalTime;

    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class) // Assuming you have a converter for LocalTime
    private LocalTime departureTime;

    @OneToMany(
            mappedBy = "ticket",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<Passenger> passengers;


    @ElementCollection(targetClass = TicketStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_statuses", joinColumns = @JoinColumn(name = "ticket_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private List<TicketStatus> statuses;

}
