package com.dayo.careround.patientservice.dto.response;

import com.dayo.careround.patientservice.data.models.enums.NextOfKinRelationship;

import java.time.Instant;
import java.util.UUID;

public record NextOfKinResponse(
        UUID id,
        String firstName,
        String lastName,
        String phone,
        String email,
        NextOfKinRelationship relationship,
        String languagePreference,
        Instant createdAt,
        int priorityOrder
) {}