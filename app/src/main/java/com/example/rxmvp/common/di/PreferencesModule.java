package com.example.rxmvp.common.di;

import android.content.Context;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import dagger.Module;
import dagger.Provides;
import com.example.rxmvp.AndroidApplication;

@Module public class PreferencesModule {

  @Provides @PerApplication
  public SharedPreferences sharedPreferences(Context context, AndroidApplication.Mode appMode) {
    String name = context.getPackageName();
    if (appMode == AndroidApplication.Mode.TEST) {
      name += ".test";
    }
    return context.getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  @Provides @PerApplication
  public RxSharedPreferences rxSharedPreferences(SharedPreferences sharedPreferences) {
    return RxSharedPreferences.create(sharedPreferences);
  }
}
