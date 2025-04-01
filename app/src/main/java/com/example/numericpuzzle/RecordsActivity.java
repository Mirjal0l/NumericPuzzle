package com.example.numericpuzzle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numericpuzzle.cache.SettingsPreference;

import java.util.Set;

public class RecordsActivity extends AppCompatActivity {

    private TextView stepRec;
    private TextView timeRec;

    private static String step;
    private static String time;
    private static int stepMin = 1000;
    private String[] timeMassivi;
    private int minut;
    private int sekund;

    private static int minutMin = 59;
    private static int sekundMin = 59;

    private ImageButton refresher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        SettingsPreference.init(this);
        stepRec = findViewById(R.id.stepRecord);
        timeRec = findViewById(R.id.timeRecord);

        //loadScore();
        findBest();

        refresher = findViewById(R.id.refresh);
        refresher.setVisibility(View.INVISIBLE);
        refresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsPreference.getSettingsPreference().clearCache();
                findBest();
            }
        });
    }

    private void findBest() {
        step = String.valueOf(SettingsPreference.getSettingsPreference().getSteps());

        if (Integer.parseInt(step) < stepMin) {
            stepMin = Integer.parseInt(step);
        }
        stepRec.setText(String.valueOf(stepMin) + "");



        time = SettingsPreference.getSettingsPreference().getTime();
        timeMassivi = time.split(":");

        if (timeMassivi[0].charAt(0) == 0) {
            minut = Integer.parseInt(String.valueOf(timeMassivi[0].charAt(1)));
        } else
            minut = Integer.parseInt(timeMassivi[0]);

        if (timeMassivi[1].charAt(0) == 0) {
            sekund = Integer.parseInt(String.valueOf(timeMassivi[1].charAt(1)));
        } else
            sekund = Integer.parseInt(timeMassivi[1]);

        if (minut <= minutMin) {
            minutMin = minut;
        }

        if (sekund <= sekundMin) {
            sekundMin = sekund;
        }
        timeRec.setText(String.valueOf(minutMin) + ":" + String.valueOf(sekundMin));

    }

//    private void loadScore() {
////        stepRec.setText(SettingsPreference.getSettingsPreference().getSteps() + "");
//        //timeRec.setText(SettingsPreference.getSettingsPreference().getTime());
//
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }
}