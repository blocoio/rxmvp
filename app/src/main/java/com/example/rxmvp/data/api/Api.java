package com.example.rxmvp.data.api;

import com.example.rxmvp.data.models.Vehicle;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {
  @GET("vehicles")
  Observable<Result<ListResponse<Vehicle>>> getAllVehiclesWithTroubles(
      @Query("page") int page);
}
