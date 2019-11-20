package com.example.picoplacaapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.example.picoplacaapp.dialogs.DialogInformation;
import com.example.picoplacaapp.functions.CommonFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ImageView imgInfo;
    RelativeLayout btnCheck;
    TextView txtDate;
    TextView txtTime;
    TextView txtVersion;
    EditText edtPlaca;

    Date toDay;
    int year;
    int month;
    int day;
    int dayOfWeek;
    int hours;
    int minutes;
    int numLicensePlate;
    SimpleDateFormat format;
    String time;
    String daySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeItems();
        setDateAndTime();
    }

    public void initializeItems() {
        imgInfo = findViewById(R.id.imgInfo);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtVersion = findViewById(R.id.txtVersion);
        edtPlaca = findViewById(R.id.edtPlaca);
        btnCheck = findViewById(R.id.btnCheck);

        format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLicensePlate();
            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNavigate();
            }
        });

        hideKeyBoard();
        getVersion();
    }

    public void setDateAndTime() {
        Calendar cal = Calendar.getInstance();
        toDay = cal.getTime();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog date = new DatePickerDialog(MainActivity.this,
                        MainActivity.this, year, month, day);
                date.show();
            }
        });

        String minutesString = "";
        minutes = cal.get(Calendar.MINUTE);

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = String.valueOf(minutes);
        }
        txtDate.setText(setDaySelected() + " " + format.format(toDay));
        hours = cal.get(Calendar.HOUR_OF_DAY);
        time = hours + ":" + minutesString;
        txtTime.setText(time);

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog time = new TimePickerDialog(MainActivity.this, MainActivity.this, hours, minutes, false);
                time.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendarActual = new GregorianCalendar(i, i1, i2);
        toDay = calendarActual.getTime();
        year = i;
        month = i1;
        day = i2;
        dayOfWeek = calendarActual.get(Calendar.DAY_OF_WEEK);
        txtDate.setText(setDaySelected() + " " + format.format(toDay));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        time = (String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, minute));
        txtTime.setText(time);
        hours = hour;
        minutes = minute;
    }

    public void checkLicensePlate() {
        if (CommonFunctions.verifyCorrectDataIntroduced(this, edtPlaca.getText().toString())) {

            String placa = edtPlaca.getText().toString();
            String last = String.valueOf(placa.charAt(placa.length() - 1));
            numLicensePlate = Integer.parseInt(last);

            if (verifyDay()) {
                if (verifyCirculationTime()) {
                    createDialogAlert(getString(R.string.alert), getString(R.string.picoplaca_positive), "alto");
                } else {
                    createDialogAlert(getString(R.string.mensaje_alert), getString(R.string.picoplaca_middle), "medio");
                }
            } else {
                createDialogAlert(getString(R.string.mensaje_alert), getString(R.string.picoplaca_negative), "bajo");
            }
        }

    }

    public boolean verifyCirculationTime() {
        String[] picoplacaTimes = getResources().getStringArray(R.array.pico_placa_times);
        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date[] date = new Date[4];

        try {
            Date timeDate = sdf.parse(time);
            for (int i = 0; i < picoplacaTimes.length; i++) {
                date[i] = sdf.parse(picoplacaTimes[i]);
            }
            if (timeDate != null && ((timeDate.after(date[0]) && timeDate.before(date[1])) || (timeDate.after(date[2]) && timeDate.before(date[3])))) {
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyDay() {
        int i = 2;

        for (int j = 1; j < 10; j++) {
            if (dayOfWeek == i && (numLicensePlate == j || numLicensePlate == j + 1)) {
                return true;
            }
            j++;
            i++;
        }

        return (dayOfWeek == 6 && numLicensePlate == 0);
    }

    public void createDialogAlert(String title, String message, String type) {
        DialogInformation dialogInformation = new DialogInformation();
        Bundle arguments = new Bundle();
        arguments.putString("TITLE_VALUE", title);
        arguments.putString("CONTENT_VALUE", message);
        arguments.putString("TYPE", type);
        dialogInformation.setArguments(arguments);
        dialogInformation.show(getSupportFragmentManager(), "MainActivity");
    }

    public String setDaySelected() {
        switch (dayOfWeek) {
            case 1:
                daySelected = "Dom";
                break;
            case 2:
                daySelected = "Lun";
                break;
            case 3:
                daySelected = "Mar";
                break;
            case 4:
                daySelected = "Mie";
                break;
            case 5:
                daySelected = "Jue";
                break;
            case 6:
                daySelected = "Vie";
                break;
            case 7:
                daySelected = "Sab";
                break;
            default:
                break;
        }

        return daySelected;
    }

    public void onClickNavigate() {
        int color = ContextCompat.getColor(getBaseContext(), R.color.colorPrimary);
        String url = getString(R.string.url_ecuador_pico_placa);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(color);
        builder.setStartAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    public void hideKeyBoard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public void getVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionCurrent = pInfo.versionName;
            txtVersion.setText("Version: " + versionCurrent);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
