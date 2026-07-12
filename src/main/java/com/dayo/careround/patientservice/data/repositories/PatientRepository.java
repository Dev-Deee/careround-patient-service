package com.dayo.careround.patientservice.data.repositories;

import com.dayo.careround.patientservice.data.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByPublicId(UUID publicId);
    List<Patient> findByWardId(UUID wardId);
}