// 앱의 기본 서비스를 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// Manifest에 등록된 기본 Service입니다.
// 현재는 만보기 측정 흐름과 직접 연결되지 않고 서비스 생명주기 로그만 확인합니다.
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
            // TODO: 실제 서비스 명령을 연결할 때 extra key를 명확한 이름으로 교체해야 합니다.
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
        // 바인드 서비스로 사용하지 않으므로 Binder를 제공하지 않습니다.
        return null;
    }
}
