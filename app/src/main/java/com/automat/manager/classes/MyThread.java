package com.automat.manager.classes;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.automat.manager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class MyThread {
    Context context;
    Activity activity;
    private interface Do {
        void body() throws JSONException;
    }

    public interface State {
        void good();
    }

    public MyThread(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public Runnable createRunnable(Call call, State state, int i) {

        return () -> {
            final ProgressBar pb = activity.findViewById(i);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    state.good();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        getHandler(() -> Toast.makeText(pb.getContext(), jObjError.getString("detail"), Toast.LENGTH_SHORT).show());
                    } catch (IOException | JSONException e) {
                        getHandler(() ->
                                Toast.makeText(pb.getContext(), response.code() == 404 ?
                                        "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show());
                    }
                    getHandler(() -> pb.setVisibility(View.GONE));
                }
            } catch (IOException e) {
                getHandler(() -> Toast.makeText(pb.getContext(), "Превышен лимит ожидания", Toast.LENGTH_SHORT).show());
                getHandler(() -> pb.setVisibility(View.GONE));
            }
        };
    }

    private void getHandler(Do Do) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                Do.body();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

}
