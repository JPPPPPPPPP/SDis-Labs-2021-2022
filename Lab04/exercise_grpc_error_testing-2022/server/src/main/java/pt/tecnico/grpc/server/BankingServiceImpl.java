package pt.tecnico.grpc.server;

import pt.tecnico.grpc.Banking.*;
import pt.tecnico.grpc.BankingServiceGrpc;

import static io.grpc.Status.INVALID_ARGUMENT;

import io.grpc.stub.StreamObserver;

public class BankingServiceImpl extends BankingServiceGrpc.BankingServiceImplBase {
	private Bank bank = new Bank();

	@Override
	public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
		bank.register(request.getClient(), request.getBalance());

		responseObserver.onNext(RegisterResponse.getDefaultInstance());
		responseObserver.onCompleted();
	}

	@Override
	public void consult(ConsultRequest request, StreamObserver responseObserver) {
		if (bank.isClient(request.getClient()) == false) {
			responseObserver.onError(INVALID_ARGUMENT.withDescription("Input has to be a valid user!").asRuntimeException());
			return;
		}

		Integer balance = bank.getBalance(request.getClient());

		responseObserver.onNext(ConsultResponse.newBuilder().setBalance(balance).build());
		responseObserver.onCompleted();
	}

	@Override
	public void deposit(DepositRequest request, StreamObserver responseObserver) {
		if (bank.isClient(request.getClient()) == false) {
			responseObserver.onError(INVALID_ARGUMENT.withDescription("Input has to be a valid user!").asRuntimeException());
			return;
		}

		if (bank.isAmount(request.getAmount()) == false) {
			responseObserver.onError(INVALID_ARGUMENT.withDescription("Input has to be a valid amount!").asRuntimeException());
			return;
		}

		bank.deposit(request.getClient(),request.getAmount());

		responseObserver.onNext(DepositResponse.getDefaultInstance());
		responseObserver.onCompleted();
	}
}
