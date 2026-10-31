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
import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import com.dayo.careround.patientservice.dto.response.PageResponse;
import com.dayo.careround.patientservice.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public static final int MAX_IDS = 100;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;
    private static final Sort WARD_ORDER = Sort.by("bedNumber", "lastName", "firstName", "publicId");

    // Ward list. DISCHARGED patients are excluded unless status=DISCHARGED or includeDischarged=true.
    // Sorted by bed number, then name; page size 20 by default, max 100.
    public Page<Patient> wardPage(UUID wardId, PatientStatus status, boolean includeDischarged, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page == null ? 0 : Math.max(page, 0),
                size == null ? DEFAULT_SIZE : Math.min(Math.max(size, 1), MAX_SIZE), WARD_ORDER);
        if (status != null) return patientRepository.findByWardIdAndStatus(wardId, status, pageable);
        if (includeDischarged) return patientRepository.findByWardId(wardId, pageable);
        return patientRepository.findByWardIdAndStatusNot(wardId, PatientStatus.DISCHARGED, pageable);
    }

    public PageResponse<PatientResponse> getByWard(UUID wardId, PatientStatus status, boolean includeDischarged, Integer page, Integer size) {
        return PageResponse.of(wardPage(wardId, status, includeDischarged, page, size), PatientMapper::toPatientResponse);
    }

    // Deprecated bare-array form of the ward list (no page/size): same filters and order, capped at MAX_SIZE.
    public List<PatientResponse> getByWardLegacy(UUID wardId, PatientStatus status, boolean includeDischarged) {
        return wardPage(wardId, status, includeDischarged, 0, MAX_SIZE).getContent().stream()
                .map(PatientMapper::toPatientResponse)
                .toList();
    }

    // Batch lookup by publicId (max 100, any status); unknown ids are simply absent.
    public List<PatientResponse> getByIds(List<UUID> ids) {
        if (ids.size() > MAX_IDS) {
            throw new BadRequestException("At most " + MAX_IDS + " ids per request");
        }
        return patientRepository.findByPublicIdIn(ids).stream()
                .sorted(java.util.Comparator.comparing(Patient::getBedNumber, java.util.Comparator.nullsLast(String::compareTo))
                        .thenComparing(Patient::getLastName))
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
