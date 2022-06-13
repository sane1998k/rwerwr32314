package com.automat.manager.classes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.automat.manager.R;

public class CustomDialog {
    private Dialog dialog;

    Button btReload;
    TextView tvResultScanQRBalloon;
    ProgressBar pbDialogInfo;

    Context context;
    public CustomDialog(Context context) {
        //this.dialog = new Dialog(context);
        this.context = context;
    }

    public Dialog createInfoDialog() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_info);
        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
//        this.pbDialogInfo = dialog.findViewById(R.id.pb_dialog_info);
//        this.btReload = dialog.findViewById(R.id.bt_dialog_info_reload);
//        this.tvResultScanQRBalloon = dialog.findViewById(R.id.tv_result_scan_qr_balloon);
        return dialog;
    }

    public void showInfoDialog() {
        dialog.show();
    }

    public void createOrderDialog() {

    }

    public void setTvResultScanQRBalloon(String string) {
        this.tvResultScanQRBalloon.setText(string);
    }

    public ProgressBar getPbDialogInfo() {
        return pbDialogInfo;
    }

    public Button getBtReload() {
        return btReload;
    }
}
