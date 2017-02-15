package com.example.rxmvp.data;

import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqldelight.SqlDelightStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.TroubleModel;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.data.models.VehicleModel;
import rx.Observable;
import timber.log.Timber;

@PerApplication public class VehicleRepository {

  private final BriteDatabase db;
  private final TroubleRepository troubleRepository;

  @Inject public VehicleRepository(BriteDatabase db, TroubleRepository troubleRepository) {
    this.db = db;
    this.troubleRepository = troubleRepository;
  }

  public List<Long> getIds() {
    List<Long> ids = new ArrayList<>();
    Cursor cursor = db.query(Vehicle.SELECT_IDS);
    while (cursor.moveToNext()) {
      ids.add(Vehicle.SELECT_IDS_MAPPER.map(cursor));
    }
    cursor.close();
    return ids;
  }

  public Observable<List<TroubleWithVehicle>> updateAll(List<Vehicle> vehicles) {
    return deferOnTransaction(() -> {
      List<TroubleWithVehicle> newNotifications = new ArrayList<>();
      List<Long> existingVehicleIds = getIds();
      List<Long> existingTroubleIds = troubleRepository.getIds();

      VehicleModel.InsertQuery insertVehicle =
          new Vehicle.InsertQuery(db.getWritableDatabase(), Vehicle.FACTORY);
      VehicleModel.UpdateQuery updateVehicle =
          new Vehicle.UpdateQuery(db.getWritableDatabase(), Vehicle.FACTORY);
      TroubleModel.InsertQuery insertTrouble =
          new Trouble.InsertQuery(db.getWritableDatabase(), Trouble.FACTORY);
      TroubleModel.UpdateQuery updateTrouble =
          new Trouble.UpdateQuery(db.getWritableDatabase(), Trouble.FACTORY);

      for (Vehicle vehicle : vehicles) {
        if (!existingVehicleIds.contains(vehicle.id())) {
          insertVehicle.bind(vehicle.id(), vehicle.description(), vehicle.status());
          db.executeInsert(insertVehicle.table, insertVehicle.program);
        } else {
          updateVehicle.bind(vehicle.description(), vehicle.status(), vehicle.id());
          db.executeUpdateDelete(updateVehicle.table, updateVehicle.program);
          existingVehicleIds.remove(vehicle.id());
        }

        for (Trouble trouble : vehicle.troubles()) {
          if (!existingTroubleIds.contains(trouble.id())) {
            insertTrouble.bind(trouble.id(), trouble.code(), trouble.description(),
                trouble.startDate(), false, vehicle.id());
            db.executeInsert(insertTrouble.table, insertTrouble.program);
            newNotifications.add(new TroubleWithVehicle(trouble, vehicle));
          } else {
            updateTrouble.bind(trouble.code(), trouble.description(), trouble.startDate(),
                trouble.id());
            db.executeUpdateDelete(updateTrouble.table, updateTrouble.program);
            existingTroubleIds.remove(trouble.id());
          }
        }
      }

      deleteByIds(existingVehicleIds);
      troubleRepository.deleteByIds(existingTroubleIds);

      return newNotifications;
    });
  }

  private void deleteByIds(List<Long> ids) {
    long[] idsArray = new long[ids.size()];
    for (int i = 0; i < ids.size(); i++) {
      idsArray[i] = ids.get(i);
    }
    SqlDelightStatement delete = Vehicle.FACTORY.deleteById(idsArray);
    db.executeAndTrigger(delete.tables, delete.statement, (Object[]) delete.args);
  }

  private List<Vehicle> mapToListOfVehicles(List<TroubleWithVehicle> troublesWithVehicle) {
    List<Vehicle> vehicles = new ArrayList<>();
    for (TroubleWithVehicle troubleWithVehicle : troublesWithVehicle) {
      Trouble trouble = troubleWithVehicle.trouble();
      Vehicle vehicle = troubleWithVehicle.vehicle();
      if (vehicles.contains(vehicle)) {
        // Get the list instance
        vehicles.get(vehicles.indexOf(vehicle)).troubles().add(trouble);
      } else {
        vehicle.setTroubles(new ArrayList<>());
        vehicle.troubles().add(trouble);
        vehicles.add(vehicle);
      }
    }
    return vehicles;
  }

  private <T> Observable<T> deferOnTransaction(Action<T> action) {
    return Observable.defer(() -> {
      T result = null;
      BriteDatabase.Transaction transaction = db.newTransaction();
      try {
        result = action.execute();
        transaction.markSuccessful();
      } catch (SQLException exception) {
        Timber.e(exception);
      } finally {
        transaction.end();
      }
      return Observable.just(result);
    });
  }

  public interface Action<T> {
    T execute() throws SQLException;
  }
}
