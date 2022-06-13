package com.automat.manager.classes;

import static android.content.Context.MODE_PRIVATE;
import static com.automat.manager.global.GlobalValue.KEY_AUTH;
import static com.automat.manager.global.GlobalValue.KEY_REMEMBER;
import static com.automat.manager.global.GlobalValue.KEY_ROLE;
import static com.automat.manager.global.GlobalValue.NAME_USER;
import static com.automat.manager.global.GlobalValue.PREF_NAME;
import static com.automat.manager.services.MyFCMService.foo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.automat.manager.activities.LogIn;

public class ButtonLogout {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button exit;
    private ImageButton exit2;
    private Context context;

    public ButtonLogout(Context context, Button exit) {
        this.context = context;
        this.exit = exit;
    }

    public ButtonLogout(Context context, ImageButton exit2) {
        this.context = context;
        this.exit2 = exit2;
    }

    public void setExit2() {exit2.setOnClickListener(deleteData());}

    public void setExit() {
        exit.setOnClickListener(deleteData());
    }

    private View.OnClickListener deleteData() {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        return view -> {
            foo("All");

            Intent intent = new Intent(context, LogIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_AUTH);
            editor.remove(KEY_ROLE);
            editor.remove(NAME_USER);
            editor.apply();

            context.startActivity(intent);
            ((Activity) context).finish();
        };
    }
}
