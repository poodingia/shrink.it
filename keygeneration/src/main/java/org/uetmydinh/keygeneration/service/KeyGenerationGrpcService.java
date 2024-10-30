package org.uetmydinh.keygeneration.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uetmydinh.lib.KeyGenerationRequest;
import org.uetmydinh.lib.KeyGenerationResponse;
import org.uetmydinh.lib.KeyGenerationServiceGrpc.KeyGenerationServiceImplBase;

@GrpcService
public class KeyGenerationGrpcService extends KeyGenerationServiceImplBase {

    private final KeyGenerationService keyGenerationService;

    @Autowired
    public KeyGenerationGrpcService(KeyGenerationService keyGenerationService) {
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    public void generateKey(KeyGenerationRequest request, StreamObserver<KeyGenerationResponse> responseObserver) {
        keyGenerationService.getAvailableKey().ifPresentOrElse(key -> {
            keyGenerationService.markKeyAsUsed(key);
            KeyGenerationResponse response = KeyGenerationResponse.newBuilder().setKey(key.getId()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }, () -> responseObserver.onError(new Exception("No available keys")));
    }
}