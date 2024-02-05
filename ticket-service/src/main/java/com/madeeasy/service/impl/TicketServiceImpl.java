package com.madeeasy.service.impl;

import com.madeeasy.dto.request.PassengerRequestDTO;
import com.madeeasy.dto.request.TicketRequestDTO;
import com.madeeasy.entity.Passenger;
import com.madeeasy.entity.Ticket;
import com.madeeasy.entity.TicketStatus;
import com.madeeasy.exception.TicketNotFoundException;
import com.madeeasy.repository.PassengerRepository;
import com.madeeasy.repository.TicketRepository;
import com.madeeasy.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;
    private static final String TICKET_CONFIRMED = "CONFIRMED";
    private static final String TICKET_WAITING = "WAITING";
    private static final String TICKET_CANCELLED = "CANCELLED";


    @Override
    public ResponseEntity<?> bookNextAvailableSeats(int numberOfTickets, TicketRequestDTO ticketRequestDTO) {

        String url = "http://train-service/train-service/available-seats/" + ticketRequestDTO.getTrainNumber()
                + "/" + ticketRequestDTO.getSeatClass();

        try {
            ResponseEntity<Map<String, Integer>> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Integer>>() {
                            }
                    );
            System.out.println("responseEntity = " + responseEntity);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Integer> availableSeats = responseEntity.getBody();

                if (numberOfTickets == ticketRequestDTO.getPassengers().size()) {


                    if (ticketRequestDTO.getSeatClass().equals("Sleeper")) {

                        if (availableSeats.get("S1") > 0) {

                            if (availableSeats.get("S1") >= 1 || availableSeats.get("S2") >= 1 || availableSeats.get("S3") >= 1) {

                                Map<String, Integer> newAvailableSeats = availableSeats;
                                AtomicBoolean isBooked = new AtomicBoolean(false);
                                ticketRequestDTO.getPassengers()
                                        .forEach(passenger -> {
                                            if (newAvailableSeats.get("S1") >= 1) {
                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "S1",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "S1");
                                                uriVariables.put("noOfSeats", 1);

                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                                newAvailableSeats.put("S1", availableSeats.get("S1") - 1);
                                            } else if (newAvailableSeats.get("S2") >= 1) {

                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "S2",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                newAvailableSeats.put("S2", availableSeats.get("S2") - 1);
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "S2");
                                                uriVariables.put("noOfSeats", 1);
                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                            } else if (newAvailableSeats.get("S3") >= 1) {

                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "S3",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                newAvailableSeats.put("S3", availableSeats.get("S3") - 1);
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "S3");
                                                uriVariables.put("noOfSeats", 1);

                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                            } else {
                                                // handle waiting tickets
                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "S3",
                                                            TICKET_WAITING);
                                                    isBooked.set(false);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                System.out.println("run count : ");
                                            }
                                        });
                                if (isBooked.get())
                                    return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                                else
                                    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                            }

                        } else if (availableSeats.get("S2") > 0) {

                            Map<String, Integer> newAvailableSeats = availableSeats;
                            AtomicBoolean isBooked = new AtomicBoolean(false);
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {
                                        if (newAvailableSeats.get("S2") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "S2",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("S2", availableSeats.get("S2") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "S2");
                                            uriVariables.put("noOfSeats", 1);
                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else if (newAvailableSeats.get("S3") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "S3",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("S3", availableSeats.get("S3") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "S3");
                                            uriVariables.put("noOfSeats", 1);

                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else {
                                            // handle waiting tickets
                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "S3",
                                                        TICKET_WAITING);
                                                isBooked.set(false);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            System.out.println("run count : ");
                                        }

                                    });
                            if (isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else if (availableSeats.get("S3") > 0) {


                            Map<String, Integer> newAvailableSeats = availableSeats;
                            AtomicBoolean isBooked = new AtomicBoolean(false);
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {

                                        if (newAvailableSeats.get("S3") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "S3",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true); // At least one seat booked
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("S3", availableSeats.get("S3") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "S3");
                                            uriVariables.put("noOfSeats", 1);

                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else {
                                            // handle waiting tickets
                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "S3",
                                                        TICKET_WAITING);
                                                isBooked.set(false);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                            if (isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else {
                            // handle waiting tickets
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {
                                        try {
                                            bookSeatSequentially(
                                                    ticketRequestDTO.getSeatClass(),
                                                    1,
                                                    ticketRequestDTO,
                                                    passenger,
                                                    "S3",
                                                    TICKET_WAITING);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

                            if (numberOfTickets == 1) {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seat is in waiting state !!");
                            }
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seats are in waiting state !!");
                        }
                    } else if (ticketRequestDTO.getSeatClass().equals("General")) {

                        if (availableSeats.get("D1") > 0) {

                            if (availableSeats.get("D1") >= 1 || availableSeats.get("D2") >= 1 || availableSeats.get("D3") >= 1) {

                                Map<String, Integer> newAvailableSeats = availableSeats;
                                AtomicBoolean isBooked = new AtomicBoolean(false);
                                ticketRequestDTO.getPassengers()
                                        .forEach(passenger -> {
                                            if (newAvailableSeats.get("D1") >= 1) {
                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "D1",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "D1");
                                                uriVariables.put("noOfSeats", 1);

                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                                newAvailableSeats.put("D1", availableSeats.get("D1") - 1);
                                            } else if (newAvailableSeats.get("D2") >= 1) {

                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "D2",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                newAvailableSeats.put("D2", availableSeats.get("D2") - 1);
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "D2");
                                                uriVariables.put("noOfSeats", 1);
                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                            } else if (newAvailableSeats.get("D3") >= 1) {

                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "D3",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                newAvailableSeats.put("D3", availableSeats.get("D3") - 1);
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "D3");
                                                uriVariables.put("noOfSeats", 1);

                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                            } else {
                                                // handle waiting tickets
                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "D3",
                                                            TICKET_WAITING);
                                                    isBooked.set(false);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                System.out.println("run count : ");
                                            }
                                        });
                                if (isBooked.get())
                                    return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                                else
                                    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                            }

                        } else if (availableSeats.get("D2") > 0) {

                            Map<String, Integer> newAvailableSeats = availableSeats;
                            AtomicBoolean isBooked = new AtomicBoolean(false);
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {
                                        if (newAvailableSeats.get("D2") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "D2",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("D2", availableSeats.get("D2") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "D2");
                                            uriVariables.put("noOfSeats", 1);
                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else if (newAvailableSeats.get("D3") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "D3",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("D3", availableSeats.get("D3") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "D3");
                                            uriVariables.put("noOfSeats", 1);

                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else {
                                            // handle waiting tickets
                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "D3",
                                                        TICKET_WAITING);
                                                isBooked.set(false);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            System.out.println("run count : ");
                                        }

                                    });
                            if (isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else if (availableSeats.get("D3") > 0) {


                            Map<String, Integer> newAvailableSeats = availableSeats;
                            AtomicBoolean isBooked = new AtomicBoolean(false);
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {

                                        if (newAvailableSeats.get("D3") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "D3",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true); // At least one seat booked
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("D3", availableSeats.get("D3") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "D3");
                                            uriVariables.put("noOfSeats", 1);

                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else {
                                            // handle waiting tickets
                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "D3",
                                                        TICKET_WAITING);
                                                isBooked.set(false);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                            if (isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else {
                            // handle waiting tickets

                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {
                                        try {
                                            bookSeatSequentially(
                                                    ticketRequestDTO.getSeatClass(),
                                                    1,
                                                    ticketRequestDTO,
                                                    passenger,
                                                    "D3",
                                                    TICKET_WAITING);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                            if (numberOfTickets == 1) {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seat is in waiting state !!");
                            }
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seats are in waiting state !!");
                        }

                    } else {
                        if (availableSeats.get("AC1") > 0) {

                            if (availableSeats.get("AC1") >= 1 || availableSeats.get("AC2") >= 1 || availableSeats.get("AC3") >= 1) {

                                Map<String, Integer> newAvailableSeats = availableSeats;
                                AtomicBoolean isBooked = new AtomicBoolean(false);
                                ticketRequestDTO.getPassengers()
                                        .forEach(passenger -> {
                                            if (newAvailableSeats.get("AC1") >= 1) {
                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "AC1",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "AC1");
                                                uriVariables.put("noOfSeats", 1);

                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                                newAvailableSeats.put("AC1", availableSeats.get("AC1") - 1);
                                            } else if (newAvailableSeats.get("AC2") >= 1) {

                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "AC2",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                newAvailableSeats.put("AC2", availableSeats.get("AC2") - 1);
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "AC2");
                                                uriVariables.put("noOfSeats", 1);
                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                            } else if (newAvailableSeats.get("AC3") >= 1) {

                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "AC3",
                                                            TICKET_CONFIRMED);
                                                    isBooked.set(true);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                newAvailableSeats.put("AC3", availableSeats.get("AC3") - 1);
                                                String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                                // Set up path variables
                                                Map<String, Object> uriVariables = new HashMap<>();
                                                uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                                uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                                uriVariables.put("coach", "AC3");
                                                uriVariables.put("noOfSeats", 1);

                                                // Make the PUT request
                                                ResponseEntity<String> response = restTemplate.exchange(
                                                        urlToReduceSeatNumbers,
                                                        HttpMethod.PUT,
                                                        null,
                                                        String.class,
                                                        uriVariables
                                                );
                                            } else {
                                                // handle waiting tickets
                                                try {
                                                    bookSeatSequentially(
                                                            ticketRequestDTO.getSeatClass(),
                                                            1,
                                                            ticketRequestDTO,
                                                            passenger,
                                                            "AC3",
                                                            TICKET_WAITING);
                                                    isBooked.set(false);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                System.out.println("run count : ");
                                            }
                                        });
                                if (isBooked.get())
                                    return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                                else
                                    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                            }

                        } else if (availableSeats.get("AC2") > 0) {

                            Map<String, Integer> newAvailableSeats = availableSeats;
                            AtomicBoolean isBooked = new AtomicBoolean(false);
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {
                                        if (newAvailableSeats.get("AC2") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "AC2",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("AC2", availableSeats.get("AC2") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "AC2");
                                            uriVariables.put("noOfSeats", 1);
                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else if (newAvailableSeats.get("AC3") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "AC3",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("AC3", availableSeats.get("AC3") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "AC3");
                                            uriVariables.put("noOfSeats", 1);

                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else {
                                            // handle waiting tickets
                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "AC3",
                                                        TICKET_WAITING);
                                                isBooked.set(false);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            System.out.println("run count : ");
                                        }
                                    });
                            if (isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else if (availableSeats.get("AC3") > 0) {


                            Map<String, Integer> newAvailableSeats = availableSeats;
                            AtomicBoolean isBooked = new AtomicBoolean(false);
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {

                                        if (newAvailableSeats.get("AC3") >= 1) {

                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "AC3",
                                                        TICKET_CONFIRMED);
                                                isBooked.set(true); // At least one seat booked
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            newAvailableSeats.put("AC3", availableSeats.get("AC3") - 1);
                                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                                            // Set up path variables
                                            Map<String, Object> uriVariables = new HashMap<>();
                                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                                            uriVariables.put("coach", "AC3");
                                            uriVariables.put("noOfSeats", 1);

                                            // Make the PUT request
                                            ResponseEntity<String> response = restTemplate.exchange(
                                                    urlToReduceSeatNumbers,
                                                    HttpMethod.PUT,
                                                    null,
                                                    String.class,
                                                    uriVariables
                                            );
                                        } else {
                                            // handle waiting tickets
                                            try {
                                                bookSeatSequentially(
                                                        ticketRequestDTO.getSeatClass(),
                                                        1,
                                                        ticketRequestDTO,
                                                        passenger,
                                                        "AC3",
                                                        TICKET_WAITING);
                                                isBooked.set(false);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                            if (isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else {
                            // handle waiting tickets
                            ticketRequestDTO.getPassengers()
                                    .forEach(passenger -> {
                                        try {
                                            bookSeatSequentially(
                                                    ticketRequestDTO.getSeatClass(),
                                                    1,
                                                    ticketRequestDTO,
                                                    passenger,
                                                    "AC3",
                                                    TICKET_WAITING);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });

                            if (numberOfTickets == 1) {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seat is in waiting state !!");
                            }
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seats are in waiting state !!");
                        }
                    }
                } else if (numberOfTickets > ticketRequestDTO.getPassengers().size()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number of tickets cannot be more than number of passengers.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number of tickets cannot be less than number of passengers.");
                }
            } else {
                // Handle the case where the request to check available seats failed
                return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Handle the 404 error specifically
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found.");
        } catch (HttpClientErrorException e) {
            // Handle other client errors
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("e = " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
        return null;
    }


    @Override
    public ResponseEntity<?> getAllTicketsByTrainNumber(String trainNumber) {
        List<Ticket> tickets = this.ticketRepository.findByTrainNumberNativeQuery(trainNumber);
        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets found for the given train number.");
        } else {
//            convertTicketsToResponseDTO(tickets);
            return ResponseEntity.status(HttpStatus.OK).body(tickets);
        }
    }

    @Override
    public ResponseEntity<?> checkTicketsStatusByPnrNumber(String pnrNumber) {
        Ticket ticket = this.ticketRepository.findByPnrNumber(pnrNumber)
                .orElseThrow(() -> new TicketNotFoundException("No such ticket found for pnr-number: " + pnrNumber));
        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }

    @Override
    public ResponseEntity<?> cancelTicketsByPnrNumber(String pnrNumber) {

        Ticket ticketsFound = this.ticketRepository.findByPnrNumber(pnrNumber)
                .orElseThrow(() -> new TicketNotFoundException("No tickets found"));

        System.out.println("ticketsFound = " + ticketsFound);

        String url = "http://train-service/train-service/number-of-seats/{trainNumber}/{seatClass}";
        // Set up path variables
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("trainNumber", ticketsFound.getTrainNumber());
        uriVariables.put("seatClass", ticketsFound.getSeatClass());

        ResponseEntity<Map<String, Integer>> responseEntity =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<String, Integer>>() {
                        },
                        uriVariables
                );
        System.out.println("response = " + responseEntity);

        Map<String, Integer> seatMap = responseEntity.getBody();

        String seatClass = ticketsFound.getSeatClass();
        String coach = ticketsFound.getCoach();

        if (seatClass.equals("Sleeper")) {

            List<Ticket> sleeperTickets = this.ticketRepository.findAllBySeatClass("Sleeper");

            if (!sleeperTickets.isEmpty()) {

                // Separate based on coach values (S1, S2, S3)
                List<Ticket> s1Tickets = filterTicketsByCoach(sleeperTickets, "S1");
                List<Ticket> s2Tickets = filterTicketsByCoach(sleeperTickets, "S2");
                List<Ticket> s3Tickets = filterTicketsByCoach(sleeperTickets, "S3");


                Integer s1PredefinedSeats = seatMap.get("S1");
                Integer s2PredefinedSeats = seatMap.get("S2") + s1PredefinedSeats;
                Integer s3PredefinedSeats = seatMap.get("S3") + s2PredefinedSeats;

                AtomicInteger s1CurrentSeatNumber = new AtomicInteger(1);
                AtomicInteger s2CurrentSeatNumber = new AtomicInteger(1);
                AtomicInteger s3CurrentSeatNumber = new AtomicInteger(1);


                sleeperTickets.forEach(ticket -> Collections.sort(ticket.getPassengers(), Comparator.comparingInt(Passenger::getSeatNumber)));

                // Sorting the list of tickets based on the seat number of the first passenger (assuming each ticket has at least one passenger)
//                Collections.sort(sleeperTickets, Comparator.comparingInt(ticket -> ticket.getPassengers().get(0).getSeatNumber()));

                sleeperTickets.sort(Comparator.comparingInt(ticket ->
                        ticket.getPassengers().isEmpty() ? Integer.MAX_VALUE : ticket.getPassengers().get(0).getSeatNumber()
                ));

                AtomicBoolean isDeleted = new AtomicBoolean(false);

                sleeperTickets.forEach(System.out::println);
                sleeperTickets.stream()
                        .filter(sleeperTicket -> sleeperTicket != ticketsFound && !sleeperTicket.getPassengers().isEmpty())
                        .forEach(sleeperTicket -> {
                            System.out.println("sleeperTicket = " + sleeperTicket);

                            if (s1CurrentSeatNumber.get() <= s1PredefinedSeats) {
                                sleeperTicket.getPassengers().get(0)
                                        .setSeatNumber(s1CurrentSeatNumber.getAndIncrement());
                                sleeperTicket.setCoach("S1");
                                sleeperTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(sleeperTicket.getPassengers().get(0));
                                Ticket savedTicketNewUpdated = this.ticketRepository.save(sleeperTicket);
                                this.ticketRepository.delete(ticketsFound);
                                System.out.println("savedTicketNewUpdated = " + savedTicketNewUpdated);
                                s2CurrentSeatNumber.set(s1CurrentSeatNumber.get());
                                isDeleted.set(true);
                            } else if (s2CurrentSeatNumber.get() <= s2PredefinedSeats) {
                                sleeperTicket.getPassengers().get(0)
                                        .setSeatNumber(s2CurrentSeatNumber.getAndIncrement());
                                sleeperTicket.setCoach("S2");
                                sleeperTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(sleeperTicket.getPassengers().get(0));
                                this.ticketRepository.save(sleeperTicket);
                                s3CurrentSeatNumber.set(s2CurrentSeatNumber.get());
                            } else if (s3CurrentSeatNumber.get() <= s3PredefinedSeats) {
                                sleeperTicket.getPassengers().get(0)
                                        .setSeatNumber(s3CurrentSeatNumber.getAndIncrement());
                                sleeperTicket.setCoach("S3");
                                sleeperTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(sleeperTicket.getPassengers().get(0));
                                this.ticketRepository.save(sleeperTicket);
                            } else {
                                sleeperTicket.getPassengers().get(0)
                                        .setSeatNumber(s3CurrentSeatNumber.getAndIncrement());
                                sleeperTicket.setCoach("S3");
                                sleeperTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.WAITING)));
                                this.passengerRepository.save(sleeperTicket.getPassengers().get(0));
                                this.ticketRepository.save(sleeperTicket);
                            }
                        });

                if (!isDeleted.get()) {
                    this.ticketRepository.delete(ticketsFound);
                }

                Integer totalSeatsInS1 = seatMap.get("S1");
                Integer totalSeatsInS2 = seatMap.get("S2") + totalSeatsInS1;
                Integer totalSeatsInS3 = seatMap.get("S3") + totalSeatsInS2;

                Integer lastSeatNumber = this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketsFound.getTrainNumber());
                lastSeatNumber = (lastSeatNumber == null) ? 1 : lastSeatNumber + 1; // i have added 1 as one record is deleted from the table using pnr number
                Integer seatNumberToBeSentInS1 = 0;
                Integer seatNumberToBeSentInS2 = 0;
                Integer seatNumberToBeSentInS3 = 0;
                if (lastSeatNumber <= totalSeatsInS3 && lastSeatNumber > totalSeatsInS2) {
                    seatNumberToBeSentInS3++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";

                    // Set up path variables for S3
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "Sleeper");
                    updateUriVariables.put("coach", "S3");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInS3));

                    ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                            seatNumberUpdateUrl,
                            HttpMethod.PUT,
                            null,
                            String.class,
                            updateUriVariables
                    );
                }
                if (lastSeatNumber <= totalSeatsInS2 && lastSeatNumber > totalSeatsInS1) {
                    seatNumberToBeSentInS2++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";

                    // Set up path variables for S2
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "Sleeper");
                    updateUriVariables.put("coach", "S2");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInS2));

                    ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                            seatNumberUpdateUrl,
                            HttpMethod.PUT,
                            null,
                            String.class,
                            updateUriVariables
                    );
                }
                if (lastSeatNumber <= totalSeatsInS1 && lastSeatNumber > 0) {
                    seatNumberToBeSentInS1++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                    // Set up path variables for S1
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "Sleeper");
                    updateUriVariables.put("coach", "S1");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInS1));

                    try {
                        ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                                seatNumberUpdateUrl,
                                HttpMethod.PUT,
                                null,
                                String.class,
                                updateUriVariables
                        );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                            // Handle BAD_REQUEST exception
                            return ResponseEntity.status(HttpStatus.OK).body("Your tickets has been canceled successfully !!");
                        } else {
                            // Handle other exceptions or rethrow them if needed
                            throw e;
                        }
                    }
                }

            }
        } else if (seatClass.equals("General")) {
            List<Ticket> generalTickets = this.ticketRepository.findAllBySeatClass("General");
            if (!generalTickets.isEmpty()) {

                // Separate based on coach values (D1, D2, D3)
                List<Ticket> d1Tickets = filterTicketsByCoach(generalTickets, "D1");
                List<Ticket> d2Tickets = filterTicketsByCoach(generalTickets, "D2");
                List<Ticket> d3Tickets = filterTicketsByCoach(generalTickets, "D3");

                Integer d1PredefinedSeats = seatMap.get("D1");
                Integer d2PredefinedSeats = seatMap.get("D2") + d1PredefinedSeats;
                Integer d3PredefinedSeats = seatMap.get("D3") + d2PredefinedSeats;

                AtomicInteger d1CurrentSeatNumber = new AtomicInteger(1);
                AtomicInteger d2CurrentSeatNumber = new AtomicInteger(1);
                AtomicInteger d3CurrentSeatNumber = new AtomicInteger(1);


                generalTickets.forEach(ticket -> Collections.sort(ticket.getPassengers(), Comparator.comparingInt(Passenger::getSeatNumber)));

                // Sorting the list of tickets based on the seat number of the first passenger (assuming each ticket has at least one passenger)
//                Collections.sort(generalTickets, Comparator.comparingInt(ticket -> ticket.getPassengers().get(0).getSeatNumber()));

                // Sorting the list of tickets based on the seat number of the first passenger
                generalTickets.sort(Comparator.comparingInt(ticket ->
                        ticket.getPassengers().isEmpty() ? Integer.MAX_VALUE : ticket.getPassengers().get(0).getSeatNumber()
                ));

                generalTickets.forEach(System.out::println);
                AtomicBoolean isDeleted = new AtomicBoolean(false);
                generalTickets.stream()
                        .filter(generalTicket -> generalTicket != ticketsFound && !generalTicket.getPassengers().isEmpty())
                        .forEach(generalTicket -> {
                            System.out.println("generalTicket = " + generalTicket);

                            if (d1CurrentSeatNumber.get() <= d1PredefinedSeats) {
                                generalTicket.getPassengers().get(0)
                                        .setSeatNumber(d1CurrentSeatNumber.getAndIncrement());
                                generalTicket.setCoach("D1");
                                generalTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(generalTicket.getPassengers().get(0));
                                Ticket savedTicketNewUpdated = this.ticketRepository.save(generalTicket);
                                this.ticketRepository.delete(ticketsFound);
                                isDeleted.set(true);
                                System.out.println("savedTicketNewUpdated = " + savedTicketNewUpdated);
                                d2CurrentSeatNumber.set(d1CurrentSeatNumber.get());
                            } else if (d2CurrentSeatNumber.get() <= d2PredefinedSeats) {
                                generalTicket.getPassengers().get(0)
                                        .setSeatNumber(d2CurrentSeatNumber.getAndIncrement());
                                generalTicket.setCoach("D2");
                                generalTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(generalTicket.getPassengers().get(0));
                                this.ticketRepository.save(generalTicket);
                                d3CurrentSeatNumber.set(d2CurrentSeatNumber.get());
                            } else if (d3CurrentSeatNumber.get() <= d3PredefinedSeats) {
                                generalTicket.getPassengers().get(0)
                                        .setSeatNumber(d3CurrentSeatNumber.getAndIncrement());
                                generalTicket.setCoach("D3");
                                generalTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(generalTicket.getPassengers().get(0));
                                this.ticketRepository.save(generalTicket);
                            } else {
                                generalTicket.getPassengers().get(0)
                                        .setSeatNumber(d3CurrentSeatNumber.getAndIncrement());
                                generalTicket.setCoach("D3");
                                generalTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.WAITING)));
                                this.passengerRepository.save(generalTicket.getPassengers().get(0));
                                this.ticketRepository.save(generalTicket);
                            }
                        });

                if (!isDeleted.get()) {
                    this.ticketRepository.delete(ticketsFound);
                }

                Integer totalSeatsInD1 = seatMap.get("D1");
                Integer totalSeatsInD2 = seatMap.get("D2") + totalSeatsInD1;
                Integer totalSeatsInD3 = seatMap.get("D3") + totalSeatsInD2;

                Integer lastSeatNumber = this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketsFound.getTrainNumber());
                lastSeatNumber = (lastSeatNumber == null) ? 1 : lastSeatNumber + 1; // i have added 1 as one record is deleted from the table using pnr number
                Integer seatNumberToBeSentInD1 = 0;
                Integer seatNumberToBeSentInD2 = 0;
                Integer seatNumberToBeSentInD3 = 0;
                if (lastSeatNumber <= totalSeatsInD3 && lastSeatNumber > totalSeatsInD2) {

                    seatNumberToBeSentInD3++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";

                    // Set up path variables for S3
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "General");
                    updateUriVariables.put("coach", "D3");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInD3));

                    ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                            seatNumberUpdateUrl,
                            HttpMethod.PUT,
                            null,
                            String.class,
                            updateUriVariables
                    );
                }
                if (lastSeatNumber <= totalSeatsInD2 && lastSeatNumber > totalSeatsInD1) {

                    seatNumberToBeSentInD2++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";

                    // Set up path variables for S2
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "General");
                    updateUriVariables.put("coach", "D2");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInD2));

                    ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                            seatNumberUpdateUrl,
                            HttpMethod.PUT,
                            null,
                            String.class,
                            updateUriVariables
                    );
                }
                if (lastSeatNumber <= totalSeatsInD1 && lastSeatNumber > 0) {

                    seatNumberToBeSentInD1++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                    // Set up path variables for S1
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "General");
                    updateUriVariables.put("coach", "D1");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInD1));

                    try {
                        ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                                seatNumberUpdateUrl,
                                HttpMethod.PUT,
                                null,
                                String.class,
                                updateUriVariables
                        );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                            // Handle BAD_REQUEST exception
                            return ResponseEntity.status(HttpStatus.OK).body("Your tickets has been canceled successfully !!");
                        } else {
                            // Handle other exceptions or rethrow them if needed
                            throw e;
                        }
                    }
                }
            }
        } else {
            List<Ticket> acTickets = this.ticketRepository.findAllBySeatClass("AC");
            if (!acTickets.isEmpty()) {
                // Separate based on coach values (AC1, AC2, AC3)
                List<Ticket> ac1Tickets = filterTicketsByCoach(acTickets, "AC1");
                List<Ticket> ac2Tickets = filterTicketsByCoach(acTickets, "AC2");
                List<Ticket> ac3Tickets = filterTicketsByCoach(acTickets, "AC3");

                Integer ac1PredefinedSeats = seatMap.get("AC1");
                Integer ac2PredefinedSeats = seatMap.get("AC2") + ac1PredefinedSeats;
                Integer ac3PredefinedSeats = seatMap.get("AC3") + ac2PredefinedSeats;

                AtomicInteger ac1CurrentSeatNumber = new AtomicInteger(1);
                AtomicInteger ac2CurrentSeatNumber = new AtomicInteger(1);
                AtomicInteger ac3CurrentSeatNumber = new AtomicInteger(1);


                acTickets.forEach(ticket -> Collections.sort(ticket.getPassengers(), Comparator.comparingInt(Passenger::getSeatNumber)));

                // Sorting the list of tickets based on the seat number of the first passenger (assuming each ticket has at least one passenger)
//                Collections.sort(acTickets, Comparator.comparingInt(ticket -> ticket.getPassengers().get(0).getSeatNumber()));

                acTickets.sort(Comparator.comparingInt(ticket ->
                        ticket.getPassengers().isEmpty() ? Integer.MAX_VALUE : ticket.getPassengers().get(0).getSeatNumber()
                ));
                AtomicBoolean isDeleted = new AtomicBoolean(false);
                acTickets.forEach(System.out::println);
                acTickets.stream()
                        .filter(acTicket -> acTicket != ticketsFound && !acTicket.getPassengers().isEmpty())
                        .forEach(acTicket -> {
                            System.out.println("acTicket = " + acTicket);

                            if (ac1CurrentSeatNumber.get() <= ac1PredefinedSeats) {
                                acTicket.getPassengers().get(0)
                                        .setSeatNumber(ac1CurrentSeatNumber.getAndIncrement());
                                acTicket.setCoach("AC1");
                                acTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(acTicket.getPassengers().get(0));
                                Ticket savedTicketNewUpdated = this.ticketRepository.save(acTicket);
                                this.ticketRepository.delete(ticketsFound);
                                System.out.println("savedTicketNewUpdated = " + savedTicketNewUpdated);
                                ac2CurrentSeatNumber.set(ac1CurrentSeatNumber.get());
                                isDeleted.set(true);
                            } else if (ac2CurrentSeatNumber.get() <= ac2PredefinedSeats) {
                                acTicket.getPassengers().get(0)
                                        .setSeatNumber(ac2CurrentSeatNumber.getAndIncrement());
                                acTicket.setCoach("AC2");
                                acTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(acTicket.getPassengers().get(0));
                                this.ticketRepository.save(acTicket);
                                ac3CurrentSeatNumber.set(ac2CurrentSeatNumber.get());
                            } else if (ac3CurrentSeatNumber.get() <= ac3PredefinedSeats) {
                                acTicket.getPassengers().get(0)
                                        .setSeatNumber(ac3CurrentSeatNumber.getAndIncrement());
                                acTicket.setCoach("AC3");
                                acTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)));
                                this.passengerRepository.save(acTicket.getPassengers().get(0));
                                this.ticketRepository.save(acTicket);
                            } else {
                                acTicket.getPassengers().get(0)
                                        .setSeatNumber(ac3CurrentSeatNumber.getAndIncrement());
                                acTicket.setCoach("AC3");
                                acTicket.setStatuses(new ArrayList<>(List.of(TicketStatus.WAITING)));
                                this.passengerRepository.save(acTicket.getPassengers().get(0));
                                this.ticketRepository.save(acTicket);
                            }
                        });

                if (!isDeleted.get()) {
                    this.ticketRepository.delete(ticketsFound);
                }

                Integer totalSeatsInAC1 = seatMap.get("AC1");
                Integer totalSeatsInAC2 = seatMap.get("AC2") + totalSeatsInAC1;
                Integer totalSeatsInAC3 = seatMap.get("AC3") + totalSeatsInAC2;

                Integer lastSeatNumber = this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketsFound.getTrainNumber());
                lastSeatNumber = (lastSeatNumber == null) ? 1 : lastSeatNumber + 1; // i have added 1 as one record is deleted from the table using pnr number

                Integer seatNumberToBeSentInAC1 = 0;
                Integer seatNumberToBeSentInAC2 = 0;
                Integer seatNumberToBeSentInAC3 = 0;

                if (lastSeatNumber <= totalSeatsInAC3 && lastSeatNumber > totalSeatsInAC2) {

                    seatNumberToBeSentInAC3++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";

                    // Set up path variables for S3
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "AC");
                    updateUriVariables.put("coach", "AC3");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInAC3));

                    ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                            seatNumberUpdateUrl,
                            HttpMethod.PUT,
                            null,
                            String.class,
                            updateUriVariables
                    );
                }
                if (lastSeatNumber <= totalSeatsInAC2 && lastSeatNumber > totalSeatsInAC1) {

                    seatNumberToBeSentInAC2++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";

                    // Set up path variables for S2
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "AC");
                    updateUriVariables.put("coach", "AC2");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInAC2));

                    ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                            seatNumberUpdateUrl,
                            HttpMethod.PUT,
                            null,
                            String.class,
                            updateUriVariables
                    );
                }
                if (lastSeatNumber <= totalSeatsInAC1 && lastSeatNumber > 0) {

                    seatNumberToBeSentInAC1++;
                    String seatNumberUpdateUrl = "http://train-service/train-service/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                    // Set up path variables for S1
                    Map<String, String> updateUriVariables = new HashMap<>();
                    updateUriVariables.put("trainNumber", ticketsFound.getTrainNumber());
                    updateUriVariables.put("seatClass", "AC");
                    updateUriVariables.put("coach", "AC1");
                    updateUriVariables.put("noOfSeats", String.valueOf(seatNumberToBeSentInAC1));

                    try {
                        ResponseEntity<String> increaseSeatNumbers = restTemplate.exchange(
                                seatNumberUpdateUrl,
                                HttpMethod.PUT,
                                null,
                                String.class,
                                updateUriVariables
                        );
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                            // Handle BAD_REQUEST exception
                            return ResponseEntity.status(HttpStatus.OK).body("Your tickets has been canceled successfully !!");
                        } else {
                            // Handle other exceptions or rethrow them if needed
                            throw e;
                        }
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Your tickets has been canceled successfully !!");
    }

    private static List<Ticket> filterTicketsByCoach(List<Ticket> tickets, String coach) {
        return tickets.stream()
                .filter(ticket -> coach.equals(ticket.getCoach()))
                .collect(Collectors.toList());
    }

    private Integer calculateEndingSeatNumber(Ticket ticket, int canceledSeatCount) {
        // Your logic to calculate the ending seat number based on the ticket and canceled seat count
        // Replace the following line with your actual implementation
//        return ticket.getStaringSeatNumber() + canceledSeatCount - 1;
        return null;
    }


    private void cancelSeat(Ticket ticket, int canceledSeatNumber) {
        // Find the passenger with the canceled seat number
//        Passenger canceledPassenger = ticket.getPassengers().stream()
//                .filter(passenger -> passenger.getSeatNumber() == canceledSeatNumber)
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Passenger not found with seat number: " + canceledSeatNumber));
//        ticket.getPassengers().remove(canceledPassenger);
        this.ticketRepository.save(ticket);
    }


    private void bookSeatSequentially(String seatClass,
                                      int numberOfTickets,
                                      TicketRequestDTO ticketRequestDTO,
                                      PassengerRequestDTO passenger,
                                      String coach,
                                      String status) throws ParseException {

        String url = "http://train-service/train-service/find-source-and-destination-by/train-number/" + ticketRequestDTO.getTrainNumber();
        ResponseEntity<Map<String, String>> responseEntity = null;
        Map<String, String> responseBody = null;
        try {
            responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, String>>() {
                            }
                    );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                responseBody = responseEntity.getBody();
                // Process the response body as needed
                System.out.println("Response: " + responseBody);
            } else {
                // Handle unexpected HTTP status codes
                System.out.println("Unexpected HTTP Status Code: " + responseEntity.getStatusCode());
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Handle 404 Not Found error
            System.out.println("Resource not found: " + e.getMessage());
        } catch (HttpClientErrorException.BadRequest e) {
            // Handle 400 Bad Request error
            System.out.println("Bad Request: " + e.getMessage());
        } catch (HttpClientErrorException.Unauthorized e) {
            // Handle 401 Unauthorized error
            System.out.println("Unauthorized: " + e.getMessage());
        } catch (HttpClientErrorException.Forbidden e) {
            // Handle 403 Forbidden error
            System.out.println("Forbidden: " + e.getMessage());
        } catch (HttpServerErrorException.InternalServerError e) {
            // Handle 500 Internal Server Error
            System.out.println("Internal Server Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("e = " + e);
            // Handle other exceptions
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        List<Ticket> ticketByTrainNumber
                = this.ticketRepository.findByTrainNumber(ticketRequestDTO.getTrainNumber());


        if (!ticketByTrainNumber.isEmpty()) {

            if (TICKET_CONFIRMED.equals(status)) {

                Integer byEndingSeatNumber = this.passengerRepository.findByEndingSeatNumberAndSeatClassNativeQuery(ticketRequestDTO
                        .getTrainNumber(), seatClass);

                if (byEndingSeatNumber != null) {

                    Ticket ticket = Ticket.builder()
                            .pnrNumber(generateRandomNumber(10))
                            .source(responseBody.get("Source"))
                            .destination(responseBody.get("Destination"))
                            .seatClass(seatClass)
                            .coach(coach)
                            .arrivalTime(LocalTime.parse(responseBody.get("ArrivalTime")))
                            .departureTime(LocalTime.parse(responseBody.get("DepartureTime")))
                            .date((new SimpleDateFormat("yyyy-MM-dd")).parse(ticketRequestDTO.getDate()))
                            .trainNumber(ticketRequestDTO.getTrainNumber())
                            .statuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)))
                            .build();

                    List<Passenger> listOfPassenger = new ArrayList<>();
                    int currentSeatNumber = byEndingSeatNumber + 1;

                    Passenger passengerBuilder = Passenger.builder()
                            .passengerId(generateRandomPassengerId(10))
                            .passengerName(passenger.getPassengerName())
                            .seatNumber(currentSeatNumber)
                            .age(passenger.getAge())
                            .ticket(ticket)
                            .build();
                    listOfPassenger.add(passengerBuilder);

                    ticket.setPassengers(listOfPassenger);
                    this.ticketRepository.save(ticket);

                } else {
                    // this is for new booking

                    Ticket ticket = Ticket.builder()
                            .pnrNumber(generateRandomNumber(10))
                            .source(responseBody.get("Source"))
                            .destination(responseBody.get("Destination"))
                            .seatClass(seatClass)
                            .coach(coach)
                            .arrivalTime(LocalTime.parse(responseBody.get("ArrivalTime")))
                            .departureTime(LocalTime.parse(responseBody.get("DepartureTime")))
                            .date(new SimpleDateFormat("yyyy-MM-dd").parse(ticketRequestDTO.getDate()))
                            .trainNumber(ticketRequestDTO.getTrainNumber())
                            .statuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)))
                            .build();
                    List<Passenger> listOfPassenger = new ArrayList<>();
                    int nextSeatNumber = 0;

                    nextSeatNumber = nextSeatNumber + 1;
                    Passenger passengerBuilder = Passenger.builder()
                            .passengerId(generateRandomPassengerId(10))
                            .passengerName(passenger.getPassengerName())
                            .seatNumber(nextSeatNumber)
                            .age(passenger.getAge())
                            .ticket(ticket)
                            .build();
                    listOfPassenger.add(passengerBuilder);

                    ticket.setPassengers(listOfPassenger);
                    this.ticketRepository.save(ticket);
                }

            } else {

                Integer byEndingSeatNumber = this.passengerRepository.findByEndingSeatNumberAndSeatClassNativeQuery(ticketRequestDTO.getTrainNumber(), seatClass);

                Ticket ticket = Ticket.builder()
                        .pnrNumber(generateRandomNumber(10))
                        .source(responseBody.get("Source"))
                        .destination(responseBody.get("Destination"))
                        .seatClass(seatClass)
                        .coach(coach)
                        .arrivalTime(LocalTime.parse(responseBody.get("ArrivalTime")))
                        .departureTime(LocalTime.parse(responseBody.get("DepartureTime")))
                        .date((new SimpleDateFormat("yyyy-MM-dd")).parse(ticketRequestDTO.getDate()))
                        .trainNumber(ticketRequestDTO.getTrainNumber())
                        .statuses(new ArrayList<>(List.of(TicketStatus.WAITING)))
                        .build();
                List<Passenger> listOfPassenger = new ArrayList<>();
                int currentSeatNumber = byEndingSeatNumber + 1;

                Passenger passengerBuilder = Passenger.builder()
                        .passengerId(generateRandomPassengerId(10))
                        .passengerName(passenger.getPassengerName())
                        .seatNumber(currentSeatNumber)
                        .age(passenger.getAge())
                        .ticket(ticket)
                        .build();
                listOfPassenger.add(passengerBuilder);

                ticket.setPassengers(listOfPassenger);
                this.ticketRepository.save(ticket);
            }

        } else {

            // this is for new booking

            Ticket ticket = Ticket.builder()
                    .pnrNumber(generateRandomNumber(10))
                    .source(responseBody.get("Source"))
                    .destination(responseBody.get("Destination"))
                    .seatClass(seatClass)
                    .coach(coach)
                    .arrivalTime(LocalTime.parse(responseBody.get("ArrivalTime")))
                    .departureTime(LocalTime.parse(responseBody.get("DepartureTime")))
                    .date(new SimpleDateFormat("yyyy-MM-dd").parse(ticketRequestDTO.getDate()))
                    .trainNumber(ticketRequestDTO.getTrainNumber())
                    .statuses(new ArrayList<>(List.of(TicketStatus.CONFIRMED)))
                    .build();
            List<Passenger> listOfPassenger = new ArrayList<>();
            int nextSeatNumber = 0;

            nextSeatNumber = nextSeatNumber + 1;
            Passenger passengerBuilder = Passenger.builder()
                    .passengerId(generateRandomPassengerId(10))
                    .passengerName(passenger.getPassengerName())
                    .seatNumber(nextSeatNumber)
                    .age(passenger.getAge())
                    .ticket(ticket)
                    .build();
            listOfPassenger.add(passengerBuilder);

            ticket.setPassengers(listOfPassenger);
            this.ticketRepository.save(ticket);

        }
        System.out.println(seatClass + " " + numberOfTickets + " " + ticketRequestDTO + " " + passenger + " " + coach + " " + status);
    }


    private String generateRandomPassengerId(int length) {
        return "PSN" + generateRandomNumber(length);
    }

    private String generateRandomNumber(int length) {
        // Implement your logic to generate a random number with the specified length
        // For simplicity, you can use a random number generator
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            stringBuilder.append(digit);
        }
        return stringBuilder.toString();
    }
}
