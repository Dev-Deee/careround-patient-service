package com.dayo.careround.patientservice.controllers;

import com.dayo.careround.patientservice.dto.request.CreatePatientRequest;
import com.dayo.careround.patientservice.dto.request.UpdatePatientStatusRequest;
import com.dayo.careround.patientservice.dto.response.PatientResponse;
import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import com.dayo.careround.patientservice.exception.BadRequestException;
import com.dayo.careround.patientservice.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(request));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<PatientResponse> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(patientService.getByPublicId(publicId));
    }

    @GetMapping
    // ?ids=<publicId,...> batch (max 100) -> list. ?wardId=[&status][&includeDischarged] -> ward list; with page/size it
    // returns {items, page, size, total}, without them the deprecated bare array (same filters). DISCHARGED is
    // excluded unless asked for.
    public ResponseEntity<?> list(@RequestParam(required = false) UUID wardId,
                                  @RequestParam(required = false) List<UUID> ids,
                                  @RequestParam(required = false) PatientStatus status,
                                  @RequestParam(defaultValue = "false") boolean includeDischarged,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer size) {
        if (ids != null) {
            if (wardId != null || status != null) {
                throw new BadRequestException("Use either ids or wardId/status, not both");
            }
            return ResponseEntity.ok(patientService.getByIds(ids));
        }
        if (wardId == null) {
            throw new BadRequestException("wardId or ids is required");
        }
        if (page == null && size == null) {
            return ResponseEntity.ok(patientService.getByWardLegacy(wardId, status, includeDischarged));
        }
        return ResponseEntity.ok(patientService.getByWard(wardId, status, includeDischarged, page, size));
    }

    @PatchMapping("/{publicId}/status")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PatientResponse> updateStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdatePatientStatusRequest request) {
        return ResponseEntity.ok(patientService.updateStatus(publicId, request));
    }
}