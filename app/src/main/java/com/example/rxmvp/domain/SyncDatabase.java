package com.example.rxmvp.domain;

import android.support.annotation.NonNull;
import com.jakewharton.rxrelay.BehaviorRelay;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.example.rxmvp.common.ErrorLogger;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.VehicleRepository;
import com.example.rxmvp.data.api.Api;
import com.example.rxmvp.data.api.ListResponse;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.domain.common.Transaction;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;

@PerApplication public class SyncDatabase {

  public enum State {
    IDLE, SYNCING
  }

  private final Schedulers schedulers;
  private final Api api;
  private final VehicleRepository vehicleRepository;
  private final ErrorLogger errorLogger;
  private final BehaviorRelay<State> state;

  @Inject
  public SyncDatabase(Schedulers schedulers, Api api, VehicleRepository vehicleRepository,
      ErrorLogger errorLogger) {
    this.schedulers = schedulers;
    this.api = api;
    this.vehicleRepository = vehicleRepository;
    this.errorLogger = errorLogger;
    this.state = BehaviorRelay.create(State.IDLE);
  }

  public Observable<State> getState() {
    return state.compose(schedulers.applyOnMain());
  }

  public Observable<Void> sync() {
    return state.first()
        .compose(schedulers.applyOnBackground())
        .filter(state -> state.equals(State.IDLE))
        .doOnNext(__ -> state.call(State.SYNCING))
        .map(__ -> getAll())
        .map(Transaction::create)
        .flatMap(transaction -> {
          if (transaction.isError()) {
            errorLogger.log(transaction.error());
            return Observable.just(null);
          } else {
            return handleNewData(transaction);
          }
        })
        .doOnNext(__ -> state.call(State.IDLE))
        .compose(schedulers.applyOnMain());
  }

  @NonNull private Observable<Void> handleNewData(Transaction<ListResponse<Vehicle>> transaction) {
    return vehicleRepository.updateAll(transaction.body().getList()).map(__ -> (Void) null);
  }

  private Result<ListResponse<Vehicle>> getAll() {
    ListResponse<Vehicle> newVehicles;
    List<Vehicle> allVehicles = new ArrayList<>();

    int page = 0;
    do {
      Result<ListResponse<Vehicle>> result =
          api.getAllVehiclesWithTroubles(page).toBlocking().first();
      if (result.isError() || !result.response().isSuccessful()) {
        return result;
      }

      newVehicles = result.response().body();
      allVehicles.addAll(newVehicles.getList());
      page++;
    } while (newVehicles.getTotal() > page * newVehicles.getLimit());

    ListResponse<Vehicle> listResponse = new ListResponse<>();
    listResponse.setList(allVehicles);
    return Result.response(Response.success(listResponse));
  }
}
