package com.example.picoplacaapp.functions;

import android.content.Context;
import android.widget.Toast;

import com.example.picoplacaapp.R;

import java.util.regex.Pattern;

public class CommonFunctions {

    public static boolean verifyCorrectDataIntroduced(Context context, String data) {
        if (data.isEmpty()) {
            Toast.makeText(context, R.string.msm_error_placa_vacia,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Pattern.matches("[A-Za-z]{3}-[0-9]{3,4}$", data)) {
            Toast.makeText(context, R.string.msm_error_formato_placa_incorrecto,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
