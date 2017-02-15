package com.example.rxmvp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import javax.inject.Inject;
import com.example.rxmvp.R;
import com.example.rxmvp.ui.BaseActivity;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements MainPresenter.View {

  @Inject MainPresenter presenter;

  @BindView(R.id.main_notifications_counter) TextView notificationsCounter;
  @BindView(R.id.main_offline) View offline;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);

    presenter.start(this);
  }

  @Override protected int getLayoutRes() {
    return R.layout.activity_main;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenter.stop();
  }

  @Override public Action1<? super Boolean> setOfflineVisibility() {
    return RxView.visibility(offline);
  }

  @Override public Action1<? super String> updateNotificationsCounter() {
    return RxTextView.text(notificationsCounter);
  }

  @Override public Action1<? super Boolean> setNotificationsCounterVisibility() {
    return RxView.visibility(notificationsCounter);
  }

  public static class Factory {
    public static Intent getIntent(Context context) {
      return new Intent(context, MainActivity.class);
    }
  }
}
