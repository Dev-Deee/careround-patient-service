package com.dayo.careround.patientservice.dto.response;

import com.dayo.careround.patientservice.data.models.enums.PatientStatus;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse(
        UUID publicId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        UUID wardId,
        String bedNumber,
        String photo,
        LocalDate dateOfBirth,
        int age,
        PatientStatus status
) {}
