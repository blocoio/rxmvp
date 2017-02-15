package com.example.rxmvp.common;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;
import timber.log.Timber;

@PerApplication public class ErrorLogger {

  @Inject public ErrorLogger() {
  }

  public void log(Throwable throwable) {
    if (isInternetError(throwable)) {
      return;
    }

    Timber.e(throwable, throwable.getMessage());
    // Log on crashlytics
  }

  public boolean isInternetError(Throwable throwable) {
    return throwable instanceof SocketTimeoutException
        || throwable instanceof ConnectException
        || throwable instanceof UnknownHostException;
  }
}
