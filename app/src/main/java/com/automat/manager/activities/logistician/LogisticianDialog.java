package com.automat.manager.activities.logistician;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.IBodyRetrofit;
import com.automat.manager.interfaces.IGetName;
import com.automat.manager.responses.GetCarResponse;
import com.automat.manager.responses.GetDriversResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogisticianDialog {
    private Context context;

    // Main elements

    // Dialog elements
    GetDriversResponse driver;
    GetCarResponse car;
    ListView lvOfSearchable;

    Button btReloadDialog;
    ProgressBar pbDialog;

    public LogisticianDialog(Context context) {
        this.context = context;
    }

    public <T> void showDialog(T obj, TextView textView, String api) {
        Call call = null;
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_searchable);
        dialog.getWindow().setLayout(800, 950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btReloadDialog = dialog.findViewById(R.id.bt_reload_dialog);
        pbDialog = dialog.findViewById(R.id.pb_dialog);
        EditText etSearchOfType = dialog.findViewById(R.id.et_search_of_list);
        lvOfSearchable = dialog.findViewById(R.id.lv_of_searchable);

        if (obj instanceof GetDriversResponse) {
            call = ApiClient
                    .getUserService()
                    .getDrivers(api);
        } else if (obj instanceof GetCarResponse) {
            call = ApiClient
                    .getUserService()
                    .getCars(api);
        }
            IBodyRetrofit iBodyRetrofit = new IBodyRetrofit() {

                @Override
                public <T> void success(Response<ArrayList<T>> response) {
                    List<String> someName = new ArrayList<>();
                    assert response.body() != null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        someName = response.body().stream()
                                .map(i -> ((IGetName) i).getNameI())
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                    ArrayAdapter<String> lvAdapterOfType = new ArrayAdapter<>(context,
                            android.R.layout.simple_list_item_1, someName);
                    lvOfSearchable.setAdapter(lvAdapterOfType);
                    etSearchOfType.addTextChangedListener(new TextWatcher() {
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
                    lvOfSearchable.setOnItemClickListener((adapterView, view, i, l) -> {
                        if (obj instanceof GetDriversResponse) {
                            driver = ((GetDriversResponse) response.body().get(i));
                            textView.setText(driver.getName());
                        } else if (obj instanceof GetCarResponse) {
                            car = ((GetCarResponse) response.body().get(i));
                            textView.setText(car.getName());
                        }
                        dialog.dismiss();
                    });
                }

                @Override
                public <T> void bad(Response<ArrayList<T>> response) {

                }

                @Override
                public void failed() {
                }
            };
            callBody(call, iBodyRetrofit, api);
    }

    private <T> void callBody(Call<ArrayList<T>> call, IBodyRetrofit iBodyRetrofit, String api) {
        pbDialog.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ArrayList<T>>() {
            @Override
            public void onResponse(Call<ArrayList<T>> call, Response<ArrayList<T>> response) {
                if (response.isSuccessful()) {
                    iBodyRetrofit.success(response);
                }
                else {
                    EErrorCode eErrorCode = EErrorCode.valueOf("ERROR_" + response.code());

                    iBodyRetrofit.bad(response);
                    Toast.makeText(context, eErrorCode.toString(), Toast.LENGTH_SHORT).show();
                }
                pbDialog.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<T>> call, Throwable t) {
                iBodyRetrofit.failed();
                ButtonReload buttonReload = new ButtonReload(context, btReloadDialog, pbDialog);
                btReloadDialog.setVisibility(View.VISIBLE);
                btReloadDialog.setOnClickListener(buttonReload.reload(new GetDriversResponse(), iBodyRetrofit, api));
                Toast.makeText(context, "Превышен лимит ожидания", Toast.LENGTH_SHORT).show();
                pbDialog.setVisibility(View.GONE);
            }
        });
    }


}
