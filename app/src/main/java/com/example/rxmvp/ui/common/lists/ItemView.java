package com.example.rxmvp.ui.common.lists;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.rxmvp.ui.BaseView;

public abstract class ItemView<T> extends BaseView {

  public ItemView(Context context) {
    super(context);
  }

  public ItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public abstract void setItem(T item);

  public void setSectionTitle(@Nullable String sectionTitle) {
  }

  public View getClickTarget() {
    return this;
  }
}
