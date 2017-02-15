package com.example.rxmvp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import javax.inject.Inject;
import com.example.rxmvp.AndroidApplication;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.Vehicle;
import timber.log.Timber;

@PerApplication public class DatabaseHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "example.db";
  private static final String TEST_DATABASE_NAME = "example_test.db";

  @Inject public DatabaseHelper(Context context, AndroidApplication.Mode appMode) {
    super(context, getAppDatabaseName(appMode), null, DATABASE_VERSION);
  }

  @NonNull private static String getAppDatabaseName(AndroidApplication.Mode appMode) {
    if (appMode == AndroidApplication.Mode.NORMAL) {
      return DATABASE_NAME;
    } else {
      return TEST_DATABASE_NAME;
    }
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Vehicle.CREATE_TABLE);
    db.execSQL(Trouble.CREATE_TABLE);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    dropTables(db);
    onCreate(db);
  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }

  public void clean(SQLiteDatabase db) {
    db.beginTransaction();
    db.delete(Vehicle.TABLE_NAME, null, null);
    db.delete(Trouble.TABLE_NAME, null, null);
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  private void dropTables(SQLiteDatabase db) {
    try {
      db.execSQL("DROP TABLE IF EXISTS " + Vehicle.TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + Trouble.TABLE_NAME);
    } catch (SQLiteException exception) {
      Timber.w(exception);
    }
  }
}
