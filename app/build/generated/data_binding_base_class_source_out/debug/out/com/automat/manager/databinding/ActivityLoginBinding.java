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
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.automat.manager.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton btLogIn;

  @NonNull
  public final Button btSetting;

  @NonNull
  public final TextInputEditText etName;

  @NonNull
  public final TextInputLayout etNameLayout;

  @NonNull
  public final TextInputEditText etPassword;

  @NonNull
  public final TextInputLayout etPasswordLayout;

  @NonNull
  public final ConstraintLayout loginConstraintLayout;

  @NonNull
  public final ProgressBar pbLogin;

  @NonNull
  public final MaterialCheckBox rememberCheckBox;

  @NonNull
  public final TextView tvDescription;

  @NonNull
  public final TextView tvWelcome;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull MaterialButton btLogIn,
      @NonNull Button btSetting, @NonNull TextInputEditText etName,
      @NonNull TextInputLayout etNameLayout, @NonNull TextInputEditText etPassword,
      @NonNull TextInputLayout etPasswordLayout, @NonNull ConstraintLayout loginConstraintLayout,
      @NonNull ProgressBar pbLogin, @NonNull MaterialCheckBox rememberCheckBox,
      @NonNull TextView tvDescription, @NonNull TextView tvWelcome) {
    this.rootView = rootView;
    this.btLogIn = btLogIn;
    this.btSetting = btSetting;
    this.etName = etName;
    this.etNameLayout = etNameLayout;
    this.etPassword = etPassword;
    this.etPasswordLayout = etPasswordLayout;
    this.loginConstraintLayout = loginConstraintLayout;
    this.pbLogin = pbLogin;
    this.rememberCheckBox = rememberCheckBox;
    this.tvDescription = tvDescription;
    this.tvWelcome = tvWelcome;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bt_logIn;
      MaterialButton btLogIn = ViewBindings.findChildViewById(rootView, id);
      if (btLogIn == null) {
        break missingId;
      }

      id = R.id.bt_setting;
      Button btSetting = ViewBindings.findChildViewById(rootView, id);
      if (btSetting == null) {
        break missingId;
      }

      id = R.id.et_name;
      TextInputEditText etName = ViewBindings.findChildViewById(rootView, id);
      if (etName == null) {
        break missingId;
      }

      id = R.id.et_name_layout;
      TextInputLayout etNameLayout = ViewBindings.findChildViewById(rootView, id);
      if (etNameLayout == null) {
        break missingId;
      }

      id = R.id.et_password;
      TextInputEditText etPassword = ViewBindings.findChildViewById(rootView, id);
      if (etPassword == null) {
        break missingId;
      }

      id = R.id.et_password_layout;
      TextInputLayout etPasswordLayout = ViewBindings.findChildViewById(rootView, id);
      if (etPasswordLayout == null) {
        break missingId;
      }

      ConstraintLayout loginConstraintLayout = (ConstraintLayout) rootView;

      id = R.id.pb_login;
      ProgressBar pbLogin = ViewBindings.findChildViewById(rootView, id);
      if (pbLogin == null) {
        break missingId;
      }

      id = R.id.remember_check_Box;
      MaterialCheckBox rememberCheckBox = ViewBindings.findChildViewById(rootView, id);
      if (rememberCheckBox == null) {
        break missingId;
      }

      id = R.id.tv_description;
      TextView tvDescription = ViewBindings.findChildViewById(rootView, id);
      if (tvDescription == null) {
        break missingId;
      }

      id = R.id.tv_welcome;
      TextView tvWelcome = ViewBindings.findChildViewById(rootView, id);
      if (tvWelcome == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, btLogIn, btSetting, etName,
          etNameLayout, etPassword, etPasswordLayout, loginConstraintLayout, pbLogin,
          rememberCheckBox, tvDescription, tvWelcome);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
