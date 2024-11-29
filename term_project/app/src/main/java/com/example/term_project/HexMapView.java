package com.example.term_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;

// 육각형 맵을 그리는 클래스
public class HexMapView extends View {
    private static final float SQRT_3 = (float)Math.sqrt(3);  // 루트3 값
    private static final float HEX_RADIUS = 100f; // 육각형 한 변의 길이
    private static final int NUM_COLS = 15; // 열의 개수
    private static final int NUM_ROWS = 15; // 행의 개수
    private Paint paint;
    private HexagonTile hexagonTile;
    private int DownX;
    private int DownY;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean multiTouch = false;

    public HexMapView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.GREEN); // 육각형의 색상
        hexagonTile = new HexagonTile(HEX_RADIUS);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offsetX = GameSetting.getMapOffset_X();
        int offsetY = GameSetting.getMapOffset_Y();
        createHexMap(NUM_ROWS, NUM_COLS, offsetX, offsetY, canvas, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int offsetX = GameSetting.getMapOffset_X();
        int offsetY = GameSetting.getMapOffset_Y();
        int hexRadius = GameSetting.getHexRadius();

        scaleGestureDetector.onTouchEvent(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                DownX = (int) event.getX();
                DownY = (int) event.getY();
                for(int row=0; row<NUM_ROWS; row++){
                    for(int col=0; col<NUM_COLS; col++){
                        HexTile tile = GameSetting.getHexTile(row, col);
                        tile.updateHover(DownX,DownY,offsetX,offsetY);
                    }
                }
                Rect dirtyRect = new Rect(DownX - hexRadius, DownY - hexRadius, DownX + hexRadius, DownY + hexRadius); // 예: 손가락 주변 100px 영역만 갱신
                invalidate(dirtyRect);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                System.out.println("hello");
                multiTouch=true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    return true;  // 두 손가락 이상일 때는 이동 처리 안 함
                }
                if (multiTouch){
                    return true;
                }
                int dX = (int) event.getX() - DownX;
                int dY = (int) event.getY() - DownY;

                GameSetting.setMapOffset_X(offsetX+dX);
                GameSetting.setMapOffset_Y(offsetY+dY);

                DownX = (int) event.getX();
                DownY = (int) event.getY();

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                multiTouch=false;
                break;
        }

        return true;
    }

    public void createHexMap(int rows, int cols, int gameOffsetX, int gameOffsetY, Canvas canvas, Paint paint) {
        int HEX_RADIUS = GameSetting.getHexRadius();
        int horizontalSpacing = (int) (Math.sqrt(3) * HEX_RADIUS); // 수평 간격
        int verticalSpacing = (int) (1.5 * HEX_RADIUS); // 수직 간격

        // 중앙 정렬을 위한 x, y 오프셋
        int offsetX = (canvas.getWidth() - cols * horizontalSpacing) / 2;
        int offsetY = (canvas.getHeight() - rows * verticalSpacing) / 2;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 홀수 행이면 오른쪽으로 반절 이동시켜서 육각형 타일이 맞물리도록
                int x = col * horizontalSpacing + (row % 2) * (horizontalSpacing / 2);
                int y = row * verticalSpacing;

                x = x + offsetX;
                y = y + offsetY;

                //hexMap[row][col];
                if (GameSetting.isInitial()) {
                    HexTile tile = new HexTile(x, y);
                    GameSetting.setHexTile(row, col, tile);

                    tile.setColor("lightblue"); // 색상 설정
                    tile.setDirty(true);
                    tile.setHexRadius(GameSetting.getHexRadius());
                    tile.setRow(row);
                    tile.setCol(col);

                    // 타일 그리기
                    tile.draw(canvas, paint, gameOffsetX, gameOffsetY);
                } else {
                    HexTile tile = GameSetting.getHexTile(row,col);
                    tile.setPosition(x, y); // 위치 업데이트

                    tile.setColor("lightblue"); // 색상 설정
                    tile.setDirty(true);
                    tile.setHexRadius(GameSetting.getHexRadius());

                    // 타일 그리기
                    tile.draw(canvas, paint, gameOffsetX, gameOffsetY);
                }
            }
        }
        GameSetting.getHexTile(0,8).setColor("red");
        GameSetting.getHexTile(0,8).setDirty(true);
        GameSetting.getHexTile(0,8).draw(canvas, paint, gameOffsetX, gameOffsetY);
        if (GameSetting.isInitial()) GameSetting.setInitial(false);
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            int oldRaidus = GameSetting.getHexRadius();
            int offsetX = GameSetting.getMapOffset_X();
            int offsetY = GameSetting.getMapOffset_Y();

            // 스케일 팩터 계산
            float scaleFactor = detector.getScaleFactor();
            int newRadius = (int) (GameSetting.getHexRadius() * scaleFactor);

            // HEX_RADIUS 크기 제한 (최소/최대 크기 설정)
            newRadius = Math.max(50, Math.min(newRadius, 150)); // 최소 50, 최대 300
            GameSetting.setHexRadius(newRadius);

            float scaleChange = (float) newRadius / oldRaidus;
            System.out.println(scaleChange+","+oldRaidus+","+GameSetting.getHexRadius());
            float x = offsetX * scaleChange;
            float y = offsetY * scaleChange;
            GameSetting.setMapOffset_X((int) x);
            GameSetting.setMapOffset_Y((int) y);

            invalidate(); // 화면 다시 그리기
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 확대/축소 시작 시 호출
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // 확대/축소 종료 시 호출
        }
    }
}
