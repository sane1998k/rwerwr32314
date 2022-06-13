package com.automat.manager.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.automat.manager.R;

public class WarningDialog extends AppCompatActivity {
    private Context context;

    public WarningDialog(Context context) {
        this.context = context;
    }

    public AlertDialog create(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.text_title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.text_yes_dialog, (dialogInterface, i) -> {})
                .setNegativeButton(R.string.text_no_dialog, (dialogInterface, i) -> {});
        return builder.create();
    }

    public AlertDialog create(String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.text_title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.text_yes_dialog, onClickListener)
                .setNegativeButton(R.string.text_no_dialog, (dialogInterface, i) -> {});
        return builder.create();
    }

    public AlertDialog create(String message, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.text_title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.text_yes_dialog, onClickListener)
                .setNegativeButton(R.string.text_no_dialog, onClickListener2);
        return builder.create();
    }
}
