package com.dayo.careround.patientservice.controllers;

import com.dayo.careround.patientservice.dto.request.AddNextOfKinRequest;
import com.dayo.careround.patientservice.dto.response.NextOfKinResponse;
import com.dayo.careround.patientservice.services.NextOfKinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients/{patientPublicId}/next-of-kin")
@RequiredArgsConstructor
public class NextOfKinController {

    private final NextOfKinService nextOfKinService;

    @PostMapping
    @PreAuthorize("hasRole('NURSE') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<NextOfKinResponse> add(
            @PathVariable UUID patientPublicId,
            @Valid @RequestBody AddNextOfKinRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nextOfKinService.add(patientPublicId, request));
    }

    @GetMapping
    public ResponseEntity<List<NextOfKinResponse>> list(@PathVariable UUID patientPublicId) {
        return ResponseEntity.ok(nextOfKinService.listForPatient(patientPublicId));
    }

    @DeleteMapping("/{nextOfKinId}")
    @PreAuthorize("hasRole('NURSE') or hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> remove(
            @PathVariable UUID patientPublicId,
            @PathVariable UUID nextOfKinId) {
        nextOfKinService.remove(patientPublicId, nextOfKinId);
        return ResponseEntity.noContent().build();
    }
}