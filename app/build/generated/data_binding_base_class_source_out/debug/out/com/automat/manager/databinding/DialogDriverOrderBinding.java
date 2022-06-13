// Generated by view binder compiler. Do not edit!
package com.automat.manager.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.automat.manager.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogDriverOrderBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btReloadDriverOrder;

  @NonNull
  public final SearchView etSearchDriverOrder;

  @NonNull
  public final ProgressBar pbDriverOrder;

  @NonNull
  public final RecyclerView rvDriverOrder;

  private DialogDriverOrderBinding(@NonNull LinearLayout rootView,
      @NonNull Button btReloadDriverOrder, @NonNull SearchView etSearchDriverOrder,
      @NonNull ProgressBar pbDriverOrder, @NonNull RecyclerView rvDriverOrder) {
    this.rootView = rootView;
    this.btReloadDriverOrder = btReloadDriverOrder;
    this.etSearchDriverOrder = etSearchDriverOrder;
    this.pbDriverOrder = pbDriverOrder;
    this.rvDriverOrder = rvDriverOrder;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogDriverOrderBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogDriverOrderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_driver_order, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogDriverOrderBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bt_reload_driver_order;
      Button btReloadDriverOrder = ViewBindings.findChildViewById(rootView, id);
      if (btReloadDriverOrder == null) {
        break missingId;
      }

      id = R.id.et_search_driver_order;
      SearchView etSearchDriverOrder = ViewBindings.findChildViewById(rootView, id);
      if (etSearchDriverOrder == null) {
        break missingId;
      }

      id = R.id.pb_driver_order;
      ProgressBar pbDriverOrder = ViewBindings.findChildViewById(rootView, id);
      if (pbDriverOrder == null) {
        break missingId;
      }

      id = R.id.rv_driver_order;
      RecyclerView rvDriverOrder = ViewBindings.findChildViewById(rootView, id);
      if (rvDriverOrder == null) {
        break missingId;
      }

      return new DialogDriverOrderBinding((LinearLayout) rootView, btReloadDriverOrder,
          etSearchDriverOrder, pbDriverOrder, rvDriverOrder);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}