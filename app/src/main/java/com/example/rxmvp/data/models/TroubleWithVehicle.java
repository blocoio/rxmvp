package com.example.rxmvp.data.models;

import android.support.annotation.NonNull;

public class TroubleWithVehicle
    implements TroubleModel.Select_all_with_vehicleModel<Trouble, Vehicle> {

  private Trouble trouble;
  private Vehicle vehicle;

  public TroubleWithVehicle(Trouble trouble, Vehicle vehicle) {
    this.trouble = trouble;
    this.vehicle = vehicle;
  }

  @NonNull @Override public Trouble trouble() {
    return trouble;
  }

  @NonNull @Override public Vehicle vehicle() {
    return vehicle;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TroubleWithVehicle that = (TroubleWithVehicle) o;

    if (!trouble.equals(that.trouble)) return false;
    return vehicle.equals(that.vehicle);
  }

  @Override public int hashCode() {
    int result = trouble.hashCode();
    result = 31 * result + vehicle.hashCode();
    return result;
  }
}
