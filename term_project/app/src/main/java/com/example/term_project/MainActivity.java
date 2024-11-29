package com.example.term_project;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mapContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 레이아웃 요소 초기화
        mapContainer = findViewById(R.id.mapContainer);
        showHexMap();
    }

    // HexMapView를 동적으로 추가하는 메서드
    private void showHexMap() {
        HexMapView hexMapView = new HexMapView(this);
        mapContainer.removeAllViews();  // 기존 뷰를 제거 (새로 그리기 전에)
        mapContainer.addView(hexMapView); // HexMapView를 mapContainer에 추가
    }
}