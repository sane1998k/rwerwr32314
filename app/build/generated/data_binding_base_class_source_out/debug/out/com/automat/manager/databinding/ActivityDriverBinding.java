// Generated by view binder compiler. Do not edit!
package com.automat.manager.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.automat.manager.R;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDriverBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btEnableScanDriver;

  @NonNull
  public final Button btExit;

  @NonNull
  public final ConstraintLayout container;

  @NonNull
  public final Guideline guideline;

  @NonNull
  public final BottomNavigationView navView;

  @NonNull
  public final ProgressBar pbLogged;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final TextView resultText;

  @NonNull
  public final RecyclerView rvStatusQrItems;

  @NonNull
  public final CodeScannerView scannerOrder;

  @NonNull
  public final Button sendOrderButton;

  @NonNull
  public final TextView tvDriverOrder;

  @NonNull
  public final LinearLayout xz;

  private ActivityDriverBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btEnableScanDriver, @NonNull Button btExit,
      @NonNull ConstraintLayout container, @NonNull Guideline guideline,
      @NonNull BottomNavigationView navView, @NonNull ProgressBar pbLogged,
      @NonNull RecyclerView recyclerView, @NonNull TextView resultText,
      @NonNull RecyclerView rvStatusQrItems, @NonNull CodeScannerView scannerOrder,
      @NonNull Button sendOrderButton, @NonNull TextView tvDriverOrder, @NonNull LinearLayout xz) {
    this.rootView = rootView;
    this.btEnableScanDriver = btEnableScanDriver;
    this.btExit = btExit;
    this.container = container;
    this.guideline = guideline;
    this.navView = navView;
    this.pbLogged = pbLogged;
    this.recyclerView = recyclerView;
    this.resultText = resultText;
    this.rvStatusQrItems = rvStatusQrItems;
    this.scannerOrder = scannerOrder;
    this.sendOrderButton = sendOrderButton;
    this.tvDriverOrder = tvDriverOrder;
    this.xz = xz;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDriverBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDriverBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_driver, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDriverBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bt_enable_scan_driver;
      Button btEnableScanDriver = ViewBindings.findChildViewById(rootView, id);
      if (btEnableScanDriver == null) {
        break missingId;
      }

      id = R.id.bt_exit;
      Button btExit = ViewBindings.findChildViewById(rootView, id);
      if (btExit == null) {
        break missingId;
      }

      ConstraintLayout container = (ConstraintLayout) rootView;

      id = R.id.guideline;
      Guideline guideline = ViewBindings.findChildViewById(rootView, id);
      if (guideline == null) {
        break missingId;
      }

      id = R.id.nav_view;
      BottomNavigationView navView = ViewBindings.findChildViewById(rootView, id);
      if (navView == null) {
        break missingId;
      }

      id = R.id.pb_logged;
      ProgressBar pbLogged = ViewBindings.findChildViewById(rootView, id);
      if (pbLogged == null) {
        break missingId;
      }

      id = R.id.recyclerView;
      RecyclerView recyclerView = ViewBindings.findChildViewById(rootView, id);
      if (recyclerView == null) {
        break missingId;
      }

      id = R.id.result_text;
      TextView resultText = ViewBindings.findChildViewById(rootView, id);
      if (resultText == null) {
        break missingId;
      }

      id = R.id.rv_status_qr_items;
      RecyclerView rvStatusQrItems = ViewBindings.findChildViewById(rootView, id);
      if (rvStatusQrItems == null) {
        break missingId;
      }

      id = R.id.scanner_order;
      CodeScannerView scannerOrder = ViewBindings.findChildViewById(rootView, id);
      if (scannerOrder == null) {
        break missingId;
      }

      id = R.id.send_order_button;
      Button sendOrderButton = ViewBindings.findChildViewById(rootView, id);
      if (sendOrderButton == null) {
        break missingId;
      }

      id = R.id.tv_driver_order;
      TextView tvDriverOrder = ViewBindings.findChildViewById(rootView, id);
      if (tvDriverOrder == null) {
        break missingId;
      }

      id = R.id.xz;
      LinearLayout xz = ViewBindings.findChildViewById(rootView, id);
      if (xz == null) {
        break missingId;
      }

      return new ActivityDriverBinding((ConstraintLayout) rootView, btEnableScanDriver, btExit,
          container, guideline, navView, pbLogged, recyclerView, resultText, rvStatusQrItems,
          scannerOrder, sendOrderButton, tvDriverOrder, xz);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}