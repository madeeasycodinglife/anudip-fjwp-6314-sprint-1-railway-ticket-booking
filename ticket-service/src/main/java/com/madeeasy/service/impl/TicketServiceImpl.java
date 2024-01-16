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
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final RestTemplate restTemplate;


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
                            bookSeatSequentially(
                                    availableSeats.get("S1"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "S1");
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "S1");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else if (availableSeats.get("S2") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("S1"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "S2");
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "S2");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else if (availableSeats.get("S3") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("S1"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "S3");
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "S3");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No seats available.");
                        }
                    } else if (ticketRequestDTO.getSeatClass().equals("General")) {
                        if (availableSeats.get("D1") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("D1"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "D1");
                            // make a rest call to train-service to reduce seat number
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "D1");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else if (availableSeats.get("D2") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("D2"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "D2");
                            // make a rest call to train-service to reduce seat number
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "D2");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else if (availableSeats.get("D3") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("D3"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "D3");
                            // make a rest call to train-service to reduce seat number
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "D3");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No seats available.");
                        }
                    } else {
                        if (availableSeats.get("AC1") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("AC1"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "AC1");
                            // make a rest call to train-service to reduce seat number
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "AC1");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else if (availableSeats.get("AC2") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("AC2"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "AC2");
                            // make a rest call to train-service to reduce seat number
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "AC2");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else if (availableSeats.get("AC3") > 0) {
                            bookSeatSequentially(
                                    availableSeats.get("AC3"),
                                    ticketRequestDTO.getSeatClass(),
                                    numberOfTickets,
                                    ticketRequestDTO,
                                    "AC3");
                            // make a rest call to train-service to reduce seat number
                            String urlToReduceSeatNumbers = "http://train-service/train-service/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}";
                            // Set up path variables
                            Map<String, Object> uriVariables = new HashMap<>();
                            uriVariables.put("trainNumber", ticketRequestDTO.getTrainNumber());
                            uriVariables.put("seatClass", ticketRequestDTO.getSeatClass());
                            uriVariables.put("coach", "AC3");
                            uriVariables.put("noOfSeats", numberOfTickets);

                            // Make the PUT request
                            ResponseEntity<String> response = restTemplate.exchange(
                                    urlToReduceSeatNumbers,
                                    HttpMethod.PUT,
                                    null,
                                    String.class,
                                    uriVariables
                            );
                            return ResponseEntity.status(HttpStatus.OK).body("Seats booked successfully.");
                        } else {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No seats available.");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
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

    private void bookSeatSequentially(Integer s1,
                                      String seatClass,
                                      int numberOfTickets,
                                      TicketRequestDTO ticketRequestDTO,
                                      String coach) throws ParseException {

        String url = "http://train-service/train-service/find-source-and-destination-by/train-number/" + ticketRequestDTO.getTrainNumber();
        Map<String, String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {
                }
        ).getBody();

        int startingSeatNumber = 0;

        Ticket ticketByTrainNumber = this.ticketRepository.findByTrainNumber(ticketRequestDTO.getTrainNumber()).orElse(null);

        if (ticketByTrainNumber != null) {

            Integer byEndingSeatNumber = this.ticketRepository.findByEndingSeatNumberNativeQuery(ticketByTrainNumber.getTrainNumber());
            int nextSeatNumber = byEndingSeatNumber + 1;


            Ticket ticket = Ticket.builder()
                    .pnrNumber(generateRandomNumber(10))
                    .source(responseEntity.get("Source"))
                    .destination(responseEntity.get("Destination"))
                    .seatClass(seatClass)
                    .coach(coach)
                    .staringSeatNumber(nextSeatNumber)
                    .endingSeatNumber(nextSeatNumber + (numberOfTickets - 1))
                    .arrivalTime(LocalTime.parse(responseEntity.get("ArrivalTime")))
                    .departureTime(LocalTime.parse(responseEntity.get("DepartureTime")))
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
            Ticket ticket = Ticket.builder()
                    .pnrNumber(generateRandomNumber(10))
                    .source(responseEntity.get("Source"))
                    .destination(responseEntity.get("Destination"))
                    .seatClass(seatClass)
                    .coach(coach)
                    .staringSeatNumber(startingSeatNumber + 1)
                    .endingSeatNumber(startingSeatNumber + numberOfTickets)
                    .arrivalTime(LocalTime.parse(responseEntity.get("ArrivalTime")))
                    .departureTime(LocalTime.parse(responseEntity.get("DepartureTime")))
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
