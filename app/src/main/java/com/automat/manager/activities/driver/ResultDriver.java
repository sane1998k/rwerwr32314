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
import static com.automat.manager.global.GlobalValue.OBJECT_DRIVER1;
import static com.automat.manager.global.GlobalValue.PREF_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.automat.manager.R;
import com.automat.manager.classes.ApiClient;
import com.automat.manager.classes.MyThread;
import com.automat.manager.classes.PutObject;
import com.automat.manager.interfaces.ICallBack;
import com.automat.manager.responses.GetCreateOrdersResponse;
import com.automat.manager.responses.GetReturningsBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultDriver extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String api, idOrder, orderCount, driverSizeArrOrder;
    String idReturnOrder, countReturnBalloon, driverSizeArrReturnOrder;
    TextView tvReturnOrderResult, tvOrderResult;
    Button btCreateOrderResult;
    ProgressBar pbLoadCollector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_driver);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        api = sharedPreferences.getString(KEY_AUTH, "");

        idOrder = sharedPreferences.getString(DRIVER_ID_ORDER, "");
        orderCount = sharedPreferences.getString(DRIVER_COUNT_ORDER_BALLOON, "");
        driverSizeArrOrder = sharedPreferences.getString(DRIVER_SIZE_ARR_ORDER, "");

        idReturnOrder = sharedPreferences.getString(DRIVER_ID_RETURN_ORDER, "");
        countReturnBalloon = sharedPreferences.getString(DRIVER_COUNT_RETURN_BALLOON, "");
        driverSizeArrReturnOrder = sharedPreferences.getString(DRIVER_SIZE_ARR_RETURN_ORDER, "");

        tvReturnOrderResult = findViewById(R.id.tv_return_order_result);
        tvOrderResult = findViewById(R.id.tv_order_result);
        btCreateOrderResult = findViewById(R.id.result_create_order);

        pbLoadCollector = findViewById(R.id.pb_result_order);

        tvOrderResult.setText("Передано клиенту " + driverSizeArrOrder + " из " + orderCount);

//        if (!driverSizeArrReturnOrder.equals("") && !countReturnBalloon.equals(""))
//        tvReturnOrderResult.setText("Возвращено " + driverSizeArrReturnOrder + " из " + countReturnBalloon);

        //Log.e("AUEEEEEE", "init: " + PutObject.retrieve(sharedPreferences, GET_NEW_ORDER, GetCreateOrdersResponse.class).isReturned());
        if (getIntent().getStringExtra("flag").equals("Driver2")) {
            tvReturnOrderResult.setText("Возвращено " + driverSizeArrReturnOrder + " из " + countReturnBalloon);
            btCreateOrderResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btCreateOrderResult.setEnabled(false);
                    Call<Void> callReturnComplete = ApiClient
                            .getUserService()
                            .returnToComplete(api, PutObject.retrieve(sharedPreferences, OBJECT_DRIVER1, GetReturningsBody.class).getId());
                    pbLoadCollector.setVisibility(View.VISIBLE);

                    MyThread myThread = new MyThread(ResultDriver.this, ResultDriver.this);
                    Thread thread = new Thread(myThread.createRunnable(callReturnComplete, new MyThread.State() {
                        @Override
                        public void good() {
                            pbLoadCollector.setVisibility(View.VISIBLE);
                            Call<Void> callSetCompleteOrder = ApiClient
                                    .getUserService()
                                    .toDoneOrder(api, idOrder);

                            pbLoadCollector.setVisibility(View.VISIBLE);

                            callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack<Void>() {
                                @Override
                                public void good(Response response) {
//                                    delete();
                                    btCreateOrderResult.setEnabled(true);
                                    pbLoadCollector.setVisibility(View.GONE);
                                    Intent intent = new Intent(ResultDriver.this, Driver1.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                    finish();
                                }

                                @Override
                                public void bad(Response response1) {
                                    btCreateOrderResult.setEnabled(true);
                                    pbLoadCollector.setVisibility(View.GONE);
                                    btCreateOrderResult.setText("Повторить");
                                    btCreateOrderResult.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Call<Void> callSetCompleteOrder2 = ApiClient
                                                    .getUserService()
                                                    .toDoneOrder(api, idOrder);
                                            callSetCompleteOrder2.enqueue(getCallBackBody(new ICallBack<Void>() {
                                                @Override
                                                public void good(Response<Void> response) {
                                                    delete();
                                                    Intent intent = new Intent(ResultDriver.this, Driver1.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void bad(Response response2) {

                                                }

                                                @Override
                                                public void failed() {

                                                }
                                            }, pbLoadCollector));
                                        }
                                    });
                                }

                                @Override
                                public void failed() {
                                    btCreateOrderResult.setEnabled(true);
                                    pbLoadCollector.setVisibility(View.GONE);
                                }
                            }, pbLoadCollector));
                        }
                    }, R.id.pb_result_order));

                    pbLoadCollector.setVisibility(View.VISIBLE);
                    thread.start();

//                    callReturnComplete.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            pbLoadCollector.setVisibility(View.VISIBLE);
//                            if (response.isSuccessful()) {
//                                pbLoadCollector.setVisibility(View.VISIBLE);
//                                Call<Void> callSetCompleteOrder = ApiClient
//                                        .getUserService()
//                                        .toDoneOrder(api, idOrder);
//
//                                pbLoadCollector.setVisibility(View.VISIBLE);
//
//                                callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack<Void>() {
//                                    @Override
//                                    public void good(Response response) {
////                                    delete();
//                                        btCreateOrderResult.setEnabled(true);
//                                        pbLoadCollector.setVisibility(View.GONE);
//                                        Intent intent = new Intent(ResultDriver.this, Driver1.class);
//                                        startActivity(intent);
//
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void bad(Response response1) {
//                                        btCreateOrderResult.setEnabled(true);
//                                        pbLoadCollector.setVisibility(View.GONE);
//                                        btCreateOrderResult.setText("Повторить");
//                                        btCreateOrderResult.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                Call<Void> callSetCompleteOrder2 = ApiClient
//                                                        .getUserService()
//                                                        .toDoneOrder(api, idOrder);
//                                                callSetCompleteOrder2.enqueue(getCallBackBody(new ICallBack<Void>() {
//                                                    @Override
//                                                    public void good(Response<Void> response) {
//                                                        delete();
//                                                        Intent intent = new Intent(ResultDriver.this, Driver1.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//
//                                                    @Override
//                                                    public void bad(Response response2) {
//
//                                                    }
//
//                                                    @Override
//                                                    public void failed() {
//
//                                                    }
//                                                }, pbLoadCollector));
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void failed() {
//                                        btCreateOrderResult.setEnabled(true);
//                                        pbLoadCollector.setVisibility(View.GONE);
//                                    }
//                                }, pbLoadCollector));
//                            } else {
//                                btCreateOrderResult.setEnabled(true);
//                                pbLoadCollector.setVisibility(View.GONE);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            btCreateOrderResult.setEnabled(true);
//                            pbLoadCollector.setVisibility(View.GONE);
//                        }
//                    });


//                getCallBackBody(new ICallBack<Void>() {
//                        @Override
//                        public void good(Response<Void> response) {
//
//
//
//
//                        }
//
//                        @Override
//                        public void bad(Response response) {
//
////                            pbLoadCollector.setVisibility(View.VISIBLE);
////                            Call<Void> callSetCompleteOrder = ApiClient
////                                    .getUserService()
////                                    .toDoneOrder(api, idOrder);
////
////
////                            callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack<Void>() {
////                                @Override
////                                public void good(Response response) {
////                                    delete();
////                                }
////
////                                @Override
////                                public void bad(Response response1) {
//////                                btCreateOrderResult.setText("Повторить");
//////                                btCreateOrderResult.setOnClickListener(new View.OnClickListener() {
//////                                    @Override
//////                                    public void onClick(View view) {
//////                                        Call<Void> callSetCompleteOrder2 = ApiClient
//////                                                .getUserService()
//////                                                .toDoneOrder(api, idOrder);
//////                                        callSetCompleteOrder2.enqueue(getCallBackBody(new ICallBack<Void>() {
//////                                            @Override
//////                                            public void good(Response<Void> response) {
//////                                                delete();
//////                                                Intent intent = new Intent(ResultDriver.this, Driver1.class);
//////                                                startActivity(intent);
//////                                                finish();
//////                                            }
//////
//////                                            @Override
//////                                            public void bad() {
//////                                                System.out.println(idOrder + "SULAAAAAAAAA");
//////                                            }
//////
//////                                            @Override
//////                                            public void failed() {
//////
//////                                            }
//////                                        }, pbLoadCollector));
//////                                    }
//////                                });
////                                }
////
////                                @Override
////                                public void failed() {
////
////                                }
////                            }, pbLoadCollector));
//
//
//                        }
//
//                        @Override
//                        public void failed() {
//                            btCreateOrderResult.setEnabled(true);
//                            pbLoadCollector.setVisibility(View.GONE);
//                        }
//                    }, pbLoadCollector));

                }
            });
        } else if (getIntent().getStringExtra("flag").equals("Driver1")){
            btCreateOrderResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pbLoadCollector.setVisibility(View.VISIBLE);
                    Call<Void> callSetCompleteOrder = ApiClient
                            .getUserService()
                            .toDoneOrder(api, idOrder);


                    callSetCompleteOrder.enqueue(getCallBackBody(new ICallBack<Void>() {
                        @Override
                        public void good(Response response) {
                            delete();
                            Intent intent = new Intent(ResultDriver.this, Driver1.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
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

    }

    private void delete() {
        tvOrderResult.setText("");
        tvReturnOrderResult.setText("");

        editor.putString(DRIVER_ID_ORDER, "");
        editor.putString(DRIVER_COUNT_ORDER_BALLOON, "");
        editor.putString(DRIVER_SIZE_ARR_ORDER, "");

        editor.putString(DRIVER_ID_RETURN_ORDER, "");
        editor.putString(DRIVER_COUNT_RETURN_BALLOON, "");
        editor.putString(DRIVER_SIZE_ARR_RETURN_ORDER, "");

        PutObject.save(sharedPreferences, new GetReturningsBody(), OBJECT_DRIVER1);
        editor.apply();
    }

}