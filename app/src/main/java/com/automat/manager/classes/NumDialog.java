package com.automat.manager.classes;

import static com.automat.manager.classes.ApiClient.getCallBackBody;
import static com.automat.manager.global.GlobalValue.DRIVER_COUNT_RETURN_BALLOON;
import static com.automat.manager.global.GlobalValue.DRIVER_ID_ORDER;
import static com.automat.manager.global.GlobalValue.DRIVER_ID_RETURN_ORDER;
import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.OBJECT_DRIVER1;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.automat.manager.R;
import com.automat.manager.activities.Splash;
import com.automat.manager.activities.driver.Driver1;
import com.automat.manager.activities.driver.Driver2;
import com.automat.manager.activities.driver.ResultDriver;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.requests.CreateReturnRequest;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetReturningsBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumDialog extends AppCompatDialogFragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String api;
    String idOrder;
    private EditText etNum;
    private Button btNextNum;
    private ProgressBar pbNumDialog;
    private Context context;

    public NumDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        api = sharedPreferences.getString(KEY_AUTH, "");
        idOrder = sharedPreferences.getString(DRIVER_ID_ORDER, "");

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.num_dialog, null);

        builder.setView(view)
                .setTitle("Укажите нужное число");

        etNum = view.findViewById(R.id.et_num);
        btNextNum = view.findViewById(R.id.bt_next_num);
        pbNumDialog = view.findViewById(R.id.pb_num_dialog);

        btNextNum.setOnClickListener(view1 -> {
            if (etNum.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Введите число!", Toast.LENGTH_SHORT).show();
                return;
            }

            CreateReturnRequest createReturnRequest = new CreateReturnRequest();
            createReturnRequest.setBalloonCount(etNum.getText().toString());
            Call<GetReturningsBody> callSetCompleteOrder = ApiClient
                    .getUserService()
                    .createReturn(api, idOrder, createReturnRequest);
            pbNumDialog.setVisibility(View.VISIBLE);
            callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack<GetReturningsBody>() {
                @Override
                public void good(Response<GetReturningsBody> response) {
                    assert response.body() != null;
                    save(response);
                    PutObject.save(sharedPreferences, response.body(), OBJECT_DRIVER1);
                    dismiss();
                }

                @Override
                public void bad(Response<GetReturningsBody> response) {
//                    assert response.body() != null;
//                    if ((response.body()).isReturned()) {
//                        save(response);
//                    }
                }

                @Override
                public void failed() {

                }
            }, pbNumDialog));


        });
        return builder.create();
    }

    // save data
    private void save(Response response) {
        editor.putString(DRIVER_COUNT_RETURN_BALLOON, etNum.getText().toString());

        assert response.body() != null;
        editor.putString(DRIVER_ID_RETURN_ORDER, ((GetReturningsBody) response.body()).getId());
        editor.apply();

        Intent intent = new Intent((Driver1) getActivity(), Driver2.class);
        startActivity(intent);
    }

}
