// Generated by view binder compiler. Do not edit!
package com.automat.manager.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.automat.manager.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLogisticianBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btLogisticianExit;

  @NonNull
  public final Button btLogisticianReloading;

  @NonNull
  public final Button btLogisticianSend;

  @NonNull
  public final Button btReloadRv;

  @NonNull
  public final ProgressBar pbLogisticianLoading;

  @NonNull
  public final RecyclerView rvLogisticianOrder;

  @NonNull
  public final TextView tvLogisticianCarName;

  @NonNull
  public final TextView tvLogisticianDriveName;

  private ActivityLogisticianBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btLogisticianExit, @NonNull Button btLogisticianReloading,
      @NonNull Button btLogisticianSend, @NonNull Button btReloadRv,
      @NonNull ProgressBar pbLogisticianLoading, @NonNull RecyclerView rvLogisticianOrder,
      @NonNull TextView tvLogisticianCarName, @NonNull TextView tvLogisticianDriveName) {
    this.rootView = rootView;
    this.btLogisticianExit = btLogisticianExit;
    this.btLogisticianReloading = btLogisticianReloading;
    this.btLogisticianSend = btLogisticianSend;
    this.btReloadRv = btReloadRv;
    this.pbLogisticianLoading = pbLogisticianLoading;
    this.rvLogisticianOrder = rvLogisticianOrder;
    this.tvLogisticianCarName = tvLogisticianCarName;
    this.tvLogisticianDriveName = tvLogisticianDriveName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLogisticianBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLogisticianBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_logistician, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLogisticianBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bt_logistician_exit;
      Button btLogisticianExit = ViewBindings.findChildViewById(rootView, id);
      if (btLogisticianExit == null) {
        break missingId;
      }

      id = R.id.bt_logistician_reloading;
      Button btLogisticianReloading = ViewBindings.findChildViewById(rootView, id);
      if (btLogisticianReloading == null) {
        break missingId;
      }

      id = R.id.bt_logistician_send;
      Button btLogisticianSend = ViewBindings.findChildViewById(rootView, id);
      if (btLogisticianSend == null) {
        break missingId;
      }

      id = R.id.bt_reload_rv;
      Button btReloadRv = ViewBindings.findChildViewById(rootView, id);
      if (btReloadRv == null) {
        break missingId;
      }

      id = R.id.pb_logistician_loading;
      ProgressBar pbLogisticianLoading = ViewBindings.findChildViewById(rootView, id);
      if (pbLogisticianLoading == null) {
        break missingId;
      }

      id = R.id.rv_logistician_order;
      RecyclerView rvLogisticianOrder = ViewBindings.findChildViewById(rootView, id);
      if (rvLogisticianOrder == null) {
        break missingId;
      }

      id = R.id.tv_logistician_car_name;
      TextView tvLogisticianCarName = ViewBindings.findChildViewById(rootView, id);
      if (tvLogisticianCarName == null) {
        break missingId;
      }

      id = R.id.tv_logistician_drive_name;
      TextView tvLogisticianDriveName = ViewBindings.findChildViewById(rootView, id);
      if (tvLogisticianDriveName == null) {
        break missingId;
      }

      return new ActivityLogisticianBinding((ConstraintLayout) rootView, btLogisticianExit,
          btLogisticianReloading, btLogisticianSend, btReloadRv, pbLogisticianLoading,
          rvLogisticianOrder, tvLogisticianCarName, tvLogisticianDriveName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
