package com.dayo.careround.patientservice.grpc;
import com.dayo.careround.patientservice.data.models.NextOfKin;
import com.dayo.careround.patientservice.data.models.Patient;
import com.dayo.careround.patientservice.data.models.enums.PatientStatus;
import com.dayo.careround.patientservice.data.repositories.NextOfKinRepository;
import com.dayo.careround.patientservice.data.repositories.PatientRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class PatientCensusGrpcService extends PatientCensusServiceGrpc.PatientCensusServiceImplBase {

    private final PatientRepository patientRepository;
    private final NextOfKinRepository nextOfKinRepository;

    @Override
    public void getAdmittedCount(WardCensusRequest request, StreamObserver<WardCensusResponse> responseObserver) {
        UUID wardId = UUID.fromString(request.getWardId());

        int count = patientRepository.countByWardIdAndStatus(wardId, PatientStatus.ADMITTED);

        WardCensusResponse response = WardCensusResponse.newBuilder()
                .setAdmittedCount(count)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void patientExists(PatientExistsRequest request, StreamObserver<PatientExistsResponse> responseObserver) {
        UUID publicId = UUID.fromString(request.getPatientId());
        boolean exists = patientRepository.findByPublicId(publicId).isPresent();

        responseObserver.onNext(PatientExistsResponse.newBuilder().setExists(exists).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPrimaryNextOfKin(NextOfKinRequest request, StreamObserver<com.dayo.careround.patientservice.grpc.NextOfKinResponse> responseObserver) {
        UUID patientPublicId = UUID.fromString(request.getPatientId());

        Patient patient = patientRepository.findByPublicId(patientPublicId).orElse(null);

        com.dayo.careround.patientservice.grpc.NextOfKinResponse.Builder builder =
                com.dayo.careround.patientservice.grpc.NextOfKinResponse.newBuilder();

        if (patient == null) {
            builder.setFound(false);
        } else {
            List<NextOfKin> kin = nextOfKinRepository.findByPatientIdOrderByPriorityOrderAsc(patient.getId());
            if (kin.isEmpty()) {
                builder.setFound(false);
            } else {
                NextOfKin primary = kin.get(0);
                builder.setFound(true)
                        .setFirstName(primary.getFirstName())
                        .setLastName(primary.getLastName())
                        .setPhone(primary.getPhone())
                        .setEmail(primary.getEmail() != null ? primary.getEmail() : "")
                        .setLanguagePreference(primary.getLanguagePreference() != null ? primary.getLanguagePreference() : "");
            }
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}