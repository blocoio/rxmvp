package com.example.rxmvp.ui.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;

@PerApplication public class DateTimeFormatter {

  private final DateFormat timeFormat;
  private final SimpleDateFormat dateTimeFormat;
  private final SimpleDateFormat dateTimeWithYearFormat;
  private final DateHelper dateHelper;

  @Inject public DateTimeFormatter(DateHelper dateHelper) {
    this.dateHelper = dateHelper;
    timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    dateTimeFormat = new SimpleDateFormat("dd MMM\nHH:mm", Locale.getDefault());
    dateTimeWithYearFormat = new SimpleDateFormat("dd MMM\nyyyy\nHH:mm", Locale.getDefault());
  }

  public String shortString(Date date) {
    if (dateHelper.isToday(date)) {
      return timeFormat.format(date);
    } else if (dateHelper.isThisYear(date)) {
      return dateTimeFormat.format(date);
    } else {
      return dateTimeWithYearFormat.format(date);
    }
  }
}
