package com.automat.manager.activities.driver;

import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.NO_PERMISSION;
import static com.automat.manager.global.GlobalValue.PREF_NAME;
import static com.automat.manager.global.GlobalValue.TEXT_STATUS_QR_ITEM_1;
import static com.automat.manager.global.GlobalValue.TEXT_STATUS_QR_ITEM_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automat.manager.R;
import com.automat.manager.activities.adapters.AdapterOrders;
import com.automat.manager.activities.adapters.RecyclerItem;
import com.automat.manager.activities.adapters.StatusQRAdapter;
import com.automat.manager.activities.logistician.Logistician;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.classes.CustomDialog;
import com.automat.manager.classes.MainRequest;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.classes.Validator;
import com.automat.manager.databinding.ActivityDriverBinding;
import com.automat.manager.interfaces.IBodyRetrofit;
import com.automat.manager.interfaces.IRetrofitArray;
import com.automat.manager.interfaces.IRetrofitNotArray;
import com.automat.manager.requests.CreateReturnBalloonRequest;
import com.automat.manager.responses.GetBalloonById;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetCreateReturnBalloonResponse;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;


public class Driver extends AppCompatActivity {
    // Panel status QR
    RecyclerView rvQRStatus;
//    TextView textView;
    String strStatusQR;
    ArrayList<String> itemsQRStatus;
    RecyclerView.LayoutManager RecyclerViewQRStatusLayoutManager;
    StatusQRAdapter statusQRAdapter;
    LinearLayoutManager horizontalQRStatusLayout;
    CustomDialog infoDialog;
    Dialog dialogTest;

    // dialog info
    Dialog dialogInfo;
    Button btDialogInfoReload;
    ProgressBar pbDialogInfo;
    TextView tvResultScanQrBalloon, tvDriverOrder;

    // dialog driver order
    Dialog dialogDriverOrder;
    ButtonReload btReload;
    SearchView etSearchDriverOrder;
    ProgressBar pbDriverOrder;
    RecyclerView rvDriverOrder;
    ArrayList<GetCreateOrdersResponse> orders = new ArrayList<>();
    GetCreateOrdersResponse changeOrder;
    OrderAdapter adapterOrder;
    OrderAdapter.OnOrderClickListener orderClickListener;

    private boolean isClickBtEnableScanDriver = false;

    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private AdapterOrders adapter;
    private List<RecyclerItem> listItems;

    private ButtonLogout buttonLogout;
    private ActivityDriverBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String api;

    private TextView sureName;
    Button btExit, btEnableScanDriver, btSendOrderButton, btReloadDriverOrder;
    ProgressBar pbLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_logged);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);

        init();
    }

    private void init() {
        dialogInfo = new Dialog(Driver.this);
        dialogInfo.setContentView(R.layout.dialog_info);
        tvResultScanQrBalloon = dialogInfo.findViewById(R.id.tv_result_scan_qr_balloon);
        btDialogInfoReload = dialogInfo.findViewById(R.id.bt_dialog_info_reload);
        pbDialogInfo = dialogInfo.findViewById(R.id.pb_dialog_info);

        dialogDriverOrder = new Dialog(Driver.this);
        dialogDriverOrder.setContentView(R.layout.dialog_driver_order);
        dialogDriverOrder.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
        etSearchDriverOrder = dialogDriverOrder.findViewById(R.id.et_search_driver_order);
        pbDriverOrder = dialogDriverOrder.findViewById(R.id.pb_driver_order);
        btReloadDriverOrder = dialogDriverOrder.findViewById(R.id.bt_reload_driver_order);
        rvDriverOrder = dialogDriverOrder.findViewById(R.id.rv_driver_order);
        btReload = new ButtonReload(Driver.this, btReloadDriverOrder, pbDriverOrder);


        tvDriverOrder = findViewById(R.id.tv_driver_order);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        api = sharedPreferences.getString(KEY_AUTH, "");
        Toast.makeText(this, api, Toast.LENGTH_SHORT).show();
        pbLogged = findViewById(R.id.pb_logged);
        //sureName = findViewById(R.id.text_home);
        btExit = findViewById(R.id.bt_exit);
        btSendOrderButton = findViewById(R.id.send_order_button);
        btEnableScanDriver = findViewById(R.id.bt_enable_scan_driver);
        buttonLogout = new ButtonLogout(Driver.this, btExit);
        buttonLogout.setExit();

        scannerView = findViewById(R.id.scanner_order);
        mCodeScanner = new CodeScanner(this, scannerView);
        // Parameters (default values)
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK); // or CAMERA_FRONT or specific camera id
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS); // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE); // or CONTINUOUS
        mCodeScanner.setScanMode(ScanMode.SINGLE); // or CONTINUOUS or PREVIEW
        mCodeScanner.setAutoFocusEnabled(true); // Whether to enable auto focus or not
        mCodeScanner.setFlashEnabled(false);
        setupPermissions();
        setAdapter();
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            tvResultScanQrBalloon.setText("");
            //infoDialog.setTvResultScanQRBalloon("");
            ButtonReload buttonReload = new ButtonReload(Driver.this, btDialogInfoReload, pbDialogInfo);
            btDialogInfoReload.setOnClickListener(buttonReload.reload(new GetBalloonById(), new IRetrofitNotArray() {
                @Override
                public <T> void success(Response<T> response) {
                    tvResultScanQrBalloon.setText(getBalloonByIdInfo(response));
                }

                @Override
                public <T> void bad(Response<T> response) { }

                @Override
                public <T> void failed(@NonNull Call<T> call, @NonNull Throwable t) { }
            }, api, result.getText()));

            Call<GetBalloonById> call = ApiClient
                    .getUserService()
                    .getBalloonById(api, result.getText());
            pbDialogInfo.setVisibility(View.VISIBLE);
            MainRequest infoBalloonRequest = new MainRequest(Driver.this, call, new IRetrofitNotArray() {
                @Override
                public <T> void success(Response<T> response) {
                    btDialogInfoReload.setVisibility(View.GONE);
                    assert response.body() != null;

                    tvResultScanQrBalloon.setText(getBalloonByIdInfo(response));
                }

                @Override
                public <T> void bad(Response<T> response) {
                    btDialogInfoReload.setVisibility(View.VISIBLE);
                }
                public <T> void failed(@NonNull retrofit2.Call<T> call, @NonNull Throwable t) {
                    Toast.makeText(Driver.this, "SIUKA", Toast.LENGTH_SHORT).show();
                    btDialogInfoReload.setVisibility(View.VISIBLE);
                }
            }, pbDialogInfo);



            // createReturnBalloonRequest
            CreateReturnBalloonRequest createReturnBalloonRequest = new CreateReturnBalloonRequest();
            createReturnBalloonRequest.setBalloonId(result.getText());
            Call<GetCreateReturnBalloonResponse> call1 = ApiClient
                    .getUserService()
                    .createReturnBalloon(api, createReturnBalloonRequest, "");
            MainRequest createReturnBalloon = new MainRequest(Driver.this, call1, new IRetrofitNotArray() {
                @Override
                public <T> void success(Response<T> response) {

                }

                @Override
                public <T> void bad(Response<T> response) {

                }

                @Override
                public <T> void failed(@NonNull Call<T> call, @NonNull Throwable t) {

                }
            }, pbLogged);

            btEnableScanDriver.setText("Включить");
            isClickBtEnableScanDriver = false;
            switch (strStatusQR) {
                case TEXT_STATUS_QR_ITEM_1:
                    dialogDriverOrder.show();
                break;
                case TEXT_STATUS_QR_ITEM_2:
                    dialogInfo.show();
                    infoBalloonRequest.sendRequest();
                break;
                default: break;
            }
        }));
        scannerView.setOnClickListener(view -> {
            mCodeScanner.startPreview();
            btEnableScanDriver.setText("Выключить");
            isClickBtEnableScanDriver = true;
        });
        btEnableScanDriver.setOnClickListener(onClickBtEnableScanDriver());
        initStatusQRPanel();
        tvDriverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Order call driver
                dialogDriverOrder.show();
                Call<ArrayList<GetCreateOrdersResponse>> callOrders = ApiClient
                        .getUserService()
                        .getOrders(api);
                MainRequest mainRequestDriverOrders = new MainRequest(Driver.this, callOrders, new IRetrofitArray() {
                    @Override
                    public <T> void success(Response<ArrayList<T>> response) {
                        if (response.body() == null) return;
                        btReloadDriverOrder.setVisibility(View.GONE);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            orders = (ArrayList<GetCreateOrdersResponse>) ((ArrayList<GetCreateOrdersResponse>) response.body()).stream().filter(
//                                    i -> !i.isСollected())
//                                    .collect(Collectors.toList());
//                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            orders = (ArrayList<GetCreateOrdersResponse>) ((ArrayList<GetCreateOrdersResponse>) response.body()).stream().filter(
                                    i -> !i.getCarId().equals("00000000-0000-0000-0000-000000000000") ||
                                            !i.getDriverId().equals("00000000-0000-0000-0000-000000000000"))
                                    .collect(Collectors.toList());
                        }
                        etSearchDriverOrder.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                adapterOrder.getFilter().filter(s);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {

                                adapterOrder.getFilter().filter(s);
                                return false;
                            }
                        });
                        orderClickListener = onClickOrder();
                        adapterOrder = new OrderAdapter(Driver.this, orders, orderClickListener);
                        rvDriverOrder.setAdapter(adapterOrder);
                    }

                    @Override
                    public <T> void bad(Response<ArrayList<T>> response) {
                        btReloadDriverOrder.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public <T> void failed(@NonNull Call<ArrayList<T>> call, @NonNull Throwable t) {
                        btReloadDriverOrder.setVisibility(View.VISIBLE);
                    }
                }, pbDriverOrder, btReloadDriverOrder);
                btReloadDriverOrder.setOnClickListener(onClickReloadDriverOrderBt());
                mainRequestDriverOrders.sendRequestWithArray();
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private OrderAdapter.OnOrderClickListener onClickOrder() {
        return (order, position, holder) -> {
            OrderAdapter.rowIndex = position;
            changeOrder = order;
            adapterOrder.setGetCreateOrdersResponse(order);
            tvDriverOrder.setText(changeOrder.getCreationDate() + " " + changeOrder.getDriverName());

            OrderAdapter.rowIndex = -1;
            dialogDriverOrder.dismiss();
        };
    }
    private void clearAllData() {
        changeOrder = new GetCreateOrdersResponse();
        OrderAdapter.rowIndex = -1;
    }
    private void initStatusQRPanel() {


        infoDialog = new CustomDialog(Driver.this);
        rvQRStatus = findViewById(R.id.rv_status_qr_items);

        RecyclerViewQRStatusLayoutManager = new LinearLayoutManager(getApplicationContext());

        rvQRStatus.setLayoutManager(RecyclerViewQRStatusLayoutManager);

        addItemsToArrayList();

        //textView.setText(itemsQRStatus.get(StatusQRAdapter.rowIndex));
        strStatusQR = itemsQRStatus.get(StatusQRAdapter.rowIndex);
        statusQRAdapter = new StatusQRAdapter(itemsQRStatus, new StatusQRAdapter.IStatusQRAdapter() {
            @Override
            public void getData(String name) {
                strStatusQR = name;
            }
        });

        horizontalQRStatusLayout = new LinearLayoutManager(Driver.this,
                LinearLayoutManager.HORIZONTAL, false);
        rvQRStatus.setLayoutManager(horizontalQRStatusLayout);

        rvQRStatus.setAdapter(statusQRAdapter);
    }

    private String getBalloonByIdInfo(Response response) {
        if (response == null) return "";
        GetBalloonById balloon = ((GetBalloonById) response.body());
        return String.format("Id: %s\n" +
                "Дата создания: %s\n" +
                "Дата варификации: %s\n" +
                "Дата мануфактуры: %s\n" +
                "Наименование: %s\n" +
                "Напечатан: " + (balloon.isPrinted() ? "Да" : "Нет") + "\n" +
                "Свободен: " + (balloon.isFree() ? "Да" : "Нет") + "\n" +
                "Утерян: " + (balloon.isLost() ? "Да" : "Нет") + "\n" +
                "Удалён: " + (balloon.isRemove() ? "Да" : "Нет") + "\n", balloon.getId(), balloon.getCreationDate(),
                balloon.getVerificationDate(), balloon.getManufactureDate(),
                balloon.getName());
    }

    private View.OnClickListener onClickBtEnableScanDriver() {
        return view -> {
            Button button = (Button) view;
            isClickBtEnableScanDriver = !isClickBtEnableScanDriver;
            button.setText(isClickBtEnableScanDriver ? "Выключить" : "Включить");
            if (isClickBtEnableScanDriver) mCodeScanner.startPreview();
            else mCodeScanner.stopPreview();
        };
    }

    public void addItemsToArrayList() {
        itemsQRStatus = new ArrayList<>();
        itemsQRStatus.add(getString(R.string.text_status_qr_item_1));
        itemsQRStatus.add(getString(R.string.text_status_qr_item_2));
    }

    private void setupPermissions() {
        int permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        String[] list = {Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, list, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                isEmpty(grantResults);
        }
    }

    private void setAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        //Set adapter
        adapter = new AdapterOrders(listItems, this);
        recyclerView.setAdapter(adapter);
    }

    private void isEmpty(int[] grantResults) {
        if (Validator.isEmpty(grantResults) || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, NO_PERMISSION, Toast.LENGTH_SHORT).show();
        } else {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(Driver.this, "start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }

    private View.OnClickListener onClickReloadDriverOrderBt() {
        return btReload.reload(new GetCreateOrdersResponse(), new IBodyRetrofit() {
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

    private void reloadRV(Response response) {
        if (response.body() == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            orders = (ArrayList<GetCreateOrdersResponse>) ((ArrayList<GetCreateOrdersResponse>) response.body()).stream().filter(
                    i -> !i.isСollected())
                    .collect(Collectors.toList());
        }
        orderClickListener = onClickOrder();
        adapterOrder = new OrderAdapter(Driver.this, orders, orderClickListener);
        rvDriverOrder.setAdapter(adapterOrder);
    }
}