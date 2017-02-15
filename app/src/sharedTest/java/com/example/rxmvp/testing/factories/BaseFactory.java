package com.example.rxmvp.testing.factories;

import com.squareup.sqlbrite.BriteDatabase;
import io.bloco.faker.Faker;
import java.util.List;
import rx.Observable;

public abstract class BaseFactory<T> {

  public static final int DEFAULT_LIST_SIZE = 3;

  protected Faker faker;

  public BaseFactory() {
    faker = new Faker();
  }

  public abstract T build();

  public List<T> buildList() {
    return buildList(DEFAULT_LIST_SIZE);
  }

  public List<T> buildList(int size) {
    return Observable.range(1, size).map(__ -> build()).toList().toBlocking().first();
  }

  // Optional to implement
  public T create(BriteDatabase db) {
    throw new UnsupportedOperationException();
  }

  public List<T> createList(BriteDatabase db) {
    return createList(db, DEFAULT_LIST_SIZE);
  }

  public List<T> createList(BriteDatabase db, int size) {
    return Observable.range(1, size).map(__ -> create(db)).toList().toBlocking().first();
  }
}
