package com.dayo.careround.patientservice.services;

import com.dayo.careround.patientservice.data.models.NextOfKin;
import com.dayo.careround.patientservice.data.models.Patient;
import com.dayo.careround.patientservice.data.repositories.NextOfKinRepository;
import com.dayo.careround.patientservice.data.repositories.PatientRepository;
import com.dayo.careround.patientservice.dto.request.AddNextOfKinRequest;
import com.dayo.careround.patientservice.dto.response.NextOfKinResponse;
import com.dayo.careround.patientservice.exception.NextOfKinNotFoundException;
import com.dayo.careround.patientservice.exception.PatientNotFoundException;
import com.dayo.careround.patientservice.utils.PatientMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NextOfKinService {

    private final NextOfKinRepository nextOfKinRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public NextOfKinResponse add(UUID patientPublicId, AddNextOfKinRequest request) {
        Patient patient = patientRepository.findByPublicId(patientPublicId)
                .orElseThrow(() -> new PatientNotFoundException("No patient with that public id"));

        List<NextOfKin> existing = nextOfKinRepository.findByPatientIdOrderByPriorityOrderAsc(patient.getId());
        int nextPriority = existing.size() + 1;

        NextOfKin nextOfKin = new NextOfKin();
        nextOfKin.setPatientId(patient.getId());
        nextOfKin.setFirstName(request.firstName());
        nextOfKin.setLastName(request.lastName());
        nextOfKin.setPhone(request.phone());
        nextOfKin.setEmail(request.email());
        nextOfKin.setRelationship(request.relationship());
        nextOfKin.setLanguagePreference(request.languagePreference());
        nextOfKin.setPriorityOrder(nextPriority);

        nextOfKinRepository.save(nextOfKin);
        return PatientMapper.toNextOfKinResponse(nextOfKin);
    }

    public List<NextOfKinResponse> listForPatient(UUID patientPublicId) {
        Patient patient = patientRepository.findByPublicId(patientPublicId)
                .orElseThrow(() -> new PatientNotFoundException("No patient with that public id"));

        return nextOfKinRepository.findByPatientIdOrderByPriorityOrderAsc(patient.getId()).stream()
                .map(PatientMapper::toNextOfKinResponse)
                .toList();
    }

    @Transactional
    public void remove(UUID patientPublicId, UUID nextOfKinId) {
        Patient patient = patientRepository.findByPublicId(patientPublicId)
                .orElseThrow(() -> new PatientNotFoundException("No patient with that public id"));

        List<NextOfKin> all = nextOfKinRepository.findByPatientIdOrderByPriorityOrderAsc(patient.getId());

        NextOfKin toRemove = all.stream()
                .filter(nok -> nok.getId().equals(nextOfKinId))
                .findFirst()
                .orElseThrow(() -> new NextOfKinNotFoundException("No next-of-kin with that id for this patient"));

        int removedPriority = toRemove.getPriorityOrder();

        List<NextOfKin> toShift = all.stream()
                .filter(nok -> !nok.getId().equals(nextOfKinId))
                .filter(nok -> nok.getPriorityOrder() > removedPriority)
                .toList();

        nextOfKinRepository.delete(toRemove);
        nextOfKinRepository.flush();

        toShift.forEach(nok -> nok.setPriorityOrder(nok.getPriorityOrder() - 1));
        nextOfKinRepository.saveAll(toShift);
    }

}