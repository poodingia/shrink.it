package org.uetmydinh.keygeneration.service;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uetmydinh.keygeneration.KeyGenerationServiceGrpc.KeyGenerationServiceImplBase;
import org.uetmydinh.keygeneration.KeyGenerationServiceOuterClass.KeyRequest;
import org.uetmydinh.keygeneration.KeyGenerationServiceOuterClass.KeyResponse;

@Service
public class KeyGenerationGrpcService extends KeyGenerationServiceImplBase {

    private final KeyGenerationService keyGenerationService;

    @Autowired
    public KeyGenerationGrpcService(KeyGenerationService keyGenerationService) {
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    public void generateKey(KeyRequest request, StreamObserver<KeyResponse> responseObserver) {
        keyGenerationService.getAvailableKey().ifPresentOrElse(key -> {
            keyGenerationService.markKeyAsUsed(key);
            KeyResponse response = KeyResponse.newBuilder().setKeyId(key.getId()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }, () -> responseObserver.onError(new Exception("No available keys")));
    }
}