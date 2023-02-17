package dev.techdozo.ride_sharing.application.domain.repository;

import dev.techdozo.ride_sharing.application.domain.model.RideData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TripRepository {
    public void saveTripData(RideData rideData) {
      log.info("Storing Trip Data {}", rideData);
    }
}
