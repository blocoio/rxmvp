package com.example.rxmvp.domain;

import java.util.List;
import javax.inject.Inject;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.TroubleRepository;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import rx.Observable;

@PerApplication public class GetTroubles {

  private final Schedulers schedulers;
  private final TroubleRepository troubleRepository;

  @Inject public GetTroubles(Schedulers schedulers, TroubleRepository troubleRepository) {
    this.schedulers = schedulers;
    this.troubleRepository = troubleRepository;
  }

  public Observable<List<TroubleWithVehicle>> get() {
    return troubleRepository.getAllWithVehicle().compose(schedulers.applyOnMain());
  }
}
