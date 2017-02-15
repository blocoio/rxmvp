package com.example.rxmvp.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import com.example.rxmvp.AndroidApplication;
import com.example.rxmvp.R;
import com.example.rxmvp.common.Preconditions;
import com.example.rxmvp.common.di.ActivityComponent;
import com.example.rxmvp.common.di.ActivityModule;
import com.example.rxmvp.common.di.ApplicationComponent;

public abstract class BaseActivity extends AppCompatActivity {

  protected Toolbar toolbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutRes());
    ButterKnife.bind(this);
    setupToolbar();
  }

  protected ActivityComponent getActivityComponent() {
    ApplicationComponent applicationComponent =
        ((AndroidApplication) getApplication()).getApplicationComponent();
    return applicationComponent.plus(new ActivityModule(this));
  }

  @LayoutRes protected abstract int getLayoutRes();

  protected void enableToolbarBack() {
    ActionBar actionBar = getSupportActionBar();
    Preconditions.checkNotNull(actionBar, "Toolbar not defined");
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());
  }

  // Private

  private void setupToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }
  }
}
