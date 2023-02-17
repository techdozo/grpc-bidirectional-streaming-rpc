package dev.techdozo.ride_sharing;

import dev.techdozo.ride_sharing.api.RideSharingAPI;
import dev.techdozo.ride_sharing.application.domain.service.TripSummaryService;
import dev.techdozo.ride_sharing.application.domain.repository.TripRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RideSharingServer {
  private final int port;
  private final Server server;

  public RideSharingServer(int port) {
    this.port = port;
    var rideSharingAPI = new RideSharingAPI(new TripRepository(), new TripSummaryService());
    this.server = ServerBuilder.forPort(port).addService(rideSharingAPI).build();
  }

  public void start() throws IOException {
    log.info("Starting Server..");
    server.start();
    log.info("Server Started on port {} ", port);

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  try {
                    this.stop();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }));
  }

  private void stop() throws InterruptedException {
    log.info("Stopping Server..");
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  private void blockUntilShutDown() throws InterruptedException {
    if (this.server != null) {
      server.awaitTermination();
    }
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    var grpcServer = new RideSharingServer(3000);
    grpcServer.start();
    grpcServer.blockUntilShutDown();
  }
}
