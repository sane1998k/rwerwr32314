package com.automat.manager.activities.driver;

import static com.automat.manager.classes.ApiClient.getCallBackBody;
import static com.automat.manager.global.GlobalValue.DRIVER_COUNT_ORDER_BALLOON;
import static com.automat.manager.global.GlobalValue.DRIVER_COUNT_RETURN_BALLOON;
import static com.automat.manager.global.GlobalValue.DRIVER_ID_ORDER;
import static com.automat.manager.global.GlobalValue.DRIVER_ID_RETURN_ORDER;
import static com.automat.manager.global.GlobalValue.DRIVER_SIZE_ARR_ORDER;
import static com.automat.manager.global.GlobalValue.DRIVER_SIZE_ARR_RETURN_ORDER;
import static com.automat.manager.global.GlobalValue.GET_NEW_ORDER;
import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.NAME_USER;
import static com.automat.manager.global.GlobalValue.OBJECT_DRIVER1;
import static com.automat.manager.global.GlobalValue.PREF_NAME;
import static com.automat.manager.global.GlobalValue.topics;
import static com.automat.manager.services.MyFCMService.foo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.activities.adapters.NewOrderAdapter;
import com.automat.manager.activities.adapters.RvCollectorsBalloonsAdapter;
import com.automat.manager.activities.adapters.RvCollectorsBalloonsAdapter2;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.Balloon;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.classes.NextDialog;
import com.automat.manager.classes.NumDialog;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.classes.PutObject;
import com.automat.manager.classes.WarningDialog;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.requests.CheckBalloonRequest;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetReturningsBody;
import com.automat.manager.responses.PidorasObj1;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Driver1 extends AppCompatActivity {
    private boolean isScannerEnable = false;
    private String api;
    ArrayList<Balloon> arrayList;

    ArrayList<Balloon> getAllGottenBalloons;

    //

    // Classes
    private PidorasObj1 getNewOrder;
    private GetAllGottenBalloons getReturnOrder;
    private Dialog dialog;

    private ButtonLogout buttonLogout;
    private ButtonReload buttonReload;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //

    // Adapter's
    private NewOrderAdapter newOrderAdapter;
    private NewOrderAdapter.OnNewOrderClickListener onNewOrderClickListener;

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
    ImageButton btCollectorExit;
            Button btCollectorScanEnable;
    CodeScannerView collectorScannerView;
    CodeScanner mCodeScanner;
    TextView tvCollectorNewOrder, tvCollectorResultTxt, tvCollectorCount;
    RecyclerView rvCollectorBalloons;
    Button btCollectorCollected;

    ProgressBar pbLoadCollector;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver1);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
        initSpinner();
        createCodeScanner();
    }

    private void init() {
        foo(topics.get(2));

        btCollectorExit = findViewById(R.id.bt_collector_exit4);
        buttonLogout = new ButtonLogout(this, btCollectorExit);
        buttonLogout.setExit2();

        btCollectorScanEnable = findViewById(R.id.bt_collector_scan_enable4);
        tvCollectorResultTxt = findViewById(R.id.tv_collector_result_txt4);
        collectorScannerView = findViewById(R.id.collector_scanner4);
        mCodeScanner = new CodeScanner(Driver1.this, collectorScannerView);

        sharedPreferences =  Driver1.this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



        api = sharedPreferences.getString(KEY_AUTH, "");

        tvCollectorNewOrder = findViewById(R.id.tv_collector_new_order4);
        tvCollectorCount = findViewById(R.id.tv_collector_count4);
        tvCollectorNewOrder.setOnClickListener(onClickTvCollectorNewOrder());
        rvCollectorBalloons = findViewById(R.id.rv_collector_balloons4);
        btCollectorCollected = findViewById(R.id.bt_collector_collected4);
        pbLoadCollector = findViewById(R.id.pb_load_collector4);

        arrayList = new ArrayList<>();
        getAllGottenBalloons = new ArrayList<>();

        rvLayoutManagerReturnBalloon = new LinearLayoutManager(Driver1.this,
                LinearLayoutManager.VERTICAL, false);
        rvCollectorBalloons.setLayoutManager(rvLayoutManagerReturnBalloon);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

        collectorScannerView.setOnClickListener(onClickCollectorScannerView());
        btCollectorScanEnable.setOnClickListener(onClickBtCollectorScanEnable() );

        btCollectorCollected.setOnClickListener(onClickBtCollectorCollected());

        delete();

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
        ActivityCompat.requestPermissions(Driver1.this, list, 101);
    }

    private void setupPermissions() {
        int permissions = ContextCompat.checkSelfPermission(Driver1.this, Manifest.permission.CAMERA);
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                Driver1.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btCollectorScanEnable.setText("Включить");
                        if (getNewOrder == null) {
                            Toast.makeText(Driver1.this, "Выберите сперва заказ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // if (count < getNewOrder.getBalloonCount()) {
                        CheckBalloonRequest checkBalloonRequest = new CheckBalloonRequest();
                        checkBalloonRequest.setLocationCoordinates("Не удалось определить");
                        checkBalloonRequest.setLocationAddress("Не удалось определить");
                        Call<Void> callCreateReturnBalloon = ApiClient
                                .getUserService()
                                .checkOrderBalloon(api, getNewOrder.getId(), result.getText(), checkBalloonRequest);
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

            if (getNewOrder.getGasTypeName() == null) {
                Toast.makeText(Driver1.this, "Выберите сперва заказ", Toast.LENGTH_SHORT).show();
                return;
            }

            editor.putString(DRIVER_ID_ORDER, getNewOrder.getId());
            editor.putString(DRIVER_COUNT_ORDER_BALLOON, getNewOrder.getBalloonCount() + "");
            editor.putString(DRIVER_SIZE_ARR_ORDER, getAllGottenBalloons.size()+"");
            PutObject.save(sharedPreferences, getNewOrder, GET_NEW_ORDER);
            editor.apply();

            if (getNewOrder.isReturned()) {
                Call<ArrayList<GetReturningsBody>> getCreateReturn = ApiClient
                        .getUserService()
                        .getCreateReturn(api, getNewOrder.getId());
                getCreateReturn.enqueue(getCallBackBody(new ICallBack<ArrayList<GetReturningsBody>>() {
                    @Override
                    public void good(Response<ArrayList<GetReturningsBody>> response) {
                        assert response.body() != null;
                        PutObject.save(sharedPreferences, response.body().get(0), OBJECT_DRIVER1);

                        Intent intent = new Intent(Driver1.this, Driver2.class);
                        startActivity(intent);
                    }

                    @Override
                    public void bad(Response<ArrayList<GetReturningsBody>> response) {

                    }

                    @Override
                    public void failed() {

                    }
                }, pbLoadCollector));

                return;
            }
            if (getNewOrder.getBalloonCount() != getAllGottenBalloons.size()) {
                WarningDialog warningDialog = new WarningDialog(Driver1.this);
                warningDialog.create("Если Вы продолжите, то не отсканированные баллоны будут утеряны. Вы точно этого хотите?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        editor.putString(DRIVER_ID_ORDER, getNewOrder.getId());


                        NextDialog nextDialog = new NextDialog(Driver1.this);
                        nextDialog.create("Есть ли баллоны, которые нужно вернуть?", (dialogInterface1, i1) -> {
                            NumDialog numDialog = new NumDialog(Driver1.this);
                            numDialog.show(getSupportFragmentManager(), "Example");
//                Intent intent = new Intent(Driver1.this, Driver2.class);
//                intent.putExtra("getNewOrder", getNewOrder);
//                startActivity(intent);
                        }, (dialogInterface2, i2) -> {
                            WarningDialog warningDialog = new WarningDialog(Driver1.this);
                            warningDialog.create("Точно ли нет баллонов на возврат?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Driver1.this, ResultDriver.class);
                                    intent.putExtra("flag", "Driver1");
                                    startActivity(intent);
                                }
                            }).show();
                        }).show();
                    }
                }).show();
            } else {

                NextDialog nextDialog = new NextDialog(Driver1.this);
                nextDialog.create("Есть ли баллоны, которые нужно вернуть?", (dialogInterface1, i1) -> {
                    NumDialog numDialog = new NumDialog(Driver1.this);
                    numDialog.show(getSupportFragmentManager(), "Example");
//                Intent intent = new Intent(Driver1.this, Driver2.class);
//                intent.putExtra("getNewOrder", getNewOrder);
//                startActivity(intent);
                }, (dialogInterface2, i2) -> {
                    WarningDialog warningDialog = new WarningDialog(Driver1.this);
                    warningDialog.create("Точно ли нет баллонов на возврат?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Driver1.this, ResultDriver.class);
                            intent.putExtra("flag", "Driver1");
                            startActivity(intent);
                        }
                    }).show();
                }).show();
            }


        };
    }

    // Click item RV return balloons

    private RvCollectorsBalloonsAdapter.IClickReturnBalloon onClickRvReturnBalloon() {
        return position -> {
            String message = getResources().getString(R.string.text_message_dialog);
            AlertDialog warningDialog = new WarningDialog(Driver1.this).create(message, (dialogInterface, i) -> {
                getReturnOrder = (GetAllGottenBalloons) getAllGottenBalloons.get(position);
                Call<Void> callDeleteReturnBalloon = ApiClient
                        .getUserService()
                        .deleteReturnedBalloon(api, getReturnOrder.getBalloonId());
                pbLoadCollector.setVisibility(View.VISIBLE);
                try {
                    if (callDeleteReturnBalloon.execute().isSuccessful()) {
                        updateListViewCollectCollector();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                callDeleteReturnBalloon.enqueue(getCallBackBody(new ICallBack<Void>() {
//                    @Override
//                    public void good(Response<Void> response) {
//                        updateListViewCollectCollector();
//                    }
//
//                    @Override
//                    public void bad(Response<Void> response) { }
//
//                    @Override
//                    public void failed() { }
//                }, pbLoadCollector));
            });
            warningDialog.show();

        };
    }

    //

    private View.OnClickListener onClickDialogBtReload() {
        return view -> {
            Call<ArrayList<PidorasObj1>> recallNewOrder = ApiClient
                    .getUserService()
                    .getCollectedOrder(api);

            dialogProgressBar.setVisibility(View.VISIBLE);
            dialogBtReload.setEnabled(false);
            recallNewOrder.enqueue(getCallBackBody(new ICallBack() {
                @Override
                public void good(Response response) {
                    onNewOrderClickListener = onClickNewOrder();
                    newOrderAdapter = new NewOrderAdapter(Driver1.this, (List<PidorasObj1>) response.body(), onNewOrderClickListener);
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
                    .getCollectedOrder(api);
            callNewOrder.enqueue(new Callback<ArrayList<PidorasObj1>>() {
                @Override
                public void onResponse(Call<ArrayList<PidorasObj1>> call, Response<ArrayList<PidorasObj1>> response) {
                    if (response.isSuccessful()) {

                        ArrayList<PidorasObj1> newOrders = (ArrayList<PidorasObj1>) response.body();

                        onNewOrderClickListener = onClickNewOrder();
                        newOrderAdapter = new NewOrderAdapter(Driver1.this, newOrders, onNewOrderClickListener);
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

    //

    // New Order Adapter
    private NewOrderAdapter.OnNewOrderClickListener onClickNewOrder() {
        return (order, position, holder) -> {
            NewOrderAdapter.rowIndex = position;
            getNewOrder = order;


            newOrderAdapter.setGetCreateOrdersResponse(order);
            tvCollectorNewOrder.setText(getNewOrder.getCounterpartyName() + "\n" +getNewOrder.getDriverName());

            OrderAdapter.rowIndex = -1;

            int to = getNewOrder.getBalloonCount();

            tvCollectorCount.setText(String.format(getResources().getString(R.string.text_count_balloons), 0, to));
           // pbLoadCollector.setVisibility(View.VISIBLE);
            updateListViewCollectCollector();
            dialog.dismiss();
        };
    }

    //

    // Void's
    private void createDialog() {
        dialog = new Dialog(Driver1.this);
        dialog.setContentView(R.layout.dialog_driver_order);

        dialog.getWindow().setLayout(900, 900);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        dialogSearchView = dialog.findViewById(R.id.et_search_driver_order);
        dialogBtReload = dialog.findViewById(R.id.bt_reload_driver_order);
        dialogProgressBar = dialog.findViewById(R.id.pb_driver_order);
        dialogRecyclerView = dialog.findViewById(R.id.rv_driver_order);

        buttonReload = new ButtonReload(Driver1.this, dialogBtReload, dialogProgressBar);

        dialogBtReload.setOnClickListener(onClickDialogBtReload());

        dialog.show();
    }

//    private void deleteDataCollect() {
//        getNewOrder = new GetCreateOrdersResponse();
//
//        getAllGottenBalloons.clear();
//        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter2(Driver1.this, getAllGottenBalloons);
//        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);
//
//        tvCollectorCount.setText("");
//        tvCollectorNewOrder.setText("");
//        tvCollectorResultTxt.setText("Результат");
//        mCodeScanner.releaseResources();
//    }



    private void reloadDataRV(ArrayList<Balloon> arrayList) {
        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter2(Driver1.this, arrayList);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);
        //rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());

    }

    private void delete() {
        getAllGottenBalloons.clear();
        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter2(Driver1.this, getAllGottenBalloons);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

        getNewOrder = new PidorasObj1();
        tvCollectorNewOrder.setText("");
        tvCollectorCount.setText("");

        editor.putString(DRIVER_ID_ORDER, "");
        editor.putString(DRIVER_COUNT_ORDER_BALLOON, "");
        editor.putString(DRIVER_SIZE_ARR_ORDER, "");

        editor.putString(DRIVER_ID_RETURN_ORDER, "");
        editor.putString(DRIVER_COUNT_RETURN_BALLOON, "");
        editor.putString(DRIVER_SIZE_ARR_RETURN_ORDER, "");

        PutObject.save(sharedPreferences, new GetReturningsBody(), OBJECT_DRIVER1);
        editor.apply();
    }

    private void updateListViewCollectCollector() {
        Call<ArrayList<GetAllGottenBalloons>> callUpdate = ApiClient
                .getUserService()
                .getAllGottenBalloons2(api, getNewOrder.getId());
        pbLoadCollector.setVisibility(View.VISIBLE);
        callUpdate.enqueue(getCallBackBody(new ICallBack<ArrayList<GetAllGottenBalloons>>() {
            @Override
            public void good(Response response) {
                int from = 0;
                int to = 0;

                getAllGottenBalloons = (ArrayList<Balloon>) ((ArrayList<Balloon>) response.body()).stream().filter(i->i.getIBalloonIsCheck()).collect(Collectors.toList());
                reloadDataRV(getAllGottenBalloons);

                from = getAllGottenBalloons.size();
                to = getNewOrder.getBalloonCount();

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



    private void initSpinner() {
        ConstraintLayout constraintLayout;

        TextInputLayout etNameBalloon = findViewById(R.id.et_name_balloon_3);
        TextInputEditText etNameBalloonTxt = findViewById(R.id.et_name_balloon_3_txt);

        showKeyBoard(etNameBalloonTxt);
        etNameBalloonTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_GO) {
                    checkByName(etNameBalloonTxt);
                    hideKeyboard(Driver1.this);
                }
                return false;
            }
        });

        etNameBalloon.setEndIconOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                checkByName(etNameBalloonTxt);
                hideKeyboard(Driver1.this);
            }
        });

        Spinner spinner = findViewById(R.id.spinner_state_3);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(Driver1.this, R.array.spinner_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        constraintLayout = findViewById(R.id.constraint_3);

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
        InputMethodManager manager = (InputMethodManager) Driver1.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                set.clear(R.id.spinner_state_3, ConstraintSet.TOP);
                set.connect(R.id.spinner_state_3, ConstraintSet.TOP, R.id.space, ConstraintSet.BOTTOM);
                break;
            case 1:
                set.clear(R.id.spinner_state_3, ConstraintSet.TOP);
                set.connect(R.id.spinner_state_3, ConstraintSet.TOP, R.id.constraint_3, ConstraintSet.TOP);
                break;
            default:break;
        }

    }

    private void checkByName(TextInputEditText textInputEditText) {
        if (getNewOrder == null) {
            Toast.makeText(Driver1.this, "Выберите сперва заказ", Toast.LENGTH_SHORT).show();
            return;
        }
        CheckBalloonRequest checkBalloonRequest = new CheckBalloonRequest();
        checkBalloonRequest.setLocationAddress("Не удалось определить");
        checkBalloonRequest.setLocationCoordinates("Не удалось определить");

        Call<Void> checkBalloonByNameDriver = ApiClient
                .getUserService()
                .checkBalloonByNameDriver(api, getNewOrder.getId(), textInputEditText.getText().toString(), checkBalloonRequest);
        checkBalloonByNameDriver.enqueue(getCallBackBody(new ICallBack<Void>() {
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

    @Override
    protected void onRestart() {
        super.onRestart();
        delete();
    }
}