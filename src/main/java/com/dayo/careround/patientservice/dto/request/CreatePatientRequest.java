package com.dayo.careround.patientservice.dto.request;

import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePatientRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String phoneNumber,
        @Email String email,
        @NotNull UUID wardId,
        @NotBlank String bedNumber,
        String photo,
        @NotNull @Past LocalDate dateOfBirth,
        @NotNull PatientStatus status
){}
