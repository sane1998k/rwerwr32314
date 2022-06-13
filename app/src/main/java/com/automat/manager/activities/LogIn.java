package com.automat.manager.activities;

import static com.automat.manager.global.GlobalValue.*;
import static com.automat.manager.services.MyFCMService.foo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.automat.manager.R;
import com.automat.manager.activities.collector.Collector;
import com.automat.manager.activities.creater.Creater;
import com.automat.manager.activities.driver.Driver;
import com.automat.manager.activities.driver.Driver1;
import com.automat.manager.activities.logistician.Logistician;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.CheckerField;
import com.automat.manager.classes.MainRequest;
import com.automat.manager.interfaces.IRetrofitNotArray;
import com.automat.manager.requests.LoginRequest;
import com.automat.manager.responses.GetUserResponse;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {
    Dictionary<String, Class<?>> dictionaryClasses;

    SharedPreferences sharedPreferences, settingSharedPreferences;
    SharedPreferences.Editor editor, settingEditor;
    CheckerField checkerField = new CheckerField();
    MainRequest mainLoginRequest, mainGetUserInfoResponse;

    TextInputLayout etNameLayout, etPasswordLayout;
    ConstraintLayout loginConstraintLayout;
    EditText etName, etPassword;
    CheckBox rememberCheckBox;
    Button btLogIn, bt_setting;
    ProgressBar pbLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
        setLayoutAlpha();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isSetting = settingSharedPreferences.getString(KEY_IP, "").equals("");
        if (isSetting) {
            settingEditor.putString(KEY_IP, getString(R.string.ip_address_text));
            settingEditor.apply();
        }
        ApiClient.IP_ADDRESS = settingSharedPreferences.getString(KEY_IP, "");
    }

    private void init() {
        foo("All");
        dictionaryClasses = new Hashtable<>();
        settingSharedPreferences = getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        settingEditor = settingSharedPreferences.edit();
        dictionaryClasses.put("Driver", Driver1.class);
        dictionaryClasses.put("OrderCreator", Creater.class);
        dictionaryClasses.put("Logistician", Logistician.class);
        dictionaryClasses.put("Collector", Collector.class);
        boolean isSetting = settingSharedPreferences.getString(KEY_IP, "").equals("");
        if (isSetting) {
            settingEditor.putString(KEY_IP, getString(R.string.ip_address_text));
            settingEditor.apply();
        }
        ApiClient.IP_ADDRESS = settingSharedPreferences.getString(KEY_IP, "");
        boolean isLogin = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (isLogin && !sharedPreferences.getString(KEY_ROLE, "").equals("")) {
            startActivity(dictionaryClasses.get(sharedPreferences.getString(KEY_ROLE, "")));
        }
        etName = findViewById(R.id.et_name);
        etPassword = findViewById(R.id.et_password);
        btLogIn = findViewById(R.id.bt_logIn);
        bt_setting = findViewById(R.id.bt_setting);
        pbLogIn = findViewById(R.id.pb_login);
        etNameLayout = findViewById(R.id.et_name_layout);
        etPasswordLayout = findViewById(R.id.et_password_layout);
        loginConstraintLayout = findViewById(R.id.login_constraint_layout);
        rememberCheckBox = findViewById(R.id.remember_check_Box);
        // LOAD Data from Shared
        getShared();
        // TODO functional
        btLogIn.setOnClickListener(view -> login());
        bt_setting.setOnClickListener(view -> showDialogSetting());
        etName.setOnFocusChangeListener(checkerField.myOnFocusChanger(etNameLayout));
        etName.addTextChangedListener(myDddTextChangedListener());
        etPassword.setOnFocusChangeListener(checkerField.myOnFocusChanger(etPasswordLayout));
        etPassword.addTextChangedListener(myDddTextChangedListener());
        rememberCheckBox.setOnCheckedChangeListener(onCheckedChangeListener());
    }
    private void login() {
        if (checkerField.isFields(new EditText[] {etName, etPassword})) return;
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(name);
        loginRequest.setPassword(password);
        Call<String> loginResponseCall = ApiClient
                .getUserService()
                .userLogin(loginRequest);
        pbLogIn.setVisibility(View.VISIBLE);
        btLogIn.setEnabled(false);
        mainLoginRequest = new MainRequest(LogIn.this, loginResponseCall, new IRetrofitNotArray() {
            @Override
            public <T> void success(Response<T> response) {
                String token = "Bearer " + response.body();
                editor.putString(KEY_AUTH, token);
                editor.apply();
                Call<GetUserResponse> getCurrentUser = ApiClient
                                                .getUserService()
                                                .getUserInfo(token);
                mainGetUserInfoResponse = new MainRequest(LogIn.this, getCurrentUser, new IRetrofitNotArray() {
                    @Override
                    public <T> void success(Response<T> response) {
                        assert response.body() != null;
                        String role = ((GetUserResponse) response.body()).getRole();
                        String name = ((GetUserResponse) response.body()).getFullFIO();
                        editor.putString(KEY_ROLE, role);
                        editor.putString(NAME_USER, name);
                        editor.apply();
                        switch (role) {
                            case "Collector":
                                startActivity(Collector.class);
                                break;
                            case "Driver":
                                startActivity(Driver1.class);
                                break;
                            case "OrderCreator":
                                startActivity(Creater.class);
                                break;
                            case "Logistician":
                                startActivity(Logistician.class);
                                break;
                        }
                    }

                    @Override
                    public <T> void bad(Response<T> response) { }
                    public <T> void failed(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {}
                }, pbLogIn, btLogIn);
                mainGetUserInfoResponse.sendRequest();
            }

            @Override
            public <T> void bad(Response<T> response) {
                pbLogIn.setVisibility(View.GONE);
                btLogIn.setEnabled(true);
            }
            public <T> void failed(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {
                pbLogIn.setVisibility(View.GONE);
                btLogIn.setEnabled(true);
            }
        });
        mainLoginRequest.sendRequest();
    }

    private void showDialogSetting() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);
        Button btSaveSetting = dialog.findViewById(R.id.bt_save_setting);
        EditText etSetting = dialog.findViewById(R.id.et_setting);
        TextInputLayout etSettingLayout = dialog.findViewById(R.id.et_setting_layout);
        etSetting.setText(settingSharedPreferences.getString(KEY_IP, "").replace("https://", ""));
        etSetting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        btSaveSetting.setOnClickListener(view -> {
            if (checkerField.isFields(new EditText[] {etSetting})) return;
            String ip = etSetting.getText().toString();
            ip = ip.replace("https://", "");
            String prefix = Objects.requireNonNull(etSettingLayout.getPrefixText()).toString();
            settingEditor.putString(KEY_IP, prefix + ip);
            settingEditor.apply();
            ApiClient.IP_ADDRESS = prefix + ip;
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public TextWatcher myDddTextChangedListener() {
        boolean isChecked = rememberCheckBox.isChecked();
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isChecked) addShared();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isChecked) addShared();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isChecked) addShared();
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener() {
        return (compoundButton, b) -> {
            if (b) addShared();
            else deleteShared();
        };
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(LogIn.this, cls);
        startActivity(intent);
        finish();
    }

    public void setLayoutAlpha() {
        ViewCompat.animate(loginConstraintLayout).withStartAction(() -> loginConstraintLayout.setVisibility(View.VISIBLE))
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .start();
    }

    public void getShared() {
        etName.setText(sharedPreferences.getString(KEY_NAME, ""));
        etPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
        rememberCheckBox.setChecked(sharedPreferences.getBoolean(KEY_REMEMBER, false));
    }

    public void addShared() {
        editor.putString(KEY_NAME, etName.getText().toString());
        editor.putString(KEY_PASSWORD, etPassword.getText().toString());
        editor.putBoolean(KEY_REMEMBER, true);
        editor.putString(KEY_ROLE, "");
        editor.apply();
    }

    public void deleteShared() {
        editor.putBoolean(KEY_REMEMBER, false);
        editor.remove(KEY_NAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }
}