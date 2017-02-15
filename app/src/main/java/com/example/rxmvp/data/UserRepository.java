package com.example.rxmvp.data;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import javax.inject.Inject;
import com.example.rxmvp.common.di.PerApplication;
import com.example.rxmvp.data.models.User;
import rx.Observable;
import rx.functions.Action1;

@PerApplication public class UserRepository {

  private static final String USER_KEY = "user";

  private Preference<User> userPreference;
  private final Gson gson;

  @Inject public UserRepository(Gson gson, RxSharedPreferences rxSharedPreferences) {
    this.gson = gson;
    userPreference = rxSharedPreferences.getObject(USER_KEY, new UserAdapter());
  }

  public Observable<User> getUser() {
    return userPreference.asObservable();
  }

  public Action1<? super User> setUser() {
    return userPreference.asAction();
  }

  private class UserAdapter implements Preference.Adapter<User> {
    @Override public User get(@NonNull String key, @NonNull SharedPreferences preferences) {
      String userJson = preferences.getString(key, null);
      if (userJson == null) {
        return null;
      }
      return gson.fromJson(userJson, User.class);
    }

    @Override public void set(@NonNull String key, @NonNull User user,
        @NonNull SharedPreferences.Editor editor) {
      String userJson = gson.toJson(user);
      editor.putString(key, userJson);
    }
  }
}
