package org.uetmydinh.appserver.service;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.uetmydinh.appserver.exception.KeyGenerationException;
import org.uetmydinh.lib.KeyGenerationRequest;
import org.uetmydinh.lib.KeyGenerationResponse;
import org.uetmydinh.lib.KeyGenerationServiceGrpc.KeyGenerationServiceBlockingStub;

@Service
public class KeyGenerationService {
    private static final Logger log = LoggerFactory.getLogger(KeyGenerationService.class);

    @GrpcClient("keyGenerationService")
    private KeyGenerationServiceBlockingStub keyGenerationServiceStub;

    // TODO: Tailor Resilience4j configuration
//    @Retry(name = "kgsRetry")
//    @CircuitBreaker(name = "kgsCircuitBreaker", fallbackMethod = "generateShortKeyFallback")
//    @Bulkhead(name = "kgsBulkhead")
//    @RateLimiter(name = "kgsRateLimiter")
    public String generateShortKey() {
        KeyGenerationRequest request = KeyGenerationRequest.newBuilder().build();
        try {
            KeyGenerationResponse response = keyGenerationServiceStub.generateKey(request);

            if (response == null || response.getKey().isEmpty()) {
                throw new KeyGenerationException("Key Generation Service returned empty key");
            }

            return response.getKey();
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                log.error("Key Generation Service ran out of keys");
                throw new KeyGenerationException("KGS unavailable or ran out of keys", e);
            } else {
                log.error("gRPC call failed: {}", e.getStatus());
                throw new KeyGenerationException("Error generating key for URL", e);
            }
        }
    }

    private String generateShortKeyFallback(Throwable t) {
        log.error("Key Generation Service is unavailable due to {}", t.getMessage());
        throw new KeyGenerationException("Key Generation Service is unavailable", t);
    }
}
