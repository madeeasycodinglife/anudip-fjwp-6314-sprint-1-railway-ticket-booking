package com.madeeasy.controller;

import com.madeeasy.dto.request.TrainRequestDTO;
import com.madeeasy.dto.response.TrainResponseDTO;
import com.madeeasy.entity.Train;
import com.madeeasy.service.TrainService;
import com.madeeasy.service.impl.TrainServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/train-service")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;
    private final TrainServiceImpl trainServiceImpl;

    @Operation(
            summary = "Get all trains",
            description = "Get all trains",
            tags = {"Get all trains"}

    )
    @GetMapping("/get-all-trains")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    public ResponseEntity<List<TrainResponseDTO>> getAllTrains() {
        List<TrainResponseDTO> trains = trainService.getAllTrains();
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a train by id",
            description = "Get a train by id",
            tags = {"Get train by id"}

    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Train found",
                            content = @Content(schema = @Schema(implementation = TrainResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid id supplied",
                            content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Train not found",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            }
    )
    @GetMapping("/{trainId}")

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TrainResponseDTO> getTrainById(
            @Parameter(description = "id of the train to be searched", required = true)
            @PathVariable String trainId
    ) {
        TrainResponseDTO train = trainService.getTrainById(trainId);
        return new ResponseEntity<>(train, HttpStatus.OK);
    }


    // check number of available seats by train id and seatClass
    @Operation(
            summary = "Check number of available seats",
            description = "Check number of available seats",
            tags = {"Check number of available seats"}
    )
    @GetMapping("/available-seats/{trainNumber}/{seatClass}")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
//    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    public ResponseEntity<?> checkNumberOfAvailableSeats(
            @Parameter(description = "number of the train to be searched", required = true)
            @PathVariable String trainNumber,
            @Parameter(description = "Class of the seat to be searched", required = true)
            @PathVariable String seatClass) {

        Map<String, Integer> availableSeats = trainService.checkNumberOfAvailableSeats(trainNumber, seatClass);
        return new ResponseEntity<>(availableSeats, HttpStatus.OK);
    }

    @GetMapping("/find-source-and-destination-by/train-number/{trainNumber}")
    @Hidden
    ResponseEntity<?> findSourceAndDestinationByTrainNumber(@PathVariable String trainNumber) {
        return this.trainServiceImpl.findByTrainNumber(trainNumber);
    }

    @Operation(
            summary = "Reduce number of available seats",
            description = "Reduce number of available seats",
            tags = {"Reduce number of available seats"}
    )
    @PutMapping("/reduce-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}")
    @Hidden
    public ResponseEntity<?> reduceSeatNumbers(
            @PathVariable String trainNumber,
            @PathVariable String seatClass,
            @PathVariable String coach,
            @PathVariable int noOfSeats) {
        return this.trainService.reduceSeatNumbers(trainNumber, seatClass, coach, noOfSeats);
    }

    @Operation(
            summary = "Increase number of available seats",
            description = "Increase number of available seats",
            tags = {"Increase number of available seats"}
    )
    @PutMapping("/increase-seat-numbers/{trainNumber}/{seatClass}/{coach}/{noOfSeats}")
    @Hidden
    public ResponseEntity<?> addSeatNumbers(
            @PathVariable String trainNumber,
            @PathVariable String seatClass,
            @PathVariable String coach,
            @PathVariable int noOfSeats) {
        return this.trainService.addSeatNumbers(trainNumber, seatClass, coach, noOfSeats);
    }

    @Operation(
            summary = "Creating a new train",
            description = "Creating a new train",
            tags = {"Creating a train"}

    )
    @PostMapping("/create")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createTrain(@RequestBody TrainRequestDTO trainRequestDTO) {
        TrainResponseDTO createdTrain = trainService.createTrain(trainRequestDTO);
        return new ResponseEntity<>("good", HttpStatus.CREATED);
    }

    @Operation(
            summary = "search trains",
            description = "Search trains",
            tags = {"Search trains by source and destination"}
    )

    @GetMapping("/get-trains/{source}/{destination}")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    public ResponseEntity<List<TrainResponseDTO>> searchTrains(
            @Parameter(description = "Source of the station to be searched", required = true)
            @PathVariable("source") String sourceStation,
            @Parameter(description = "Destination of the station to be searched", required = true)
            @PathVariable("destination") String destinationStation) {

        List<TrainResponseDTO> trains = trainService.searchTrains(sourceStation, destinationStation);

        if (trains.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @PutMapping("/{trainId}")
    @Hidden
    public ResponseEntity<TrainResponseDTO> updateTrain(
            @PathVariable String trainId,
            @RequestBody @Valid TrainRequestDTO trainRequestDTO
    ) {
        TrainResponseDTO updatedTrain = trainService.updateTrain(trainId, trainRequestDTO);
        return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
    }

    @DeleteMapping("/{trainId}")
    @Hidden
    public ResponseEntity<Void> deleteTrain(@PathVariable String trainId) {
        trainService.deleteTrain(trainId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/trains/{source}/{destination}")
    public List<TrainResponseDTO> getTrainsBySourceAndDestination(
            @PathVariable("source") String source,
            @PathVariable("destination") String destination) {
        return trainService.getTrainsBySourceAndDestination(source, destination);
    }
}
