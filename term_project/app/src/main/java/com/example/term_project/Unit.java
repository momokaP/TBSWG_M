package com.example.term_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Unit {
    private OnUnitChangeListener listener; // 상태 변경 알림을 위한 인터페이스

    // 인터페이스 정의
    public interface OnUnitChangeListener {
        void onUnitChanged(); // 상태 변경 시 호출
    }

    // 리스너 설정
    public void setOnUnitChangeListener(OnUnitChangeListener listener) {
        this.listener = listener;
    }

    private String name = "유닛";

    private int x, y; // 유닛의 중심 좌표
    private String defaultColor = "blue"; // 기본 색상

    private String color = defaultColor; // 현재 색상
    private boolean isClicked = false; // 클릭 상태
    private int UNIT_RADIUS; // 육각형의 반지름
    private int row;
    private int col;

    private int health = 3;
    private int damage = 1;

    private boolean isMovable = true; // 이동 가능 여부

    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    private boolean isDamaged = false;

    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public int getHealth() {
        return health;
    }

    public void addHealth(int health) {
        this.health += health;
        // 피격 시 애니메이션 트리거
        if (health < 0) {
            triggerHitAnimation();
        }
    }

    // 피격 애니메이션 메서드
    // 피격 애니메이션
    private void triggerHitAnimation() {
        int originalRadius = UNIT_RADIUS;

        // 변경 알림
        if (listener != null) {
            listener.onUnitChanged();
        }

        isDamaged = true;

        // 200ms 후 복구
        new android.os.Handler().postDelayed(() -> {
            UNIT_RADIUS = originalRadius;
            isDamaged = false;

            // 복구 알림
            if (listener != null) {
                listener.onUnitChanged();
            }
        }, 200);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public void setUnitRadius(int unitRadius) {
        UNIT_RADIUS = unitRadius;
    }

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 색상 설정
    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }

    // 유닛 그리기
    public void draw(Canvas canvas, Paint paint, float offsetX, float offsetY) {
        if(isDamaged) UNIT_RADIUS = (int) (UNIT_RADIUS * 0.1);

        // 원을 그리기 위해 drawCircle 사용
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK); // 원의 테두리 색깔
        canvas.drawCircle(x + offsetX, y + offsetY, UNIT_RADIUS, paint);

        paint.setStyle(Paint.Style.FILL);

        if(isMovable){
            paint.setColor(getColorFromString(color));
        } else{
            paint.setColor(getColorFromString(invertColor(color)));
        }

        canvas.drawCircle(x + offsetX, y + offsetY, UNIT_RADIUS, paint);
    }

    // 색상 문자열을 실제 색상 값으로 변환
    private int getColorFromString(String color) {
        switch (color) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "cyan":
                return Color.CYAN;
            case "darkblue":
                return Color.parseColor("#000064"); // DarkBlue
            case "darkred":
                return Color.parseColor("#640000");
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.parseColor("#FFA500"); // Orange
            default:
                return Color.WHITE;
        }
    }

    private String invertColor(String color) {
        if (color.equals("red")) return "darkred";
        if (color.equals("blue")) return "darkblue";
        return color;
    }

    // 클릭 상태 업데이트
    public void updateClick(float mouseX, float mouseY, float offsetX, float offsetY) {
        boolean wasClicked = isClicked;
        isClicked = isPointInUnit(mouseX, mouseY, offsetX, offsetY); // 클릭 상태 업데이트
    }

    // 점이 원 내부에 있는지 확인
    public boolean isPointInUnit(float mouseX, float mouseY, float offsetX, float offsetY) {
        float dx = mouseX - (x + offsetX);
        float dy = mouseY - (y + offsetY);
        return Math.sqrt(dx * dx + dy * dy) <= UNIT_RADIUS; // 원의 반지름 이내에 있는지 확인
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public boolean isDead() {
        return this.health <= 0;
    }

}
