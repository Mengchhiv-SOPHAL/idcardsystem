package com.example.idcardsystem.repository;

import com.example.idcardsystem.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByUuid(String uuid);
    List<Profile> findByFullNameContainingIgnoreCase(String keyword);
}