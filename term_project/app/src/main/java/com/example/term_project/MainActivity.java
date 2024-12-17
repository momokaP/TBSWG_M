package com.example.term_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

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
    public static TextView line1;
    public static TextView line2;


    private FrameLayout mapContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HexMapView hexMapView = new HexMapView(this);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

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

                // GPT API 호출
                callGPTAPI("턴제 게잉메서 상대방을 도발하는 대사를 20글자 이내로 창의적으로 작성해줘.");
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

    // GPT API 호출 메서드
    private void callGPTAPI(String prompt) {
        String apiKey = "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"; // 환경 변수나 안전한 저장소에서 가져오는 것을 권장
        Retrofit retrofit = RetrofitClient.getClient(apiKey);
        GPTService gptService = retrofit.create(GPTService.class);

        GPTRequest request = new GPTRequest(prompt);

        Log.d("GPTRequest", "Request Data: " + new Gson().toJson(request));

        gptService.getResponse(request).enqueue(new retrofit2.Callback<GPTResponse>() {
            @Override
            public void onResponse(Call<GPTResponse> call, retrofit2.Response<GPTResponse> response) {
                System.out.println(response.message());
                if (response.isSuccessful() && response.body() != null) {
                    // 응답 본문이 null이 아닌지 확인

                    GPTResponse gptResponse = response.body();

                    // 전체 응답을 JSON 형식으로 로그로 확인
                    //String fullResponse = new Gson().toJson(gptResponse);
                    //Log.d("GPTResponse", "Full Response: " + fullResponse);

                    // 다른 필드들도 출력
                    //Log.d("GPTResponse", "Response ID: " + gptResponse.getId());
                    //Log.d("GPTResponse", "Response Object: " + gptResponse.getObject());
                    //Log.d("GPTResponse", "Response Created: " + gptResponse.getCreated());
                    //Log.d("GPTResponse", "Response Model: " + gptResponse.getModel());

                    if (gptResponse.getChoices() != null && !gptResponse.getChoices().isEmpty()) {
                        // 선택지가 비어 있지 않다면, 첫 번째 선택지를 가져옴
                        String gptResponseText = gptResponse.getChoices().get(0).getMessage().getContent();
                        if (gptResponseText != null) {
                            // 텍스트가 null이 아니면 trim() 처리
                            gptResponseText = gptResponseText.trim();
                            line1.setText(gptResponseText);
                        } else {
                            // 텍스트가 null인 경우 처리
                            line1.setText("응답 텍스트가 null입니다.");
                        }
                    } else {
                        // 선택지가 없을 경우 처리
                        line1.setText("선택지가 비어 있습니다.");
                    }
                } else {
                    // 응답 실패 처리
                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e("GPT_API_ERROR", "Error: " + errorResponse);  // 에러 본문 출력
                        line1.setText("응답 실패: t");
                        System.out.println(errorResponse);
                    } catch (IOException e) {
                        Log.e("GPT_API_ERROR", "Error reading error body", e);
                        line1.setText("응답 실패: c");
                        System.out.println(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GPTResponse> call, Throwable t) {
                line1.setText("도발 대사 요청 실패!");
            }
        });
    }
}