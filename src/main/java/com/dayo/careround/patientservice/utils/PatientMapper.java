package com.dayo.careround.patientservice.utils;

import com.dayo.careround.patientservice.data.models.NextOfKin;
import com.dayo.careround.patientservice.data.models.Patient;
import com.dayo.careround.patientservice.dto.request.CreatePatientRequest;
import com.dayo.careround.patientservice.dto.response.NextOfKinResponse;
import com.dayo.careround.patientservice.dto.response.PatientResponse;

public class PatientMapper {

    private PatientMapper() {
    }

    public static Patient toPatient(CreatePatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setPhoneNumber(request.phoneNumber());
        patient.setEmail(request.email());
        patient.setWardId(request.wardId());
        patient.setBedNumber(request.bedNumber());
        patient.setPhoto(request.photo());
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setStatus(request.status());
        return patient;
    }

    public static PatientResponse toPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getPublicId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getWardId(),
                patient.getBedNumber(),
                patient.getPhoto(),
                patient.getDateOfBirth(),
                patient.getAge(),
                patient.getStatus()
        );
    }

    public static NextOfKinResponse toNextOfKinResponse(NextOfKin nextOfKin) {
        return new NextOfKinResponse(
                nextOfKin.getId(),
                nextOfKin.getFirstName(),
                nextOfKin.getLastName(),
                nextOfKin.getPhone(),
                nextOfKin.getEmail(),
                nextOfKin.getRelationship(),
                nextOfKin.getLanguagePreference(),
                nextOfKin.getCreatedAt(),
                nextOfKin.getPriorityOrder()
        );
    }
}