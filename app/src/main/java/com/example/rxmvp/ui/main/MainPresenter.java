package com.example.rxmvp.ui.main;

import javax.inject.Inject;
import com.example.rxmvp.common.di.PerActivity;
import com.example.rxmvp.domain.GetUnreadTroublesCount;
import com.example.rxmvp.domain.IsDeviceOnline;
import com.example.rxmvp.domain.SyncDatabase;
import com.example.rxmvp.ui.BasePresenter;
import rx.Observable;
import rx.functions.Action1;

@PerActivity class MainPresenter extends BasePresenter {

  private static final long MAX_COUNTER_VALUE = 99;

  private final IsDeviceOnline isDeviceOnline;
  private final GetUnreadTroublesCount getUnreadTroublesCount;
  private final SyncDatabase syncDatabase;

  @Inject MainPresenter(IsDeviceOnline isDeviceOnline,
      GetUnreadTroublesCount getUnreadTroublesCount, SyncDatabase syncDatabase) {
    this.isDeviceOnline = isDeviceOnline;
    this.getUnreadTroublesCount = getUnreadTroublesCount;
    this.syncDatabase = syncDatabase;
  }

  void start(View view) {
    Observable<Long> unreadTroublesCount = getUnreadTroublesCount.get().share();
    addSub(

        syncDatabase.sync().subscribe(),
        isDeviceOnline.check().map(online -> !online).subscribe(view.setOfflineVisibility()),

        unreadTroublesCount.map(this::wrapCounterValue)
            .subscribe(view.updateNotificationsCounter()),
        unreadTroublesCount.map(count -> count != 0)
            .subscribe(view.setNotificationsCounterVisibility())

    );
  }

  private String wrapCounterValue(Long value) {
    if (value <= MAX_COUNTER_VALUE) {
      return String.valueOf(value);
    } else {
      return MAX_COUNTER_VALUE + "+";
    }
  }

  public interface View {
    Action1<? super Boolean> setOfflineVisibility();

    Action1<? super String> updateNotificationsCounter();

    Action1<? super Boolean> setNotificationsCounterVisibility();
  }
}
