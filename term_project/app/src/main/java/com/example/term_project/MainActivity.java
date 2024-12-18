package com.example.term_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static TextView whosturn;
    public static TextView nowturn;
    public static TextView howmany_tile;
    public static TextView howmany_computerai_tile;
    public static TextView textName;
    public static TextView textOwner;
    public static TextView textView1;
    public static TextView textView2;
    public static Button turnbutton;
    public static Button button2;
    public static TextView line1;
    public static TextView line2;

    private static MainActivity instance;

    private static AlertDialog dialog;

    private static FrameLayout mapContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HexMapView hexMapView = new HexMapView(this);
        GameSetting.setContext(this);

        line1 = findViewById(R.id.line1);
        line1.setText(" ");
        line2 = findViewById(R.id.line2);
        line2.setText(" ");

        whosturn = findViewById(R.id.whosturn);
        whosturn.setText(" ");
        
        nowturn = findViewById(R.id.nowturn);
        nowturn.setText("현재 턴 : 1");
        
        turnbutton = findViewById(R.id.turnbutton);
        turnbutton.setText("턴 넘기기");
        
        howmany_tile = findViewById(R.id.howmany_tile);
        howmany_tile.setText("내가 점령한 타일 수 : 0");

        howmany_computerai_tile = findViewById(R.id.howmany_computerai_tile);
        howmany_computerai_tile.setText("상대가 점령한 타일 수 : 0");

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
                if(GameSetting.getWhoAmI() == GameSetting.getCurrentPlayer()) {
                    hexMapView.nextTurn();
                    String next = GameSetting.getNextPlayer();
                    GameSetting.setCurrentPlayer(next);

                    if (GameSetting.getCurrentPlayer() != GameSetting.getWhoAmI()) {
                        turnbutton.setVisibility(View.INVISIBLE);

                        // 플래그 초기화
                        final AtomicBoolean isAICompleted = new AtomicBoolean(false);
                        final AtomicBoolean isAPICalled = new AtomicBoolean(false);

                        // AI 작업 실행
                        hexMapView.computerAi(() -> {
                            isAICompleted.set(true);
                            checkAndShowTurnButton(isAICompleted, isAPICalled);
                        });

                        // GPT API 호출
                        callGPTAPI("턴제 게임에서 상대방을 도발하는 대사를 20글자 이내로 창의적으로 작성해줘.", () -> {
                            isAPICalled.set(true);
                            checkAndShowTurnButton(isAICompleted, isAPICalled);
                        });

                        hexMapView.nextTurn();
                    }
                    whosturn.setText(GameSetting.getCurrentPlayer()+" 의 턴");
                    nowturn.setText("현재 턴 : " + (int)(GameSetting.getTurn()/2));
                }

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
                else{
                    // Toast 메시지 표시
                    // Toast 메시지를 1초 동안 표시
                    final Toast toast = Toast.makeText(v.getContext(), "타일 수가 부족합니다!", Toast.LENGTH_SHORT);
                    toast.show();
                    new Handler().postDelayed(toast::cancel, 500); // 0.5초 후에 취소
                }

            }
        });

        instance = this;

        // 레이아웃 요소 초기화
        mapContainer = findViewById(R.id.mapContainer);
        showHexMap(hexMapView);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    // 모든 작업 완료 시 버튼 표시
    private void checkAndShowTurnButton(AtomicBoolean isAICompleted, AtomicBoolean isAPICalled) {
        if (isAICompleted.get() && isAPICalled.get()) {
            turnbutton.setVisibility(View.VISIBLE);
            String next = GameSetting.getNextPlayer();
            GameSetting.setCurrentPlayer(next);
            whosturn.setText(GameSetting.getCurrentPlayer()+" 의 턴");
        }
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
    private void callGPTAPI(String prompt, Runnable onComplete) {
        String apiKey =
                "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"
                ;//"asdf"; // 환경 변수나 안전한 저장소에서 가져오는 것을 권장
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
                onComplete.run();  // 작업 완료 콜백 실행
            }

            @Override
            public void onFailure(Call<GPTResponse> call, Throwable t) {
                line1.setText("도발 대사 요청 실패!");
                onComplete.run();  // 작업 완료 콜백 실행
            }
        });
    }

    public static void showGameEndDialog(String winnerName, String reason) {
        MainActivity activity = MainActivity.getInstance();

        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return; // 액티비티가 종료된 경우 다이얼로그 표시를 중단
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss(); // 기존에 표시 중인 다이얼로그가 있으면 닫기
        }

        dialog = new AlertDialog.Builder(MainActivity.getInstance())  // MainActivity의 Context를 가져옴
                .setTitle("게임 종료")
                .setMessage("우승자: " + winnerName + "\n이유: " + reason)
                .setPositiveButton("확인", (dialog, which) -> {
                    // 메뉴 화면으로 이동
                    Intent intent = new Intent(MainActivity.getInstance(), MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    resetGame(MainActivity.getInstance());
                    MainActivity.getInstance().startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }

    public static void resetGame(Context context) {
        // 1. 게임 설정 초기화
        GameSetting.reset(); // GameSetting 클래스에 reset 메서드 추가 필요

        // 2. UI 초기화 (MainActivity에서 수행)
        resetUI(context);
    }

    private static void resetUI(Context context) {
        // 1. UI 요소 초기화
        whosturn.setText("남은 턴");
        nowturn.setText("현재 턴 : 1");
        turnbutton.setText("턴 넘기기");
        howmany_tile.setText("내가 점령한 타일 수 : 0");
        howmany_computerai_tile.setText("상대가 점령한 타일 수 : 0");
        textName.setText(" ");
        textOwner.setText(" ");
        textView1.setText(" ");
        textView2.setText(" ");
        line1.setText(" ");
        line2.setText(" ");

        // 2. 지도 및 맵 초기화
        HexMapView hexMapView = new HexMapView(context);
        GameSetting.reset();
        mapContainer.removeAllViews();
        mapContainer.addView(hexMapView); // HexMapView를 다시 추가

        // 3. 턴, 비용, AI 작업 등 초기화 (게임 시작 전 상태)
        turnbutton.setVisibility(View.VISIBLE);
        button2.setVisibility(View.GONE); // 적절한 초기화 (필요한 경우)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;  // 액티비티가 종료될 때 참조 제거
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss(); // 다이얼로그가 표시되고 있으면 닫기
        }
    }
}