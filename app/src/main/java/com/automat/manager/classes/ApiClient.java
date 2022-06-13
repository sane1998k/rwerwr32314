package com.automat.manager.classes;




import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.interfaces.IUserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String IP_ADDRESS;
    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .baseUrl(IP_ADDRESS)
                    .build();
        } catch (NullPointerException nullPointerException) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .baseUrl("https://trsavtomatika.ru")
                    .build();
        }


        return retrofit;
    }



    // Call body
    public static  <T> Callback<T> getCallBackBody(ICallBack<T> iCallBack, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    iCallBack.good(response);
                    //progressBar.setVisibility(View.GONE);
                    //Toast.makeText(progressBar.getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                } else {
                   try {
                       JSONObject jObjError = new JSONObject(response.errorBody().string());
                       Toast.makeText(progressBar.getContext(), jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                       Toast.makeText(progressBar.getContext(), response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
                    }

                    iCallBack.bad(response);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

                Toast.makeText(progressBar.getContext(), "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                iCallBack.failed();
            }
        };
    }



    public static IUserService getUserService() {
        IUserService userService = getRetrofit().create(IUserService.class);
        return userService;
    }
}
