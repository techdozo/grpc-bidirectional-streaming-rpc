package dev.techdozo.ride_sharing.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TripSummary {
  double distance;
  double charge;
  long time;
}
