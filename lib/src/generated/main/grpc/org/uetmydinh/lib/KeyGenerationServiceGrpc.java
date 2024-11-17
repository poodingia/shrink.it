package org.uetmydinh.lib;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: KeyGenerationService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class KeyGenerationServiceGrpc {

  private KeyGenerationServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "KeyGenerationService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.uetmydinh.lib.KeyGenerationRequest,
      org.uetmydinh.lib.KeyGenerationResponse> getGenerateKeyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GenerateKey",
      requestType = org.uetmydinh.lib.KeyGenerationRequest.class,
      responseType = org.uetmydinh.lib.KeyGenerationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.uetmydinh.lib.KeyGenerationRequest,
      org.uetmydinh.lib.KeyGenerationResponse> getGenerateKeyMethod() {
    io.grpc.MethodDescriptor<org.uetmydinh.lib.KeyGenerationRequest, org.uetmydinh.lib.KeyGenerationResponse> getGenerateKeyMethod;
    if ((getGenerateKeyMethod = KeyGenerationServiceGrpc.getGenerateKeyMethod) == null) {
      synchronized (KeyGenerationServiceGrpc.class) {
        if ((getGenerateKeyMethod = KeyGenerationServiceGrpc.getGenerateKeyMethod) == null) {
          KeyGenerationServiceGrpc.getGenerateKeyMethod = getGenerateKeyMethod =
              io.grpc.MethodDescriptor.<org.uetmydinh.lib.KeyGenerationRequest, org.uetmydinh.lib.KeyGenerationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GenerateKey"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.uetmydinh.lib.KeyGenerationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.uetmydinh.lib.KeyGenerationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KeyGenerationServiceMethodDescriptorSupplier("GenerateKey"))
              .build();
        }
      }
    }
    return getGenerateKeyMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KeyGenerationServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KeyGenerationServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KeyGenerationServiceStub>() {
        @java.lang.Override
        public KeyGenerationServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KeyGenerationServiceStub(channel, callOptions);
        }
      };
    return KeyGenerationServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KeyGenerationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KeyGenerationServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KeyGenerationServiceBlockingStub>() {
        @java.lang.Override
        public KeyGenerationServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KeyGenerationServiceBlockingStub(channel, callOptions);
        }
      };
    return KeyGenerationServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KeyGenerationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<KeyGenerationServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<KeyGenerationServiceFutureStub>() {
        @java.lang.Override
        public KeyGenerationServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new KeyGenerationServiceFutureStub(channel, callOptions);
        }
      };
    return KeyGenerationServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void generateKey(org.uetmydinh.lib.KeyGenerationRequest request,
        io.grpc.stub.StreamObserver<org.uetmydinh.lib.KeyGenerationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGenerateKeyMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service KeyGenerationService.
   */
  public static abstract class KeyGenerationServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return KeyGenerationServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service KeyGenerationService.
   */
  public static final class KeyGenerationServiceStub
      extends io.grpc.stub.AbstractAsyncStub<KeyGenerationServiceStub> {
    private KeyGenerationServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KeyGenerationServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KeyGenerationServiceStub(channel, callOptions);
    }

    /**
     */
    public void generateKey(org.uetmydinh.lib.KeyGenerationRequest request,
        io.grpc.stub.StreamObserver<org.uetmydinh.lib.KeyGenerationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGenerateKeyMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service KeyGenerationService.
   */
  public static final class KeyGenerationServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<KeyGenerationServiceBlockingStub> {
    private KeyGenerationServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KeyGenerationServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KeyGenerationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.uetmydinh.lib.KeyGenerationResponse generateKey(org.uetmydinh.lib.KeyGenerationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateKeyMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service KeyGenerationService.
   */
  public static final class KeyGenerationServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<KeyGenerationServiceFutureStub> {
    private KeyGenerationServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KeyGenerationServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new KeyGenerationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.uetmydinh.lib.KeyGenerationResponse> generateKey(
        org.uetmydinh.lib.KeyGenerationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGenerateKeyMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GENERATE_KEY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GENERATE_KEY:
          serviceImpl.generateKey((org.uetmydinh.lib.KeyGenerationRequest) request,
              (io.grpc.stub.StreamObserver<org.uetmydinh.lib.KeyGenerationResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGenerateKeyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.uetmydinh.lib.KeyGenerationRequest,
              org.uetmydinh.lib.KeyGenerationResponse>(
                service, METHODID_GENERATE_KEY)))
        .build();
  }

  private static abstract class KeyGenerationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KeyGenerationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.uetmydinh.lib.KeyGenerationServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("KeyGenerationService");
    }
  }

  private static final class KeyGenerationServiceFileDescriptorSupplier
      extends KeyGenerationServiceBaseDescriptorSupplier {
    KeyGenerationServiceFileDescriptorSupplier() {}
  }

  private static final class KeyGenerationServiceMethodDescriptorSupplier
      extends KeyGenerationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    KeyGenerationServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (KeyGenerationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KeyGenerationServiceFileDescriptorSupplier())
              .addMethod(getGenerateKeyMethod())
              .build();
        }
      }
    }
    return result;
  }
}
