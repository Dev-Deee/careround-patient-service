package com.dayo.careround.patientservice.dto.request;

import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePatientStatusRequest(
        @NotNull PatientStatus status
) {}
