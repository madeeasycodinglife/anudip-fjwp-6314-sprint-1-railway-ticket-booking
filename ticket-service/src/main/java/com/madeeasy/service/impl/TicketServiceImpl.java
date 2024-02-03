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

                            if (availableSeats.get("S1") == 1 || availableSeats.get("S2") >= 1 || availableSeats.get("S3") >= 1) {

                                Map<String, Integer> newAvailableSeats = availableSeats;
                                AtomicBoolean isBooked = new AtomicBoolean(false);
                                ticketRequestDTO.getPassengers()
                                        .forEach(passenger -> {
                                            if (newAvailableSeats.get("S1") == 1) {
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
                                                newAvailableSeats.put("S1", 0);
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
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                System.out.println("run count : ");
                                            }
                                        });
                                if (!isBooked.get())
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
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            System.out.println("run count : ");
                                        }

                                    });
                            if (!isBooked.get())
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
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                            if (!isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else {
                            // handle waiting tickets
                            bookSeatSequentially(
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "S3",
                                    TICKET_WAITING);
                            if (numberOfTickets == 1) {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seat is in waiting state !!");
                            }
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seats are in waiting state !!");
                        }
                    } else if (ticketRequestDTO.getSeatClass().equals("General")) {

                        if (availableSeats.get("D1") > 0) {

                            if (availableSeats.get("D1") == 1 || availableSeats.get("D2") >= 1 || availableSeats.get("D3") >= 1) {

                                Map<String, Integer> newAvailableSeats = availableSeats;
                                AtomicBoolean isBooked = new AtomicBoolean(false);
                                ticketRequestDTO.getPassengers()
                                        .forEach(passenger -> {
                                            if (newAvailableSeats.get("D1") == 1) {
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
                                                newAvailableSeats.put("D1", 0);
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
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                System.out.println("run count : ");
                                            }
                                        });
                                if (!isBooked.get())
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
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            System.out.println("run count : ");
                                        }

                                    });
                            if (!isBooked.get())
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
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                            if (!isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else {
                            // handle waiting tickets
                            bookSeatSequentially(
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "D3",
                                    TICKET_WAITING);
                            if (numberOfTickets == 1) {
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seat is in waiting state !!");
                            }
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your seats are in waiting state !!");
                        }

                    } else {
                        if (availableSeats.get("AC1") > 0) {

                            if (availableSeats.get("AC1") == 1 || availableSeats.get("AC2") >= 1 || availableSeats.get("AC3") >= 1) {

                                Map<String, Integer> newAvailableSeats = availableSeats;
                                AtomicBoolean isBooked = new AtomicBoolean(false);
                                ticketRequestDTO.getPassengers()
                                        .forEach(passenger -> {
                                            if (newAvailableSeats.get("AC1") == 1) {
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
                                                newAvailableSeats.put("AC1", 0);
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
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                System.out.println("run count : ");
                                            }
                                        });
                                if (!isBooked.get())
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
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                            System.out.println("run count : ");
                                        }

                                    });
                            if (!isBooked.get())
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
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                            if (!isBooked.get())
                                return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                            else
                                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Some seats are confirmed and some are waiting !!");
                        } else {
                            // handle waiting tickets
                            bookSeatSequentially(
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "AC3",
                                    TICKET_WAITING);
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


        List<Integer> canceledSeatNumbers = new ArrayList<Integer>();

        ticketsFound.getPassengers().forEach(passenger -> {
            canceledSeatNumbers.add(passenger.getSeatNumber());
        });

        // for ascending order
        Collections.sort(canceledSeatNumbers);

        this.ticketRepository.delete(ticketsFound);

        int lastIndex = canceledSeatNumbers.size() - 1;
        Integer lastSeatNumber = canceledSeatNumbers.get(lastIndex);

        List<Ticket> ticketsBySeatNumberGreaterThan = this.ticketRepository
                .findTicketsBySeatNumberGreaterThan(lastSeatNumber);

        final Integer[] firstPositionOfCancelledSeat = {canceledSeatNumbers.get(0)};

        ticketsBySeatNumberGreaterThan.forEach(ticket -> {

            boolean currentSeatPositionModified = false;

            if (ticket.getPassengers().size() == canceledSeatNumbers.size()) {

//                ticket.setStaringSeatNumber(firstPositionOfCancelledSeat[0]);
//                ticket.setEndingSeatNumber(lastSeatNumber);


                for (Passenger passenger : ticket.getPassengers()) {
                    passenger.setSeatNumber(firstPositionOfCancelledSeat[0]);
                    firstPositionOfCancelledSeat[0]++;
                }

                currentSeatPositionModified = true;

            } else if (ticket.getPassengers().size() > canceledSeatNumbers.size()) {

//                ticket.setStaringSeatNumber(firstPositionOfCancelledSeat[0]);
//                ticket.setEndingSeatNumber(firstPositionOfCancelledSeat[0] + ticket.getPassengers().size());

                for (Passenger passenger : ticket.getPassengers()) {
                    passenger.setSeatNumber(firstPositionOfCancelledSeat[0]++);
                }

            } else {
                // Handle the case where ticket.getPassengers().size() < canceledSeatNumbers.size()
                // Adjusting seat numbers based on the difference in sizes
                int difference = canceledSeatNumbers.size() - ticket.getPassengers().size();
//                ticket.setStaringSeatNumber(firstPositionOfCancelledSeat[0]);
//                ticket.setEndingSeatNumber(firstPositionOfCancelledSeat[0] + ticket.getPassengers().size() - 1 == 0
//                        ? 1 : firstPositionOfCancelledSeat[0] + ticket.getPassengers().size() - 1);

                // Set seat numbers for the remaining passengers
                for (Passenger passenger : ticket.getPassengers()) {
                    passenger.setSeatNumber(firstPositionOfCancelledSeat[0]++);
                }
            }

            if (currentSeatPositionModified) {

            }

        });


//
//        for (int canceledSeatNumber : canceledSeatNumbers) {
//            cancelSeat(ticket, canceledSeatNumber);
//        }
        return ResponseEntity.status(HttpStatus.OK).body("Your tickets has been canceled successfully !!");
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

                Integer byEndingSeatNumber = this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketRequestDTO
                        .getTrainNumber());

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

                Integer byEndingSeatNumber = this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketRequestDTO.getTrainNumber());

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


    /**
     * Method to book tickets sequentially
     */

    private void bookSeatSequentially(String seatClass,
                                      int numberOfTickets,
                                      TicketRequestDTO ticketRequestDTO,
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
            // Handle other exceptions
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        System.out.println("responseEntity = " + responseEntity);

        List<Ticket> ticketByTrainNumber = this.ticketRepository.findByTrainNumber(ticketRequestDTO.getTrainNumber());

        if (!ticketByTrainNumber.isEmpty()) {

            if (TICKET_CONFIRMED.equals(status)) {

                Integer byEndingSeatNumber = this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketRequestDTO.getTrainNumber());
                int nextSeatNumber = byEndingSeatNumber + 1;

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
                for (PassengerRequestDTO passengerRequestDTO : ticketRequestDTO.getPassengers()) {
                    Passenger passenger = Passenger.builder()
                            .passengerId(generateRandomPassengerId(10))
                            .passengerName(passengerRequestDTO.getPassengerName())
                            .seatNumber(currentSeatNumber)
                            .age(passengerRequestDTO.getAge())
                            .ticket(ticket)
                            .build();
                    listOfPassenger.add(passenger);
                    currentSeatNumber = currentSeatNumber + 1;
                }

                ticket.setPassengers(listOfPassenger);
                this.ticketRepository.save(ticket);

            } else {

                Integer byEndingSeatNumber =
                        this.passengerRepository.findByEndingSeatNumberNativeQuery(ticketRequestDTO.getTrainNumber());
                int nextSeatNumber = byEndingSeatNumber + 1;

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
                for (PassengerRequestDTO passengerRequestDTO : ticketRequestDTO.getPassengers()) {
                    Passenger passenger = Passenger.builder()
                            .passengerId(generateRandomPassengerId(10))
                            .passengerName(passengerRequestDTO.getPassengerName())
                            .seatNumber(currentSeatNumber)
                            .age(passengerRequestDTO.getAge())
                            .ticket(ticket)
                            .build();
                    listOfPassenger.add(passenger);
                    currentSeatNumber = currentSeatNumber + 1;
                }

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
            for (PassengerRequestDTO passengerRequestDTO : ticketRequestDTO.getPassengers()) {
                nextSeatNumber = nextSeatNumber + 1;
                Passenger passenger = Passenger.builder()
                        .passengerId(generateRandomPassengerId(10))
                        .passengerName(passengerRequestDTO.getPassengerName())
                        .seatNumber(nextSeatNumber)
                        .age(passengerRequestDTO.getAge())
                        .ticket(ticket)
                        .build();
                listOfPassenger.add(passenger);
            }

            ticket.setPassengers(listOfPassenger);
            this.ticketRepository.save(ticket);

        }
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
