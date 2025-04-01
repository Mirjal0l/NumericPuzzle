package com.example.numericpuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numericpuzzle.cache.SettingsPreference;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private ViewGroup viewGroup;
    private TextView timeCounter;
    private TextView stepCounter;
    private Button[][] buttons = new Button[3][3];
    private ToggleButton toggleButton;
    private Button stopBtn;
    private static int boshbtnI = 0;
    private static int boshbtnJ = 0;
    public Timer timer;
    private static int time = 0;
    private static int step = 0;
    private static int[] numbers = new int[8];

    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SettingsPreference.init(this);
        loadNew();

        loadViews();
        loadTime();
        loadNumbers();
        shuffleNumbers();
        loadDataToViews();

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                time = 0;
                timeCounter.setText("00:00");
                step = 0;
                stepCounter.setText("0");
                loadViews();
                loadTime();
                loadNumbers();
                shuffleNumbers();
                loadDataToViews();
            }

        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()) {
                    isPaused = true;
                    timer.cancel();
                } else {
                    isPaused = false;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    time++;
                                    setTimeToView();
                                }
                            });
                        }
                    }, 1000, 1000);
                }
            }
        });
    }

    private void loadNew() {
        buttons = new Button[3][3];
        boshbtnI = 0;
        boshbtnJ = 0;
        time = 0;
        step = 0;
        numbers = new int[8];
        timer = new Timer();
    }
    ////////////////////

    private void loadViews(){
        viewGroup = findViewById(R.id.button_group);
        timeCounter = findViewById(R.id.timeCounter);
        stepCounter = findViewById(R.id.stepCounter);
        stopBtn = findViewById(R.id.stopBtn);
        toggleButton = findViewById(R.id.toggleBtn);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            buttons[i / 3][i % 3] = (Button) viewGroup.getChildAt(i);
        }
    }

    public void loadTime() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        setTimeToView();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void setTimeToView() {
        int minut = time / 60;
        int sekund = time % 60;
        String time1 = String.format("%02d:%02d", minut, sekund);
        timeCounter.setText(time1);
    }

    private void loadNumbers() {
        for (int i = 0; i < 8; i++) {
            numbers[i] = i + 1;
        }
    }

    private void shuffleNumbers() {
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(i + 1);
            int temp = numbers[index];
            numbers[index] = numbers[i];
            numbers[i] = temp;
        }
    }

    private void loadDataToViews() {
        boshbtnI = 2;
        boshbtnJ = 2;
        for (int i = 0; i < 8; i++) {
            buttons[i / 3][i % 3].setText(String.valueOf(numbers[i]));
            buttons[i / 3][i % 3].setBackgroundResource(R.drawable.btn_style);
        }

        buttons[boshbtnI][boshbtnJ].setText("");
        buttons[boshbtnI][boshbtnJ].setBackgroundResource(R.drawable.btn_empty);
    }

    public void btn_clicked(View view) {
        Button button = (Button) view;
        String tag = button.getTag().toString();
        char x = tag.charAt(0);
        char y = tag.charAt(1);
        int a = Integer.parseInt(String.valueOf(x));
        int b = Integer.parseInt(String.valueOf(y));

        if ((Math.abs(boshbtnI - a) == 0 && Math.abs(boshbtnJ - b) == 1) || (Math.abs(boshbtnI - a) == 1 && Math.abs(boshbtnJ - b) == 0)) {
            buttons[boshbtnI][boshbtnJ].setBackgroundResource(R.drawable.btn_style);
            buttons[boshbtnI][boshbtnJ].setText(button.getText().toString());

            boshbtnI = a;
            boshbtnJ = b;

            button.setText("");
            button.setBackgroundResource(R.drawable.btn_empty);
            step++;
            stepCounter.setText(String.valueOf(step));
            checkWin();
        }

    }

    private void checkWin() {
        boolean isWin = true;
        for (int i = 0; i < 8; i++) {
            isWin &= buttons[i / 3][i % 3].getText().toString().equalsIgnoreCase(String.valueOf(i + 1));
        }

        if (isWin) {
            timer.cancel();
            Toast.makeText(this, "You Won!", Toast.LENGTH_SHORT).show();
            boolean natija = SettingsPreference.getSettingsPreference().setScore(Integer.parseInt(stepCounter.getText().toString()), timeCounter.getText().toString());
            if (natija) {
                Toast.makeText(this, "Successfully saved!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Error writing in cache!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity2.class);
        timer.cancel();
        startActivity(intent);
        this.finish();
        super.onBackPressed();
    }


}
