package com.example.rxmvp.data;

import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqldelight.SqlDelightStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.data.models.Vehicle;
import rx.Observable;

import static rx.Observable.defer;
import static rx.Observable.just;

@PerApplication public class TroubleRepository {

  private final BriteDatabase db;

  @Inject public TroubleRepository(BriteDatabase db) {
    this.db = db;
  }

  public List<Long> getIds() {
    List<Long> ids = new ArrayList<>();
    Cursor cursor = db.query(Trouble.SELECT_IDS);
    while (cursor.moveToNext()) {
      ids.add(Trouble.SELECT_IDS_MAPPER.map(cursor));
    }
    cursor.close();
    return ids;
  }

  public Observable<List<TroubleWithVehicle>> getAllWithVehicle() {
    return db.createQuery(Arrays.asList(Vehicle.TABLE_NAME, Trouble.TABLE_NAME),
        Trouble.SELECT_ALL_WITH_VEHICLE).mapToList(Trouble.SELECT_ALL_WITH_VEHICLE_MAPPER::map);
  }

  public Observable<Void> readAll() {
    return defer(() -> {
      db.executeAndTrigger(Trouble.TABLE_NAME, Trouble.READALL);
      return just(null);
    });
  }

  public Observable<Long> countUnread() {
    return db.createQuery(Trouble.TABLE_NAME, Trouble.COUNT_UNREAD)
        .mapToOne(Trouble.COUNT_UNREAD_MAPPER::map);
  }

  public void deleteByIds(List<Long> ids) {
    long[] idsArray = new long[ids.size()];
    for (int i = 0; i < ids.size(); i++) {
      idsArray[i] = ids.get(i);
    }
    SqlDelightStatement delete = Trouble.FACTORY.deleteById(idsArray);
    db.executeAndTrigger(delete.tables, delete.statement, (Object[]) delete.args);
  }
}
