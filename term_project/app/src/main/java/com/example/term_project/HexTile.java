package com.example.term_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;

public class HexTile {
    private int x, y; // 타일의 중심 좌표
    private String defaultColor = "lightblue"; // 기본 색상
    private String color = defaultColor; // 현재 색상
    private boolean isHovered = false; // hover 상태
    private boolean isClicked = false; // 클릭 상태
    private boolean dirty = true; // 타일의 상태가 변경되었을 때만 true로 설정
    private int HEX_RADIUS; // 육각형의 반지름
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

    public void setHexRadius(int hexRadius) {
        HEX_RADIUS = hexRadius;
    }

    public HexTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    // 색상 설정
    public void setColor(String color) {
        this.color = color;
    }

    // 색상 반전 함수
    private String invertColor(String color) {
        if (color.equals("red")) return "cyan";
        if (color.equals("lightblue")) return "darkblue";
        if (color.equals("yellow")) return "orange";
        if (color.equals("gold")) return "orange";
        return color;
    }

    // 육각형 그리기
    public void draw(Canvas canvas, Paint paint, float offsetX, float offsetY) {
        if (!dirty) return;

        Path path = new Path();
        for (int i = 0; i < 6; i++) {
            float angle = (float) (Math.PI / 3 * (i + 0.5)); // 각도를 30도 회전
            float xPos = x + HEX_RADIUS * (float) Math.cos(angle) + offsetX;
            float yPos = y + HEX_RADIUS * (float) Math.sin(angle) + offsetY;

            if (i == 0) {
                path.moveTo(xPos, yPos);
            } else {
                path.lineTo(xPos, yPos);
            }
        }
        path.close();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK); // 선 색깔
        canvas.drawPath(path, paint);

        // hover 상태일 경우 색상 반전
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getColorFromString(isHovered ? invertColor(color) : color)); // 면 색깔
        canvas.drawPath(path, paint);

        dirty = false;
    }

    // 색상 문자열을 실제 색상 값으로 변환
    private int getColorFromString(String color) {
        switch (color) {
            case "red":
                return Color.RED;
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

    // hover 상태 업데이트
    public void updateHover(float mouseX, float mouseY, float offsetX, float offsetY) {
        boolean wasHovered = isHovered;
        isHovered = isPointInHexagon(mouseX, mouseY, offsetX, offsetY); // hover 상태 업데이트

        // 상태가 변경되었으면 dirty 상태를 true로 설정
        if (isHovered != wasHovered) {
            dirty = true;
        }
    }

    // 클릭 상태 업데이트
    public void updateClick(float mouseX, float mouseY, float offsetX, float offsetY) {
        boolean wasClicked = isClicked;
        isClicked = isPointInHexagon(mouseX, mouseY, offsetX, offsetY); // 클릭 상태 업데이트

        if (isClicked != wasClicked) {
            dirty = true;
        }
    }

    // 점이 육각형 내부에 있는지 확인 (Region 사용)
    private boolean isPointInHexagon(float mouseX, float mouseY, float offsetX, float offsetY) {
        /* Ray-Casting 알고리즘 (chatGPT)
        점에서 임의의 방향으로 반직선을 그립니다.
        다각형의 각 변과 반직선의 교차 횟수를 계산합니다.
        교차 횟수가 홀수면 점은 내부에 있음, 짝수면 점은 외부에 있음. */

        float[] hexX = new float[6];
        float[] hexY = new float[6];

        for (int i = 0; i < 6; i++) {
            float angle = (float) (Math.PI / 3 * (i + 0.5));
            hexX[i] = x + HEX_RADIUS * (float) Math.cos(angle) + offsetX;
            hexY[i] = y + HEX_RADIUS * (float) Math.sin(angle) + offsetY;
        }

        // Ray-Casting 알고리즘으로 점 내부 여부 확인
        boolean inside = false;
        for (int i = 0, j = 5; i < 6; j = i++) {
            if ((hexY[i] > mouseY) != (hexY[j] > mouseY) &&
                    mouseX < (hexX[j] - hexX[i]) * (mouseY - hexY[i]) / (hexY[j] - hexY[i]) + hexX[i]) {
                inside = !inside;
            }
        }
        return inside;
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
