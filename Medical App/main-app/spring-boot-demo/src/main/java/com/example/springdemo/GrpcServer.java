package com.example.springdemo;
import java.io.IOException;
import com.example.springdemo.services.GrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
public class GrpcServer implements CommandLineRunner {

	@Autowired
	private GrpcService grpcService;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("starting GRPC Server");
		Server server = ServerBuilder.forPort(9090).addService(grpcService).build();
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("server started at "+ server.getPort());
		try {
			server.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
