package com.example.rxmvp.ui;

import android.support.annotation.CallSuper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter {

  private CompositeSubscription compositeSubscription;

  public BasePresenter() {
    compositeSubscription = new CompositeSubscription();
  }

  @CallSuper public void stop() {
    compositeSubscription.clear();
  }

  protected void addSub(Subscription subscription) {
    compositeSubscription.add(subscription);
  }

  protected void addSub(Subscription... subscriptions) {
    compositeSubscription.addAll(subscriptions);
  }
}
