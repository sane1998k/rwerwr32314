package com.automat.manager.activities.creater;

import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.classes.Adaptery;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.IBodyRetrofit;
import com.automat.manager.interfaces.Test;
import com.automat.manager.requests.CreateOrderRequest;
import com.automat.manager.requests.CreateOrderRequest2;
import com.automat.manager.responses.GetCounterpartyResponse;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetGasTypesResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Creater extends AppCompatActivity {
    TextView tvIsClass;
    ListView lvOfSearchable;
    Button btReloadDialog;
    ProgressBar pbDialog;
    EditText etSearchOfType;

    private SharedPreferences sharedPreferences;
    private String api;
    private ButtonLogout buttonLogout;
    Button btExitCreater, btCreateOrder;
    TextView tvFirm, tvTypeOfBalloon;
    EditText etNumOfBalloons;
    ProgressBar pbCreateOrder;
    Dialog dialog;
    ArrayList<String> listOfId;
    ArrayList<String> listOfName1;
    Map<String, String> listOfName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creater);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        api = sharedPreferences.getString(KEY_AUTH, "");
        pbCreateOrder = findViewById(R.id.pb_create_order);
        etNumOfBalloons = findViewById(R.id.et_num_of_balloons);
        listOfName = new HashMap<>();
        dialog = new Dialog(Creater.this);
        btExitCreater = findViewById(R.id.bt_exit_creater);
        tvFirm = findViewById(R.id.tv_firm);
        tvTypeOfBalloon = findViewById(R.id.tv_type_of_balloon);
        buttonLogout = new ButtonLogout(Creater.this, btExitCreater);
        btCreateOrder = findViewById(R.id.bt_create_order);
        buttonLogout.setExit();
        btCreateOrder.setOnClickListener(onClickBtCreateOrder());
        tvFirm.setOnClickListener(onClickTvFirm());
        tvTypeOfBalloon.setOnClickListener(onClickTvTypeOfBalloon());
    }

    private View.OnClickListener onClickTvFirm() {
        return view -> {
            createDialog();

            Call<ArrayList<GetCounterpartyResponse>> counterpartyResponseCall = ApiClient
                    .getUserService()
                    .getCounterparty(api);
            showDialog(counterpartyResponseCall, tvFirm, onClickTvFirm(), "1");
            btReloadDialog.setOnClickListener(reloadingData(new GetCounterpartyResponse()));
            //showDialog((TextView) view, "sd");
        };
    }

    private View.OnClickListener onClickTvTypeOfBalloon() {
        return view -> {
            createDialog();
            //btReloadDialog.setOnClickListener(reloadingData(lvOfSearchable));
            Call<ArrayList<GetGasTypesResponse>> gasTypesResponse = ApiClient
                    .getUserService()
                    .getGasTypes(api);
            showDialog(gasTypesResponse, tvTypeOfBalloon, onClickTvTypeOfBalloon(), "2");
            btReloadDialog.setOnClickListener(reloadingData(new GetGasTypesResponse()));
            //showDialog((TextView) view, "sd");
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private View.OnClickListener onClickBtCreateOrder() {
        return view -> {
            Stream<String> idFirms = keys(listOfName, tvFirm.getText().toString());
            Stream<String> idBalloons = keys(listOfName, tvTypeOfBalloon.getText().toString());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                if(tvFirm.getText().toString().equals("") || tvTypeOfBalloon.getText().toString().equals("") || etNumOfBalloons.getText().toString().equals("")) return;
                String idFirm = idFirms.findFirst().get();
                String idBalloon = idBalloons.findFirst().get();
                int countBalloons = Integer.parseInt(etNumOfBalloons.getText().toString());

//                ArrayList<CreateOrderRequest2.OrderDetail> orderDetails = new ArrayList<>();
//                orderDetails.add(new CreateOrderRequest2.OrderDetail())
//                CreateOrderRequest2 createOrderRequest2 = new CreateOrderRequest2(idFirm, );

                CreateOrderRequest createOrderRequest = new CreateOrderRequest();
                createOrderRequest.setCounterpartyId(idFirm);
                createOrderRequest.setGasTypeId(idBalloon);
                createOrderRequest.setBalloonCount(countBalloons);
                startLoading(pbCreateOrder, btCreateOrder);
                Call<GetCreateOrdersResponse> createOrdersResponseCall = ApiClient
                        .getUserService()
                        .getCreateOrdersResponse(api, createOrderRequest);
                createOrdersResponseCall.enqueue(new Callback<GetCreateOrdersResponse>() {
                    @Override
                    public void onResponse(Call<GetCreateOrdersResponse> call, Response<GetCreateOrdersResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Creater.this, "Успешно", Toast.LENGTH_SHORT).show();
                            clearData();
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(Creater.this, jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                Toast.makeText(Creater.this, response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
                            }
                        }
                        stopLoading(pbCreateOrder, btCreateOrder);
                    }

                    @Override
                    public void onFailure(Call<GetCreateOrdersResponse> call, Throwable t) {
                        Toast.makeText(Creater.this, "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
                        stopLoading(pbCreateOrder, btCreateOrder);
                    }
                });
            }
            //listOfName.clear();
        };
    }

    private void foo(EditText editText, ListView lv, ArrayAdapter<String> lvAdapterOfType) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lvAdapterOfType.getFilter().filter(charSequence);
                //adaptery.getFilter().filter(charSequence);
                //adaptery.filter(charSequence.toString());
                //  lvOfSearchable.setAdapter(adaptery);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            if (tvIsClass != null)
                tvIsClass.setText((lvAdapterOfType.getItem(i)));
            dialog.dismiss();
        });
    }

    private void xz(GetCounterpartyResponse getCounterpartyResponse) {
        IBodyRetrofit iBodyRetrofit = new IBodyRetrofit() {
            @Override
            public void success(Response response) {
                btReloadDialog.setVisibility(View.GONE);
                Toast.makeText(Creater.this, "Ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void bad(Response response) {

            }

            @Override
            public void failed() {

            }
        };
        Call<ArrayList<GetCounterpartyResponse>> call =  ApiClient.getUserService().getCounterparty(api);
        bbody(call, iBodyRetrofit);
    }

    private void xz(GetGasTypesResponse getGasTypesResponse) {
        IBodyRetrofit iBodyRetrofit = new IBodyRetrofit() {
            @Override
            public void success(Response response) {
                Toast.makeText(Creater.this, "Ok", Toast.LENGTH_SHORT).show();
                //stopLoading(pbDialog, btReloadDialog);
                btReloadDialog.setVisibility(View.GONE);
            }

            @Override
            public void bad(Response response) {
                //stopLoading(pbDialog, btReloadDialog);
            }

            @Override
            public void failed() {
                //stopLoading(pbDialog, btReloadDialog);
                //btReloadDialog.setVisibility(View.GONE);
            }
        };
        Call<ArrayList<GetGasTypesResponse>> call =  ApiClient.getUserService().getGasTypes(api);
        bbody(call, iBodyRetrofit);
    }

    private <T> void bbody(Call<ArrayList<T>> call, IBodyRetrofit iBodyRetrofit) {
        startLoading(pbDialog, btReloadDialog);
        call.enqueue(new Callback<ArrayList<T>>() {
            @Override
            public void onResponse(Call<ArrayList<T>> call, Response<ArrayList<T>> response) {
                if (response.isSuccessful()) {
                    iBodyRetrofit.success(response);
                    for (T res : response.body()) {
                        listOfName.put(((Test) res).getIdCounterparty(), ((Test) res).getNameCounterparty());
                    }
                    ArrayAdapter<String> lvAdapterOfType = new ArrayAdapter<>(Creater.this,
                              android.R.layout.simple_list_item_1, new ArrayList<>(listOfName.values()));
                    lvOfSearchable.setAdapter(lvAdapterOfType);
                    foo(etSearchOfType, lvOfSearchable, lvAdapterOfType);
                }
                else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(Creater.this, jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(Creater.this, response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
                    }
                    iBodyRetrofit.bad(response);
                }
                stopLoading(pbDialog, btReloadDialog);
            }

            @Override
            public void onFailure(Call<ArrayList<T>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
                stopLoading(pbDialog, btReloadDialog);
                iBodyRetrofit.failed();
            }
        });
    }


    private <T> View.OnClickListener reloadingData(T getResponse) {
  //      Call<ArrayList<Test>> call =  ApiClient.getUserService().getCounterparty(api);

    //        call = new <ArrayList<GetCounterpartyResponse>>(ApiClient.getUserService().getCounterparty(api));

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResponse instanceof GetCounterpartyResponse) {
                    xz((GetCounterpartyResponse) getResponse);
                    tvIsClass = tvFirm;
                }
                else if(getResponse instanceof GetGasTypesResponse) {
                    xz((GetGasTypesResponse) getResponse);
                    tvIsClass = tvTypeOfBalloon;
                }

            }
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

    private void clearData() {
        tvFirm.setText("");
        tvTypeOfBalloon.setText("");
        etNumOfBalloons.setText("");
        listOfName.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public <K, V> Stream<K> keys(Map<K, V> map, V value) {
            return map
                    .entrySet()
                    .stream()
                    .filter(entry -> value.equals(entry.getValue()))
                    .map(Map.Entry::getKey);

    }
//
//    private View.OnClickListener onClickTvType() {
//        return view -> {
//            showDialog((TextView) view, listOfType,
//                    getString(R.string.text_of_description_the_searchable) + " тип баллона");
//        };
//    }

    private <T> void showDialog(Call<ArrayList<T>> call, TextView textView, View.OnClickListener viewOnClickListener, String tesd) {
        tvIsClass = textView;
        listOfName1 = new ArrayList<>();
        listOfId = new ArrayList<>();
        btReloadDialog = dialog.findViewById(R.id.bt_reload_dialog);
        etSearchOfType = dialog.findViewById(R.id.et_search_of_list);
        TextView tvDescriptionOfSearchable = dialog.findViewById(R.id.tv_description_of_searchable);
        pbDialog = dialog.findViewById(R.id.pb_dialog);
        lvOfSearchable = dialog.findViewById(R.id.lv_of_searchable);
        pbDialog.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ArrayList<T>>() {
            @Override
            public void onResponse(Call<ArrayList<T>> call, Response<ArrayList<T>> response) {
                if (response.isSuccessful()) {
                    btReloadDialog.setVisibility(View.GONE);
                    assert response.body() != null;
                    for (T response1 : response.body()) {
                        listOfName.put(((Test) response1).getIdCounterparty(), ((Test) response1).getNameCounterparty());
                        //listOfName.add(((GetCounterpartyResponse) response1).getName());
                        listOfName1.add(((Test) response1).getNameCounterparty() + "");
                    }
                   // ArrayAdapter<String> lvAdapterOfType = new ArrayAdapter<>(Creater.this,
                          //  android.R.layout.simple_list_item_1, new ArrayList<>((Collection<String>) listOfName.values()));
                    ArrayAdapter<String> lvAdapterOfType = new ArrayAdapter<>(Creater.this, android.R.layout.simple_list_item_1, listOfName1);
                    lvOfSearchable.setAdapter(lvAdapterOfType);
                    //putDataIntoRecyclerView(listOfId);

                    foo(etSearchOfType, lvOfSearchable, lvAdapterOfType);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(Creater.this, jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        Toast.makeText(Creater.this, response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
                    }

                    btReloadDialog.setVisibility(View.VISIBLE);
                }

                pbDialog.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Call<ArrayList<T>> call, Throwable t) {
                btReloadDialog.setVisibility(View.VISIBLE);
                if (tesd.equals("1")) {
                    Call<ArrayList<GetCounterpartyResponse>> call1 = ApiClient
                            .getUserService()
                            .getCounterparty(api);

                }
                pbDialog.setVisibility(View.GONE);
            }
        });
    }



    private void createDialog() {
        dialog.setContentView(R.layout.dialog_searchable);
        dialog.getWindow().setLayout(800, 950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}