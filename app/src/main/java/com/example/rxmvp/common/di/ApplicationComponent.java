package com.example.rxmvp.common.di;

import android.content.SharedPreferences;
import com.squareup.sqlbrite.BriteDatabase;
import dagger.Component;
import com.example.rxmvp.data.DatabaseManager;
import com.example.rxmvp.data.TroubleRepository;
import com.example.rxmvp.data.UserRepository;
import com.example.rxmvp.data.VehicleRepository;
import com.example.rxmvp.data.api.Api;
import com.example.rxmvp.domain.SyncDatabase;

@PerApplication
@Component(modules = { ApplicationModule.class, ApiModule.class, PreferencesModule.class })
public interface ApplicationComponent {
  ActivityComponent plus(ActivityModule activityModule);

  BriteDatabase briteDatabase();

  Api api();

  SharedPreferences sharedPreferences();

  UserRepository sessionRepository();

  TroubleRepository troubleRepository();

  VehicleRepository vehicleRepository();

  SyncDatabase syncDatabase();

  UserRepository userRepository();

  DatabaseManager databaseManager();
}
