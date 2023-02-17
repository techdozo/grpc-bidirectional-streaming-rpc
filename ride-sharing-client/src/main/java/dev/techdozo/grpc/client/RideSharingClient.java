package dev.techdozo.grpc.client;

import dev.techdozo.ride_sharing.trip.Service;
import dev.techdozo.ride_sharing.trip.Service.TripSummaryResponse;
import dev.techdozo.ride_sharing.trip.TripServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
public class RideSharingClient {
  private final String host;
  private final int port;

  public RideSharingClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @SneakyThrows
  public void callServer() {

    log.info("Calling Server..");
    var managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    TripServiceGrpc.TripServiceStub tripServiceStub = TripServiceGrpc.newStub(managedChannel);

    StreamObserver<Service.TripDataRequest> tripDataRequestStreamObserver =
        tripServiceStub.sendTripData(new TripSummaryCallback());

    // Create stream of random 1000 calls with random lat and long, with delay of 1 sec

    IntStream.range(0, 600)
        .mapToObj(
            n -> {
              try {
                Thread.sleep(100);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              return Service.TripDataRequest.newBuilder()
                  .setRideType(
                      Service.RideType.newBuilder()
                          .setDriverId("Driver_1")
                          .setRideId("Ride_1")
                          .build())
                  .setLatitude(ThreadLocalRandom.current().nextDouble(-90, 90))
                  .setLongitude(ThreadLocalRandom.current().nextDouble(-180, 180))
                  .build();
            })
        .forEach(tripDataRequestStreamObserver::onNext);

    log.info("Calling complete..");
    tripDataRequestStreamObserver.onCompleted();
    Thread.sleep(30000);
  }

  private static class TripSummaryCallback implements StreamObserver<TripSummaryResponse> {

    @Override
    public void onNext(TripSummaryResponse tripSummaryResponse) {
      DecimalFormat df = new DecimalFormat("0.00");
      log.info(
          "Trip Summary : distance {}, charge {}, time remaining {} minutes",
          df.format(tripSummaryResponse.getDistance()),
          df.format(tripSummaryResponse.getCharge()),
          df.format((double) tripSummaryResponse.getTime() / 60));
    }

    @Override
    public void onError(Throwable cause) {
      log.error("Error occurred, cause {}", cause.getMessage());
    }

    @Override
    public void onCompleted() {
      log.info("Stream completed");
    }
  }

  public static void main(String[] args) {
    var client = new RideSharingClient("0.0.0.0", 3000);
    client.callServer();
  }
}
