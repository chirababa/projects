package grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: user.proto")
public final class userGrpc {

  private userGrpc() {}

  public static final String SERVICE_NAME = "user";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.User.LoginRequest,
      grpc.User.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "login",
      requestType = grpc.User.LoginRequest.class,
      responseType = grpc.User.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.User.LoginRequest,
      grpc.User.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<grpc.User.LoginRequest, grpc.User.LoginResponse> getLoginMethod;
    if ((getLoginMethod = userGrpc.getLoginMethod) == null) {
      synchronized (userGrpc.class) {
        if ((getLoginMethod = userGrpc.getLoginMethod) == null) {
          userGrpc.getLoginMethod = getLoginMethod = 
              io.grpc.MethodDescriptor.<grpc.User.LoginRequest, grpc.User.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user", "login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.LoginResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new userMethodDescriptorSupplier("login"))
                  .build();
          }
        }
     }
     return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.User.MedicationPlanRequest,
      grpc.User.MedicationPlanResponse> getGetMedicationPlanMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getMedicationPlan",
      requestType = grpc.User.MedicationPlanRequest.class,
      responseType = grpc.User.MedicationPlanResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.User.MedicationPlanRequest,
      grpc.User.MedicationPlanResponse> getGetMedicationPlanMethod() {
    io.grpc.MethodDescriptor<grpc.User.MedicationPlanRequest, grpc.User.MedicationPlanResponse> getGetMedicationPlanMethod;
    if ((getGetMedicationPlanMethod = userGrpc.getGetMedicationPlanMethod) == null) {
      synchronized (userGrpc.class) {
        if ((getGetMedicationPlanMethod = userGrpc.getGetMedicationPlanMethod) == null) {
          userGrpc.getGetMedicationPlanMethod = getGetMedicationPlanMethod = 
              io.grpc.MethodDescriptor.<grpc.User.MedicationPlanRequest, grpc.User.MedicationPlanResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user", "getMedicationPlan"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.MedicationPlanRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.MedicationPlanResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new userMethodDescriptorSupplier("getMedicationPlan"))
                  .build();
          }
        }
     }
     return getGetMedicationPlanMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.User.PublishTakenRequest,
      grpc.User.PublishTakenResponse> getPublishTakenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "publishTaken",
      requestType = grpc.User.PublishTakenRequest.class,
      responseType = grpc.User.PublishTakenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.User.PublishTakenRequest,
      grpc.User.PublishTakenResponse> getPublishTakenMethod() {
    io.grpc.MethodDescriptor<grpc.User.PublishTakenRequest, grpc.User.PublishTakenResponse> getPublishTakenMethod;
    if ((getPublishTakenMethod = userGrpc.getPublishTakenMethod) == null) {
      synchronized (userGrpc.class) {
        if ((getPublishTakenMethod = userGrpc.getPublishTakenMethod) == null) {
          userGrpc.getPublishTakenMethod = getPublishTakenMethod = 
              io.grpc.MethodDescriptor.<grpc.User.PublishTakenRequest, grpc.User.PublishTakenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user", "publishTaken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.PublishTakenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.PublishTakenResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new userMethodDescriptorSupplier("publishTaken"))
                  .build();
          }
        }
     }
     return getPublishTakenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.User.PublishNotTakenRequest,
      grpc.User.PublishTakenResponse> getPublishNotTakenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "publishNotTaken",
      requestType = grpc.User.PublishNotTakenRequest.class,
      responseType = grpc.User.PublishTakenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.User.PublishNotTakenRequest,
      grpc.User.PublishTakenResponse> getPublishNotTakenMethod() {
    io.grpc.MethodDescriptor<grpc.User.PublishNotTakenRequest, grpc.User.PublishTakenResponse> getPublishNotTakenMethod;
    if ((getPublishNotTakenMethod = userGrpc.getPublishNotTakenMethod) == null) {
      synchronized (userGrpc.class) {
        if ((getPublishNotTakenMethod = userGrpc.getPublishNotTakenMethod) == null) {
          userGrpc.getPublishNotTakenMethod = getPublishNotTakenMethod = 
              io.grpc.MethodDescriptor.<grpc.User.PublishNotTakenRequest, grpc.User.PublishTakenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "user", "publishNotTaken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.PublishNotTakenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.User.PublishTakenResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new userMethodDescriptorSupplier("publishNotTaken"))
                  .build();
          }
        }
     }
     return getPublishNotTakenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static userStub newStub(io.grpc.Channel channel) {
    return new userStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static userBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new userBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static userFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new userFutureStub(channel);
  }

  /**
   */
  public static abstract class userImplBase implements io.grpc.BindableService {

    /**
     */
    public void login(grpc.User.LoginRequest request,
        io.grpc.stub.StreamObserver<grpc.User.LoginResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    public void getMedicationPlan(grpc.User.MedicationPlanRequest request,
        io.grpc.stub.StreamObserver<grpc.User.MedicationPlanResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMedicationPlanMethod(), responseObserver);
    }

    /**
     */
    public void publishTaken(grpc.User.PublishTakenRequest request,
        io.grpc.stub.StreamObserver<grpc.User.PublishTakenResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPublishTakenMethod(), responseObserver);
    }

    /**
     */
    public void publishNotTaken(grpc.User.PublishNotTakenRequest request,
        io.grpc.stub.StreamObserver<grpc.User.PublishTakenResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPublishNotTakenMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getLoginMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.User.LoginRequest,
                grpc.User.LoginResponse>(
                  this, METHODID_LOGIN)))
          .addMethod(
            getGetMedicationPlanMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.User.MedicationPlanRequest,
                grpc.User.MedicationPlanResponse>(
                  this, METHODID_GET_MEDICATION_PLAN)))
          .addMethod(
            getPublishTakenMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.User.PublishTakenRequest,
                grpc.User.PublishTakenResponse>(
                  this, METHODID_PUBLISH_TAKEN)))
          .addMethod(
            getPublishNotTakenMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.User.PublishNotTakenRequest,
                grpc.User.PublishTakenResponse>(
                  this, METHODID_PUBLISH_NOT_TAKEN)))
          .build();
    }
  }

  /**
   */
  public static final class userStub extends io.grpc.stub.AbstractStub<userStub> {
    private userStub(io.grpc.Channel channel) {
      super(channel);
    }

    private userStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected userStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new userStub(channel, callOptions);
    }

    /**
     */
    public void login(grpc.User.LoginRequest request,
        io.grpc.stub.StreamObserver<grpc.User.LoginResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMedicationPlan(grpc.User.MedicationPlanRequest request,
        io.grpc.stub.StreamObserver<grpc.User.MedicationPlanResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMedicationPlanMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void publishTaken(grpc.User.PublishTakenRequest request,
        io.grpc.stub.StreamObserver<grpc.User.PublishTakenResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPublishTakenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void publishNotTaken(grpc.User.PublishNotTakenRequest request,
        io.grpc.stub.StreamObserver<grpc.User.PublishTakenResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPublishNotTakenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class userBlockingStub extends io.grpc.stub.AbstractStub<userBlockingStub> {
    private userBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private userBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected userBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new userBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.User.LoginResponse login(grpc.User.LoginRequest request) {
      return blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.User.MedicationPlanResponse getMedicationPlan(grpc.User.MedicationPlanRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetMedicationPlanMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.User.PublishTakenResponse publishTaken(grpc.User.PublishTakenRequest request) {
      return blockingUnaryCall(
          getChannel(), getPublishTakenMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.User.PublishTakenResponse publishNotTaken(grpc.User.PublishNotTakenRequest request) {
      return blockingUnaryCall(
          getChannel(), getPublishNotTakenMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class userFutureStub extends io.grpc.stub.AbstractStub<userFutureStub> {
    private userFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private userFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected userFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new userFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.User.LoginResponse> login(
        grpc.User.LoginRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.User.MedicationPlanResponse> getMedicationPlan(
        grpc.User.MedicationPlanRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMedicationPlanMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.User.PublishTakenResponse> publishTaken(
        grpc.User.PublishTakenRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPublishTakenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.User.PublishTakenResponse> publishNotTaken(
        grpc.User.PublishNotTakenRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPublishNotTakenMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_GET_MEDICATION_PLAN = 1;
  private static final int METHODID_PUBLISH_TAKEN = 2;
  private static final int METHODID_PUBLISH_NOT_TAKEN = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final userImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(userImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((grpc.User.LoginRequest) request,
              (io.grpc.stub.StreamObserver<grpc.User.LoginResponse>) responseObserver);
          break;
        case METHODID_GET_MEDICATION_PLAN:
          serviceImpl.getMedicationPlan((grpc.User.MedicationPlanRequest) request,
              (io.grpc.stub.StreamObserver<grpc.User.MedicationPlanResponse>) responseObserver);
          break;
        case METHODID_PUBLISH_TAKEN:
          serviceImpl.publishTaken((grpc.User.PublishTakenRequest) request,
              (io.grpc.stub.StreamObserver<grpc.User.PublishTakenResponse>) responseObserver);
          break;
        case METHODID_PUBLISH_NOT_TAKEN:
          serviceImpl.publishNotTaken((grpc.User.PublishNotTakenRequest) request,
              (io.grpc.stub.StreamObserver<grpc.User.PublishTakenResponse>) responseObserver);
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

  private static abstract class userBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    userBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.User.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("user");
    }
  }

  private static final class userFileDescriptorSupplier
      extends userBaseDescriptorSupplier {
    userFileDescriptorSupplier() {}
  }

  private static final class userMethodDescriptorSupplier
      extends userBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    userMethodDescriptorSupplier(String methodName) {
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
      synchronized (userGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new userFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getGetMedicationPlanMethod())
              .addMethod(getPublishTakenMethod())
              .addMethod(getPublishNotTakenMethod())
              .build();
        }
      }
    }
    return result;
  }
}
