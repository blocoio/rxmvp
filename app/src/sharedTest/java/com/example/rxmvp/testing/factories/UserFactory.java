package com.example.rxmvp.testing.factories;

import com.example.rxmvp.data.models.User;

public class UserFactory extends BaseFactory<User> {

  @Override public User build() {
    User user = new User();
    user.setId((long) faker.number.positive());
    user.setUsername(faker.internet.userName());
    user.setPassword(faker.internet.password());
    return user;
  }
}
