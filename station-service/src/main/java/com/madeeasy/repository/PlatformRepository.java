package com.madeeasy.repository;

import com.madeeasy.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, String> {
    Optional<Platform> findByPlatformNumber(String platformNumber);
}
