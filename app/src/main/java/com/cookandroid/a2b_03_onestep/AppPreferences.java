// 앱에서 공통으로 사용하는 저장소를 제공하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import android.content.Context;
import android.content.SharedPreferences;

// 화면마다 같은 SharedPreferences 파일을 사용하도록 저장소 이름을 한 곳에서 관리합니다.
public class AppPreferences {

    private static final String NAME = "OneStep";

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }
}
