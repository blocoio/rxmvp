package com.example.rxmvp.common.di;

import android.content.res.Resources;
import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import com.example.rxmvp.AndroidApplication;
import com.example.rxmvp.BuildConfig;
import com.example.rxmvp.R;
import com.example.rxmvp.data.api.Api;
import com.example.rxmvp.data.api.AuthenticationInterceptor;
import com.example.rxmvp.data.api.MockApiInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class ApiModule {

  @Provides @PerApplication public OkHttpClient provideOkHttpClient(AndroidApplication.Mode appMode,
      MockApiInterceptor mockApiInterceptor, AuthenticationInterceptor authenticationInterceptor) {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    if (BuildConfig.DEBUG) {
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    int timeout = 60;
    OkHttpClient.Builder builder =
        new OkHttpClient.Builder().addInterceptor(authenticationInterceptor)
            .addInterceptor(loggingInterceptor);

    //if (appMode == AndroidApplication.Mode.TEST) {
    builder.addInterceptor(mockApiInterceptor);
    //}

    return builder.connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build();
  }

  @Provides @PerApplication
  public Retrofit retrofitApp(OkHttpClient client, Gson gson, Resources resources) {
    return new Retrofit.Builder().client(client)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(resources.getString(R.string.url_api))
        .build();
  }

  @Provides @PerApplication public Api api(Retrofit retrofit) {
    return retrofit.create(Api.class);
  }
}