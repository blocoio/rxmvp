package com.example.rxmvp.ui.notifications;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import butterknife.BindView;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import java.util.List;
import javax.inject.Inject;
import com.example.rxmvp.R;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.ui.BaseView;
import com.example.rxmvp.ui.common.lists.DividerItemDecoration;
import com.example.rxmvp.ui.common.lists.RecyclerAdapter;
import rx.Observable;

public class NotificationsView extends BaseView implements NotificationsPresenter.View {

  @BindView(R.id.notifications_refresh) SwipeRefreshLayout refresh;
  @BindView(R.id.notifications_list) RecyclerView list;
  @BindView(R.id.notifications_empty) View empty;

  @Inject NotificationsPresenter presenter;

  private RecyclerAdapter<TroubleWithVehicle, NotificationItemView> adapter;

  public NotificationsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    getViewComponent().inject(this);

    adapter = new RecyclerAdapter<>(NotificationItemView.class);
    list.setAdapter(adapter);
    list.setLayoutManager(new LinearLayoutManager(getContext()));
    list.addItemDecoration(new DividerItemDecoration(getContext()));

    presenter.start(this);
  }

  @Override protected int getLayoutRes() {
    return R.layout.view_notifications;
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.stop();
  }

  @Override public Observable<TroubleWithVehicle> notificationClicks() {
    return adapter.getItemClicks();
  }

  @Override public Observable<Void> refresh() {
    return RxSwipeRefreshLayout.refreshes(refresh);
  }

  @Override public void showNotifications(List<TroubleWithVehicle> notifications) {
    empty.setVisibility(GONE);
    list.setVisibility(VISIBLE);
    adapter.updateItems(notifications);
  }

  @Override public void showEmpty() {
    empty.setVisibility(VISIBLE);
    list.setVisibility(GONE);
  }

  @Override public void setRefreshEnabled(boolean isEnabled) {
    refresh.setEnabled(isEnabled);
  }

  @Override public void updateRefreshing(boolean isRefreshing) {
    refresh.setRefreshing(isRefreshing);
  }

  @Override public void openNotification(TroubleWithVehicle notification) {
    // Open notification
  }
}
