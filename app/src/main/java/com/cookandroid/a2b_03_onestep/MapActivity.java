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

// Google Map을 화면에 표시하고 기본 위치를 보여줍니다.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 지도 Fragment가 준비되면 onMapReady에서 지도 설정을 진행합니다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // 우선 지도 표시 복구가 목적이라 기본 위치를 서울로 잡아 화면에 표시합니다.
        googleMap = map;
        LatLng seoul = new LatLng(37.5665, 126.9780);
        googleMap.addMarker(new MarkerOptions().position(seoul).title("기본 위치"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15));
    }
}
