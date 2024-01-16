package com.madeeasy.service;

import com.madeeasy.dto.request.TrainRequestDTO;
import com.madeeasy.dto.response.TrainResponseDTO;
import com.madeeasy.entity.Train;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TrainService {

    List<TrainResponseDTO> getAllTrains();

    TrainResponseDTO getTrainById(String trainId);

    TrainResponseDTO createTrain(TrainRequestDTO trainRequestDTO);

    TrainResponseDTO updateTrain(String trainId, TrainRequestDTO trainRequestDTO);

    void deleteTrain(String trainId);

    List<TrainResponseDTO> searchTrains(String sourceStation, String destinationStation);

    Map<String, Integer> checkNumberOfAvailableSeats(String trainId, String seatClass);

    ResponseEntity<?> findByTrainNumber(String trainNumber);

    ResponseEntity<?> reduceSeatNumbers(String trainNumber, String seatClass, String coach, int noOfSeats);

    ResponseEntity<?> addSeatNumbers(String trainNumber, String seatClass, String coach, int noOfSeats);

    List<TrainResponseDTO> getTrainsBySourceAndDestination(String source, String destination);
}

