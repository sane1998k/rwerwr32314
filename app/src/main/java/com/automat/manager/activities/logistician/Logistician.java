package com.automat.manager.activities.logistician;

import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.classes.MainRequest;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.interfaces.IBodyRetrofit;
import com.automat.manager.interfaces.IRetrofitNotArray;
import com.automat.manager.requests.PutOrdersRequest;
import com.automat.manager.responses.GetCarResponse;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetDriversResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Logistician extends AppCompatActivity {
    private String api;
    OrderAdapter adapter;
    GetCreateOrdersResponse changeOrder;
    ArrayList<GetCreateOrdersResponse> orders = new ArrayList<>();
    ButtonLogout buttonLogout;
    ButtonReload buttonReload;
    OrderAdapter.OnOrderClickListener orderClickListener;
    LogisticianDialog logisticianDialog;
    RecyclerView recyclerView;
    PutOrdersRequest putOrdersRequest;

    Button btLogisticiaExit, btLogisticianSend, btLogisticianReloading, btReloadRv;
    TextView tvLogisticianDriverName, tvLogisticianCarName;
    ProgressBar pbLogisticianLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistician);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
        setInitialData();
    }

    private void init() {
        logisticianDialog = new LogisticianDialog(Logistician.this);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        api = sharedPreferences.getString(KEY_AUTH, "");
        btLogisticiaExit = findViewById(R.id.bt_logistician_exit);
        buttonLogout = new ButtonLogout(this, btLogisticiaExit);
        buttonLogout.setExit();
        btLogisticianSend = findViewById(R.id.bt_logistician_send);
        btLogisticianReloading = findViewById(R.id.bt_logistician_reloading);
        btReloadRv = findViewById(R.id.bt_reload_rv);
        tvLogisticianDriverName = findViewById(R.id.tv_logistician_drive_name);
        tvLogisticianCarName = findViewById(R.id.tv_logistician_car_name);
        pbLogisticianLoading = findViewById(R.id.pb_logistician_loading);

        recyclerView = findViewById(R.id.rv_logistician_order);

        buttonReload = new ButtonReload(Logistician.this, btLogisticianReloading, pbLogisticianLoading);

        tvLogisticianDriverName.setOnClickListener(onClickDriveNameTv());
        tvLogisticianCarName.setOnClickListener(onClickCarNameTv());
        btLogisticianSend.setOnClickListener(onClickSendBt());
        btReloadRv.setOnClickListener(onClickReloadBt());
        btLogisticianReloading.setOnClickListener(onClickReloadBt());
    }

    private View.OnClickListener onClickCarNameTv() {
        return view -> logisticianDialog
                .showDialog(new GetCarResponse(), tvLogisticianCarName, api);
    }

    private View.OnClickListener onClickDriveNameTv() {
        return view -> logisticianDialog
                .showDialog(new GetDriversResponse(), tvLogisticianDriverName, api);
    }

    private View.OnClickListener onClickSendBt() {
        return view -> {
            String nameDriver = tvLogisticianDriverName.getText().toString();
            String nameCar = tvLogisticianCarName.getText().toString();
            String id = changeOrder != null ? changeOrder.getId() : "";

            if (nameDriver.equals("") || nameCar.equals("") || id.equals("")) {
                Toast.makeText(Logistician.this,
                        "Заполните имя водителя или Машину, и выберите поле!", Toast.LENGTH_SHORT).show();
                return;
            }

            String idDriver = logisticianDialog.driver.getId();
            String idCar = logisticianDialog.car.getId();
            String idCounterparty = changeOrder.getCounterpartyId();
            int countOfBalloons = changeOrder.getBalloonCount();
            putOrdersRequest = new PutOrdersRequest(idCounterparty, idDriver, idCar, countOfBalloons);

            Call<PutOrdersRequest> putOrdersRequestCall = ApiClient
                    .getUserService()
                    .updateOrders(api, id, putOrdersRequest);

            MainRequest mainRequest = new MainRequest(Logistician.this, putOrdersRequestCall, new IRetrofitNotArray() {
                @Override
                public <T> void success(Response<T> response) {
                    orders.remove(changeOrder);
                    updateDateRV(orders);
                    clearAllData();

                    Toast.makeText(Logistician.this, "Успешно", Toast.LENGTH_SHORT).show();
                }

                @Override
                public <T> void bad(Response<T> response) { }
                public <T> void failed(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {}
            }, pbLogisticianLoading, btLogisticianSend);

            mainRequest.sendRequest();
        };

    }

    private View.OnClickListener onClickReloadBt() {
        return buttonReload.reload(new GetCreateOrdersResponse(), new IBodyRetrofit() {
            @Override
            public <T> void success(Response<ArrayList<T>> response) {
                orders.clear();
               reloadRV(response);
            }

            @Override
            public <T> void bad(Response<ArrayList<T>> response) {

            }

            @Override
            public void failed() {

            }
        }, api);
    }

    @SuppressLint("NotifyDataSetChanged")
    private OrderAdapter.OnOrderClickListener onClickOrder() {
        return (order, position, holder) -> {
            OrderAdapter.rowIndex = position;
            changeOrder = order;
            adapter.setGetCreateOrdersResponse(order);
        };
    }

    private void setInitialData(){
        Call<ArrayList<GetCreateOrdersResponse>> call = ApiClient
                .getUserService()
                .getOrders(api);
        pbLogisticianLoading.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ArrayList<GetCreateOrdersResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GetCreateOrdersResponse>> call, @NonNull Response<ArrayList<GetCreateOrdersResponse>> response) {
                if (response.isSuccessful()) {
                    reloadRV(response);
                }
                else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(Logistician.this, jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(Logistician.this, response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
                    }
                    btLogisticianReloading.setVisibility(View.VISIBLE);
                }
                pbLogisticianLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<GetCreateOrdersResponse>> call, @NonNull Throwable t) {
                    Toast.makeText(Logistician.this, "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
                    pbLogisticianLoading.setVisibility(View.GONE);
                btLogisticianReloading.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void reloadRV(Response response) {
        if (response.body() == null) return;
        orders = (ArrayList<GetCreateOrdersResponse>) ((ArrayList<GetCreateOrdersResponse>) response.body()).stream().filter(
                i -> i.getCarId().equals("00000000-0000-0000-0000-000000000000") ||
                        i.getDriverId().equals("00000000-0000-0000-0000-000000000000"))
                .collect(Collectors.toList());
        orderClickListener = onClickOrder();
        adapter = new OrderAdapter(Logistician.this, orders, orderClickListener);
        recyclerView.setAdapter(adapter);
    }

    private void updateDateRV(ArrayList<GetCreateOrdersResponse> arrayList) {
        adapter.setOrders(arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void clearAllData() {
        changeOrder = new GetCreateOrdersResponse();
        OrderAdapter.rowIndex = -1;

        tvLogisticianDriverName.setText("");
        tvLogisticianCarName.setText("");
    }
}