package com.example.rxmvp.data;

import android.support.test.runner.AndroidJUnit4;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.helpers.TestStateManager;
import com.example.rxmvp.testing.factories.TroubleFactory;

import static com.google.common.truth.Truth.assertThat;
import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

@RunWith(AndroidJUnit4.class) public class TroubleRepositoryTest {

  private TroubleFactory troubleFactory;
  private BriteDatabase db;
  private TroubleRepository troubleRepository;

  @Before public void setUp() throws Exception {
    troubleFactory = new TroubleFactory();
    db = getApplicationComponent().briteDatabase();
    troubleRepository = getApplicationComponent().troubleRepository();
  }

  @After public void tearDown() throws Exception {
    new TestStateManager().clean();
  }

  @Test public void getAllWithVehicle() throws Exception {
    List<Trouble> troubles = troubleFactory.createList(db);

    List<TroubleWithVehicle> troubleWithVehicles =
        troubleRepository.getAllWithVehicle().toBlocking().first();

    assertThat(troubleWithVehicles).hasSize(troubles.size());
  }

  @Test public void readAll() throws Exception {
    Trouble trouble = troubleFactory.create(db);
    assertThat(trouble.read()).isFalse();

    troubleRepository.readAll().toBlocking().first();

    Trouble retrievedTrouble = getTroubleFromDatabase(trouble.id());
    assertThat(retrievedTrouble.read()).isTrue();
  }

  @Test public void countUnread() throws Exception {
    Trouble troubleRead = troubleFactory.build();
    troubleRead.setRead(true);
    troubleFactory.create(db, troubleRead);

    Trouble troubleUnread = troubleFactory.build();
    troubleUnread.setRead(false);
    troubleFactory.create(db, troubleUnread);

    Long count = troubleRepository.countUnread().toBlocking().first();

    assertThat(count).isEqualTo(1);
  }

  private Trouble getTroubleFromDatabase(long troubleId) {
    return db.createQuery("trouble", "SELECT * FROM trouble WHERE id = ?",
        String.valueOf(troubleId))
        .mapToOne(Trouble.MAPPER::map)
        .toBlocking()
        .first();
  }
}
