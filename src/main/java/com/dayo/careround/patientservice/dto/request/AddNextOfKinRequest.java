package com.dayo.careround.patientservice.dto.request;

import com.dayo.careround.patientservice.data.models.enums.NextOfKinRelationship;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddNextOfKinRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        @Email String email,
        @NotNull NextOfKinRelationship relationship,
        String languagePreference
) {}