package com.example.rxmvp.testing;

import com.example.rxmvp.common.Schedulers;
import rx.Scheduler;

public class TestSchedulers extends Schedulers {

  @Override public Scheduler getMainThread() {
    return rx.schedulers.Schedulers.immediate();
  }

  @Override public Scheduler getBackgroundThread() {
    return rx.schedulers.Schedulers.immediate();
  }
}

