package com.dayo.careround.patientservice.services;

import com.dayo.careround.patientservice.data.models.Patient;
import com.dayo.careround.patientservice.data.repositories.PatientRepository;
import com.dayo.careround.patientservice.dto.request.CreatePatientRequest;
import com.dayo.careround.patientservice.dto.request.UpdatePatientStatusRequest;
import com.dayo.careround.patientservice.dto.response.PatientResponse;
import com.dayo.careround.patientservice.exception.PatientNotFoundException;
import com.dayo.careround.patientservice.exception.WardNotFoundException;
import com.dayo.careround.patientservice.grpc.WardVerificationClient;
import com.dayo.careround.patientservice.utils.PatientMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private  final PatientRepository patientRepository;
    private final WardVerificationClient wardVerificationClient;

    @Transactional
    public PatientResponse create(CreatePatientRequest request) {
        if (!wardVerificationClient.wardExists(request.wardId())) {
            throw new WardNotFoundException("No ward with that id");
        }
        Patient patient = PatientMapper.toPatient(request);
        patientRepository.save(patient);
        return PatientMapper.toPatientResponse(patient);
    }

    public PatientResponse getByPublicId(UUID publicId) {
        Patient patient = findByPublicIdOrThrow(publicId);
        return PatientMapper.toPatientResponse(patient);
    }

    public List<PatientResponse>getByWard(UUID wardId) {
        return patientRepository.findByWardId(wardId)
                .stream()
                .map(PatientMapper::toPatientResponse)
                .toList();
    }

    @Transactional
    public PatientResponse updateStatus(UUID publicId, UpdatePatientStatusRequest request) {
        Patient patient = findByPublicIdOrThrow(publicId);
        patient.setStatus(request.status());
        patientRepository.save(patient);
        return PatientMapper.toPatientResponse(patient);
    }

    private Patient findByPublicIdOrThrow(UUID publicId) {
        return patientRepository.findByPublicId(publicId)
                .orElseThrow(() -> new PatientNotFoundException("No patient with that public id"));
    }

}
