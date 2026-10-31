package com.dayo.careround.patientservice.data.repositories;

import com.dayo.careround.patientservice.data.models.Patient;
import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByPublicId(UUID publicId);
    List<Patient> findByWardId(UUID wardId);
    Page<Patient> findByWardId(UUID wardId, Pageable pageable);
    Page<Patient> findByWardIdAndStatus(UUID wardId, PatientStatus status, Pageable pageable);
    Page<Patient> findByWardIdAndStatusNot(UUID wardId, PatientStatus status, Pageable pageable);
    List<Patient> findByPublicIdIn(Collection<UUID> publicIds);
    int countByWardIdAndStatus(UUID wardId, PatientStatus status);
}