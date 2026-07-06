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
import android.os.Vibrator;
import android.preference.PreferenceManager;
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

    // 측정 화면의 표시 요소와 센서 관리 객체입니다.
    Button BT;
    TextView today, Steps, cal, Km;
    Chronometer Time;
    SensorManager sensorManager;
    double S;
    boolean running = false;
    boolean bell = false;

    //원래 걸음
    int mStep = 0;

    //현재 걸음
    int tStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //날짜
        today = (TextView) findViewById(R.id.Dey);
        today.setText(getTime());

        //시간 측정
        Time = findViewById(R.id.Time);

        //거리
        Km = findViewById(R.id.Km);

        //칼로리
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

        //만보기
        Steps = (TextView) findViewById(R.id.Steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //센서 매니저 생성

        //화면 유지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


        //날짜
        private String getTime() {
            long now = System.currentTimeMillis();
            Date date = new Date(now); //날짜를 담을 변수 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); //년-월-일
            return dateFormat.format(date);
        }

        //타이머
        protected void onStart() {
            super.onStart();
            Time = findViewById(R.id.Time);
            // Chronometer의 기본 표시를 시:분:초 형태로 직접 구성합니다.
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

        //지도



    //걸음 수
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


    public void  onSensorChanged(SensorEvent event) { //실제 카운트 함수
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

                //거리
                double a = S * 0.0006;
                String km = kk.format(a);
                Km.setText(km);

                //칼로리
                double b = S * 0.02;
                String ca = kk.format(b);
                cal.setText(ca);

                // 목표 달성 여부를 확인해 알림을 처리합니다.
                Belling();
                }
            }
        }

        public void Belling() {
            // 저장된 목표 걸음 수와 현재 세션+이전 걸음 수를 비교해 달성 알림을 띄웁니다.

            if (bell = false) {
                //알림
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String GSW = sharedPreferences.getString("GSW", "");

                String value = sharedPreferences.getString("goal", "");
                int STG = Integer.parseInt(value);

                Intent intentStart = getIntent();
                String StepT = intentStart.getStringExtra("Step");
                int STT = Integer.parseInt(StepT);

                if (STG >= STT) {
                    if (GSW == "1") {
                        int TSE = tStep + STT;
                        if (STG <= TSE) {
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(1000); //1초간 진동
                            Toast.makeText(this, "목표 걸음수를 달성했습니다!", Toast.LENGTH_LONG).show();
                            bell = true;
                        }
                    } else {
                        Toast.makeText(this, "알림이 꺼져 있씁니다.", Toast.LENGTH_LONG).show();
                        bell = true;
                    }
                } else {
                    Toast.makeText(this, "이미 목표 걸음 수를 달성하셨습니다!", Toast.LENGTH_LONG).show();
                    bell = true;
                }
            }
        }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
