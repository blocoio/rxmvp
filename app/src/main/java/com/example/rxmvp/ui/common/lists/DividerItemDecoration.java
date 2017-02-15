package com.example.rxmvp.ui.common.lists;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.rxmvp.R;
import com.example.rxmvp.common.Preconditions;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

  private Drawable mDivider;
  private int mDividerHeight;

  public DividerItemDecoration(Context context) {
    this(context, R.drawable.list_divider);
  }

  public DividerItemDecoration(Context context, @DrawableRes int dividerRes) {
    mDivider = ResourcesCompat.getDrawable(context.getResources(), dividerRes, context.getTheme());
    Preconditions.checkNotNull(mDivider, "Divider drawable does not exist");
    mDividerHeight = mDivider.getIntrinsicHeight();
  }

  @Override public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + mDividerHeight;

      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
      outRect.bottom = mDividerHeight;
    }
  }
}
