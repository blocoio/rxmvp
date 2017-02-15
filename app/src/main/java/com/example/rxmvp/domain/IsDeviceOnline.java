package com.example.rxmvp.domain;

import android.content.Context;
import android.net.NetworkInfo;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import javax.inject.Inject;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.common.di.PerApplication;
import rx.Observable;

@PerApplication public class IsDeviceOnline {

  private final Context context;
  private final Schedulers schedulers;

  @Inject public IsDeviceOnline(Schedulers schedulers, Context context) {
    this.schedulers = schedulers;
    this.context = context;
  }

  public Observable<Boolean> check() {
    return ReactiveNetwork.observeNetworkConnectivity(context)
        .map(connectivity -> connectivity.getState().equals(NetworkInfo.State.CONNECTED))
        .compose(schedulers.applyOnMain());
  }
}
