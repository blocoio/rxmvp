package com.example.rxmvp.domain;

import javax.inject.Inject;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.TroubleRepository;
import rx.Observable;

@PerApplication public class ReadAllTroubles {

  private final Schedulers schedulers;
  private final TroubleRepository troubleRepository;

  @Inject public ReadAllTroubles(Schedulers schedulers, TroubleRepository troubleRepository) {
    this.schedulers = schedulers;
    this.troubleRepository = troubleRepository;
  }

  public Observable<Void> read() {
    return troubleRepository.readAll().compose(schedulers.applyOnMain());
  }
}
