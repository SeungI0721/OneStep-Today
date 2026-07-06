// 목표와 알림 설정 화면을 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

// 목표 걸음 수와 알림 옵션을 설정하는 화면입니다.
// 입력값은 SharedPreferences에 저장되어 MainActivity와 Start 화면에서 다시 사용됩니다.
@SuppressLint("UseSwitchCompatOrMaterialCode")
public class Setting extends AppCompatActivity {

    // 목표 입력 필드와 알림 스위치입니다.
    Button back, save;
    EditText Mgoal, Rgoal, Sgoal, Wgoal;
    Switch Gb, Mb;

    // 저장할 목표값과 스위치 상태 문자열입니다.
    String Goal;
    String GSW, MSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // 설정값은 기본 SharedPreferences에서 불러오고 다시 저장합니다.
        SharedPreferences sharedPreferences = AppPreferences.get(this);

        //설정값 불러오기
        //그냥 목표
        Mgoal = findViewById(R.id.Mgoal);
        String goal = sharedPreferences.getString("goal", "");
        if (goal.length()==0) {
            Mgoal.setText("10000");
        } else {
            Mgoal.setText(goal);
        }

        //비 오는 날
        Rgoal = findViewById(R.id.Rgoal);
        String Roal = sharedPreferences.getString("Roal", "");
        if (Roal.length()==0) {
            Rgoal.setText("10000");
        } else {
            Rgoal.setText(Roal);
        }

        //눈 오는날
        Sgoal = findViewById(R.id.Sgoal);
        String Soal = sharedPreferences.getString("Soal", "");
        if (Soal.length()==0) {
            Sgoal.setText("10000");
        } else {
            Sgoal.setText(Soal);
        }

        //기상 악화
        Wgoal = findViewById(R.id.Wgoal);
        String Woal = sharedPreferences.getString("Woal", "");
        if (Woal.length()==0) {
            Wgoal.setText("10000");
        } else {
            Wgoal.setText(Woal);
        }


        //알림 설정------------------------------------------------------------------------------------------
        //목표
        String Gell = sharedPreferences.getString("GSW", "1");
        GSW = Gell;
        Gb = findViewById(R.id.GB);
        if ("1".equals(Gell)) {
            Gb.setChecked(true);
        } else if ("0".equals(Gell)) {
            Gb.setChecked(false);
        } else {
            Toast.makeText(getApplicationContext(),"알림 설정값 불러오기에 실패하였습니다.",Toast.LENGTH_SHORT).show();
        }

        // 목표 달성 알림 스위치 상태를 저장용 문자열로 변환합니다.
        Gb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    GSW = "1";
                    Toast.makeText(getApplicationContext(),"달성 알림 받기",Toast.LENGTH_SHORT).show();
                } else {
                    GSW = "0";
                    Toast.makeText(getApplicationContext(),"달성 알림 안받기",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //날씨
        Mb = findViewById(R.id.MB);
        String Mell = sharedPreferences.getString("MSW", "0");
        MSW = Mell;
        if ("1".equals(Mell)) {
            Mb.setChecked(true);
        } else if ("0".equals(Mell)) {
            Mb.setChecked(false);
        } else {
            Toast.makeText(getApplicationContext(),"날씨 설정값 불러오기에 실패하였습니다.",Toast.LENGTH_SHORT).show();
        }

        // 날씨 알림 스위치 상태를 저장용 문자열로 변환합니다.
        Mb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MSW = "1";
                } else {
                    MSW = "0";
                }
            }
        });


        //저장하기----------------------------------------------------------------------------------------------------
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 네 가지 목표값이 모두 입력되어 있고 최소 길이를 만족할 때 저장합니다.
                Goal = Mgoal.getText().toString();
                if (Mgoal.length()==0 || Rgoal.length()==0 || Sgoal.length()==0 || Wgoal.length()==0 ) {
                    Toast.makeText(getApplicationContext(), "목표 값을 모두 입력했는지 확인 해주세요.", Toast.LENGTH_SHORT).show();

                } else if (Mgoal.length()<=2 || Rgoal.length()<=2 || Sgoal.length()<=2 || Wgoal.length()<=2 ) {
                    Toast.makeText(getApplicationContext(),"모든 목표 값을 100걸음 이상으로 입력해주세요.",Toast.LENGTH_SHORT).show();

                } else  {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String goal = Mgoal.getText().toString();
                    editor.putString("goal", goal);

                    String Soal = Sgoal.getText().toString();
                    editor.putString("Soal", Soal);

                    String Roal = Rgoal.getText().toString();
                    editor.putString("Roal", Roal);

                    String Woal = Wgoal.getText().toString();
                    editor.putString("Woal", Woal);

                    editor.putString("GSW", GSW);
                    editor.putString("MSW", MSW);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //돌아가기----------------------------------------------------------------------------------------------------------
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
