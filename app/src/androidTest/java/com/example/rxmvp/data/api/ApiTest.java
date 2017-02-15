package com.example.rxmvp.data.api;

import android.support.test.runner.AndroidJUnit4;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.helpers.TestStateManager;

import static com.google.common.truth.Truth.assertThat;
import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

@RunWith(AndroidJUnit4.class) public class ApiTest {

  private Api api;

  @Before public void setUp() throws Exception {
    api = getApplicationComponent().api();
    new TestStateManager().login();
  }

  @After public void tearDown() throws Exception {
    new TestStateManager().clean();
  }

  @Test public void getAllVehiclesWithActiveTroubles() throws Exception {
    ListResponse<Vehicle> response =
        api.getAllVehiclesWithTroubles(0).toBlocking().first().response().body();

    List<Vehicle> vehicles = response.getList();
    assertThat(vehicles).hasSize(2);

    Vehicle vehicle = vehicles.get(0);
    assertThat(vehicle.id()).isEqualTo(100);
    assertThat(vehicle.description()).isEqualTo("Honda NSX");
    assertThat(vehicle.status()).isEqualTo(Vehicle.Status.ACTIVE);

    List<Trouble> troubles = vehicle.troubles();
    assertThat(troubles).hasSize(1);

    Trouble trouble = troubles.get(0);
    assertThat(trouble.id()).isEqualTo(100);
    assertThat(trouble.code()).isEqualTo("P0055");
  }
}
