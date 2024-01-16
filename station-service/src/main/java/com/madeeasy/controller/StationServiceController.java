package com.madeeasy.controller;

import com.madeeasy.dto.request.PlatformUpdateRequest;
import com.madeeasy.dto.request.StationRequestDTO;
import com.madeeasy.dto.request.StationUpdateRequestDTO;
import com.madeeasy.dto.response.StationResponseDTO;
import com.madeeasy.service.StationService;
import com.madeeasy.util.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station-service")
@RequiredArgsConstructor
public class StationServiceController {


    /**
     * This service is temporarily closed . In the future, it will be expanded with more features
     */

    private final StationService stationService;

    @GetMapping(value = "/get-all-stations")
    public ResponseEntity<?> getAllStations() {
        List<StationResponseDTO> stations = stationService.getAllStations();
        if (stations.isEmpty()) return ResponseEntity.ok().body("No stations found");
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    @GetMapping(value = "/{stationCode}")
    public ResponseEntity<?> getStationByCode(@PathVariable String stationCode) {
        StationResponseDTO station = stationService.getStationByCode(stationCode);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    @GetMapping(value = "/{stationName}")
    public ResponseEntity<?> getStationByStationName(@PathVariable("stationName") String stationName) {
        StationResponseDTO station = stationService.getStationByStationName(stationName);
        return new ResponseEntity<>(station, HttpStatus.OK);
    }

    @PostMapping(value = "/create-station")
    public ResponseEntity<?> createStation(@RequestBody @Valid StationRequestDTO stationRequestDTO) {
        ValidationUtils.validate(stationRequestDTO);
        StationResponseDTO createdStation = this.stationService.createStation(stationRequestDTO);
        return new ResponseEntity<>(createdStation, HttpStatus.CREATED);
    }

    @PostMapping(value = "/create-platforms/{station-code}")
    public ResponseEntity<?> createPlatform(@PathVariable("station-code") String stationCode,
                                            @RequestBody @Valid PlatformUpdateRequest platformUpdateRequest) {
        ValidationUtils.validate(platformUpdateRequest);
        this.stationService.createPlatform(stationCode, platformUpdateRequest);
        return ResponseEntity.ok().body("Platform Successfully created");
    }

    @PutMapping(value = "/update-station/{stationCode}")
    public ResponseEntity<?> updateStation(@PathVariable String stationCode,
                                           @RequestBody @Valid StationUpdateRequestDTO stationUpdateRequestDTO) {
        ValidationUtils.validate(stationUpdateRequestDTO);
        StationResponseDTO updatedStation = stationService.updateStation(stationCode, stationUpdateRequestDTO);
        return new ResponseEntity<>(updatedStation, HttpStatus.OK);
    }


}
