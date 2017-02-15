package com.example.rxmvp.data.models;

import android.support.test.runner.AndroidJUnit4;
import com.squareup.sqlbrite.BriteDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.helpers.TestStateManager;
import com.example.rxmvp.testing.factories.TroubleFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

@RunWith(AndroidJUnit4.class) public class TroubleTest {

  private TroubleFactory troubleFactory;
  private TestStateManager testStateManager;
  private BriteDatabase briteDatabase;

  @Before public void setUp() throws Exception {
    troubleFactory = new TroubleFactory();
    testStateManager = new TestStateManager();
    briteDatabase = getApplicationComponent().briteDatabase();
  }

  @After public void tearDown() throws Exception {
    testStateManager.clean();
  }

  @Test public void fields() throws Exception {
    Trouble trouble = troubleFactory.build();

    saveTrouble(trouble);
    Trouble retrievedTrouble = getFirstTrouble();

    assertThat(retrievedTrouble.id(), is(equalTo(trouble.id())));
    assertThat(retrievedTrouble.code(), is(equalTo(trouble.code())));
    assertThat(retrievedTrouble.description(), is(equalTo(trouble.description())));
    assertThat(retrievedTrouble.startDate(), is(equalTo(trouble.startDate())));
    assertThat(retrievedTrouble.read(), is(equalTo(trouble.read())));
  }

  private void saveTrouble(Trouble trouble) {
    TroubleModel.InsertQuery insert =
        new TroubleModel.InsertQuery(briteDatabase.getWritableDatabase(), Trouble.FACTORY);
    insert.bind(trouble.id(), trouble.code(), trouble.description(), trouble.startDate(),
        trouble.read(), trouble.vehicleId());
    briteDatabase.executeInsert(insert.table, insert.program);
  }

  private Trouble getFirstTrouble() {
    return briteDatabase.createQuery(Trouble.TABLE_NAME, "SELECT * FROM trouble")
        .mapToList(new TroubleModel.Mapper<>(Trouble.FACTORY)::map)
        .toBlocking()
        .first()
        .get(0);
  }
}
