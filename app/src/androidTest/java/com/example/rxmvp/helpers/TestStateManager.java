package com.example.rxmvp.helpers;

import android.content.SharedPreferences;
import com.example.rxmvp.data.DatabaseManager;
import com.example.rxmvp.data.UserRepository;
import com.example.rxmvp.domain.SyncDatabase;
import com.example.rxmvp.testing.factories.UserFactory;

import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

public class TestStateManager {

  private SyncDatabase syncDatabase;
  private UserRepository userRepository;
  private DatabaseManager databaseManager;
  private SharedPreferences sharedPreferences;

  public TestStateManager() {
    syncDatabase = getApplicationComponent().syncDatabase();
    userRepository = getApplicationComponent().userRepository();
    databaseManager = getApplicationComponent().databaseManager();
    sharedPreferences = getApplicationComponent().sharedPreferences();
  }

  public void login() {
    userRepository.setUser().call(new UserFactory().build());
  }

  public void create() {
    syncDatabase.sync().toBlocking().first();
  }

  public void clean() {
    databaseManager.clearDatabase().call(null);
    sharedPreferences.edit().clear().apply();
  }
}
