// 지도 화면을 담당하는 파일입니다.
package com.cookandroid.a2b_03_onestep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// Google Map을 화면에 표시하고 기본 위치 마커를 보여줍니다.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 지도 Fragment가 준비되면 onMapReady에서 초기 지도 상태를 설정합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // TODO: 위치 권한과 경로 기록 기능을 연결하면 기본 위치 대신 사용자 위치를 표시해야 합니다.
        googleMap = map;
        LatLng seoul = new LatLng(37.5665, 126.9780);
        googleMap.addMarker(new MarkerOptions().position(seoul).title("기본 위치"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15));
    }
}
