package com.example.rxmvp.data;

import android.database.sqlite.SQLiteDatabase;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;
import rx.functions.Action1;

@PerApplication public class DatabaseManager {

  private final DatabaseHelper dbHelper;
  private final SQLiteDatabase sqLiteDatabase;

  @Inject public DatabaseManager(DatabaseHelper dbHelper, SQLiteDatabase sqLiteDatabase) {
    this.dbHelper = dbHelper;
    this.sqLiteDatabase = sqLiteDatabase;
  }

  public Action1<Void> clearDatabase() {
    return __ -> dbHelper.clean(sqLiteDatabase);
  }
}
