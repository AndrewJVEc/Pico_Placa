package com.example.picoplacaapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.picoplacaapp.functions.CommonFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ImageView imgMenu;
    LinearLayout navPane;
    TextView txtDate;
    TextView txtTime;
    TextView txtVersion;
    EditText edtPlaca;
    RelativeLayout btnCheck;

    Date toDay;
    int year;
    int month;
    int day;
    int hours;
    int minutes;
    boolean openNave;
    SimpleDateFormat format;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeItems();
        setMenu();
        setDateAndTime();
    }

    public void initializeItems() {
        imgMenu = findViewById(R.id.imgMenu);
        navPane = findViewById(R.id.navPane);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtVersion = findViewById(R.id.txtVersion);
        edtPlaca = findViewById(R.id.edtPlaca);
        btnCheck = findViewById(R.id.btnCheck);

        openNave = false;
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLicensePlate();
            }
        });

        hideKeyBoard();
        getVersion();
    }

    public void setMenu() {
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!openNave) {
                    navPane.setVisibility(View.VISIBLE);
                    openNave = true;
                } else {
                    navPane.setVisibility(View.GONE);
                    openNave = false;
                }
            }
        });
    }

    public void setDateAndTime() {
        Calendar cal = Calendar.getInstance();
        toDay = cal.getTime();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

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
        txtDate.setText(format.format(toDay));
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
        txtDate.setText(format.format(toDay));
        year = i;
        month = i1;
        day = i2;
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
            Toast.makeText(this, "Verificando info", Toast.LENGTH_SHORT).show();

            verifyCirculationTime();

            String placa = edtPlaca.getText().toString();
            String last = String.valueOf(placa.charAt(placa.length() - 1));

            Toast.makeText(this, last, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Te encuentras en horario de pico y placa", Toast.LENGTH_SHORT).show();
                return true;

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void hideKeyBoard() {
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
