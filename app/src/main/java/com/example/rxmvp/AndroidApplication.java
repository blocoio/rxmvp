package com.example.rxmvp;

import android.app.Application;
import android.os.StrictMode;
import com.example.rxmvp.common.di.ApplicationComponent;
import com.example.rxmvp.common.di.ApplicationModule;
import com.example.rxmvp.common.di.DaggerApplicationComponent;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AndroidApplication extends Application {

  private Mode mode;
  private ApplicationComponent applicationComponent;
  private CompositeSubscription subscriptions;

  @Override public void onCreate() {
    super.onCreate();
    subscriptions = new CompositeSubscription();
    checkTestMode();
    initializeInjector();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
      configStrictMode();
    }
  }

  @Override public void onTerminate() {
    super.onTerminate();
    subscriptions.clear();
  }

  public ApplicationComponent getApplicationComponent() {
    return this.applicationComponent;
  }

  public Mode getMode() {
    return mode;
  }

  private void initializeInjector() {
    this.applicationComponent =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
  }

  private void configStrictMode() {
    if (getMode() == Mode.TEST) {
      return;
    }
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
        .permitDiskReads()  // Initializing sharedPreferences reads from disk
        .permitDiskWrites() // Initializing sharedPreferences writes to disk
        .penaltyLog()
        .penaltyDialog()
        .build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
  }

  // Test loading a random test class, to check if we're in test mode
  private void checkTestMode() {
    try {
      getClassLoader().loadClass("com.example.rxmvp.ApplicationTest");
      mode = Mode.TEST;
    } catch (final Exception e) {
      mode = Mode.NORMAL;
    }
  }

  public enum Mode {
    NORMAL, TEST
  }
}