package com.example.rxmvp.data.models;

import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.EnumColumnAdapter;
import com.squareup.sqldelight.RowMapper;
import java.util.Collections;
import java.util.List;

public class Vehicle implements VehicleModel {

  public enum Status {
    ACTIVE, INACTIVE
  }

  private long id;
  private String description;
  private Status status;
  @SerializedName("troubles") private List<Trouble> troubles;

  @Override public long id() {
    return id;
  }

  @Nullable @Override public String description() {
    return description;
  }

  @Nullable @Override public Status status() {
    return status;
  }

  public List<Trouble> troubles() {
    if (troubles == null) {
      return Collections.EMPTY_LIST;
    } else {
      return troubles;
    }
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setTroubles(List<Trouble> troubles) {
    this.troubles = troubles;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Vehicle vehicle = (Vehicle) o;

    if (id != vehicle.id) return false;
    if (description != null ? !description.equals(vehicle.description)
        : vehicle.description != null) {
      return false;
    }
    return status() != null ? status().equals(vehicle.status()) : vehicle.status() == null;
  }

  @Override public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (status() != null ? status().hashCode() : 0);
    return result;
  }

  // Config

  private static final ColumnAdapter<Status, String> STATUS_ADAPTER =
      EnumColumnAdapter.create(Vehicle.Status.class);

  public static final Factory<Vehicle> FACTORY =
      new Factory<>((id, description, status) -> {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setDescription(description);
        vehicle.setStatus(status);
        return vehicle;
      }, STATUS_ADAPTER);

  public static final RowMapper<Long> SELECT_IDS_MAPPER = Vehicle.FACTORY.select_idsMapper();
}
