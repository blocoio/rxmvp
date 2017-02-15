package com.example.rxmvp.ui.common;

import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;

@PerApplication public class DateHelper {

  @Inject public DateHelper() {
  }

  boolean isThisYear(Date date) {
    Calendar todayCalendar = Calendar.getInstance();
    Calendar dateCalendar = Calendar.getInstance();
    dateCalendar.setTime(date);

    return todayCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR);
  }

  public boolean isToday(Date date) {
    Calendar todayCalendar = Calendar.getInstance();
    Calendar dateCalendar = Calendar.getInstance();
    dateCalendar.setTime(date);

    return todayCalendar.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
        && isThisYear(date);
  }
}