package com.automat.manager.classes;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.IBodyRetrofit;
import com.automat.manager.interfaces.IRetrofitNotArray;
import com.automat.manager.responses.GetBalloonById;
import com.automat.manager.responses.GetCarResponse;
import com.automat.manager.responses.GetCounterpartyResponse;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetDriversResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ButtonReload {
    Context context;
    private Button btReload;
    private ProgressBar pbDialog;
    public ButtonReload(Context context, Button btReload, ProgressBar pbDialog) {
        this.context = context;
        this.btReload = btReload;
        this.pbDialog = pbDialog;
    }

    public <T> View.OnClickListener reload(T obj, IBodyRetrofit iBodyRetrofit, String api, String... id) {
        return view -> {
            Call call2 = null;
            if (obj instanceof GetDriversResponse) {
                call2 = ApiClient
                        .getUserService()
                        .getDrivers(api);
            } else if (obj instanceof GetCarResponse) {
                call2 = ApiClient
                        .getUserService()
                        .getCars(api);
            } else if (obj instanceof GetCreateOrdersResponse) {
                call2 = ApiClient
                        .getUserService()
                        .getOrders(api);
            } else if (obj instanceof GetBalloonById) {
                call2 = ApiClient
                        .getUserService()
                        .getBalloonById(api, id[0]);
            }

            if (call2 == null) return;
            startLoading(pbDialog, btReload);
            call2.enqueue(new Callback<ArrayList<T>>() {
                @Override
                public void onResponse(Call<ArrayList<T>> call, Response<ArrayList<T>> response) {
                    if (response.isSuccessful()) {
                        iBodyRetrofit.success(response);
                        btReload.setVisibility(View.GONE);
                    }
                    else {
                        EErrorCode eErrorCode = EErrorCode.valueOf("ERROR_" + response.code());
                        iBodyRetrofit.bad(response);
                        Toast.makeText(context, eErrorCode.toString(), Toast.LENGTH_SHORT).show();
                    }
                    stopLoading(pbDialog, btReload);
                }

                @Override
                public void onFailure(Call<ArrayList<T>> call, Throwable t) {
                    iBodyRetrofit.failed();
                    stopLoading(pbDialog, btReload);
                    Toast.makeText(context, "Превышен лимит ожидания",Toast.LENGTH_SHORT).show();
                }
            });

        };
    }

    public <T> View.OnClickListener reload(T obj, IRetrofitNotArray IRetrofitNotArray, String api, String... id) {
        return view -> {
            Call call2 = null;
            if (obj instanceof GetBalloonById) {
                call2 = ApiClient
                        .getUserService()
                        .getBalloonById(api, id[0]);
            }

            if (call2 == null) return;
            startLoading(pbDialog, btReload);
            call2.enqueue(new Callback<ArrayList<T>>() {
                @Override
                public void onResponse(Call<ArrayList<T>> call, Response<ArrayList<T>> response) {
                    if (response.isSuccessful()) {
                        IRetrofitNotArray.success(response);
                        btReload.setVisibility(View.GONE);
                    }
                    else {
                        EErrorCode eErrorCode = EErrorCode.valueOf("ERROR_" + response.code());
                        IRetrofitNotArray.bad(response);
                        Toast.makeText(context, eErrorCode.toString(), Toast.LENGTH_SHORT).show();
                    }
                    stopLoading(pbDialog, btReload);
                }

                @Override
                public void onFailure(Call<ArrayList<T>> call, Throwable t) {
                    IRetrofitNotArray.failed(call, t);
                    stopLoading(pbDialog, btReload);
                    Toast.makeText(context, "Превышен лимит ожидания",Toast.LENGTH_SHORT).show();
                }
            });

        };
    }

    private void startLoading(ProgressBar pb, Button bt) {
        pb.setVisibility(View.VISIBLE);
        bt.setEnabled(false);
    }

    private void stopLoading(ProgressBar pb, Button bt) {
        pb.setVisibility(View.GONE);
        bt.setEnabled(true);
    }
}
