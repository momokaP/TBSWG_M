package com.example.term_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static TextView leftturn;
    public static TextView nowturn;
    public static TextView howmany_tile;
    public static TextView howmany_unit;
    public static TextView textName;
    public static TextView textOwner;
    public static TextView textView1;
    public static TextView textView2;
    public static Button turnbutton;
    public static Button button2;


    private FrameLayout mapContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HexMapView hexMapView = new HexMapView(this);

        leftturn = findViewById(R.id.leftturn);
        leftturn.setText("남은 턴");
        
        nowturn = findViewById(R.id.nowturn);
        nowturn.setText("현재 턴");
        
        turnbutton = findViewById(R.id.turnbutton);
        turnbutton.setText("턴 넘기기");
        
        howmany_tile = findViewById(R.id.howmany_tile);
        howmany_tile.setText("점령한 타일 수 : 0");

        howmany_unit = findViewById(R.id.howmany_unit);
        howmany_unit.setText("소유한 유닛 수 : 0");

        textName = findViewById(R.id.name);
        textName.setText(" ");

        textOwner = findViewById(R.id.owner);
        textOwner.setText(" ");

        textView1 = findViewById(R.id.textView1);
        textView1.setText(" ");

        textView2 = findViewById(R.id.textView2);
        textView2.setText(" ");

        button2 = findViewById(R.id.button2);

        turnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(GameSetting.getWhoAmI()==GameSetting.getCurrentPlayer()){
                //}
                hexMapView.nextTurn();
                nowturn.setText("현재 턴 : "+GameSetting.getTurn());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = GameSetting.getUser(GameSetting.getWhoAmI());
                if(user.getHowManyTiles() >= GameSetting.getCost()){
                    hexMapView.produceUnit();
                    GameSetting.multiplyCost(2);
                    MainActivity.textView1.setText("비용 : "+GameSetting.getCost());
                }

            }
        });

        // 레이아웃 요소 초기화
        mapContainer = findViewById(R.id.mapContainer);
        showHexMap(hexMapView);
    }

    public void showProductionOptions(boolean isProduction) {
        if (isProduction) {
            button2.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.GONE);
        } else {
            button2.setVisibility(View.GONE);
            textView2.setVisibility(View.VISIBLE);
        }
    }

    // HexMapView를 동적으로 추가하는 메서드
    private void showHexMap(HexMapView hexMapView) {
        mapContainer.removeAllViews();  // 기존 뷰를 제거 (새로 그리기 전에)
        mapContainer.addView(hexMapView); // HexMapView를 mapContainer에 추가
    }
}