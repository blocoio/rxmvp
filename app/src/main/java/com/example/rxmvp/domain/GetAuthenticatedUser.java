package com.example.rxmvp.domain;

import javax.inject.Inject;
import com.example.rxmvp.common.Schedulers;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.UserRepository;
import com.example.rxmvp.data.models.User;
import rx.Observable;

@PerApplication public class GetAuthenticatedUser {

  private final Schedulers schedulers;
  private final UserRepository userRepository;

  @Inject public GetAuthenticatedUser(Schedulers schedulers, UserRepository userRepository) {
    this.schedulers = schedulers;
    this.userRepository = userRepository;
  }

  public Observable<User> get() {
    return userRepository.getUser().compose(schedulers.applyOnMain());
  }
}
