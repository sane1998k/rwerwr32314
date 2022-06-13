package com.automat.manager.classes;

import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.automat.manager.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;

public class CheckerField {

    public View.OnFocusChangeListener myOnFocusChanger(TextInputLayout textInputLayout) {
        return (view, b) -> {
            EditText currentET = ((EditText) view);
            boolean isEmpty = currentET.getText().toString().equals("");
            if (!b) textInputLayout.setHelperText(isEmpty ? "Поле \"" + currentET.getHint() + "\" не может быть пустым" : "");
        };
    }

    public boolean isFields(EditText[] editText) {
        long isEmpty = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            isEmpty = Arrays.stream(editText)
                    .filter(et -> et.getText().toString().equals(""))
                    .count();
        }
        return isEmpty > 0;
    }
}
