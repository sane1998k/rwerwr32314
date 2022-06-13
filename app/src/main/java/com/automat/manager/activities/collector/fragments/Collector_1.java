package com.automat.manager.activities.collector.fragments;

import static com.automat.manager.classes.ApiClient.getCallBackBody;
import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.activities.adapters.NewOrderAdapter;
import com.automat.manager.activities.adapters.NewOrderAdapterPidoras2;
import com.automat.manager.activities.adapters.RvCollectorsBalloonsAdapter;
import com.automat.manager.activities.adapters.StatusQRAdapter;
import com.automat.manager.activities.collector.Collector;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.Balloon;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.classes.StaticClass;
import com.automat.manager.classes.WarningDialog;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.requests.CreateReturnBalloonRequest;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetCreateReturnBalloonResponse;
import com.automat.manager.responses.PidorasObj1;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Collector_1 extends Fragment {
    private boolean isScannerEnable = false;
    private String api;
    ArrayList<Balloon> arrayList;

    ArrayList<Balloon> getAllGottenBalloons;

    //

    // Classes
    private PidorasObj1 getNewOrder;
    private PidorasObj1.OrderDetail getNewOrderPidor2;
    private GetAllGottenBalloons getReturnOrder;
    private Dialog dialog;

    private ButtonLogout buttonLogout;
    private ButtonReload buttonReload;

    private SharedPreferences sharedPreferences;
    //

    // Adapter's
    private NewOrderAdapter newOrderAdapter;
    private NewOrderAdapterPidoras2 newOrderAdapterPidoras2;
    private NewOrderAdapter.OnNewOrderClickListener onNewOrderClickListener;
    private NewOrderAdapterPidoras2.OnNewOrderClickListener onNewOrderClickListenerPidoras2;
    RvCollectorsBalloonsAdapter rvCollectorsBalloonsAdapter;
    RecyclerView.LayoutManager rvLayoutManagerReturnBalloon;
    //

    // Dialog activity
    private SearchView dialogSearchView;
    private Button dialogBtReload;
    private ProgressBar dialogProgressBar;
    private RecyclerView dialogRecyclerView;
    //

    // Collector activity
    Button btCollectorExit, btCollectorScanEnable;
    CodeScannerView collectorScannerView;
    CodeScanner mCodeScanner;
    TextView tvCollectorNewOrder, tvCollectorNewOrderPidoras2, tvCollectorResultTxt, tvCollectorCount;
    RecyclerView rvCollectorBalloons;
    Button btCollectorCollected;
    ProgressBar pbLoadCollector;
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_collector_1, container, false);
        init(inflatedView);

        createCodeScanner();


        return inflatedView;
    }

    private void init(View inflatedView) {
        getNewOrder = new PidorasObj1();
        getNewOrder.setOrderDetails(new ArrayList<>());

        btCollectorScanEnable = inflatedView.findViewById(R.id.bt_collector_scan_enable2);
        tvCollectorResultTxt = inflatedView.findViewById(R.id.tv_collector_result_txt2);
        collectorScannerView = inflatedView.findViewById(R.id.collector_scanner2);
        mCodeScanner = new CodeScanner(inflatedView.getContext(), collectorScannerView);

        sharedPreferences =  inflatedView.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        api = sharedPreferences.getString(KEY_AUTH, "");

        tvCollectorNewOrder = inflatedView.findViewById(R.id.tv_collector_new_order2);
        tvCollectorNewOrderPidoras2 = inflatedView.findViewById(R.id.tv_collector_new_order_pidoras2);

        tvCollectorCount = inflatedView.findViewById(R.id.tv_collector_count2);
        tvCollectorNewOrder.setOnClickListener(onClickTvCollectorNewOrder());
        tvCollectorNewOrderPidoras2.setOnClickListener(onClickTvCollectorNewOrderPidoras2());

        rvCollectorBalloons = inflatedView.findViewById(R.id.rv_collector_balloons2);
        btCollectorCollected = inflatedView.findViewById(R.id.bt_collector_collected2);
        pbLoadCollector = inflatedView.findViewById(R.id.pb_load_collector2);

        arrayList = new ArrayList<>();
        getAllGottenBalloons = new ArrayList<>();

        rvLayoutManagerReturnBalloon = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvCollectorBalloons.setLayoutManager(rvLayoutManagerReturnBalloon);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

        collectorScannerView.setOnClickListener(onClickCollectorScannerView());
        btCollectorScanEnable.setOnClickListener(onClickBtCollectorScanEnable() );

        btCollectorCollected.setOnClickListener(onClickBtCollectorCollected());
    }

    private void createCodeScanner() {
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);

        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(false);
        setupPermissions();
    }

    private void makeRequest() {
        String[] list = {Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(getActivity(), list, 101);
    }

    private void setupPermissions() {
        int permissions = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btCollectorScanEnable.setText("Включить");
                        if (getNewOrder == null) {
                            Toast.makeText(getActivity(), "Выберите сперва заказ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        rvCollectorsBalloonsAdapter.setIClickReturnBalloon(new RvCollectorsBalloonsAdapter.IClickReturnBalloon() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        // if (count < getNewOrder.getBalloonCount()) {
                        CreateReturnBalloonRequest createReturnBalloonRequest = new CreateReturnBalloonRequest();
                        createReturnBalloonRequest.setBalloonId(result.getText());
                        Call<GetCreateReturnBalloonResponse> callCreateReturnBalloon = ApiClient
                                .getUserService()
                                .createReturnBalloon(api, createReturnBalloonRequest, getNewOrderPidor2.getId());
                        Log.e("TAG", "run: " + getNewOrderPidor2.getId() );
                        callCreateReturnBalloon.enqueue(getCallBackBody(new ICallBack<GetCreateReturnBalloonResponse>() {
                            @Override
                            public void good(Response<GetCreateReturnBalloonResponse> response) {
                                rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
                                updateListViewCollectCollector();
                            }

                            @Override
                            public void bad(Response<GetCreateReturnBalloonResponse> response) {
                               // rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
                            }

                            @Override
                            public void failed() {
                                //rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
                            }
                        }, pbLoadCollector));
                    }
                });
            }
        });
    }

    void stateOne(Result s) {
        btCollectorScanEnable.setText("Включить");
        Toast.makeText(this.getActivity(), "1 " + s, Toast.LENGTH_SHORT).show();
    }

    void stateTwo(Result s) {
        btCollectorScanEnable.setText("Включить");
        Toast.makeText(this.getActivity(), "2 " + s, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onClickBtCollectorScanEnable() {
        return view -> {
            isScannerEnable = !isScannerEnable;
            ((Button) view).setText(isScannerEnable ? "Выключить" : "Включить");
            if (isScannerEnable) mCodeScanner.startPreview();
            else mCodeScanner.stopPreview();
        };
    }

    // Scanner
    private View.OnClickListener onClickCollectorScannerView() {
        return view -> {
            mCodeScanner.startPreview();
            btCollectorScanEnable.setText("Выключить");
            isScannerEnable = true;
        };
    }
//

    // On Click's


    // Buttons
    private View.OnClickListener onClickBtCollectorCollected() {
        return view -> {

            Call<Void> callSetCompleteOrder = ApiClient
                    .getUserService()
                    .setCompleteOrder(api, getNewOrderPidor2.getId());
            pbLoadCollector.setVisibility(View.VISIBLE);
            callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack() {
                @Override
                public void good(Response response) {

                    deleteDataCollect();
                }

                @Override
                public void bad(Response response) {

                }

                @Override
                public void failed() {

                }
            }, pbLoadCollector));
        };
    }

    // Click item RV return balloons

    private RvCollectorsBalloonsAdapter.IClickReturnBalloon onClickRvReturnBalloon() {
        return position -> {
            String message = getResources().getString(R.string.text_message_dialog);
            AlertDialog warningDialog = new WarningDialog(getActivity()).create(message, (dialogInterface, i) -> {
                getReturnOrder = (GetAllGottenBalloons) getAllGottenBalloons.get(position);
                Call<Void> callDeleteReturnBalloon = ApiClient
                        .getUserService()
                        .deleteReturnedBalloon(api, getReturnOrder.getId());
                pbLoadCollector.setVisibility(View.VISIBLE);
                callDeleteReturnBalloon.enqueue(getCallBackBody(new ICallBack<Void>() {
                    @Override
                    public void good(Response<Void> response) {
                        updateListViewCollectCollector();
                    }

                    @Override
                    public void bad(Response response) { }

                    @Override
                    public void failed() { }
                }, pbLoadCollector));
            });
            warningDialog.show();

        };
    }

    //

    private View.OnClickListener onClickDialogBtReload() {
        return view -> {
            Call<ArrayList<PidorasObj1>> recallNewOrder = ApiClient
                    .getUserService()
                    .getNewOrders(api);

            dialogProgressBar.setVisibility(View.VISIBLE);
            dialogBtReload.setEnabled(false);
            recallNewOrder.enqueue(getCallBackBody(new ICallBack() {
                @Override
                public void good(Response response) {
                    onNewOrderClickListener = onClickNewOrder();
                    newOrderAdapter = new NewOrderAdapter(getActivity(), (List<PidorasObj1>) response.body(), onNewOrderClickListener);
                    dialogRecyclerView.setAdapter(newOrderAdapter);

                    dialogBtReload.setVisibility(View.GONE);
                }

                @Override
                public void bad(Response response) {
                    dialogBtReload.setVisibility(View.VISIBLE);
                    dialogBtReload.setEnabled(true);
                }

                @Override
                public void failed() {
                    dialogBtReload.setVisibility(View.VISIBLE);
                    dialogBtReload.setEnabled(true);
                }
            }, dialogProgressBar));
        };
    }

    // TextView's
    private View.OnClickListener onClickTvCollectorNewOrder() {
            return view -> {
                createDialog();
                dialogProgressBar.setVisibility(View.VISIBLE);
                Call<ArrayList<PidorasObj1>> callNewOrder = ApiClient
                        .getUserService()
                        .getNewOrders(api);
                callNewOrder.enqueue(new Callback<ArrayList<PidorasObj1>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PidorasObj1>> call, Response<ArrayList<PidorasObj1>> response) {
                        if (response.isSuccessful()) {

                            ArrayList<PidorasObj1> newOrders = (ArrayList<PidorasObj1>) response.body().stream().filter(i->!i.getCarName().equals("") && !i.getDriverName().equals("")).collect(Collectors.toList());

                            onNewOrderClickListener = onClickNewOrder();
                            newOrderAdapter = new NewOrderAdapter(getActivity(), newOrders, onNewOrderClickListener);
                            dialogRecyclerView.setAdapter(newOrderAdapter);

                            dialogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String s) {
                                    newOrderAdapter.getFilter().filter(s);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String s) {
                                    newOrderAdapter.getFilter().filter(s);
                                    return false;
                                }
                            });
                            dialogBtReload.setVisibility(View.GONE);
                        } else {
                            dialogBtReload.setVisibility(View.VISIBLE);
                        }
                        dialogProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<PidorasObj1>> call, Throwable t) {
                        dialogBtReload.setVisibility(View.VISIBLE);
                        dialogProgressBar.setVisibility(View.GONE);
                    }
                });
            };
    }


    private View.OnClickListener onClickTvCollectorNewOrderPidoras2() {
        return view -> {
            if (!tvCollectorNewOrder.toString().equals("")) {
                createDialog();
                onNewOrderClickListenerPidoras2 = onClickNewOrderPidoras2();
                if (getNewOrder.getOrderDetails() != null)
                newOrderAdapterPidoras2 = new NewOrderAdapterPidoras2(getActivity(), getNewOrder.getOrderDetails(), onNewOrderClickListenerPidoras2);
                dialogRecyclerView.setAdapter(newOrderAdapterPidoras2);


                dialogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        newOrderAdapter.getFilter().filter(s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        newOrderAdapter.getFilter().filter(s);
                        return false;
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Выберите заказ!", Toast.LENGTH_SHORT).show();
            }

        };
    }

    //

    // New Order Adapter
    private NewOrderAdapter.OnNewOrderClickListener onClickNewOrder() {
        return (order, position, holder) -> {
            NewOrderAdapter.rowIndex = position;
            getNewOrder = order;
            newOrderAdapter.setGetCreateOrdersResponse(order);
           // tvCollectorNewOrder.setText(getNewOrder.getCounterpartyName() + "\nТип газа: " + getNewOrder.getGasTypeName());
            tvCollectorNewOrder.setText(getNewOrder.getCounterpartyName());
            OrderAdapter.rowIndex = -1;

            //pbLoadCollector.setVisibility(View.VISIBLE);
           // updateListViewCollectCollector();
            dialog.dismiss();
        };
    }

    private NewOrderAdapterPidoras2.OnNewOrderClickListener onClickNewOrderPidoras2() {
        return (order, position, holder) -> {
//            NewOrderAdapter.rowIndex = position;
            getNewOrderPidor2 = order;
//            newOrderAdapter.setGetCreateOrdersResponse(order);
            tvCollectorNewOrderPidoras2.setText("Тип газа: " + getNewOrderPidor2.getGasTypeName() + "\nОбъём: " + getNewOrderPidor2.getVolumeName());
//
//            OrderAdapter.rowIndex = -1;
//
//            pbLoadCollector.setVisibility(View.VISIBLE);
            updateListViewCollectCollector();
//            tvCollectorCount.setText(String.format(getResources().getString(R.string.text_count_balloons), getNewOrder., getNewOrder.getOrderDetails().get(position).getBalloonCount()));
            dialog.dismiss();
        };
    }

    //

    // Void's
    private void createDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_driver_order);

        dialog.getWindow().setLayout(800, 950);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        dialogSearchView = dialog.findViewById(R.id.et_search_driver_order);
        dialogBtReload = dialog.findViewById(R.id.bt_reload_driver_order);
        dialogProgressBar = dialog.findViewById(R.id.pb_driver_order);
        dialogRecyclerView = dialog.findViewById(R.id.rv_driver_order);

        buttonReload = new ButtonReload(getActivity(), dialogBtReload, dialogProgressBar);

        dialogBtReload.setOnClickListener(onClickDialogBtReload());

        dialog.show();
    }

    private void deleteDataCollect() {

           // getNewOrderPidor2 = new PidorasObj1.OrderDetail();
         //   getAllGottenBalloons.remove(getNewOrderPidor2);
            getNewOrder.getOrderDetails().remove(getNewOrderPidor2);
            newOrderAdapterPidoras2 = new NewOrderAdapterPidoras2(getActivity(), getNewOrder.getOrderDetails(), onNewOrderClickListenerPidoras2);

            reloadDataRV(new ArrayList<>());
        if (getNewOrder.getOrderDetails().size() == 0) {
            getNewOrder = new PidorasObj1();
            getNewOrderPidor2 = new PidorasObj1.OrderDetail();
//            getAllGottenBalloons.remove(getNewOrderPidor2);
//            newOrderAdapterPidoras2 = new NewOrderAdapterPidoras2(getActivity(), getNewOrder.getOrderDetails(), onNewOrderClickListenerPidoras2);
//            reloadDataRV(getAllGottenBalloons);
            //getAllGottenBalloons.clear();
            //rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter(getActivity(), getAllGottenBalloons);
            //rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

            tvCollectorCount.setText("");
            tvCollectorNewOrder.setText("");
            tvCollectorResultTxt.setText("Результат");
            tvCollectorNewOrderPidoras2.setText("");
        }
//            //getAllGottenBalloons.clear();
//            rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter(getActivity(), getAllGottenBalloons);
//            rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

            tvCollectorCount.setText("");
            tvCollectorNewOrderPidoras2.setText("");


        mCodeScanner.releaseResources();
    }



    private void reloadDataRV(ArrayList<Balloon> arrayList) {
        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter(getActivity(), arrayList);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);
        rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
    }

    private void updateListViewCollectCollector() {

        Call<ArrayList<GetAllGottenBalloons>> callUpdate = ApiClient
                .getUserService()
                .getAllGottenBalloons(api, getNewOrderPidor2.getId());
        Log.e("TAG", "updateListViewCollectCollector: " + getNewOrderPidor2.getId());
        pbLoadCollector.setVisibility(View.VISIBLE);
        callUpdate.enqueue(getCallBackBody(new ICallBack<ArrayList<GetAllGottenBalloons>>() {
            @Override
            public void good(Response response) {
              int from = 0;
               int to = 0;

                getAllGottenBalloons = (ArrayList<Balloon>) response.body();
                reloadDataRV(getAllGottenBalloons);
                from = getAllGottenBalloons.size();
               to = getNewOrderPidor2.getBalloonCount();

               tvCollectorCount.setText(String.format(getResources().getString(R.string.text_count_balloons), from, to));

                pbLoadCollector.setVisibility(View.GONE);

                rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
            }

            @Override
            public void bad(Response response) {
                //rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
            }

            @Override
            public void failed() {
                //rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
            }
        }, pbLoadCollector));
    }
    //
    //

    // new



    @Override
    public void onPause() {
        super.onPause();
        btCollectorScanEnable.setText("Включить");
        mCodeScanner.releaseResources();
    }

}