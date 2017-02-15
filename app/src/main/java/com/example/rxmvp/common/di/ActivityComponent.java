package com.example.rxmvp.common.di;

import dagger.Subcomponent;
import com.example.rxmvp.ui.main.MainActivity;

@PerActivity @Subcomponent(modules = ActivityModule.class) public interface ActivityComponent {
  ViewComponent plus(ViewModule viewModule);

  void inject(MainActivity activity);
}
