package com.dayo.careround.patientservice.data.repositories;

import com.dayo.careround.patientservice.data.models.NextOfKin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NextOfKinRepository extends JpaRepository<NextOfKin, UUID> {
    List<NextOfKin> findByPatientIdOrderByPriorityOrderAsc(UUID patientId);
}