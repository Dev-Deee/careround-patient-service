package com.dayo.careround.patientservice.data.models;

import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Data
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private UUID wardId;

    @Column(nullable = false)
    private String bedNumber;

    @Column
    private String photo;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientStatus status;

    // Computed, never persisted — age changes with time and must never go stale in storage
    @Transient
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}