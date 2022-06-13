package com.automat.manager.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.automat.manager.R;

public class NextDialog extends AppCompatActivity {
    private Context context;

    public NextDialog(Context context) {
        this.context = context;
    }

    public AlertDialog create(String message, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Создать возврат")
                .setMessage(message)
                .setPositiveButton(R.string.text_yes_dialog, onClickListener)
                .setNegativeButton(R.string.text_no_dialog, onClickListener2);
        return builder.create();
    }
}
