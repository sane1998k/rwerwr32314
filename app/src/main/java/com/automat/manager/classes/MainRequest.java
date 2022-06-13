package com.automat.manager.classes;


import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.IBodyRetrofit;
import com.automat.manager.interfaces.IRetrofitArray;
import com.automat.manager.interfaces.IRetrofitNotArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRequest {
    Context context;
    private retrofit2.Call call;
    private IRetrofitNotArray iRetrofitNotArray;
    private IRetrofitArray IRetrofitArray;
    private ProgressBar progressBar;
    private Button button;

    public MainRequest(Context context, retrofit2.Call call, IRetrofitNotArray iRetrofitNotArray, ProgressBar progressBar, Button button) {
        this.context = context;
        this.call = call;
        this.iRetrofitNotArray = iRetrofitNotArray;
        this.progressBar = progressBar;
        this.button = button;
    }

    public MainRequest(Context context, retrofit2.Call call, IRetrofitArray IRetrofitArray, ProgressBar progressBar, Button button) {
        this.context = context;
        this.call = call;
        this.IRetrofitArray = IRetrofitArray;
        this.progressBar = progressBar;
        this.button = button;
    }

    public MainRequest(Context context, retrofit2.Call call, IRetrofitNotArray iRetrofitNotArray, ProgressBar progressBar) {
        this.context = context;
        this.call = call;
        this.iRetrofitNotArray = iRetrofitNotArray;
        this.progressBar = progressBar;
    }

    public MainRequest(Context context, retrofit2.Call call, IRetrofitNotArray iRetrofitNotArray) {
        this.context = context;
        this.call = call;
        this.iRetrofitNotArray = iRetrofitNotArray;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public <T> void sendRequest() {
        boolean isNotNull = progressBar != null && button != null;
        if (isNotNull) startLoading(progressBar, button);
        else if (progressBar != null) startLoading(progressBar);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<T> call, @NonNull Response<T> response) {
                if (response.isSuccessful()) iRetrofitNotArray.success(response);
                else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(context, response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
                    }
                    iRetrofitNotArray.bad(response);
                }
                if (isNotNull) stopLoading(progressBar, button);
                else if (progressBar != null) stopLoading(progressBar);
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {
                if (isNotNull) stopLoading(progressBar, button);
                else if (progressBar != null) stopLoading(progressBar);
                iRetrofitNotArray.failed(call, t);
                Toast.makeText(context, "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public <T> void sendRequestWithArray() {
        boolean isNotNull = progressBar != null && button != null;
        if (isNotNull) startLoading(progressBar, button);
        else if (progressBar != null) startLoading(progressBar);
        call.enqueue(new Callback<ArrayList<T>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ArrayList<T>> call, @NonNull Response<ArrayList<T>> response) {
                if (response.isSuccessful()) IRetrofitArray.success(response);
                else {
                    EErrorCode eErrorCode = EErrorCode.valueOf("ERROR_" + response.code());
                    Toast.makeText(context, eErrorCode.toString(), Toast.LENGTH_SHORT).show();
                    IRetrofitArray.bad(response);
                }
                if (isNotNull) stopLoading(progressBar, button);
                else if (progressBar != null) stopLoading(progressBar);
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ArrayList<T>> call, @NonNull Throwable t) {
                if (isNotNull) stopLoading(progressBar, button);
                else if (progressBar != null) stopLoading(progressBar);
                IRetrofitArray.failed(call, t);
                Toast.makeText(context, "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startLoading(ProgressBar progressBar, Button button) {
        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);
    }
    public void startLoading(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void stopLoading(ProgressBar progressBar, Button button) {
        progressBar.setVisibility(View.GONE);
        button.setEnabled(true);
    }
    public void stopLoading(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }
}
