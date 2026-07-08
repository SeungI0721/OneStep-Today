// 통계 화면과 동적 캘린더를 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

// 현재 연/월을 기준으로 달력을 다시 그리고 오늘 기록 요약을 함께 보여줍니다.
public class Statistics extends AppCompatActivity {

    // 현재 기록 표시 영역과 월 이동 버튼입니다.
    TextView Step, Km, Cal, STime, Achievement;
    Button back, bt1, bt2;
    GridLayout calendarGrid;

    // 화면에 표시할 연도와 월을 Calendar 기준으로 관리합니다.
    Calendar currentCalendar;
    Calendar todayCalendar;

    int ST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // 통계 화면에서 사용할 기록 표시 영역과 월 이동 버튼을 연결합니다.
        Step = findViewById(R.id.step);
        Km = findViewById(R.id.Km);
        Cal = findViewById(R.id.cal);
        Achievement = findViewById(R.id.achievement);
        STime = findViewById(R.id.Stime);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        calendarGrid = findViewById(R.id.calendarGrid);

        todayCalendar = Calendar.getInstance(Locale.KOREA);
        currentCalendar = Calendar.getInstance(Locale.KOREA);

        setRecordSummary();
        drawCalendar();

        // 지난 달 버튼은 현재 표시 월을 한 달 이전으로 이동합니다.
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, -1);
                drawCalendar();
            }
        });

        // 이후 달 버튼은 현재 표시 월을 한 달 다음으로 이동합니다.
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, 1);
                drawCalendar();
            }
        });

        // 돌아가기 버튼은 메인 화면으로 이동합니다.
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

    private void setRecordSummary() {
        // MainActivity에서 전달받은 오늘 기록 값과 목표 달성률을 화면에 표시합니다.
        Intent intentSta = getIntent();

        String st = intentSta.getStringExtra("Step");
        if (st == null || st.equals("")) {
            st = "0";
        }
        Step.setText(st);
        ST = parseInt(st, 0);

        String km = intentSta.getStringExtra("Km");
        if (km == null || km.equals("")) {
            km = "0.0";
        }
        Km.setText(km);

        String ca = intentSta.getStringExtra("Cal");
        if (ca == null || ca.equals("")) {
            ca = "0.00";
        }
        Cal.setText(ca);

        String savedGoal = AppPreferences.get(this).getString("goal", "10000");
        int goal = parseInt(savedGoal, 10000);

        DecimalFormat format = new DecimalFormat("#.0");
        double achievement = goal == 0 ? 0 : (ST * 100.0 / goal);
        Achievement.setText(format.format(achievement));
    }

    private void drawCalendar() {
        // 선택된 연도와 월을 기준으로 6주짜리 달력 칸을 다시 구성합니다.
        calendarGrid.removeAllViews();

        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);
        STime.setText(String.format(Locale.KOREA, "%d년 %02d월", year, month + 1));

        Calendar firstDay = Calendar.getInstance(Locale.KOREA);
        firstDay.set(year, month, 1);

        int firstWeekDay = firstDay.get(Calendar.DAY_OF_WEEK);
        int lastDay = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startIndex = firstWeekDay - 1;

        for (int i = 0; i < 42; i++) {
            TextView dayView = makeDayView();
            int day = i - startIndex + 1;

            if (day >= 1 && day <= lastDay) {
                dayView.setText(String.valueOf(day));
                setDayStyle(dayView, year, month, day);
            } else {
                dayView.setText("");
                dayView.setBackgroundColor(Color.TRANSPARENT);
            }

            calendarGrid.addView(dayView);
        }
    }

    private int parseInt(String value, int defaultValue) {
        // 비어 있거나 숫자가 아닌 값이 들어와도 통계 화면이 종료되지 않도록 기본값을 사용합니다.
        try {
            if (value == null || value.length() == 0) {
                return defaultValue;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private TextView makeDayView() {
        // 달력 한 칸에 들어갈 날짜 TextView를 만듭니다.
        TextView dayView = new TextView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 0;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);
        dayView.setLayoutParams(params);
        dayView.setGravity(Gravity.CENTER);
        dayView.setTextColor(Color.WHITE);
        dayView.setTextSize(20);
        dayView.setTypeface(null, Typeface.BOLD);
        dayView.setBackgroundColor(Color.argb(60, 0, 0, 0));
        return dayView;
    }

    private void setDayStyle(TextView dayView, int year, int month, int day) {
        // 오늘 날짜는 구분하기 쉽도록 배경색을 더 진하게 표시합니다.
        boolean isToday = year == todayCalendar.get(Calendar.YEAR)
                && month == todayCalendar.get(Calendar.MONTH)
                && day == todayCalendar.get(Calendar.DAY_OF_MONTH);

        if (isToday) {
            dayView.setBackgroundColor(Color.argb(180, 0, 0, 0));
        } else {
            dayView.setBackgroundColor(Color.argb(60, 0, 0, 0));
        }
    }
}
