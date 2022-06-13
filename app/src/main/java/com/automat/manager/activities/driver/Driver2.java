package com.automat.manager.activities.driver;

import static com.automat.manager.classes.ApiClient.getCallBackBody;
import static com.automat.manager.global.GlobalValue.DRIVER_COUNT_RETURN_BALLOON;
import static com.automat.manager.global.GlobalValue.DRIVER_ID_RETURN_ORDER;
import static com.automat.manager.global.GlobalValue.DRIVER_SIZE_ARR_RETURN_ORDER;
import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.OBJECT_DRIVER1;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

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
import android.widget.EditText;
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
import com.automat.manager.classes.MyThread;
import com.automat.manager.classes.NextDialog;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.classes.PutObject;
import com.automat.manager.classes.WarningDialog;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.requests.CheckBalloonRequest;
import com.automat.manager.requests.CreateReturnBalloonByNameDriverRequest;
import com.automat.manager.requests.CreateReturnRequest;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetReturningsBody;
import com.automat.manager.responses.PidorasObj1;
import com.automat.manager.responses.ReturnBalloonResponse;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Driver2 extends AppCompatActivity {
    GetCreateOrdersResponse returnOrder;
    ImageButton back;

    GetReturningsBody returnObjectDriver1;

    private boolean isScannerEnable = false;
    private String api;
    private String idReturnOrder;
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
    TextView tvCollectorNewOrder;
    TextView tvCollectorResultTxt, tvCollectorCount;
    RecyclerView rvCollectorBalloons;
    Button btCollectorCollected;
    Button btReloadDiver2;

    ProgressBar pbLoadCollector;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver2);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
        initSpinner();
    }

    private void init() {
        ImageButton back = findViewById(R.id.back_to_drive1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarningDialog warningDialog = new WarningDialog(Driver2.this);
                warningDialog.create("Все изменения будут стёрты! Вы точно хотите выйти?",
                        (dialogInterface, i) -> onBackPressed()).show();
            }
        });

        btCollectorScanEnable = findViewById(R.id.bt_collector_scan_enable5);
        tvCollectorResultTxt = findViewById(R.id.tv_collector_result_txt5);
        collectorScannerView = findViewById(R.id.collector_scanner5);
        btReloadDiver2 = findViewById(R.id.bt_reload_driver2);
        mCodeScanner = new CodeScanner(Driver2.this, collectorScannerView);

        sharedPreferences =  Driver2.this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api = sharedPreferences.getString(KEY_AUTH, "");
        returnObjectDriver1 = PutObject.retrieve(sharedPreferences, OBJECT_DRIVER1, GetReturningsBody.class);

        idReturnOrder = sharedPreferences.getString(DRIVER_ID_RETURN_ORDER, "");



        tvCollectorCount = findViewById(R.id.tv_collector_count5);
//        tvCollectorNewOrder.setOnClickListener(onClickTvCollectorNewOrder());
        rvCollectorBalloons = findViewById(R.id.rv_collector_balloons5);
        btCollectorCollected = findViewById(R.id.bt_collector_collected5);
        pbLoadCollector = findViewById(R.id.pb_load_collector5);

        arrayList = new ArrayList<>();
        getAllGottenBalloons = new ArrayList<>();

        rvLayoutManagerReturnBalloon = new LinearLayoutManager(Driver2.this,
                LinearLayoutManager.VERTICAL, false);
        rvCollectorBalloons.setLayoutManager(rvLayoutManagerReturnBalloon);
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);

        collectorScannerView.setOnClickListener(onClickCollectorScannerView());
        btCollectorScanEnable.setOnClickListener(onClickBtCollectorScanEnable() );

        btCollectorCollected.setOnClickListener(onClickBtCollectorCollected());
        btReloadDiver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateListViewCollectCollector();
            }
        });

     //   if (!sharedPreferences.getString(DRIVER_SIZE_ARR_RETURN_ORDER, "").equals("") && !sharedPreferences.getString(DRIVER_COUNT_RETURN_BALLOON, "").equals(""))
//        tvCollectorNewOrder.setText("Отсканированно " + sharedPreferences.getString(DRIVER_SIZE_ARR_RETURN_ORDER, "") + " / " + sharedPreferences.getString(DRIVER_COUNT_RETURN_BALLOON, ""));
    //    else tvCollectorNewOrder.setText("Отсканированные баллоны");
        createCodeScanner();

        updateListViewCollectCollector();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateListViewCollectCollector();
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
        ActivityCompat.requestPermissions(Driver2.this, list, 101);
    }

    private void setupPermissions() {
        int permissions = ContextCompat.checkSelfPermission(Driver2.this, Manifest.permission.CAMERA);
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                Driver2.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btCollectorScanEnable.setText("Включить");

                        ReturnBalloonResponse returnBalloonResponse = new ReturnBalloonResponse();
                        returnBalloonResponse.setBalloonId(result.getText());

                        rvCollectorsBalloonsAdapter.setIClickReturnBalloon(new RvCollectorsBalloonsAdapter.IClickReturnBalloon() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });

                        Call<GetCreateOrdersResponse> callCreateReturnBalloon = ApiClient
                                .getUserService()
                                .createReturnBalloon1(api, returnObjectDriver1.getId(), returnBalloonResponse);

                        MyThread myThread = new MyThread(Driver2.this, Driver2.this);
                        Thread thread = new Thread(myThread.createRunnable(callCreateReturnBalloon, new MyThread.State() {
                            @Override
                            public void good() {
                                rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
                                updateListViewCollectCollector();
                            }
                        }, R.id.pb_load_collector5));
                        pbLoadCollector.setVisibility(View.VISIBLE);
                        thread.start();
//                        callCreateReturnBalloon.enqueue(getCallBackBody(new ICallBack<GetCreateOrdersResponse>() {
//                            @Override
//                            public void good(Response<GetCreateOrdersResponse> response) {
//                                rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
//                                updateListViewCollectCollector();
//
//                            }
//
//                            @Override
//                            public void bad(Response<GetCreateOrdersResponse> response) {
//                                rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
//                            }
//
//                            @Override
//                            public void failed() {
//                                rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
//                                updateListViewCollectCollector();
//                            }
//                        }, pbLoadCollector));
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
            if (!sharedPreferences.getString(DRIVER_SIZE_ARR_RETURN_ORDER, "").equals(sharedPreferences.getString(DRIVER_COUNT_RETURN_BALLOON, ""))){
                WarningDialog warningDialog = new WarningDialog(Driver2.this);
                warningDialog.create("Если Вы продолжите, то не отсканированные баллоны будут утеряны. Вы точно этого хотите?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Driver2.this, ResultDriver.class);
                        intent.putExtra("flag", "Driver2");
//                editor.putString(DRIVER_SIZE_ARR_RETURN_ORDER, getAllGottenBalloons.size() + "");
//                editor.apply();
                        startActivity(intent);
                    }
                }).show();
                return;
            }
            Intent intent = new Intent(Driver2.this, ResultDriver.class);
            intent.putExtra("flag", "Driver2");
            startActivity(intent);

//            CreateReturnRequest createReturnRequest = new CreateReturnRequest();
//            createReturnRequest.setBalloonCount(tvCollectorNewOrder.getText().toString());
//            Call<GetCreateOrdersResponse> callSetCompleteOrder = ApiClient
//                    .getUserService()
//                    .createReturn(api, getNewOrder.getId(), createReturnRequest);
//
//            pbLoadCollector.setVisibility(View.VISIBLE);
//            callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack<GetCreateOrdersResponse>() {
//                @Override
//                public void good(Response response) {
//                    deleteDataCollect();
//                    returnOrder = (GetCreateOrdersResponse) response.body();
//                }
//
//                @Override
//                public void bad() {
//
//                }
//
//                @Override
//                public void failed() {
//
//                }
//            }, pbLoadCollector));
        };
    }

    // Click item RV return balloons

    private RvCollectorsBalloonsAdapter.IClickReturnBalloon onClickRvReturnBalloon() {

        return position -> {
            String message = getResources().getString(R.string.text_message_dialog);
            AlertDialog warningDialog = new WarningDialog(Driver2.this).create(message, (dialogInterface, i) -> {
                getReturnOrder = (GetAllGottenBalloons) getAllGottenBalloons.get(position);
                Call<Void> callDeleteReturnBalloon = ApiClient
                        .getUserService()
                        .deleteReturnBalloonDriver(api, getReturnOrder.getId());

                MyThread myThread = new MyThread(Driver2.this, this);
                Thread thread = new Thread(myThread.createRunnable(callDeleteReturnBalloon, this::updateListViewCollectCollector, R.id.pb_load_collector5));

//                Thread mThread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final ProgressBar progressBar = findViewById(R.id.pb_load_collector5);
//                        try {
//                            Response response = callDeleteReturnBalloon.execute();
//                            if (response.isSuccessful()) {
//                                updateListViewCollectCollector();
//                            } else {
//                                try {
//                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                    Toast.makeText(Driver2.this, jObjError.getString("detail"), Toast.LENGTH_SHORT).show();
//                                } catch (IOException | JSONException e) {
//                                    Toast.makeText(Driver2.this, response.code() == 404 ? "Не найдено" : response.code() + "", Toast.LENGTH_LONG).show();
//                                }
//                                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
//                            }
//                        } catch (IOException e) {
//
//                            runOnUiThread(() -> {
//                                final Toast toast = Toast.makeText(Driver2.this, "Превышен лимит ожидания", Toast.LENGTH_SHORT);
//                                toast.show();
//                                progressBar.setVisibility(View.GONE);
//                            });
//
//                        }
//                    }
//                });
                pbLoadCollector.setVisibility(View.VISIBLE);
                thread.start();
//                mThread.start();

                System.out.println("111111111111111");
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
                    newOrderAdapter = new NewOrderAdapter(Driver2.this, (List<PidorasObj1>) response.body(), onNewOrderClickListener);
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
//    private View.OnClickListener onClickTvCollectorNewOrder() {
//        return view -> {
//            createDialog();
//            dialogProgressBar.setVisibility(View.VISIBLE);
//            Call<ArrayList<GetCreateOrdersResponse>> callNewOrder = ApiClient
//                    .getUserService()
//                    .getCollectedOrder(api);
//            callNewOrder.enqueue(new Callback<ArrayList<GetCreateOrdersResponse>>() {
//                @Override
//                public void onResponse(Call<ArrayList<GetCreateOrdersResponse>> call, Response<ArrayList<GetCreateOrdersResponse>> response) {
//                    if (response.isSuccessful()) {
//
//                        ArrayList<GetCreateOrdersResponse> newOrders = (ArrayList<GetCreateOrdersResponse>) response.body();
//
//                        onNewOrderClickListener = onClickNewOrder();
//                        newOrderAdapter = new NewOrderAdapter(Driver2.this, newOrders, onNewOrderClickListener);
//                        dialogRecyclerView.setAdapter(newOrderAdapter);
//
//                        dialogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                            @Override
//                            public boolean onQueryTextSubmit(String s) {
//                                newOrderAdapter.getFilter().filter(s);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onQueryTextChange(String s) {
//                                newOrderAdapter.getFilter().filter(s);
//                                return false;
//                            }
//                        });
//                        dialogBtReload.setVisibility(View.GONE);
//                    } else {
//                        dialogBtReload.setVisibility(View.VISIBLE);
//                    }
//                    dialogProgressBar.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<GetCreateOrdersResponse>> call, Throwable t) {
//                    dialogBtReload.setVisibility(View.VISIBLE);
//                    dialogProgressBar.setVisibility(View.GONE);
//                }
//            });
//        };
//    }

    //

    // New Order Adapter
    private NewOrderAdapter.OnNewOrderClickListener onClickNewOrder() {
        return (order, position, holder) -> {
            NewOrderAdapter.rowIndex = position;
            getNewOrder = order;
            newOrderAdapter.setGetCreateOrdersResponse(order);
           // tvCollectorNewOrder.setText(getNewOrder.getCreationDate() + " " + getNewOrder.getDriverName());

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
        dialog = new Dialog(Driver2.this);
        dialog.setContentView(R.layout.dialog_driver_order);

        dialog.getWindow().setLayout(700, 700);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        dialogSearchView = dialog.findViewById(R.id.et_search_driver_order);
        dialogBtReload = dialog.findViewById(R.id.bt_reload_driver_order);
        dialogProgressBar = dialog.findViewById(R.id.pb_driver_order);
        dialogRecyclerView = dialog.findViewById(R.id.rv_driver_order);

        buttonReload = new ButtonReload(Driver2.this, dialogBtReload, dialogProgressBar);

        dialogBtReload.setOnClickListener(onClickDialogBtReload());

        dialog.show();
    }





    private void reloadDataRV(ArrayList<Balloon> arrayList) {
        rvCollectorsBalloonsAdapter = new RvCollectorsBalloonsAdapter(Driver2.this, arrayList);
        System.out.println("3333333333333");
        editor.putString(DRIVER_SIZE_ARR_RETURN_ORDER, arrayList.size()+"");
        editor.apply();
        rvCollectorBalloons.setAdapter(rvCollectorsBalloonsAdapter);
        rvCollectorsBalloonsAdapter.setIClickReturnBalloon(onClickRvReturnBalloon());
    }

    private void updateListViewCollectCollector() {
        Call<ArrayList<GetAllGottenBalloons>> callUpdate = ApiClient
                .getUserService()
                .createReturnBalloon2(api, returnObjectDriver1.getId());
        pbLoadCollector.setVisibility(View.VISIBLE);
        btReloadDiver2.setEnabled(false);
        callUpdate.enqueue(getCallBackBody(new ICallBack<ArrayList<GetAllGottenBalloons>>() {
            @Override
            public void good(Response response) {
                int from = 0;
                int to = 0;
                btReloadDiver2.setVisibility(View.GONE);
                System.out.println("2222222222222222");
                getAllGottenBalloons = (ArrayList<Balloon>) response.body();


                reloadDataRV(getAllGottenBalloons);

                editor.putString(DRIVER_COUNT_RETURN_BALLOON, returnObjectDriver1.getBalloonCount()+"");
                editor.apply();

             //   from = getAllGottenBalloons.size();
              //  to = getNewOrder.getBalloonCount();
               // tvCollectorNewOrder.setText("Отсканированно " + sharedPreferences.getString(DRIVER_SIZE_ARR_RETURN_ORDER, "") + " / " + sharedPreferences.getString(DRIVER_COUNT_RETURN_BALLOON, ""));
                tvCollectorCount.setText(String.format(getResources().getString(R.string.text_count_balloons), getAllGottenBalloons.size(), returnObjectDriver1.getBalloonCount()));
                pbLoadCollector.setVisibility(View.GONE);
            }

            @Override
            public void bad(Response response) {
                btReloadDiver2.setVisibility(View.VISIBLE);
                btReloadDiver2.setEnabled(true);
            }

            @Override
            public void failed() {
                btReloadDiver2.setVisibility(View.VISIBLE);
                btReloadDiver2.setEnabled(true);
            }
        }, pbLoadCollector));
    }

    private void initSpinner() {
        ConstraintLayout constraintLayout;

        TextInputLayout etNameBalloon = findViewById(R.id.et_name_balloon_4);
        TextInputEditText etNameBalloonTxt = findViewById(R.id.et_name_balloon_4_txt);

        showKeyBoard(etNameBalloonTxt);
        etNameBalloonTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_GO) {
                    checkByName(etNameBalloonTxt);
                    hideKeyboard(Driver2.this);
                }
                return false;
            }
        });

        etNameBalloon.setEndIconOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                checkByName(etNameBalloonTxt);
                hideKeyboard(Driver2.this);
            }
        });

        Spinner spinner = findViewById(R.id.spinner_state_4);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(Driver2.this, R.array.spinner_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

   //     constraintLayout = findViewById(R.id.constraint_4);

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

           //             ConstraintSet set = new ConstraintSet();

                  //      set.clone(constraintLayout);

                  //      changeConstraints(set, 0);

                //        TransitionManager.beginDelayedTransition(constraintLayout);

              //          set.applyTo(constraintLayout);

                        break;
                    case 1:
                        etNameBalloon.setVisibility(View.VISIBLE);
                        btCollectorScanEnable.setVisibility(View.GONE);
                        collectorScannerView.setVisibility(View.GONE);
                        mCodeScanner.stopPreview();
//
                //        ConstraintSet set2 = new ConstraintSet();

                 //       set2.clone(constraintLayout);

                 //       changeConstraints(set2, 1);
//
                    //    TransitionManager.beginDelayedTransition(constraintLayout);

                //        set2.applyTo(constraintLayout);
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
        InputMethodManager manager = (InputMethodManager) Driver2.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                set.clear(R.id.spinner_state_4, ConstraintSet.BOTTOM);
                set.connect(R.id.spinner_state_4, ConstraintSet.BOTTOM, R.id.bt_collector_scan_enable5, ConstraintSet.TOP);
                break;
            case 1:
                set.clear(R.id.spinner_state_4, ConstraintSet.BOTTOM);
                set.connect(R.id.spinner_state_4, ConstraintSet.BOTTOM, R.id.et_name_balloon_4, ConstraintSet.TOP);
                break;
            default:break;
        }

    }

    private void checkByName(TextInputEditText textInputEditText) {

        CreateReturnBalloonByNameDriverRequest createReturnBalloonByNameDriverRequest = new CreateReturnBalloonByNameDriverRequest();
        createReturnBalloonByNameDriverRequest.setBalloonName(textInputEditText.getText().toString());

        Call<Void> checkBalloonByNameDriver = ApiClient
                .getUserService()
                .createReturnBalloonByNameDriver(api, returnObjectDriver1.getId(), createReturnBalloonByNameDriverRequest);
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
}