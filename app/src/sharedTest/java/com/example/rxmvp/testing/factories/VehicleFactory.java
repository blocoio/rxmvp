package com.example.rxmvp.testing.factories;

import com.squareup.sqlbrite.BriteDatabase;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.data.models.VehicleModel;

public class VehicleFactory extends BaseFactory<Vehicle> {

  @Override public Vehicle build() {
    Vehicle vehicle = new Vehicle();
    vehicle.setId(faker.number.positive());
    vehicle.setDescription(faker.commerce.productName());
    return vehicle;
  }

  public Vehicle create(BriteDatabase db) {
    return create(db, build());
  }

  public Vehicle create(BriteDatabase db, Vehicle vehicle) {
    VehicleModel.InsertQuery insertVehicle =
        new Vehicle.InsertQuery(db.getWritableDatabase(), Vehicle.FACTORY);
    insertVehicle.bind(vehicle.id(), vehicle.description(), vehicle.status());
    db.executeInsert(insertVehicle.table, insertVehicle.program);
    return vehicle;
  }
}
