package com.automat.manager.activities.collector.fragments;

import static com.automat.manager.classes.ApiClient.getCallBackBody;
import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.NAME_USER;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.activities.adapters.NewOrderAdapter;
import com.automat.manager.activities.adapters.NewOrderAdapter2;
import com.automat.manager.activities.adapters.RvCollectorsBalloonsAdapter;
import com.automat.manager.activities.adapters.RvCollectorsBalloonsAdapter2;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.Balloon;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.classes.WarningDialog;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.requests.CheckBalloonRequest;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetReturnOrdersResponse;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Collector_2 extends Fragment {
    private boolean isScannerEnable = false;
    private String api;
    ArrayList<Balloon> arrayList;

    ArrayList<Balloon> getAllGottenBalloons;

    //

    // Classes
    private GetReturnOrdersResponse getNewOrder;
    private GetAllGottenBalloons getReturnOrder;
    private Dialog dialog;

    private ButtonLogout buttonLogout;
    private ButtonReload buttonReload;

    private SharedPreferences sharedPreferences;
    //

    // Adapter's
    private NewOrderAdapter2 newOrderAdapter;
    private NewOrderAdapter2.OnNewOrderClickListener onNewOrderClickListener;

    RvCollectorsBalloonsAdapter2 rvCollectorsBalloonsAdapter;
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
    TextView tvCollectorNewOrder, tvCollectorResultTxt, tvCollectorCount;
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
        View inflatedView = inflater.inflate(R.layout.fragment_collector_2, container, false);
        init(inflatedView);
        initSpinner(inflatedView);
        createCodeScanner();
        return inflatedView;
    }

    private void init(View inflatedView) {
        btCollectorScanEnable = inflatedView.findViewById(R.id.bt_collector_scan_enable3);
        tvCollectorResultTxt = inflatedView.findViewById(R.id.tv_collector_result_txt3);
        collectorScannerView = inflatedView.findViewById(R.id.collector_scanner3);
        mCodeScanner = new CodeScanner(inflatedView.getContext(), collectorScannerView);

        sharedPreferences =  inflatedView.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        api = sharedPreferences.getString(KEY_AUTH, "");

        tvCollectorNewOrder = inflatedView.findViewById(R.id.tv_collector_new_order3);
        tvCollectorCount = inflatedView.findViewById(R.id.tv_collector_count3);
        tvCollectorNewOrder.setOnClickListener(onClickTvCollectorNewOrder());
        rvCollectorBalloons = inflatedView.findViewById(R.id.rv_collector_balloons3);
        btCollectorCollected = inflatedView.findViewById(R.id.bt_collector_collected3);
        pbLoadCollector = inflatedView.findViewById(R.id.pb_load_collector3);

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
                        // if (count < getNewOrder.getBalloonCount()) {
                        CheckBalloonRequest checkBalloonRequest = new CheckBalloonRequest();
                        checkBalloonRequest.setLocationAddress("Не удалось определить");
                        checkBalloonRequest.setLocationCoordinates("Не удалось определить");
                        Call<Void> callCreateReturnBalloon = ApiClient
                                .getUserService()
                                .checkReturnBalloon(api, getNewOrder.getId(), result.getText(), checkBalloonRequest);
                        callCreateReturnBalloon.enqueue(getCallBackBody(new ICallBack<Void>() {
                            @Override
                            public void good(Response<Void> response) {
                                updateListViewCollectCollector();
                            }

                            @Override
                            public void bad(Response response) {

                            }

                            @Override
                            public void failed() {

                            }
                        }, pbLoadCollector));
                    }
                });
            }
        });
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
            if (getAllGottenBalloons.size() == getNewOrder.getDoneCount()) {
                Call<Void> callSetCompleteOrder = ApiClient
                        .getUserService()
                        .toDoneReturnOrder(api, getNewOrder.getId());
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
            } else {
                WarningDialog warningDialog = new WarningDialog(getActivity());
                warningDialog.create("Отсканированно " + getAllGottenBalloons.size() + " из " + getNewOrder.getDoneCount() + "! \nДальнейшиее действие приведёт к потере не отсканированных баллонов",
                        (dialogInterface, i) -> {
                            Call<Void> callSetCompleteOrder = ApiClient
                                    .getUserService()
                                    .toDoneReturnOrder(api, getNewOrder.getId());
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
                        }).show();
            }

        };
    }

    // Click item RV return balloons

    private RvCollectorsBalloonsAdapter2.IClickReturnBalloon onClickRvReturnBalloon() {
        return position -> {
            String message = getResources().getString(R.string.text_message_dialog);
            AlertDialog warningDialog = new WarningDialog(getActivity()).create(message, (dialogInterface, i) -> {
                getReturnOrder = (GetAllGottenBalloons) getAllGottenBalloons.get(position);
                Call<Void> callDeleteReturnBalloon = ApiClient
                        .getUserService()
                        .deleteReturnedBalloon(api, getReturnOrder.getBalloonId());
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
            Call<ArrayList<GetReturnOrdersResponse>> recallNewOrder = ApiClient
                    .getUserService()
                    .getReturnOrder(api);

            dialogProgressBar.setVisibility(View.VISIBLE);
            dialogBtReload.setEnabled(false);
            recallNewOrder.enqueue(getCallBackBody(new ICallBack() {
                @Override
                public void good(Response response) {
                    onNewOrderClickListener = onClickNewOrder();
                    newOrderAdapter = new NewOrderAdapter2(getActivity(), (List<GetReturnOrdersResponse>) response.body(), onNewOrderClickListener);
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
            Call<ArrayList<GetReturnOrdersResponse>> callNewOrder = ApiClient
                    .getUserService()
                    .getReturnOrder(api);
            callNewOrder.enqueue(new Callback<ArrayList<GetReturnOrdersResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<GetReturnOrdersResponse>> call, Response<ArrayList<GetReturnOrdersResponse>> response) {
                    if (response.isSuccessful()) {

                        ArrayList<GetReturnOrdersResponse> newOrders = (ArrayList<GetReturnOrdersResponse>) response.body();

                        onNewOrderClickListener = onClickNewOrder();
                        newOrderAdapter = new NewOrderAdapter2(getActivity(), newOrders, onNewOrderClickListener);
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
                public void onFailure(Call<ArrayList<GetReturnOrdersResponse>> call, Throwable t) {
                    dialogBtReload.setVisibility(View.VISIBLE);
                    dialogProgressBar.setVisibility(View.GONE);
                }
            });
        };
    }

    //

    // New Order Adapter
    private NewOrderAdapter2.OnNewOrderClickListener onClickNewOrder() {
        return (order, position, holder) -> {
            NewOrderAdapter.rowIndex = position;
            getNewOrder = order;
            newOrderAdapter.setGetCreateOrdersResponse(order);
            tvCollectorNewOrder.setText(getNewOrder.getCounterpartyName() + "\nТ/С: " + getNewOrder.getCarName());
            OrderAdapter.rowIndex = -1;
            updateListViewCollectCollector();
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
        getNewOrder = new GetReturnOrdersResponse();

        getAllGottenBalloons.clear();
        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter2(getActivity(), getAllGottenBalloons);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

        tvCollectorCount.setText("");
        tvCollectorNewOrder.setText("");
        tvCollectorResultTxt.setText("Результат");
        mCodeScanner.releaseResources();
    }



    private void reloadDataRV(ArrayList<Balloon> arrayList) {
        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter2(getActivity(), arrayList);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);
        rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());

    }

    private void updateListViewCollectCollector() {
        Call<ArrayList<GetAllGottenBalloons>> callUpdate = ApiClient
                .getUserService()
                .createReturnBalloon2(api, getNewOrder.getId());
        pbLoadCollector.setVisibility(View.VISIBLE);
        callUpdate.enqueue(getCallBackBody(new ICallBack<ArrayList<GetAllGottenBalloons>>() {
            @Override
            public void good(Response response) {
                int from = 0;
                int to = 0;

                getAllGottenBalloons = (ArrayList<Balloon>) ((ArrayList<Balloon>) response.body()).stream().filter(i->i.getIBalloonIsCheck()).collect(Collectors.toList());

                reloadDataRV(getAllGottenBalloons);
                from = getAllGottenBalloons.size();
                to = getNewOrder.getDoneCount();
                tvCollectorCount.setText(String.format(getResources().getString(R.string.text_count_balloons), from, to));
                pbLoadCollector.setVisibility(View.GONE);
            }

            @Override
            public void bad(Response response) {

            }

            @Override
            public void failed() {

            }
        }, pbLoadCollector));
    }
    //
    //

    private void initSpinner(View inflatedView) {
        ConstraintLayout constraintLayout;

        TextInputLayout etNameBalloon = inflatedView.findViewById(R.id.et_name_balloon_2);
        TextInputEditText etNameBalloonTxt = inflatedView.findViewById(R.id.et_name_balloon_2_txt);

        showKeyBoard(etNameBalloonTxt);
        etNameBalloonTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_GO) {
                    checkByName(inflatedView);
                    hideKeyboard(getActivity());
                }
                return false;
            }
        });

        etNameBalloon.setEndIconOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                checkByName(inflatedView);
                hideKeyboard(getActivity());
            }
        });

        Spinner spinner = inflatedView.findViewById(R.id.spinner_state_2);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        constraintLayout = inflatedView.findViewById(R.id.constraint_2);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        btCollectorScanEnable.setVisibility(View.VISIBLE);
                        btCollectorScanEnable.setText("Включить");
                        collectorScannerView.setVisibility(View.VISIBLE);
                        etNameBalloon.setVisibility(View.GONE);

//                        ConstraintSet set = new ConstraintSet();
//
//                        set.clone(constraintLayout);
//
//                        changeConstraints(set, 0);
//
//                        TransitionManager.beginDelayedTransition(constraintLayout);
//
//                        set.applyTo(constraintLayout);

                        break;
                    case 1:
                        etNameBalloon.setVisibility(View.VISIBLE);
                        btCollectorScanEnable.setVisibility(View.GONE);
                        collectorScannerView.setVisibility(View.GONE);
                        mCodeScanner.stopPreview();
//
//                        ConstraintSet set2 = new ConstraintSet();
//
//                        set2.clone(constraintLayout);
//
//                        changeConstraints(set2, 1);
////
//                        TransitionManager.beginDelayedTransition(constraintLayout);
//
//                        set2.applyTo(constraintLayout);
                        break;
                    default:break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showKeyBoard(TextInputEditText textInputEditText) {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(textInputEditText.getRootView(), InputMethodManager.SHOW_IMPLICIT);
        textInputEditText.requestFocus();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void changeConstraints(ConstraintSet set, int i) {
        switch (i) {
            case 0:
                set.clear(R.id.spinner_state_2, ConstraintSet.BOTTOM);
                set.connect(R.id.spinner_state_2, ConstraintSet.BOTTOM, R.id.bt_collector_scan_enable3, ConstraintSet.TOP);
                break;
            case 1:
                set.clear(R.id.spinner_state_2, ConstraintSet.BOTTOM);
                set.connect(R.id.spinner_state_2, ConstraintSet.BOTTOM, R.id.et_name_balloon_2, ConstraintSet.TOP);
                break;
            default:break;
        }

    }

    private void checkByName(View inflatedView) {
        if (getNewOrder == null) {
            Toast.makeText(getActivity(), "Выберите сперва заказ", Toast.LENGTH_SHORT).show();
            return;
        }
        CheckBalloonRequest checkBalloonRequest = new CheckBalloonRequest();
        checkBalloonRequest.setLocationAddress("Не удалось определить");
        checkBalloonRequest.setLocationCoordinates("Не удалось определить");

        Call<Void> checkBalloonByNameCollector = ApiClient
                .getUserService()
                .checkBalloonByNameCollector(api, getNewOrder.getId(), ((TextInputEditText) inflatedView.findViewById(R.id.et_name_balloon_2_txt)).getText().toString(), checkBalloonRequest);
        checkBalloonByNameCollector.enqueue(getCallBackBody(new ICallBack<Void>() {
            @Override
            public void good(Response<Void> response) {
                updateListViewCollectCollector();
            }

            @Override
            public void bad(Response<Void> response) {

            }

            @Override
            public void failed() {

            }
        }, pbLoadCollector));
    }

    @Override
    public void onPause() {
        super.onPause();
        btCollectorScanEnable.setText("Включить");
        mCodeScanner.releaseResources();
    }
}