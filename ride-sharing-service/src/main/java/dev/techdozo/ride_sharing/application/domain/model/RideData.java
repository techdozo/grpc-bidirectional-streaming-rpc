package dev.techdozo.ride_sharing.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class RideData {
  String driverId;
  String rideId;
  double latitude;
  double longitude;
}
