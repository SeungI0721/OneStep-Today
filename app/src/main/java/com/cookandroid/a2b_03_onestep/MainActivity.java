// 앱의 메인 화면을 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 앱의 메인 화면입니다.
// 누적 걸음 수, 목표, 시간, 거리, 칼로리, 날씨 정보를 한 화면에 모아 보여줍니다.
public class MainActivity extends AppCompatActivity {

    // 메인 화면에 표시되는 텍스트, 버튼, 상태 이미지 영역입니다.
    TextView day, steps, goal, Hh, Mm, Ss, Km, Cal, Temperature, TemCondition;
    Button start, set, sta, re;
    LinearLayout success, fail;
    ImageView suc, fai, ima;

    // 걷기 세션에서 넘어온 거리와 칼로리를 기존 누적값에 더할 때 사용하는 값입니다.
    Double mKM, mCA;

    // 시, 분, 초, 걸음 수 계산 과정에서 사용하는 임시 누적 변수입니다.
    int H1, H2, H3, H4;
    int M1, M2, M3, M4, M5;
    int S1, S2, S3, S4, S5;
    int R1, R2, R3;
    int G;
    String Suc;

    // 기상청 XML 응답에서 추출한 기온, 하늘 상태, 강수 형태입니다.
    String Tem, Sky, Pre;
    String key = "890cr%2FEzAADYYKUnn9SK9JLTjoAuXWGmw7j%2FFIczgY08F7VXDeP1oJSHD38NziFRLMWNhndxBs%2BCLqxlGmUYfQ%3D%3D";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.Start);
        set = (Button) findViewById(R.id.Set);
        sta = (Button) findViewById(R.id.Sta);

        suc = findViewById(R.id.suc);
        suc.setImageResource(R.drawable.good);

        fai = findViewById(R.id.fai);
        fai.setImageResource(R.drawable.sneaker2);

        // 설정 화면과 시작 화면에서 저장한 값을 기본 SharedPreferences에서 불러옵니다.
        SharedPreferences sharedPreferences = AppPreferences.get(this);
        Intent intent = getIntent();

        //걸음 수
        steps = findViewById(R.id.Steps);
        success = findViewById(R.id.success);
        fail = findViewById(R.id.fail);

        String ST = sharedPreferences.getString("STEP", "");
        if (ST == null || ST.equals("")) {
            ST = "0";
            steps.setText(ST);
            R1 = 0;
        } else {
            steps.setText(ST);
            R1 = Integer.parseInt(ST);
        }

        // Start 화면에서 돌아온 세션 걸음 수가 있으면 기존 걸음 수에 더합니다.
        String st = intent.getStringExtra("Step");
        if (st == null || st.equals("")) {
            steps.setText(ST);
        } else {
            R2 = Integer.parseInt(st);
            R3 = R1+R2;

            String Ste = String.valueOf(R3);
            steps.setText(Ste);
            }



        //목표
        goal = findViewById(R.id.Mgoal);
        String value = sharedPreferences.getString("goal", "");
        if (value == null || value.equals("")) {
            value = "10000";
            goal.setText(value);
            G = 10000;
        } else {
            goal.setText(value);
            G = Integer.parseInt(value);
        }

        //시간
        Hh = findViewById(R.id.HH);
        String HH = sharedPreferences.getString("HH", "");
        if (HH == null || HH.equals("")) {
            HH = "00";
            Hh.setText(HH);
            H1 = 00;
        } else {
            Hh.setText(HH);
            String H6 = Hh.getText().toString();
            H3 = Integer.parseInt(H6);
            H1 = Integer.parseInt(HH);
        }

        Mm = findViewById(R.id.MM);
        String MM = sharedPreferences.getString("MM", "");
        if (MM == null || MM.equals("")) {
            MM = "00";
            Mm.setText(MM);
            M1 = 00;
        } else {
            Mm.setText(MM);

            String M6 = Mm.getText().toString();
            M3 = Integer.parseInt(M6);

            M1 = Integer.parseInt(MM);
        }

        Ss = findViewById(R.id.SS);
        String SS = sharedPreferences.getString("SS", "");
        if (SS == null || SS.equals("")) {
            SS = "00";
            Ss.setText(SS);
            S1 = 00;
        } else {
            Ss.setText(SS);
            S1 = Integer.parseInt(SS);
        }



        //초
        // Start 화면에서 넘어온 초 단위 기록을 누적하고 60초 이상이면 분으로 올림 처리합니다.
        String ss = intent.getStringExtra("ss");
        if (ss == null || ss.equals("")) {
            Ss.setText(SS);
        } else {
            S2 = Integer.parseInt(ss);
            S3 = S1+S2;
            if (S3 >= 60) {
                M4 = 1;

                S5 = S3%60;
                String Se = String.valueOf(S5);
                if (S5 < 10) {
                    Ss.setText("0"+Se);
                } else {
                    Ss.setText(Se);
                }
            } else {
                String Se = String.valueOf(S3);
                if (S3 < 10) {
                    Ss.setText("0"+Se);
                } else {
                    Ss.setText(Se);
                }
                M4 = 0;
            }
        }

        //분
        // Start 화면에서 넘어온 분 단위 기록을 누적하고 60분 이상이면 시간으로 올림 처리합니다.
        String mm = intent.getStringExtra("mm");
        if (mm == null || mm.equals("")) {
            Mm.setText(MM);
        } else {
            M2 = Integer.parseInt(mm);
            M3 = M1+M2+M4;
            if (M3 >= 60) {
                H4 = 1;

                M5 = M3%60;
                String Mu = String.valueOf(M5);
                if (M5 < 10) {
                    Mm.setText("0"+Mu);
                } else {
                    Mm.setText(Mu);
                }
            } else {
                String Mu = String.valueOf(M3);
                if (M3 < 10) {
                    Mm.setText("0"+Mu);
                } else {
                    Mm.setText(Mu);
                }
                H4 = 0;
            }
        }

        //시
        // Start 화면에서 넘어온 시간 기록을 기존 누적 시간에 더합니다.
        String hh = intent.getStringExtra("hh");
        if (hh == null || hh.equals("")) {
            Hh.setText(HH);
        } else {
            H2 = Integer.parseInt(hh);
            H3 = H1+H2+H4;

            String Ho = String.valueOf(H3);
            if (H3 < 10) {
                Hh.setText("0"+Ho);
            } else {
                Hh.setText(Ho);
            }
        }

        DecimalFormat DF = new DecimalFormat("#.##");
        //이동거리
        Km = findViewById(R.id.Km);
        String KK = sharedPreferences.getString("KK", "");
        if (KK == null || KK.equals("")) {
            KK = "0.00";
            Km.setText(KK);
            mKM = 0.0;
        } else {
            Km.setText(KK);
            double K2 = Double.parseDouble(KK);
            mKM = K2;
        }

        // Start 화면에서 계산된 이동 거리를 기존 누적 거리와 합산합니다.
        String kk = intent.getStringExtra("Km");
        if (kk == null || kk.equals("")) {
            Km.setText(KK);
        } else {
            double K1 = Double.parseDouble(kk);
            mKM = K1 + mKM;
            String K3 = DF.format(mKM);
            Km.setText(K3);
        }


        //소모 칼로리
        Cal = findViewById(R.id.Calorie);
        String CC = sharedPreferences.getString("CC", "");
        if (CC == null || CC.equals("")) {
            CC="0.00";
            Cal.setText(CC);
            mCA = 0.00;
        } else {
            Cal.setText(CC);
            double C2 = Double.parseDouble(CC);
            mCA = C2;
        }

        // Start 화면에서 계산된 칼로리를 기존 누적 칼로리와 합산합니다.
        String cc = intent.getStringExtra("Cal");
        if (cc == null || cc.equals("")) {
            Cal.setText(CC);
        } else {
            double C1 = Double.parseDouble(cc);
            mCA = C1 + mCA;
            String C3 = DF.format(mCA);
            Cal.setText(C3);
        }


        //시작 버튼--------------------------------------------------------------------------------------------------------------
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStart = new Intent(getApplicationContext(), Start.class);
                String STE = steps.getText().toString();
                intentStart.putExtra("Step", STE);
                startActivity(intentStart);
            }
        });


        //설정 버튼-------------------------------------------------------------------------------------------------------------
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSet = new Intent(getApplicationContext(), Setting.class);
                startActivity(intentSet);
            }
        });


        //통계 버튼-----------------------------------------------------------------------------------------------------------------
        sta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSta = new Intent(getApplicationContext(), Statistics.class);
                String STE = steps.getText().toString();
                String k = Km.getText().toString();
                String c = Cal.getText().toString();
                String Su = Suc;

                intentSta.putExtra("Step", STE);
                intentSta.putExtra("Km",k);
                intentSta.putExtra("Cal", c);
                intentSta.putExtra("Suc", Su);

                startActivity(intentSta);
            }
        });

        // 기록 초기화 버튼은 현재 화면 표시값만 기본값으로 되돌립니다.
        re = findViewById(R.id.reset);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("기록 초기화");
                dlg.setMessage("오늘 기록을 초기화 하시겠습니까? \n꼭 오류가 있을 시에만 초기화 해주세요.");
                dlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                steps.setText("0");
                                Hh.setText("00");
                                Mm.setText("00");
                                Ss.setText("00");
                                Km.setText("0.0");
                                Cal.setText("0.00");
                                fail.setVisibility(View.VISIBLE);
                                success.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"초기화 되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });


        //오늘 날짜 기능
        day = (TextView) findViewById(R.id.Dey);
        day.setText(getTime());


        //활동 퍼미션 확인
        // 걸음 수 권한이 거부된 상태라면 런타임 권한을 요청합니다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        updateGoalStatus();
    }

    //오늘 날짜 확인
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now); //날짜를 담을 변수 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA); //년-월-일
        return dateFormat.format(date);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateGoalStatus();
    }

    private void updateGoalStatus() {
        if (success == null || fail == null) {
            return;
        }
        // 누적 걸음 수가 목표 이상이면 성공 영역을 보여주고, 아니면 실패 영역을 보여줍니다.
        if (R3 >= G || R1 >= G ) {
            Suc = "TT";
            success.setVisibility(View.VISIBLE);
            fail.setVisibility(View.INVISIBLE);

        } else {
            Suc = "NN";
            fail.setVisibility(View.VISIBLE);
            success.setVisibility(View.INVISIBLE);
        }
    }

    //날씨
    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = getXmlData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 백그라운드 스레드에서 가져온 날씨 값을 UI 스레드에서 화면에 반영합니다.
                        Temperature = findViewById(R.id.Temperature);
                        if (Tem == null || Sky == null || Pre == null) {
                            Temperature.setText("-");
                            TemCondition = findViewById(R.id.TemCondition);
                            TemCondition.setText("날씨 정보를 불러오지 못했습니다");
                            return;
                        }
                        Temperature.setText(Tem+"º");

                        TemCondition = findViewById(R.id.TemCondition);
                        if ("0".equals(Pre)) {
                            TemCondition.setText(Sky);
                        } else {
                            TemCondition.setText(Sky+" 그리고 "+Pre);
                        }

                        // 하늘 상태와 강수 형태에 따라 날씨 이미지를 바꿉니다.
                        ima = findViewById(R.id.imageView);
                        if ("0".equals(Pre)) {
                            if ("맑음".equals(Sky)) {
                                ima.setImageResource(R.drawable.sunny);
                            } else if ("구름많음".equals(Sky) || "흐림".equals(Sky)) {
                                ima.setImageResource(R.drawable.cloudy);
                            }
                        } else if ( "진눈개비".equals(Pre) || "빗방울".equals(Pre) || "빗방울 눈날림".equals(Pre)) {
                            ima.setImageResource(R.drawable.rain);
                        }  else if ("비".equals(Pre) || "소나기".equals(Pre)) {
                            ima.setImageResource(R.drawable.heavyrain);
                        } else if ("눈".equals(Pre)) {
                            ima.setImageResource(R.drawable.heavysnow);
                        } else if ("눈날림".equals(Pre)) {
                            ima.setImageResource(R.drawable.snow);
                        }
                        showWeatherToast();
                    }
                });
            }
        }).start();
    }

    private void showWeatherToast() {
        SharedPreferences sharedPreferences = AppPreferences.get(this);
        String Mell = sharedPreferences.getString("MSW", "0");
        if (!"1".equals(Mell)) {
            return;
        }

        if ("맑음".equals(Sky)) {
            Toast.makeText(getApplicationContext(), "오늘은 날이 맑아요! 신나게 걸어볼까요?", Toast.LENGTH_SHORT).show();
        } else if ("비".equals(Pre) || "진눈개비".equals(Pre) || "소나기".equals(Pre) || "빗방울".equals(Pre) || "빗방울 눈날림".equals(Pre)) {
            Toast.makeText(getApplicationContext(), "오늘은 비가 오네요! 우산 챙기기 잊지 마세요!", Toast.LENGTH_SHORT).show();
        } else if ("눈".equals(Pre) || "눈날림".equals(Pre)) {
            Toast.makeText(getApplicationContext(), "오늘은 눈이 오네요! 눈길은 미끄러우니 조심하세요!", Toast.LENGTH_SHORT).show();
        }
    }

    String getXmlData() {
        // 기상청 단기예보 XML API를 호출하고 필요한 fcstValue 순서의 값을 추출합니다.
        StringBuffer buffer = new StringBuffer();

        String serviceKey = key;
        String numOfRows = "100";
        String pageNo = "1";

        SimpleDateFormat real_time = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String base_data = real_time.format(time);
        String base_time = "0600";
        String nx = "58";
        String ny = "121";

        // 현재 코드는 고정 좌표(nx, ny)와 오전 6시 기준 예보를 사용합니다.
        String queryUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey="
                + serviceKey + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&base_date=" + base_data + "&base_time=" + base_time + "&nx=" + nx + "&ny=" + ny;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            int i = 0;
            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("item")) ; // 첫번째 검색결과
                        else if (tag.equals("fcstValue")) {
                            i++;
                            if (i == 1) {
                                //기온
                                xpp.next();
                                Tem = xpp.getText();
                                buffer.append(xpp.getText() + "ºC");
                            }
                            if (i == 6) {
                                //하늘 상태
                                xpp.next();
                                if (xpp.getText().equals("1")) {
                                    Sky = "맑음";
                                    buffer.append("맑음");
                                } else if (xpp.getText().equals("3")) {
                                    Sky = "구름많음";
                                    buffer.append("구름많음");
                                } else if (xpp.getText().equals("4")) {
                                    Sky = "흐림";
                                    buffer.append("흐림");
                                }
                            }
                            if (i == 7) {
                                //강수 형태
                                xpp.next();
                                if (xpp.getText().equals("0")) {
                                    buffer.append("없음");
                                    Pre = "0";
                                } else if (xpp.getText().equals("1")) {
                                    buffer.append("비");
                                    Pre = "비";
                                } else if (xpp.getText().equals("2")) {
                                    buffer.append("비+눈(진눈개비)");
                                    Pre = "진눈개비";
                                } else if (xpp.getText().equals("3")) {
                                    buffer.append("눈");
                                    Pre = "눈";
                                } else if (xpp.getText().equals("4")) {
                                    buffer.append("소나기");
                                    Pre = "소나기";
                                } else if (xpp.getText().equals("5")) {
                                    buffer.append("빗방울");
                                    Pre = "빗방울";
                                } else if (xpp.getText().equals("6")) {
                                    buffer.append("빗방울+눈날림");
                                    Pre = "빗방울 눈날림";
                                } else if (xpp.getText().equals("7")) {
                                    buffer.append("눈날림");
                                    Pre = "눈날림";
                                }
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기
                        if (tag.equals("item")) buffer.append("\n");
                        break;
                }
                eventType= xpp.next();
            }

        } catch (Exception e) {
            // 날씨 정보를 가져오지 못한 경우 현재 화면 값은 갱신하지 않습니다.
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCurrentRecord();
    }

    //저장
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCurrentRecord();
    }

    private void saveCurrentRecord() {
        if (steps == null || goal == null || Hh == null || Mm == null || Ss == null || Km == null || Cal == null) {
            return;
        }
        // 화면이 종료될 때 현재 표시 중인 누적 기록을 저장합니다.
        SharedPreferences sharedPreferences = AppPreferences.get(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String STEP = steps.getText().toString();
        editor.putString("STEP", STEP);

        String value = goal.getText().toString();
        editor.putString("goal", value);

        String HH = Hh.getText().toString();
        editor.putString("HH", HH);

        String MM = Mm.getText().toString();
        editor.putString("MM", MM);

        String SS = Ss.getText().toString();
        editor.putString("SS", SS);

        String KK = Km.getText().toString();
        editor.putString("KK", KK);

        String CC = Cal.getText().toString();
        editor.putString("CC", CC);

        editor.apply();
    }
}
