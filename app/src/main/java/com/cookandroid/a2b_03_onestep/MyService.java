// 앱의 기본 서비스를 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// 현재 Manifest에 등록되어 있는 기본 Service입니다.
// 핵심 만보기 흐름과 직접 연결되어 있지는 않고, 시작/종료 로그 확인용에 가깝습니다.
public class MyService extends Service {
    public MyService() {
    }

    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate() called");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called");

        // 시스템이 서비스를 재시작하면서 intent를 넘기지 않는 경우를 방어합니다.
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            // 호출 측에서 전달한 명령 문자열을 로그로 확인합니다.
            String command = intent.getStringExtra("채ㅡㅡㅁㅇ");

            Log.d(TAG, "전달" + command);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy(){
        super.onDestroy();

        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
