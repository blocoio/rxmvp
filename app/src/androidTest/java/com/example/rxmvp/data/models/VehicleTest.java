package com.example.rxmvp.data.models;

import android.support.test.runner.AndroidJUnit4;
import com.squareup.sqlbrite.BriteDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.helpers.TestStateManager;
import com.example.rxmvp.testing.factories.VehicleFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

@RunWith(AndroidJUnit4.class) public class VehicleTest {

  private VehicleFactory vehicleFactory;
  private TestStateManager testStateManager;
  private BriteDatabase briteDatabase;

  @Before public void setUp() throws Exception {
    vehicleFactory = new VehicleFactory();
    testStateManager = new TestStateManager();
    briteDatabase = getApplicationComponent().briteDatabase();
  }

  @After public void tearDown() throws Exception {
    testStateManager.clean();
  }

  @Test public void fields() throws Exception {
    Vehicle vehicle = vehicleFactory.build();
    saveVehicle(vehicle);
    assertThat(getFirstVehicle().status(), is(equalTo(vehicle.status())));
    assertThat(getFirstVehicle().description(), is(equalTo(vehicle.description())));
  }

  private void saveVehicle(Vehicle vehicle) {
    VehicleModel.InsertQuery insert =
        new VehicleModel.InsertQuery(briteDatabase.getWritableDatabase(), Vehicle.FACTORY);
    insert.bind(vehicle.id(), vehicle.description(), vehicle.status());
    briteDatabase.executeInsert(insert.table, insert.program);
  }

  private Vehicle getFirstVehicle() {
    return briteDatabase.createQuery(Vehicle.TABLE_NAME, "SELECT * FROM vehicle")
        .mapToList(new VehicleModel.Mapper<>(Vehicle.FACTORY)::map)
        .toBlocking()
        .first()
        .get(0);
  }
}
