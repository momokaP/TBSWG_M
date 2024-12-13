package com.example.term_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Unit {
    private int x, y; // 유닛의 중심 좌표
    private String defaultColor = "blue"; // 기본 색상
    private String color = defaultColor; // 현재 색상
    private boolean isClicked = false; // 클릭 상태
    private int UNIT_RADIUS; // 육각형의 반지름
    private int row;
    private int col;

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

    // 유닛 그리기
    public void draw(Canvas canvas, Paint paint, float offsetX, float offsetY) {
        // 원을 그리기 위해 drawCircle 사용
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK); // 원의 테두리 색깔
        canvas.drawCircle(x + offsetX, y + offsetY, UNIT_RADIUS, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getColorFromString(color));
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
            case "lightblue":
                return Color.parseColor("#ADD8E6"); // LightBlue
            case "darkblue":
                return Color.parseColor("#00008B"); // DarkBlue
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.parseColor("#FFA500"); // Orange
            default:
                return Color.WHITE;
        }
    }

    // 클릭 상태 업데이트
    public void updateClick(float mouseX, float mouseY, float offsetX, float offsetY) {
        boolean wasClicked = isClicked;
        isClicked = isPointInCircle(mouseX, mouseY, offsetX, offsetY); // 클릭 상태 업데이트
    }

    // 점이 원 내부에 있는지 확인
    private boolean isPointInCircle(float mouseX, float mouseY, float offsetX, float offsetY) {
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

}
