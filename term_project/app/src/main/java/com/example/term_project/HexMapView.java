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

import android.widget.TextView;

import androidx.annotation.NonNull;

// 육각형 맵을 그리는 클래스
public class HexMapView extends View {
    private static final float SQRT_3 = (float)Math.sqrt(3);  // 루트3 값
    private static final float HEX_RADIUS = 100f; // 육각형 한 변의 길이
    private static final int NUM_COLS = 9; // 열의 개수
    private static final int NUM_ROWS = 9; // 행의 개수
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
        int unitRadius = GameSetting.getUnitRadius();

        scaleGestureDetector.onTouchEvent(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                DownX = (int) event.getX();
                DownY = (int) event.getY();

                for(int row=0; row<NUM_ROWS; row++){
                    for(int col=0; col<NUM_COLS; col++){
                        // 유닛 클릭 확인
                        Unit unit = GameSetting.getUnit(row, col);
                        if (unit != null && unit.isPointInUnit(DownX, DownY, offsetX, offsetY)) {
                            GameSetting.setSelectedunit(unit);
                            GameSetting.resetHoverTiles();
                            // 이동 가능 타일 강조
                            GameSetting.highlightMovableTiles(unit);
                            invalidate(); // 화면 다시 그리기
                            return true;
                        }

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
                Unit selectedUnit = GameSetting.getSelectedunit();
                if (selectedUnit != null) {
                    for (int row = 0; row < NUM_ROWS; row++) {
                        for (int col = 0; col < NUM_COLS; col++) {
                            HexTile targetTile = GameSetting.getHexTile(row, col);
                            if(targetTile.isHovered()) {
                                if (targetTile.isMovable()) {
                                    GameSetting.moveUnit(selectedUnit, row, col);
                                    GameSetting.setSelectedunit(null); // 선택 해제
                                    // 이동 가능 상태 초기화
                                    GameSetting.resetMovableTiles();

                                    // TextView에 텍스트 설정
                                    if(selectedUnit.getUser() == "user1"){
                                        int howmManyTiles = GameSetting.getUser(selectedUnit.getUser()).getHowManyTiles();
                                        MainActivity.howmany_tile.setText("점령한 타일 수 : " + howmManyTiles);
                                    }

                                    invalidate(); // 화면 다시 그리기
                                    return true;
                                }

                                if(GameSetting.getUnit(row, col)==null){
                                    System.out.println("아님");
                                    // 이동 가능 상태 초기화
                                    GameSetting.resetMovableTiles();
                                    invalidate(); // 화면 다시 그리기
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
        }

        return true;
    }

    public void createHexMap(int rows, int cols, int gameOffsetX, int gameOffsetY, Canvas canvas, Paint paint) {
        int HEX_RADIUS = GameSetting.getHexRadius();
        int horizontalSpacing = (int) (Math.sqrt(3) * HEX_RADIUS); // 수평 간격
        int verticalSpacing = (int) (1.5 * HEX_RADIUS); // 수직 간격

        int UINT_RADIUS = GameSetting.getUnitRadius();

        // 중앙 정렬을 위한 x, y 오프셋
        int offsetX = (canvas.getWidth() - cols * horizontalSpacing) / 2;
        int offsetY = (canvas.getHeight() - rows * verticalSpacing) / 2;

        // 빨간색으로 설정할 좌표들
        int[][] redTiles = {
                {1, 1}, {1, 4}, {1, 7},
                {4, 1}, {4, 4}, {4, 7},
                {7, 1}, {7, 4}, {7, 7}
        };

        // 랜덤으로 빨간 타일을 하나 선택
        // 첫 번째 랜덤 인덱스 선택
        int randomIndex1 = (int) (Math.random() * redTiles.length);

        // 두 번째 랜덤 인덱스를 첫 번째와 다르게 선택
        int randomIndex2;
        do {
            randomIndex2 = (int) (Math.random() * redTiles.length);
        } while (randomIndex1 == randomIndex2);

        // 좌표 가져오기
        int randomRow1 = redTiles[randomIndex1][0];
        int randomCol1 = redTiles[randomIndex1][1];

        int randomRow2 = redTiles[randomIndex2][0];
        int randomCol2 = redTiles[randomIndex2][1];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 홀수 행이면 오른쪽으로 반절 이동시켜서 육각형 타일이 맞물리도록
                int x = col * horizontalSpacing + (row % 2) * (horizontalSpacing / 2);
                int y = row * verticalSpacing;

                x = x + offsetX;
                y = y + offsetY;

                if (GameSetting.isInitial()) {
                    HexTile tile = new HexTile(x, y);
                    GameSetting.setHexTile(row, col, tile);

                    // 빨간색으로 설정할 타일인지 확인
                    for (int[] redTile : redTiles) {
                        if (redTile[0] == row && redTile[1] == col) {
                            tile.setIsproduction(true);
                            break;
                        }
                    }

                    tile.setDirty(true);
                    tile.setHexRadius(GameSetting.getHexRadius());
                    tile.setRow(row);
                    tile.setCol(col);

                    // 타일 그리기
                    tile.draw(canvas, paint, gameOffsetX, gameOffsetY);
                } else {
                    HexTile tile = GameSetting.getHexTile(row, col);
                    tile.setPosition(x, y); // 위치 업데이트

                    // 빨간색으로 설정할 타일인지 확인
                    for (int[] redTile : redTiles) {
                        if (redTile[0] == row && redTile[1] == col) {
                            tile.setIsproduction(true);
                            break;
                        }
                    }

                    tile.setDirty(true);
                    tile.setHexRadius(GameSetting.getHexRadius());

                    // 타일 그리기
                    tile.draw(canvas, paint, gameOffsetX, gameOffsetY);

                    Unit unit = GameSetting.getUnit(row, col);
                    if(unit != null){
                        unit.setPosition(x, y);
                        unit.setUnitRadius(GameSetting.getUnitRadius());
                        unit.draw(canvas, paint, gameOffsetX, gameOffsetY);
                    }
                }
            }
        }

        if (GameSetting.isInitial()) {
            User user1 = new User("user1","blue");
            GameSetting.addUser(user1.getName(), user1);
            HexTile randomTile1 = GameSetting.getHexTile(randomRow1, randomCol1);

            if (randomTile1 != null) {
                Unit unit1 = new Unit(randomTile1.getX(), randomTile1.getY());
                GameSetting.setUnit(randomTile1.getRow(), randomTile1.getCol(), unit1);

                unit1.setUser(user1.getName());
                user1.addHowManyUnits(1);
                MainActivity.howmany_unit.setText("소유한 유닛 수 : " + user1.getHowManyUnits());
                unit1.setColor(user1.getColor());

                unit1.setUnitRadius(GameSetting.getUnitRadius());
                unit1.setRow(randomTile1.getRow());
                unit1.setCol(randomTile1.getCol());

                unit1.draw(canvas, paint, gameOffsetX, gameOffsetY);
            }

            User user2 = new User("user2", "red");
            GameSetting.addUser(user2.getName(), user2);
            HexTile randomTile2 = GameSetting.getHexTile(randomRow2, randomCol2);

            if (randomTile2 != null) {
                Unit unit2 = new Unit(randomTile2.getX(), randomTile2.getY());
                GameSetting.setUnit(randomTile2.getRow(), randomTile2.getCol(), unit2);

                unit2.setUser(user2.getName());
                user2.addHowManyUnits(1);
                unit2.setColor(user2.getColor());

                unit2.setUnitRadius(GameSetting.getUnitRadius());
                unit2.setRow(randomTile2.getRow());
                unit2.setCol(randomTile2.getCol());

                unit2.draw(canvas, paint, gameOffsetX, gameOffsetY);
            }
            GameSetting.setInitial(false);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            int oldHexRaidus = GameSetting.getHexRadius();
            int offsetX = GameSetting.getMapOffset_X();
            int offsetY = GameSetting.getMapOffset_Y();

            // 스케일 팩터 계산
            float scaleFactor = detector.getScaleFactor();
            int newHexRadius = (int) (GameSetting.getHexRadius() * scaleFactor);

            // HEX_RADIUS 크기 제한 (최소/최대 크기 설정)
            newHexRadius = Math.max(50, Math.min(newHexRadius, 150)); // 최소 50, 최대 `50
            GameSetting.setHexRadius(newHexRadius);
            GameSetting.setUnitRadius((int) (newHexRadius*0.7));

            float scaleChange = (float) newHexRadius / oldHexRaidus;
            System.out.println(scaleChange+","+oldHexRaidus+","+GameSetting.getHexRadius());
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
