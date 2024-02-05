package com.madeeasy.service.impl;

import com.madeeasy.dto.request.TrainRequestDTO;
import com.madeeasy.dto.response.TrainClassResponseDTO;
import com.madeeasy.dto.response.TrainCoachResponseDTO;
import com.madeeasy.dto.response.TrainResponseDTO;
import com.madeeasy.dto.response.TrainStopResponseDTO;
import com.madeeasy.entity.Coach;
import com.madeeasy.entity.Train;
import com.madeeasy.entity.TrainClass;
import com.madeeasy.entity.TrainStop;
import com.madeeasy.exception.TrainNotFoundException;
import com.madeeasy.repository.CoachRepository;
import com.madeeasy.repository.TrainRepository;
import com.madeeasy.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {

    private final TrainRepository trainRepository;
    private final CoachRepository coachRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @Override
    public List<TrainResponseDTO> getAllTrains() {
        List<Train> trains = this.trainRepository.findAll();
        if (trains.isEmpty()) {
            throw new TrainNotFoundException("No trains found");
        }
        return trains.stream()
                .map(this::convertToTrainResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TrainResponseDTO getTrainById(String trainId) {
        Train train = this.trainRepository.findById(trainId)
                .orElseThrow(() -> new TrainNotFoundException("Train not found for train id : " + trainId));
        return convertToTrainResponseDTO(train);
    }

    @Override
    public TrainResponseDTO createTrain(TrainRequestDTO trainRequestDTO) {

        // check where the given station exists or not if exists then allow for further processing.

        Train train = mapToTrain(trainRequestDTO);
        Train savedTrain = trainRepository.save(train);
        return convertToTrainResponseDTO(savedTrain);
    }

    private Train mapToTrain(TrainRequestDTO trainRequestDTO) {
        Train train = Train.builder()
                .trainId(generateRandomTrainId())
                .trainNumber(generateRandomNumber(6))
                .trainName(trainRequestDTO.getTrainName())
                .startingStation(trainRequestDTO.getStartingStation())
                .endingStation(trainRequestDTO.getEndingStation())
                .trainDescription(trainRequestDTO.getTrainDescription())
                .build();

        List<TrainClass> trainClasses = trainRequestDTO.getClasses().stream()
                .map(classDto -> {
                    TrainClass trainClass = TrainClass.builder()
                            .classId(generateRandomClassId())
                            .className(classDto.getClassName())
                            .totalSeats(classDto.getTotalSeats())
                            .train(train)
                            .build();

                    List<Coach> coaches = classDto.getCoaches().stream()
                            .map(coachDto -> Coach.builder()
                                    .coachId(generateRandomCoachId())
                                    .name(coachDto.getName())
                                    .seats(coachDto.getSeats())
                                    .availableSeats(coachDto.getSeats())
                                    .trainClass(trainClass)
                                    .build())
                            .collect(Collectors.toList());

                    trainClass.setCoach(coaches);


                    return trainClass;
                })
                .collect(Collectors.toList());

        List<TrainStop> trainStops = trainRequestDTO.getStops().stream()
                .map(stopDto -> TrainStop.builder()
                        .stopId(generateRandomStopId())
                        .stationName(stopDto.getStationName())
                        .arrivalTime(LocalTime.parse(stopDto.getArrivalTime()))
                        .departureTime(LocalTime.parse(stopDto.getDepartureTime()))
                        .train(train)
                        .build())
                .collect(Collectors.toList());

        train.setClasses(trainClasses);
        train.setStops(trainStops);

        return train;
    }


    private TrainResponseDTO convertToTrainResponseDTO(Train train) {

        return TrainResponseDTO.builder()
                .trainId(train.getTrainId())
                .trainName(train.getTrainName())
                .trainNumber(train.getTrainNumber())
                .startingStation(train.getStartingStation())
                .endingStation(train.getEndingStation())
                .trainDescription(train.getTrainDescription())
                .classes(convertTrainClassesToDTO(train.getClasses()))
                .stops(convertTrainStopsToDTO(train.getStops()))
                .build();
    }


    private List<TrainClassResponseDTO> convertTrainClassesToDTO(List<TrainClass> trainClasses) {
        return trainClasses.stream()
                .map(this::convertTrainClassToDTO)
                .collect(Collectors.toList());
    }


    private TrainClassResponseDTO convertTrainClassToDTO(TrainClass trainClass) {
        List<TrainCoachResponseDTO> coachResponseDTOS = trainClass.getCoach()
                .stream()
                .map(coach -> TrainCoachResponseDTO.builder()
                        .coachId(coach.getCoachId())
                        .name(coach.getName())
                        .seats(coach.getSeats())
                        .availableSeats(coach.getAvailableSeats())
                        .build()
                )
                .collect(Collectors.toList());

        return TrainClassResponseDTO.builder()
                .classId(trainClass.getClassId())
                .className(trainClass.getClassName())
                .totalSeats(trainClass.getTotalSeats())
                .coaches(coachResponseDTOS)
                .build();
    }

    private List<TrainStopResponseDTO> convertTrainStopsToDTO(List<TrainStop> trainStops) {
        return trainStops.stream()
                .map(this::convertTrainStopToDTO)
                .collect(Collectors.toList());
    }

    private TrainStopResponseDTO convertTrainStopToDTO(TrainStop trainStop) {
        return TrainStopResponseDTO.builder()
                .stopId(trainStop.getStopId())
                .stationName(trainStop.getStationName())
                .arrivalTime(trainStop.getArrivalTime().toString()) // Convert LocalTime to String
                .departureTime(trainStop.getDepartureTime().toString()) // Convert LocalTime to String
                .build();
    }


    private String generateRandomCoachId() {
        // Implement your logic to generate a random coachId
        // For example, you can use a combination of prefix and a random number
        return "COACH" + generateRandomNumber(6);
    }

    private String generateRandomTrainId() {
        // Implement your logic to generate a random trainId
        // For example, you can use a combination of prefix and a random number
        return "TRN" + generateRandomNumber(6);
    }

    private String generateRandomClassId() {
        // Implement your logic to generate a random classId
        // For example, you can use a combination of prefix and a random number
        return "CLS" + generateRandomNumber(6);
    }

    private String generateRandomStopId() {
        // Implement your logic to generate a random stopId
        // For example, you can use a combination of prefix and a random number
        return "STP" + generateRandomNumber(6);
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

    @Override
    public TrainResponseDTO updateTrain(String trainId, TrainRequestDTO trainRequestDTO) {

        return null;
    }

    @Override
    public void deleteTrain(String trainId) {

    }

    @Override
    public List<TrainResponseDTO> searchTrains(String sourceStation, String destinationStation) {
        List<Train> trainsByStations = this.trainRepository.findTrainsByStations(sourceStation, destinationStation);
        if (trainsByStations.isEmpty())
            throw new TrainNotFoundException("No trains found for starting station " + sourceStation + " and destination station " + destinationStation);
        return trainsByStations.stream()
                .map(this::convertToTrainResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> checkNumberOfAvailableSeats(String trainNumber, String seatClass) {
        Train train = findTrainByNumber(trainNumber);
        TrainClass trainClass = findTrainClassByName(train, seatClass);

        if (trainClass != null) {
            return getAvailableSeatsPerCoach(trainClass);
        } else {
            throw new TrainNotFoundException("No class found with name: " + seatClass + " for train: " + trainNumber);
        }
    }

    @Override
    public ResponseEntity<?> findByTrainNumber(String trainNumber) {
        Train train = this.trainRepository.findTrainByTrainNumber(trainNumber);
        String startingStation = train.getStartingStation();
        String endingStation = train.getEndingStation();
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(
                        "Source", startingStation,
                        "Destination", endingStation,
                        "ArrivalTime", Objects.requireNonNull(train.getStops().stream().map(TrainStop::getArrivalTime).findFirst().orElse(null)),
                        "DepartureTime", Objects.requireNonNull(train.getStops().stream().map(TrainStop::getDepartureTime).findFirst().orElse(null))));
    }

    @Override
    public ResponseEntity<?> reduceSeatNumbers(String trainNumber, String seatClass, String coach, int noOfSeats) {
        Train train = this.trainRepository.findTrainByTrainNumber(trainNumber);
        if (train == null) {
            throw new TrainNotFoundException("No train found for train number " + trainNumber);
        }
        TrainClass trainClass = findTrainClassByName(train, seatClass);
        if (trainClass == null) {
            throw new TrainNotFoundException("No class found with name: " + seatClass + " for train: " + trainNumber);
        }
        Coach coachInClass = findCoachInClass(trainClass, coach);
        if (coachInClass.getAvailableSeats() > 0 && noOfSeats <= coachInClass.getAvailableSeats()) {
            coachInClass.setAvailableSeats(coachInClass.getAvailableSeats() - noOfSeats);
            this.coachRepository.save(coachInClass);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Seats reduced successfully");
        }
        if (noOfSeats > coachInClass.getAvailableSeats()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seats are out of range in the coach");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough available seats in the coach");
    }

    @Override
    public ResponseEntity<?> addSeatNumbers(String trainNumber, String seatClass, String coach, int noOfSeats) {
        Train train = this.trainRepository.findTrainByTrainNumber(trainNumber);
        if (train == null) {
            throw new TrainNotFoundException("No train found for train number " + trainNumber);
        }
        TrainClass trainClass = findTrainClassByName(train, seatClass);
        if (trainClass == null) {
            throw new TrainNotFoundException("No class found with name: " + seatClass + " for train: " + trainNumber);
        }
        Coach coachInClass = findCoachInClass(trainClass, coach);
        if (noOfSeats <= coachInClass.getSeats() && noOfSeats > 0 && coachInClass.getAvailableSeats() < coachInClass.getSeats()
                && (coachInClass.getAvailableSeats() + noOfSeats) <= coachInClass.getSeats()) {
            coachInClass.setAvailableSeats(coachInClass.getAvailableSeats() + noOfSeats);
            this.coachRepository.save(coachInClass);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Seats added successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Seats cannot be added in the coach");
    }

    @Override
    public List<TrainResponseDTO> getTrainsBySourceAndDestination(String source, String destination) {
        List<Train> listOfTrains = this.trainRepository.findTrainsBySourceAndDestination(source, destination);
        return listOfTrains.stream()
                .map(this::convertToTrainResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> checkNumberOfSeats(String trainNumber, String seatClass) {
        Train train = findTrainByNumber(trainNumber);
        TrainClass trainClass = findTrainClassByName(train, seatClass);

        if (trainClass != null) {
            return getSeatsPerCoach(trainClass);
        } else {
            throw new TrainNotFoundException("No class found with name: " + seatClass + " for train: " + trainNumber);
        }
    }

    private Map<String, Integer> getSeatsPerCoach(TrainClass trainClass) {
        Map<String, Integer> coachSeatsMap = new HashMap<>();

        trainClass.getCoach().forEach(coach -> {
            coachSeatsMap.put(coach.getName(), coach.getSeats());
        });
        return coachSeatsMap;
    }

    private Coach findCoachInClass(TrainClass trainClass, String coach) {
        return trainClass.getCoach().stream()
                .filter(c -> c.getName().equals(coach))
                .findFirst()
                .orElse(null);
    }

    private Train findTrainByNumber(String trainNumber) {
        return Optional.ofNullable(trainRepository.findTrainByTrainNumber(trainNumber))
                .orElseThrow(() -> new TrainNotFoundException("No train found for train number " + trainNumber));
    }

    private TrainClass findTrainClassByName(Train train, String seatClass) {
        return train.getClasses().stream()
                .filter(tc -> tc.getClassName().equals(seatClass))
                .findFirst()
                .orElse(null);
    }

    private Map<String, Integer> getAvailableSeatsPerCoach(TrainClass trainClass) {
        Map<String, Integer> coachSeatsMap = new HashMap<>();

        trainClass.getCoach().forEach(coach -> {
            coachSeatsMap.put(coach.getName(), coach.getAvailableSeats());
        });

        return coachSeatsMap;
    }
}
