package com.example.rxmvp.data;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.data.models.User;
import com.example.rxmvp.testing.factories.UserFactory;

import static com.google.common.truth.Truth.assertThat;
import static com.example.rxmvp.helpers.ApplicationHelper.getApplicationComponent;

@RunWith(AndroidJUnit4.class)
public class UserRepositoryTest {

  private UserRepository userRepository;
  private UserFactory userFactory;

  @Before public void setUp() throws Exception {
    userRepository = getApplicationComponent().sessionRepository();
    userFactory = new UserFactory();
  }

  @Test
  public void setAndGet() throws Exception {
    User user = userFactory.build();

    userRepository.setUser().call(user);
    User retrievedUser = userRepository.getUser().toBlocking().first();

    assertThat(retrievedUser.getUsername()).isEqualTo(user.getUsername());
  }
}
