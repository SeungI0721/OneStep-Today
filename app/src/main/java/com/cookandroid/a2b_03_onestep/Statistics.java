package com.cookandroid.a2b_03_onestep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

// 통계 화면입니다.
// MainActivity에서 전달받은 오늘 기록을 표시하고 월별 정적 레이아웃을 전환합니다.
public class Statistics extends AppCompatActivity {

    // 현재 기록 표시 영역과 월 이동 버튼입니다.
    TextView Step, Km, Cal, STime;
    Button back, bt1, bt2;
    ImageView Sun, Mon, Tue, Wed, Thu, Fri, Sat;

    // 2021년 10월, 11월, 12월 통계 레이아웃입니다.
    LinearLayout Nov, Oct, Dce, chak;

    int ST;
    String V = "nov";


    @Override
    protected void onStart() {
        super.onStart();
        // 화면 진입 시 기본 월을 2021년 11월 레이아웃으로 맞춥니다.
        Oct.setVisibility(View.INVISIBLE);
        Nov.setVisibility(View.VISIBLE);
        Dce.setVisibility(View.INVISIBLE);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Step = findViewById(R.id.step);
        Km = findViewById(R.id.Km);
        Cal = findViewById(R.id.cal);

        STime = findViewById(R.id.Stime);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);

        Oct = findViewById(R.id.Oct);
        Nov = findViewById(R.id.Nov);
        Dce = findViewById(R.id.Dce);

//        chak = findViewById(R.id.chak);
//        chak.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        // 왼쪽 버튼은 현재 월 상태에 따라 이전 월 레이아웃을 보여줍니다.
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (V == "nov") {
                    STime.setText("2021년 10월");
                    Oct.setVisibility(View.VISIBLE);
                    Nov.setVisibility(View.INVISIBLE);
                    Dce.setVisibility(View.INVISIBLE);
                    V="oct";
                } else if (V == "dec") {
                    STime.setText("2021년 11월");
                    Oct.setVisibility(View.INVISIBLE);
                    Nov.setVisibility(View.VISIBLE);
                    Dce.setVisibility(View.INVISIBLE);
                    V="nov";
                }
            }
        });

        // 오른쪽 버튼은 현재 월 상태에 따라 다음 월 레이아웃을 보여줍니다.
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (V == "nov") {
                    STime.setText("2021년 12월");
                    Oct.setVisibility(View.INVISIBLE);
                    Nov.setVisibility(View.INVISIBLE);
                    Dce.setVisibility(View.VISIBLE);
                    V="dec";

                } else if (V == "oct") {
                    STime.setText("2021년 11월");
                    Oct.setVisibility(View.INVISIBLE);
                    Nov.setVisibility(View.VISIBLE);
                    Dce.setVisibility(View.INVISIBLE);
                    V="nov";
                }
            }
        });

        // MainActivity에서 전달받은 오늘 기록 값을 화면에 표시합니다.
        Intent intentSta = getIntent();

        //걸음수
        String st = intentSta.getStringExtra("Step");
        if (st == null || st.equals("")) {
            Step.setText("fail");
        } else {
            Step.setText(st);
            ST = Integer.parseInt(st);
        }

        //거리
        String km = intentSta.getStringExtra("Km");
        if (km == null || km.equals("")) {
            Km.setText("fail");
        } else {
            Km.setText(km);
        }

        //칼로리
        String ca = intentSta.getStringExtra("Cal");
        if (ca == null || ca.equals("")) {
            Cal.setText("fail");
        } else {
            Cal.setText(ca);
        }

//        Sun = findViewById(R.id.Sun);
//        Sun.setImageResource(R.drawable.faii);
//
//        //String Suc = intentSta.getStringExtra("Suc");
//        Mon = findViewById(R.id.Mon);
//        Mon.setImageResource(R.drawable.succ);
//
//
//        Tue = findViewById(R.id.Tue);
//        Tue.setImageResource(R.drawable.faii);
//
//        Wed = findViewById(R.id.Wed);
//        Wed.setImageResource(R.drawable.faii);
//
        // 목표 달성 여부에 따라 통계 달력의 해당 날짜 아이콘을 변경합니다.
        String Su = intentSta.getStringExtra("Suc");
        Thu = findViewById(R.id.Thu);
        if (Su == "TT") {
            Thu.setImageResource(R.drawable.succ);
        } else {
            Thu.setImageResource(R.drawable.faii);
        }
//
//        Fri = findViewById(R.id.Fri);
//        Fri.setImageResource(R.drawable.faii);
//
//        Sat = findViewById(R.id.Sat);
//        Sat.setImageResource(R.drawable.faii);

        //돌아가기
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSta = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentSta);
                finish();
            }
        });
    }

}
