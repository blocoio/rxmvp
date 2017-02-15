package com.example.rxmvp.domain;

import android.support.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.example.rxmvp.common.ErrorLogger;
import com.example.rxmvp.data.UserRepository;
import com.example.rxmvp.data.VehicleRepository;
import com.example.rxmvp.data.api.Api;
import com.example.rxmvp.data.api.ListResponse;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.testing.TestSchedulers;
import com.example.rxmvp.testing.factories.VehicleFactory;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

@RunWith(MockitoJUnitRunner.class) public class SyncDatabaseTest {

  private VehicleFactory vehicleFactory;
  private SyncDatabase syncDatabase;

  @Mock Api api;
  @Mock VehicleRepository vehicleRepository;
  @Mock ErrorLogger errorLogger;
  @Mock UserRepository userRepository;

  @Before public void setUp() {
    vehicleFactory = new VehicleFactory();
    syncDatabase = new SyncDatabase(new TestSchedulers(), api, vehicleRepository, errorLogger);
  }

  @Test public void sync() throws Exception {
    ListResponse<Vehicle> response = mockResponse();

    syncDatabase.sync().toBlocking().first();

    verify(api).getAllVehiclesWithTroubles(eq(0));
    verify(vehicleRepository).updateAll(eq(response.getList()));
  }

  @Test public void getState() throws Exception {
    mockResponse();
    TestSubscriber<SyncDatabase.State> stateSubscriber = new TestSubscriber<>();
    syncDatabase.getState().subscribe(stateSubscriber);

    syncDatabase.sync().toBlocking().first();

    stateSubscriber.assertReceivedOnNext(
        Arrays.asList(SyncDatabase.State.IDLE, SyncDatabase.State.SYNCING,
            SyncDatabase.State.IDLE));
  }

  @NonNull private ListResponse<Vehicle> mockResponse() {
    List<Vehicle> vehicles = vehicleFactory.buildList();
    ListResponse<Vehicle> response = new ListResponse<>();
    response.setList(vehicles);
    when(api.getAllVehiclesWithTroubles(eq(0))).thenReturn(
        just(Result.response(Response.success(response))));
    when(vehicleRepository.updateAll(any(List.class))).thenReturn(just(Collections.EMPTY_LIST));
    return response;
  }
}