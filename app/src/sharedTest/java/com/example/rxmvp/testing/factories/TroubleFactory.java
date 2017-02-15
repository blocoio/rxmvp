package com.example.rxmvp.testing.factories;

import com.squareup.sqlbrite.BriteDatabase;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.TroubleModel;
import com.example.rxmvp.data.models.Vehicle;

public class TroubleFactory extends BaseFactory<Trouble> {

  private final VehicleFactory vehicleFactory;

  public TroubleFactory() {
    vehicleFactory = new VehicleFactory();
  }

  @Override public Trouble build() {
    Trouble trouble = new Trouble();
    trouble.setId(faker.number.positive());
    trouble.setCode(faker.lorem.character() + faker.number.number(5));
    trouble.setDescription(faker.lorem.sentence());
    trouble.setStartDate(faker.date.backward());
    trouble.setRead(false);
    trouble.setVehicleId((long) faker.number.positive());
    return trouble;
  }

  public Trouble create(BriteDatabase db) {
    return create(db, build());
  }

  public Trouble create(BriteDatabase db, Trouble trouble) {
    return create(db, trouble, vehicleFactory.create(db));
  }

  public Trouble create(BriteDatabase db, Trouble trouble, Vehicle vehicle) {
    trouble.setVehicleId(vehicle.id());

    TroubleModel.InsertQuery insertQuery =
        new TroubleModel.InsertQuery(db.getWritableDatabase(), Trouble.FACTORY);
    insertQuery.bind(trouble.id(), trouble.code(), trouble.description(), trouble.startDate(),
        trouble.read(), trouble.vehicleId());
    db.executeInsert(insertQuery.table, insertQuery.program);

    return trouble;
  }
}
