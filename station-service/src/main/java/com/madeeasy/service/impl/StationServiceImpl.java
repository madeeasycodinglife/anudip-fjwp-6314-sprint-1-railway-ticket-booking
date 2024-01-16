package com.madeeasy.service.impl;

import com.madeeasy.dto.request.PlatformRequestDTO;
import com.madeeasy.dto.request.PlatformUpdateRequest;
import com.madeeasy.dto.request.StationRequestDTO;
import com.madeeasy.dto.request.StationUpdateRequestDTO;
import com.madeeasy.dto.response.PlatformResponseDTO;
import com.madeeasy.dto.response.StationResponseDTO;
import com.madeeasy.entity.Platform;
import com.madeeasy.entity.Station;
import com.madeeasy.exception.StationNotFoundException;
import com.madeeasy.repository.PlatformRepository;
import com.madeeasy.repository.StationRepository;
import com.madeeasy.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final PlatformRepository platformRepository;

    private static final String STATION_CODE_PREFIX = "STN";
    private static final String PLATFORM_CODE_PREFIX = "PF";
    private static final int RANDOM_NUMBER_LENGTH = 7; // You can adjust the length as needed


    @Override
    public List<StationResponseDTO> getAllStations() {
        List<Station> stationList = this.stationRepository.findAll();
        if (stationList.isEmpty()) {
            return Collections.emptyList();
        }
        return stationList.stream()
                .map(this::convertToStationResponseDTO)
                .collect(Collectors.toList());
    }

    private StationResponseDTO convertToStationResponseDTO(Station station) {
        return StationResponseDTO.builder()
                .stationCode(station.getStationCode())
                .stationName(station.getStationName())
                .location(station.getLocation())
                .description(station.getDescription())
                .platforms(convertPlatformEntitiesToPlatformDTOs(station.getPlatforms()))
                .build();
    }

    private List<PlatformResponseDTO> convertPlatformEntitiesToPlatformDTOs(List<Platform> platforms) {
        if (platforms == null || platforms.isEmpty()) {
            return Collections.emptyList();
        }
        return platforms.stream()
                .map(this::convertPlatformEntityToPlatResponseDto)
                .collect(Collectors.toList());
    }

    private PlatformResponseDTO convertPlatformEntityToPlatResponseDto(Platform platform) {
        return PlatformResponseDTO.builder()
                .platformNumber(platform.getPlatformNumber())
                .description(platform.getDescription())
                .build();
    }

    @Override
    public StationResponseDTO getStationByCode(String stationCode) {
        Station station = this.stationRepository.findByStationCode(stationCode)
                .orElseThrow(() -> new StationNotFoundException("Station not found for station code : " + stationCode));
        return convertToStationResponseDTO(station);
    }

    @Override
    public StationResponseDTO createStation(StationRequestDTO stationRequestDTO) {
        Station station = convertToEntity(stationRequestDTO);

        List<PlatformRequestDTO> platformRequestDTOS = stationRequestDTO.getPlatforms();

        // debug this point

        List<Platform> platformList = platformRequestDTOS.stream()
                .map(platformRequestDTO -> Platform.builder()
                        .platformId(generatePlatformCode())
                        .platformNumber(platformRequestDTO.getPlatformNumber())
                        .description(platformRequestDTO.getDescription())
                        .station(station)
                        .build())
                .collect(Collectors.toList());

        station.setPlatforms(platformList);

        platformList.forEach(this.platformRepository::save);
        this.stationRepository.save(station);
        return convertToStationResponseDTO(station);
    }

    private Station convertToEntity(StationRequestDTO stationRequestDTO) {
        return Station.builder()
                .stationCode(generateStationCode())
                .stationName(stationRequestDTO.getStationName())
                .description(stationRequestDTO.getDescription())
                .location(stationRequestDTO.getLocation())
                .platforms(Collections.emptyList()) // Initialize with an empty list
                .build();
    }

    private String generateStationCode() {
        // Generate a random number with the specified length
        String randomNumber = generateRandomNumber(RANDOM_NUMBER_LENGTH);

        // Combine the prefix and the random number
        return STATION_CODE_PREFIX + randomNumber;
    }

    private String generatePlatformCode() {
        // Generate a random number with the specified length
        String randomNumber = generateRandomNumber(RANDOM_NUMBER_LENGTH);

        // Combine the prefix and the random number
        return PLATFORM_CODE_PREFIX + randomNumber;
    }

    private static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            stringBuilder.append(digit);
        }
        return stringBuilder.toString();
    }

    private List<Platform> convertPlatformDtosToPlatformEntity(List<PlatformRequestDTO> platforms, Station station) {
        if (platforms == null || platforms.isEmpty()) {
            return Collections.emptyList();
        }
        return platforms.stream()
                .map(platformResponseDTO -> convertPlatformDtoToPlatformEntity(platformResponseDTO, station))
                .collect(Collectors.toList());
    }

    private Platform convertPlatformDtoToPlatformEntity(PlatformRequestDTO platformResponseDTO, Station station) {
        return Platform.builder()
                .platformId(generatePlatformCode())
                .platformNumber(platformResponseDTO.getPlatformNumber())
                .description(platformResponseDTO.getDescription())
                .station(station)  // Set the reference to the Station entity
                .build();
    }

    @Override
    public StationResponseDTO updateStation(String stationCode,
                                            StationUpdateRequestDTO stationRequestDTO) {
        Station existingStation = this.stationRepository.findByStationCode(stationCode)
                .orElseThrow(() -> new StationNotFoundException("Station not found for stationCode: " + stationCode));

        if (existingStation != null) {
            if (stationRequestDTO.getStationName() != null) {
                existingStation.setStationName(stationRequestDTO.getStationName());
            }
            if (stationRequestDTO.getDescription() != null) {
                existingStation.setDescription(stationRequestDTO.getDescription());
            }
            if (stationRequestDTO.getLocation() != null) {
                existingStation.setLocation(stationRequestDTO.getLocation());
            }

            Station savedStation = stationRepository.save(existingStation);
            return convertToStationResponseDTO(savedStation);

        }

        return null;
    }

    @Override
    public void createPlatform(String stationCode, PlatformUpdateRequest platformUpdateRequest) {
        Station station = this.stationRepository.findByStationCode(stationCode)
                .orElseThrow(() -> new StationNotFoundException("Station not found with station code  : " + stationCode));
        platformUpdateRequest.getPlatforms()
                .forEach(platformRequestDTO -> {
                    Platform platform = convertPlatformDtoToPlatformEntity(platformRequestDTO, station);
                    station.getPlatforms().add(platform);
                });
        this.stationRepository.save(station);
    }

    @Override
    public StationResponseDTO getStationByStationName(String stationName) {
        Station station = this.stationRepository.findByStationName(stationName)
                .orElseThrow(() -> new StationNotFoundException("Station not found for station name " + stationName));
        return convertToStationResponseDTO(station);
    }

    private List<Platform> convertPlatformDtosToPlatformEntityToUpdatePlatform(List<PlatformRequestDTO> platforms, Station savedStation) {

        return null;
    }

    private List<Platform> updateOrAddPlatforms(List<PlatformRequestDTO> platforms, Station existingStation) {
        if (platforms == null || platforms.isEmpty()) {
            return Collections.emptyList();
        }

        // Create a map for quick lookup of existing platforms
        Map<String, Platform> existingPlatformsMap = existingStation.getPlatforms()
                .stream()
                .collect(Collectors.toMap(Platform::getPlatformNumber, Function.identity()));

        return platforms.stream()
                .map(platformResponseDTO -> updateOrAddPlatform(platformResponseDTO, existingPlatformsMap, existingStation))
                .collect(Collectors.toList());
    }

    private Platform updateOrAddPlatform(PlatformRequestDTO platformResponseDTO,
                                         Map<String, Platform> existingPlatformsMap,
                                         Station existingStation) {

        String platformNumber = platformResponseDTO.getPlatformNumber();
        Platform existingPlatform = existingPlatformsMap.get(platformNumber);

        if (existingPlatform != null) {
            // If the platform with the same number exists, update its properties
            existingPlatform.setPlatformNumber(platformResponseDTO.getPlatformNumber());
            existingPlatform.setDescription(platformResponseDTO.getDescription());
            // Add more updates if needed

            return existingPlatform;
        } else {
            // If the platform with the same number doesn't exist, create a new one
            return Platform.builder()
                    .platformNumber(platformNumber)
                    .description(platformResponseDTO.getDescription())
                    .station(existingStation)
                    .build();
        }
    }

}
