package com.example.rxmvp.ui.notifications;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.BindView;
import javax.inject.Inject;
import com.example.rxmvp.R;
import com.example.rxmvp.data.models.Trouble;
import com.example.rxmvp.data.models.TroubleWithVehicle;
import com.example.rxmvp.data.models.Vehicle;
import com.example.rxmvp.ui.common.DateTimeFormatter;
import com.example.rxmvp.ui.common.lists.ItemView;
import com.example.rxmvp.ui.common.views.VehicleStatusView;

public class NotificationItemView extends ItemView<TroubleWithVehicle> {

  @BindView(R.id.notification_item_code) TextView code;
  @BindView(R.id.notification_item_description) TextView description;
  @BindView(R.id.notification_item_vehicle_status) VehicleStatusView vehicleStatus;
  @BindView(R.id.notification_item_vehicle_description) TextView vehicleDescription;
  @BindView(R.id.notification_item_date) TextView date;

  @Inject DateTimeFormatter dateTimeFormatter;

  public NotificationItemView(Context context) {
    super(context);
    getViewComponent().inject(this);
  }

  @Override protected int getLayoutRes() {
    return R.layout.item_notification;
  }

  @Override public void setItem(TroubleWithVehicle item) {
    Trouble trouble = item.trouble();
    Vehicle vehicle = item.vehicle();

    code.setText(trouble.code());
    description.setVisibility(TextUtils.isEmpty(trouble.description()) ? GONE : VISIBLE);
    description.setText(trouble.description());
    vehicleDescription.setText(vehicle.description());
    date.setText(dateTimeFormatter.shortString(trouble.startDate()));

    if (trouble.read()) {
      code.setTypeface(Typeface.DEFAULT);
      description.setTypeface(Typeface.DEFAULT);
    } else {
      code.setTypeface(Typeface.DEFAULT_BOLD);
      description.setTypeface(Typeface.DEFAULT_BOLD);
    }
  }
}
