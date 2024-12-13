package com.example.term_project;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static TextView howmany_tile;
    public static TextView howmany_unit;


    private FrameLayout mapContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        howmany_tile = findViewById(R.id.howmany_tile);
        howmany_tile.setText("점령한 타일 수 : 0");

        howmany_unit = findViewById(R.id.howmany_unit);
        howmany_unit.setText("소유한 유닛 수 : 0");

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