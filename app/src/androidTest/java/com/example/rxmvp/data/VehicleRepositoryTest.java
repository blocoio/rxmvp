package com.example.rxmvp.data;

import android.support.test.runner.AndroidJUnit4;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.data.models.VehicleModel;
import com.example.rxmvp.helpers.TestStateManager;
import com.example.rxmvp.testing.factories.BaseFactory;
import com.example.rxmvp.testing.factories.TroubleFactory;
import com.example.rxmvp.testing.factories.VehicleFactory;
import rx.Observable;

import static com.google.common.truth.Truth.assertThat;
import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

@RunWith(AndroidJUnit4.class) public class VehicleRepositoryTest {

  private VehicleFactory vehicleFactory;
  private TroubleFactory troubleFactory;
  private BriteDatabase db;
  private VehicleRepository vehicleRepository;

  @Before public void setUp() throws Exception {
    vehicleFactory = new VehicleFactory();
    troubleFactory = new TroubleFactory();
    db = getApplicationComponent().briteDatabase();
    vehicleRepository = getApplicationComponent().vehicleRepository();
  }

  @After public void tearDown() throws Exception {
    new TestStateManager().clean();
  }

  @Test public void updateAll() throws Exception {
    // Create bogus data first to make sure it deletes it
    Vehicle oldVehicle = vehicleFactory.create(db);
    Trouble oldTrouble = troubleFactory.build();
    troubleFactory.create(db, oldTrouble, oldVehicle);

    // Existing trouble already read, that will be kept
    Vehicle readVehicle = vehicleFactory.create(db);
    Trouble readTrouble = troubleFactory.build();
    readTrouble.setRead(true);
    troubleFactory.create(db, readTrouble, readVehicle);
    readVehicle.setTroubles(Collections.singletonList(readTrouble));

    // New vehicles and troubles received
    List<Vehicle> newVehicles = vehicleFactory.buildList();
    for (Vehicle vehicle : newVehicles) {
      vehicle.setTroubles(troubleFactory.buildList());
    }
    newVehicles.add(readVehicle);

    List<TroubleWithVehicle> newNotifications =
        vehicleRepository.updateAll(newVehicles).toBlocking().first();

    List<Vehicle> vehiclesFromDb = getAllVehicles();
    List<Trouble> troublesFromDb = getAllTroubles();

    for (Vehicle newVehicle : newVehicles) {
      assertThat(vehiclesFromDb).contains(newVehicle);
      for (Trouble newTrouble : newVehicle.troubles()) {
        assertThat(troublesFromDb).contains(newTrouble);
        // Compare read state
        Trouble troubleFromDb = Observable.from(troublesFromDb)
            .filter(t -> t.id() == newTrouble.id())
            .toBlocking()
            .first();
        assertThat(troubleFromDb.read()).isEqualTo(newTrouble.read());
      }
    }

    assertThat(vehiclesFromDb).doesNotContain(oldVehicle);
    assertThat(troublesFromDb).doesNotContain(oldTrouble);

    assertThat(newNotifications.size()).isEqualTo(
        BaseFactory.DEFAULT_LIST_SIZE * BaseFactory.DEFAULT_LIST_SIZE);
  }

  private List<Vehicle> getAllVehicles() {
    return db.createQuery(Vehicle.TABLE_NAME, "SELECT * FROM " + Vehicle.TABLE_NAME)
        .mapToList(new VehicleModel.Mapper<>(Vehicle.FACTORY)::map)
        .toBlocking()
        .first();
  }

  private List<Trouble> getAllTroubles() {
    return db.createQuery(Trouble.TABLE_NAME, "SELECT * FROM " + Trouble.TABLE_NAME)
        .mapToList(Trouble.MAPPER::map)
        .toBlocking()
        .first();
  }
}
