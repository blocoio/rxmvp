package com.example.rxmvp.ui.notifications;

import com.jakewharton.rxrelay.PublishRelay;
import java.util.List;
import javax.inject.Inject;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.domain.GetTroubles;
import com.example.rxmvp.domain.IsDeviceOnline;
import com.example.rxmvp.domain.ReadAllTroubles;
import com.example.rxmvp.domain.SyncDatabase;
import com.example.rxmvp.ui.BasePresenter;
import rx.Observable;

class NotificationsPresenter extends BasePresenter {

  private final GetTroubles getTroubles;
  private final SyncDatabase syncDatabase;
  private final IsDeviceOnline isDeviceOnline;
  private final ReadAllTroubles readAllTroubles;
  private final PublishRelay<List<TroubleWithVehicle>> notifications;

  @Inject NotificationsPresenter(GetTroubles getTroubles,
      SyncDatabase syncDatabase, IsDeviceOnline isDeviceOnline, ReadAllTroubles readAllTroubles) {
    this.getTroubles = getTroubles;
    this.syncDatabase = syncDatabase;
    this.isDeviceOnline = isDeviceOnline;
    this.readAllTroubles = readAllTroubles;
    this.notifications = PublishRelay.create();
  }

  void start(View view) {
    Observable<Void> sharedViewRefresh = view.refresh().share();

    addSub(

        getTroubles.get().subscribe(notifications),
        notifications.filter(List::isEmpty).subscribe(__ -> view.showEmpty()),
        notifications.filter(list -> !list.isEmpty()).subscribe(view::showNotifications),
        isDeviceOnline.check().subscribe(view::setRefreshEnabled), syncDatabase.getState()
            .map(state -> state.equals(SyncDatabase.State.SYNCING))
            .subscribe(view::updateRefreshing),

        view.notificationClicks().subscribe(view::openNotification),
        sharedViewRefresh.flatMap(__ -> readAllTroubles.read()).subscribe(),
        sharedViewRefresh.flatMap(__ -> syncDatabase.sync()).subscribe());
  }

  interface View {
    Observable<TroubleWithVehicle> notificationClicks();

    Observable<Void> refresh();

    void showNotifications(List<TroubleWithVehicle> notifications);

    void showEmpty();

    void setRefreshEnabled(boolean isEnabled);

    void updateRefreshing(boolean isRefreshing);

    void openNotification(TroubleWithVehicle notification);
  }
}
