package dev.techdozo.ride_sharing.application.domain.service;

import dev.techdozo.ride_sharing.application.domain.model.TripSummary;

public class TripSummaryService {
  // Dummy implementation
  private long startTime;
  private double distance;

  private double charge;

  public TripSummary getTripSummary(String rideId) {
    if (this.startTime == 0) {
      this.startTime = System.currentTimeMillis() + 80*1000;
    }
    distance = distance + 0.01;
    long tripTime =  startTime - System.currentTimeMillis() ;
    return new TripSummary(distance, distance * .5, tripTime / 1000);
  }
}
