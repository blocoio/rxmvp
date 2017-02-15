package com.example.rxmvp.common.di;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import com.example.rxmvp.AndroidApplication;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.data.DatabaseHelper;

@Module public class ApplicationModule {
  private final AndroidApplication application;

  public ApplicationModule(AndroidApplication application) {
    this.application = application;
  }

  @Provides @PerApplication public Context applicationContext() {
    return application;
  }

  @Provides @PerApplication public AndroidApplication.Mode applicationMode() {
    return application.getMode();
  }

  @Provides @PerApplication public Resources resources(Context context) {
    return context.getResources();
  }

  @Provides @PerApplication public NotificationManager notificationManager(Context context) {
    return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Provides @PerApplication public SQLiteDatabase sqLiteDatabase(DatabaseHelper databaseHelper) {
    return databaseHelper.getWritableDatabase();
  }

  @Provides @PerApplication
  public BriteDatabase briteDatabase(DatabaseHelper databaseHelper, Schedulers schedulers) {
    SqlBrite sqlBrite = new SqlBrite.Builder().build();
    return sqlBrite.wrapDatabaseHelper(databaseHelper, schedulers.getBackgroundThread());
  }

  @Provides @PerApplication public Gson gson() {
    return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
  }
}
