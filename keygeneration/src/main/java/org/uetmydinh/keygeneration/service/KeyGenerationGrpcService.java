package org.uetmydinh.keygeneration.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.uetmydinh.keygeneration.entity.Key;
import org.uetmydinh.lib.KeyGenerationRequest;
import org.uetmydinh.lib.KeyGenerationResponse;
import org.uetmydinh.lib.KeyGenerationServiceGrpc.KeyGenerationServiceImplBase;

import java.util.Optional;

@GrpcService
public class KeyGenerationGrpcService extends KeyGenerationServiceImplBase {

    private final KeyGenerationService keyGenerationService;

    private static final Logger log = LoggerFactory.getLogger(KeyGenerationGrpcService.class);

    @Autowired
    public KeyGenerationGrpcService(KeyGenerationService keyGenerationService) {
        this.keyGenerationService = keyGenerationService;
    }

    @Override
    public void generateKey(KeyGenerationRequest request, StreamObserver<KeyGenerationResponse> responseObserver) {
        Optional<Key> key = keyGenerationService.getAvailableKey();

        if (key.isPresent()) {
            Key keyEntity = key.get();
            log.info("Retrieved key: {}", keyEntity.getId());
            keyGenerationService.markKeyAsUsed(keyEntity);
            KeyGenerationResponse response = KeyGenerationResponse.newBuilder().setKey(keyEntity.getId()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            log.error("No available keys");
            StatusRuntimeException exception = Status.UNAVAILABLE
                    .withDescription("No available keys")
                    .asRuntimeException();
            responseObserver.onError(exception);
        }
    }
}