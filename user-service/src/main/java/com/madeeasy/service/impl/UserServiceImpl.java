package com.madeeasy.service.impl;

import com.madeeasy.dto.request.UserRequestDTO;
import com.madeeasy.dto.response.AddressResponseDTO;
import com.madeeasy.dto.response.UserResponseDTO;
import com.madeeasy.dto.response.UserResponseSecureDTO;
import com.madeeasy.entity.Address;
import com.madeeasy.entity.Role;
import com.madeeasy.entity.User;
import com.madeeasy.exception.UserNotFoundException;
import com.madeeasy.repository.AddressRepository;
import com.madeeasy.repository.UserRepository;
import com.madeeasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> createUser(UserRequestDTO userRequestDTO) {
        User user = convertToUserEntity(userRequestDTO);
        this.userRepository.save(user);
        Address address = convertToAddressEntity(userRequestDTO);
        address.setUser(user);
        this.addressRepository.save(address);
        UserResponseDTO userResponseDTO = convertToUserResponseDTO(user, address);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = this.userRepository.findAll();
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users found");
        }
        List<UserResponseDTO> userResponseDTOList = userList.stream()
                .map(user -> convertToUserResponseDTO(user, user.getAddress() == null ? null : user.getAddress()))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOList);
    }

    @Override
    public ResponseEntity<?> getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertToUserResponseSecureDTO(user, user.getAddress()));
    }

    private UserResponseDTO convertToUserResponseDTO(User user, Address address) {
        return UserResponseDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .address(
                        AddressResponseDTO.builder()
                                .street(address.getStreet())
                                .city(address.getCity())
                                .state(address.getState())
                                .country(address.getCountry())
                                .build()
                )
                .roles(user.getRole().stream().map(Role::name).toList())
                .build();
    }

    private UserResponseSecureDTO convertToUserResponseSecureDTO(User user, Address address) {
        return UserResponseSecureDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .address(
                        AddressResponseDTO.builder()
                                .street(address.getStreet())
                                .city(address.getCity())
                                .state(address.getState())
                                .country(address.getCountry())
                                .build()
                )
                .roles(user.getRole().stream().map(Role::name).toList())
                .build();
    }


    private User convertToUserEntity(UserRequestDTO userRequestDTO) {
        List<Role> roleList = userRequestDTO.getRoles().stream()
                .map(Role::valueOf)
                .toList();

        return User.builder()
                .id(generateRandomUserId())
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .role(roleList)
                .address(null)
                .build();
    }

    private Address convertToAddressEntity(UserRequestDTO userRequestDTO) {
        return Address.builder()
                .id(generateRandomAddressId())
                .street(userRequestDTO.getAddress().getStreet())
                .city(userRequestDTO.getAddress().getCity())
                .state(userRequestDTO.getAddress().getState())
                .country(userRequestDTO.getAddress().getCountry())
                .build();
    }

    private String generateRandomUserId() {
        return "USER" + generateRandomNumber(6);
    }

    private String generateRandomAddressId() {
        return "ADDR" + generateRandomNumber(6);
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
