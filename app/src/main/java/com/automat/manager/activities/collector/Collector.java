package com.automat.manager.activities.collector;

import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.NO_PERMISSION;
import static com.automat.manager.global.GlobalValue.PREF_NAME;
import static com.automat.manager.global.GlobalValue.topics;
import static com.automat.manager.services.MyFCMService.foo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.automat.manager.R;
import com.automat.manager.activities.adapters.NewOrderAdapter;
import com.automat.manager.activities.adapters.RvCollectorsBalloonsAdapter;
import com.automat.manager.activities.adapters.StatusQRAdapter;
import com.automat.manager.activities.collector.fragments.Collector_1;
import com.automat.manager.activities.collector.fragments.Collector_2;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.Balloon;
import com.automat.manager.classes.ButtonLogout;
import com.automat.manager.classes.ButtonReload;
import com.automat.manager.classes.OrderAdapter;
import com.automat.manager.classes.Validator;
import com.automat.manager.classes.WarningDialog;
import com.automat.manager.enums.EErrorCode;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.requests.CreateReturnBalloonRequest;
import com.automat.manager.responses.GetAllGottenBalloons;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetCreateReturnBalloonResponse;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Collector extends AppCompatActivity {
    // Value
    private boolean isScannerEnable = false;
    private String api;
    ArrayList<Balloon> arrayList;

    ArrayList<Balloon> getAllGottenBalloons;

    RecyclerView rvQRCollectorStatus;
    String strStatusQRCollector;
    ArrayList<String> itemsQRCollectorStatus;
    RecyclerView.LayoutManager RecyclerViewQRCollectorStatusLayoutManager;
    StatusQRAdapter statusQRCollectorAdapter;
    LinearLayoutManager horizontalQRCollectorStatusLayout;
    //

    // Classes
    private GetCreateOrdersResponse getNewOrder;
    private GetAllGottenBalloons getReturnOrder;
    private Dialog dialog;

    private ButtonLogout buttonLogout;
    private ButtonReload buttonReload;

    private SharedPreferences sharedPreferences;
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
    Button btFrameOrder, btFrameReturn;
    ImageButton btCollectorExit, btCollectorScanEnable;
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
        setContentView(R.layout.activity_collector);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        createFragment(new Collector_1());
 //       FirebaseApp.initializeApp(Collector.this);
        init();
        //createCodeScanner();
    }



    private void init() {
        btCollectorExit = findViewById(R.id.bt_collector_exit);
        buttonLogout = new ButtonLogout(this, btCollectorExit);
        buttonLogout.setExit2();

        btFrameOrder = findViewById(R.id.bt_frame_order);
        btFrameReturn = findViewById(R.id.bt_frame_return);

        btFrameOrder.setOnClickListener(onFrameOrder());
        btFrameReturn.setOnClickListener(onFrameReturn());

        foo(topics.get(0));
    }




// On Click's
    private View.OnClickListener onFrameOrder() {
        return view -> {
            btFrameOrder.setBackgroundColor(Color.parseColor("#2680FF"));
            btFrameReturn.setBackgroundColor(Color.parseColor("#63A2FB"));
            createFragment(new Collector_1());
        };
    }

    private View.OnClickListener onFrameReturn() {
        return view -> {
            btFrameOrder.setBackgroundColor(Color.parseColor("#63A2FB"));
            btFrameReturn.setBackgroundColor(Color.parseColor("#2680FF"));
            createFragment(new Collector_2());
        };
    }



    // Void's
    private void createFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_collector, fragment);
        ft.commit();
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
        ActivityCompat.requestPermissions(this, list, 101);
    }

    private void setupPermissions() {
        int permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }


    //

}