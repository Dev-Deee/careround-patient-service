package com.dayo.careround.patientservice.controllers;

import com.dayo.careround.patientservice.dto.request.CreatePatientRequest;
import com.dayo.careround.patientservice.dto.request.UpdatePatientStatusRequest;
import com.dayo.careround.patientservice.dto.response.PatientResponse;
import com.dayo.careround.patientservice.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(request));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<PatientResponse> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(patientService.getByPublicId(publicId));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getByWard(@RequestParam UUID wardId) {
        return ResponseEntity.ok(patientService.getByWard(wardId));
    }

    @PatchMapping("/{publicId}/status")
    public ResponseEntity<PatientResponse> updateStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdatePatientStatusRequest request) {
        return ResponseEntity.ok(patientService.updateStatus(publicId, request));
    }
}