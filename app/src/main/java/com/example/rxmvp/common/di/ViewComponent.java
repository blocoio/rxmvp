package com.example.rxmvp.common.di;

import dagger.Subcomponent;
import com.example.rxmvp.ui.notifications.NotificationItemView;
import com.example.rxmvp.ui.notifications.NotificationsView;

@PerView @Subcomponent(modules = ViewModule.class) public interface ViewComponent {
  void inject(NotificationsView notificationsView);

  void inject(NotificationItemView notificationItemView);
}