package com.example.rxmvp.common;

import com.example.rxmvp.common.di.PerApplication;
import javax.inject.Inject;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@PerApplication public class Schedulers {

  @Inject public Schedulers() {
  }

  public Scheduler getMainThread() {
    return AndroidSchedulers.mainThread();
  }

  public Scheduler getBackgroundThread() {
    return rx.schedulers.Schedulers.io();
  }

  public <T> Observable.Transformer<T, T> applyOnMain() {
    return observable -> observable.subscribeOn(getBackgroundThread()).observeOn(getMainThread());
  }

  public <T> Observable.Transformer<T, T> applyOnBackground() {
    return observable -> observable.subscribeOn(getBackgroundThread())
        .observeOn(getBackgroundThread());
  }
}
