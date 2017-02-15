package com.example.rxmvp.data.api;

import java.io.IOException;
import javax.inject.Inject;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.models.User;
import com.example.rxmvp.domain.GetAuthenticatedUser;

@PerApplication public class AuthenticationInterceptor implements Interceptor {

  private final GetAuthenticatedUser getAuthenticatedUser;

  @Inject public AuthenticationInterceptor(GetAuthenticatedUser getAuthenticatedUser) {
    this.getAuthenticatedUser = getAuthenticatedUser;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    User user = getUser();
    if (user != null) {
      request = request.newBuilder().header("Authorization", getAuthToken(user)).build();
    }

    return chain.proceed(request);
  }

  private User getUser() {
    return getAuthenticatedUser.get().toBlocking().first();
  }

  private String getAuthToken(User user) {
    return Credentials.basic(user.getUsername(), user.getPassword());
  }
}
