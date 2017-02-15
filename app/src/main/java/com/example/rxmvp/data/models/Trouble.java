package com.example.rxmvp.data.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.squareup.sqldelight.RowMapper;
import java.util.Date;

public class Trouble implements TroubleModel {

  private long id;
  private String code;
  private String description;
  private Date startDate;
  private Boolean read;
  private long vehicleId;

  @Override public long id() {
    return id;
  }

  @Nullable @Override public String code() {
    return code;
  }

  @Nullable @Override public String description() {
    return description;
  }

  @Nullable @Override public Date startDate() {
    return startDate;
  }

  @NonNull @Override public Boolean read() {
    return read == null ? false : read;
  }

  @Override public long vehicleId() {
    return vehicleId;
  }

  public boolean unread() {
    return !read();
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

  public void setVehicleId(Long vehicleId) {
    this.vehicleId = vehicleId;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Trouble trouble = (Trouble) o;

    if (id != trouble.id) return false;
    if (code != null ? !code.equals(trouble.code) : trouble.code != null) return false;
    if (description != null ? !description.equals(trouble.description)
        : trouble.description != null) {
      return false;
    }
    return read != null ? read.equals(trouble.read) : trouble.read == null;
  }

  @Override public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (code != null ? code.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (read != null ? read.hashCode() : 0);
    return result;
  }

  // Config

  private static final DateAdapter DATE_ADAPTER = new DateAdapter();

  public static final TroubleModel.Factory<Trouble> FACTORY =
      new TroubleModel.Factory<>((id, code, description, startDate, read, vehicleId) -> {
        Trouble trouble = new Trouble();
        trouble.id = id;
        trouble.code = code;
        trouble.description = description;
        trouble.startDate = startDate;
        trouble.read = read;
        trouble.vehicleId = vehicleId;
        return trouble;
      }, DATE_ADAPTER);

  public static final RowMapper<Trouble> MAPPER = new TroubleModel.Mapper<>(Trouble.FACTORY);

  public static final RowMapper<Long> SELECT_IDS_MAPPER = FACTORY.select_idsMapper();

  public static final RowMapper<TroubleWithVehicle> SELECT_ALL_WITH_VEHICLE_MAPPER =
      FACTORY.select_all_with_vehicleMapper(TroubleWithVehicle::new, Vehicle.FACTORY);

  public static final RowMapper<Long> COUNT_UNREAD_MAPPER = FACTORY.count_unreadMapper();
}
