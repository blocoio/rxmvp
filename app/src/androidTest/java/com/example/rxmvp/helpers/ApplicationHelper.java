package com.example.rxmvp.helpers;

import android.support.test.InstrumentationRegistry;
import com.example.rxmvp.AndroidApplication;
import com.example.rxmvp.common.di.ApplicationComponent;

public class ApplicationHelper {
  public static ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) InstrumentationRegistry.getTargetContext()
        .getApplicationContext()).getApplicationComponent();
  }
}
