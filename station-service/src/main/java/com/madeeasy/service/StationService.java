package com.madeeasy.service;

import com.madeeasy.dto.request.PlatformUpdateRequest;
import com.madeeasy.dto.request.StationRequestDTO;
import com.madeeasy.dto.request.StationUpdateRequestDTO;
import com.madeeasy.dto.response.StationResponseDTO;

import java.util.List;

public interface StationService {
    List<StationResponseDTO> getAllStations();

    StationResponseDTO getStationByCode(String stationCode);

    StationResponseDTO createStation(StationRequestDTO stationRequestDTO);

    StationResponseDTO updateStation(String stationCode, StationUpdateRequestDTO stationRequestDTO);

    void createPlatform(String stationCode,PlatformUpdateRequest platformUpdateRequest);

    StationResponseDTO getStationByStationName(String stationName);
}
