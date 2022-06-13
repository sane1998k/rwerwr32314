// Generated by view binder compiler. Do not edit!
package com.automat.manager.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.automat.manager.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class RecyclerItemQrBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView txtDescription;

  @NonNull
  public final TextView txtOptionDigit;

  @NonNull
  public final TextView txtTitle;

  private RecyclerItemQrBinding(@NonNull LinearLayout rootView, @NonNull TextView txtDescription,
      @NonNull TextView txtOptionDigit, @NonNull TextView txtTitle) {
    this.rootView = rootView;
    this.txtDescription = txtDescription;
    this.txtOptionDigit = txtOptionDigit;
    this.txtTitle = txtTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static RecyclerItemQrBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecyclerItemQrBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recycler_item_qr, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecyclerItemQrBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.txtDescription;
      TextView txtDescription = ViewBindings.findChildViewById(rootView, id);
      if (txtDescription == null) {
        break missingId;
      }

      id = R.id.txtOptionDigit;
      TextView txtOptionDigit = ViewBindings.findChildViewById(rootView, id);
      if (txtOptionDigit == null) {
        break missingId;
      }

      id = R.id.txtTitle;
      TextView txtTitle = ViewBindings.findChildViewById(rootView, id);
      if (txtTitle == null) {
        break missingId;
      }

      return new RecyclerItemQrBinding((LinearLayout) rootView, txtDescription, txtOptionDigit,
          txtTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}