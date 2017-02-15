package com.example.rxmvp.domain;

import javax.inject.Inject;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.TroubleRepository;
import rx.Observable;

@PerApplication public class GetUnreadTroublesCount {

  private final Schedulers schedulers;
  private final TroubleRepository troubleRepository;

  @Inject
  public GetUnreadTroublesCount(Schedulers schedulers, TroubleRepository troubleRepository) {
    this.schedulers = schedulers;
    this.troubleRepository = troubleRepository;
  }

  public Observable<Long> get() {
    return troubleRepository.countUnread().compose(schedulers.applyOnMain());
  }
}
