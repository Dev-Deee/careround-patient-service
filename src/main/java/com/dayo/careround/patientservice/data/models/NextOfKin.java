package com.dayo.careround.patientservice.data.models;

import com.dayo.careround.patientservice.data.models.enums.NextOfKinRelationship;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "next_of_kin", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"patient_id", "priority_order"})
})
public class NextOfKin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NextOfKinRelationship relationship;

    @Column
    private String languagePreference;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "priority_order", nullable = false)
    private int priorityOrder;
}