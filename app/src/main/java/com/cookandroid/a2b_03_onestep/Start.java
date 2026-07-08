// 걷기 측정 화면을 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// 걷기 측정 화면입니다.
// TYPE_STEP_COUNTER 센서로 현재 세션의 걸음 수를 계산하고 시간, 거리, 칼로리를 함께 표시합니다.
public class Start extends AppCompatActivity
        implements SensorEventListener {

    // 측정 화면에서 사용하는 표시 요소와 센서 관리 객체입니다.
    Button BT;
    TextView today, Steps, cal, Km;
    Chronometer Time;
    SensorManager sensorManager;
    double S;
    boolean running = false;
    boolean bell = false;

    // TYPE_STEP_COUNTER는 부팅 이후 누적값을 주므로, 세션 시작 기준값을 따로 저장합니다.
    int mStep = 0;

    // 현재 측정 세션에서 계산한 걸음 수입니다.
    int tStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 측정 화면에 오늘 날짜를 표시합니다.
        today = (TextView) findViewById(R.id.Dey);
        today.setText(getTime());

        // Chronometer로 현재 세션의 걷기 시간을 측정합니다.
        Time = findViewById(R.id.Time);

        Km = findViewById(R.id.Km);

        cal = findViewById(R.id.cal);


        // 완료 버튼을 누르면 현재 세션 기록을 MainActivity로 전달하고 측정 화면을 종료합니다.
        BT = (Button) findViewById(R.id.BT);
        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Time.stop();
                String a = (String) Time.getText();
                String[] b = a.split(":");

                String STE = (String) Steps.getText();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("Step", STE);

                intent.putExtra("hh", b[0]);
                intent.putExtra("mm", b[1]);
                intent.putExtra("ss", b[2]);

                String k = (String) Km.getText();
                String c = (String) cal.getText();

                intent.putExtra("Km",k);
                intent.putExtra("Cal", c);

                startActivity(intent);
                finish();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

        // 걸음 수 센서 사용을 위해 SensorManager를 준비합니다.
        Steps = (TextView) findViewById(R.id.Steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 측정 중 화면이 꺼지면 사용자가 시간을 확인하기 어려워 화면을 유지합니다.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


        // 화면에 표시할 오늘 날짜 문자열을 만듭니다.
        private String getTime() {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            return dateFormat.format(date);
        }

        // Chronometer 기본 표시 형식을 시:분:초 형태로 맞춰 측정 시간을 보여줍니다.
        protected void onStart() {
            super.onStart();
            Time = findViewById(R.id.Time);
            Time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    String hh = h < 10 ? "0" + h : h + "";
                    String mm = m < 10 ? "0" + m : m + "";
                    String ss = s < 10 ? "0" + s : s + "";
                    chronometer.setText(hh + ":" + mm + ":" + ss);
                }
            });
            Time.setBase(SystemClock.elapsedRealtime());
            Time.start();
        }

    // 화면이 다시 보이면 걸음 수 센서 리스너를 등록합니다.
    protected void onResume() {
        super.onResume();
        running = true;
        // 기기에서 걸음 수 누적 센서를 찾아 리스너를 등록합니다.
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "센서를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }


    public void  onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (running) {
                // 센서 값은 부팅 이후 누적 걸음 수이므로 첫 값을 기준점으로 저장합니다.
                if (mStep < 1) {
                    mStep = (int) event.values[0];
                }
                tStep = (int) event.values[0] - mStep;
                Steps.setText(Integer.toString(tStep));

                S = tStep;

                DecimalFormat kk = new DecimalFormat("#.##");

                double a = S * 0.0006;
                String km = kk.format(a);
                Km.setText(km);

                double b = S * 0.02;
                String ca = kk.format(b);
                cal.setText(ca);

                // 목표 달성 여부를 확인해 알림을 처리합니다.
                Belling();
                }
            }
        }

        @SuppressWarnings("deprecation")
        public void Belling() {
            // 저장된 목표 걸음 수와 현재 세션+이전 걸음 수를 비교해 달성 알림을 한 번만 띄웁니다.

            if (!bell) {
                SharedPreferences sharedPreferences = AppPreferences.get(this);
                String GSW = sharedPreferences.getString("GSW", "1");

                String value = sharedPreferences.getString("goal", "10000");
                int STG = Integer.parseInt(value);

                Intent intentStart = getIntent();
                String StepT = intentStart.getStringExtra("Step");
                if (StepT == null || StepT.length() == 0) {
                    StepT = "0";
                }
                int STT = Integer.parseInt(StepT);

                if (STG >= STT) {
                    if ("1".equals(GSW)) {
                        int TSE = tStep + STT;
                        if (STG <= TSE) {
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibrator.vibrate(1000);
                            }
                            Toast.makeText(this, "목표 걸음수를 달성했습니다!", Toast.LENGTH_LONG).show();
                            bell = true;
                        }
                    } else {
                        Toast.makeText(this, "알림이 꺼져 있습니다.", Toast.LENGTH_LONG).show();
                        bell = true;
                    }
                } else {
                    Toast.makeText(this, "이미 목표 걸음 수를 달성하셨습니다!", Toast.LENGTH_LONG).show();
                    bell = true;
                }
            }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
