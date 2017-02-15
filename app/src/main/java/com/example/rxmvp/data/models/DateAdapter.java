package com.example.rxmvp.data.models;

import android.support.annotation.NonNull;
import com.squareup.sqldelight.ColumnAdapter;
import java.util.Date;

public class DateAdapter implements ColumnAdapter<Date, Long> {
  @NonNull @Override public Date decode(Long databaseValue) {
    return new Date(databaseValue);
  }

  @Override public Long encode(@NonNull Date value) {
    return value.getTime();
  }
}
