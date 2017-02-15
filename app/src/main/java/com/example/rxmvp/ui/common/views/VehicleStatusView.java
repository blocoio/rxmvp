package com.example.rxmvp.ui.common.views;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import butterknife.BindView;
import com.example.rxmvp.R;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.ui.BaseView;

public class VehicleStatusView extends BaseView {

  @BindView(R.id.vehicle_status_inside) AppCompatImageView statusInside;

  public VehicleStatusView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected int getLayoutRes() {
    return R.layout.view_vehicle_status;
  }

  public void bind(Vehicle.Status status) {
    @ColorRes int colorRes;
    if (status.equals(Vehicle.Status.ACTIVE)) {
      colorRes = R.color.statusActive;
    } else {
      colorRes = R.color.statusInactive;
    }
    statusInside.setSupportBackgroundTintList(
        ContextCompat.getColorStateList(getContext(), colorRes));
  }
}
