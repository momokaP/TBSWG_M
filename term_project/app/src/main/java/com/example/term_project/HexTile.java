package com.example.term_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class HexTile {
    private int x, y; // 타일의 중심 좌표
    private String defaultColor = "silver"; // 기본 색상
    private String color = defaultColor; // 현재 색상
    private boolean isHovered = false; // hover 상태
    private boolean isClicked = false; // 클릭 상태
    private boolean dirty = true; // 타일의 상태가 변경되었을 때만 true로 설정
    private int HEX_RADIUS; // 육각형의 반지름
    private int row;
    private int col;
    private boolean isMovable = false; // 이동 가능 여부
    private boolean isAttackable = false; // 공격 가능 여부

    private boolean isproduction = false;

    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setIsproduction(boolean isproduction) {
        this.isproduction = isproduction;
    }

    public boolean Isproduction() {
        return isproduction;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public boolean isAttackable() {
        return isAttackable;
    }

    public void setAttackable(boolean attackable) {
        isAttackable = attackable;
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
        if (color.equals("lightblue")) return "darkblue";
        if (color.equals("yellow")) return "orange";
        if (color.equals("gold")) return "orange";
        if (color.equals("silver")) return "gray";
        return color;
    }

    // 육각형 그리기
    public void draw(Canvas canvas, Paint paint, float offsetX, float offsetY) {
        if (!dirty) return;

        // 큰 육각형을 그리기 위한 점 계산
        float[] hexX = new float[6];
        float[] hexY = new float[6];

        for (int i = 0; i < 6; i++) {
            float angle = (float) (Math.PI / 3 * (i + 0.5)); // 각도를 30도 회전
            hexX[i] = x + HEX_RADIUS * (float) Math.cos(angle) + offsetX;
            hexY[i] = y + HEX_RADIUS * (float) Math.sin(angle) + offsetY;
        }

        // 육각형 내부를 색으로 채우기
        Path hexPath = new Path();
        hexPath.moveTo(hexX[0], hexY[0]);
        for (int i = 1; i < 6; i++) {
            hexPath.lineTo(hexX[i], hexY[i]);
        }
        hexPath.close();

        // 육각형 테두리를 그리기
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawPath(hexPath, paint);

        paint.setStyle(Paint.Style.FILL);

        String displayColor;
        if (isHovered) {
            displayColor = invertColor(color); // 마우스 오버 시 색상 반전
        } else {
            displayColor = color; // 기본 색상
        }
        paint.setColor(getColorFromString(displayColor));

        canvas.drawPath(hexPath, paint);

        // user가 null이 아닐 때, 육각형을 삼각형으로 나누고 각 삼각형의 테두리를 하얗게 그리기
        if (isproduction) {
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE); // 선만 그리도록 설정

            for (int i = 0; i < 6; i++) {
                Path trianglePath = new Path();
                trianglePath.moveTo(x + offsetX, y + offsetY); // 중심점
                trianglePath.lineTo(hexX[i], hexY[i]); // 현재 꼭지점
                trianglePath.lineTo(hexX[(i + 1) % 6], hexY[(i + 1) % 6]); // 다음 꼭지점
                trianglePath.close();

                canvas.drawPath(trianglePath, paint); // 삼각형 테두리 그리기
            }
        }

        // 작은 육각형 그리기 (user가 null이 아닐 때)
        if (isproduction) {
            float smallHexRadius = HEX_RADIUS * 0.5f; // 작은 육각형의 반지름을 기존 반지름의 50%로 설정
            Path smallPath = new Path();

            for (int i = 0; i < 6; i++) {
                float angle = (float) (Math.PI / 3 * (i + 0.5));
                float xPos = x + smallHexRadius * (float) Math.cos(angle) + offsetX;
                float yPos = y + smallHexRadius * (float) Math.sin(angle) + offsetY;

                if (i == 0) {
                    smallPath.moveTo(xPos, yPos);
                } else {
                    smallPath.lineTo(xPos, yPos);
                }
            }
            smallPath.close();

            if(user != null) {
                paint.setColor(getColorFromString(displayColor)); // 작은 육각형 색상
            }
            else {
                paint.setColor(Color.WHITE);
            }

            if(isHovered) paint.setColor(Color.WHITE);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(smallPath, paint);
        }

        if (isMovable) {
            float smallHexRadius = HEX_RADIUS;
            Path smallPath = new Path();

            for (int i = 0; i < 6; i++) {
                float angle = (float) (Math.PI / 3 * (i + 0.5));
                float xPos = x + smallHexRadius * (float) Math.cos(angle) + offsetX;
                float yPos = y + smallHexRadius * (float) Math.sin(angle) + offsetY;

                if (i == 0) {
                    smallPath.moveTo(xPos, yPos);
                } else {
                    smallPath.lineTo(xPos, yPos);
                }
            }
            smallPath.close();

            // 투명한 하얀색 설정
            paint.setColor(Color.WHITE);
            paint.setAlpha(128); // 투명도 설정 (0: 완전 투명, 255: 완전 불투명)
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(smallPath, paint);

            // 투명도를 255로 되돌려 다른 그리기 작업에 영향 없도록 설정
            paint.setAlpha(255);
        }

        if (isAttackable) {
            float smallHexRadius = HEX_RADIUS;
            Path smallPath = new Path();

            for (int i = 0; i < 6; i++) {
                float angle = (float) (Math.PI / 3 * (i + 0.5));
                float xPos = x + smallHexRadius * (float) Math.cos(angle) + offsetX;
                float yPos = y + smallHexRadius * (float) Math.sin(angle) + offsetY;

                if (i == 0) {
                    smallPath.moveTo(xPos, yPos);
                } else {
                    smallPath.lineTo(xPos, yPos);
                }
            }
            smallPath.close();

            // 투명한 하얀색 설정
            paint.setColor(Color.YELLOW);
            paint.setAlpha(128); // 투명도 설정 (0: 완전 투명, 255: 완전 불투명)
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(smallPath, paint);

            // 투명도를 255로 되돌려 다른 그리기 작업에 영향 없도록 설정
            paint.setAlpha(255);
        }

        dirty = false;
    }

    // 색상 문자열을 실제 색상 값으로 변환
    private int getColorFromString(String color) {
        switch (color) {
            case "red":
                return Color.parseColor("#E04375");
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
            case "silver":
                return Color.parseColor("#C0C0C0");
            case "gray":
                return Color.parseColor("#808080");
            case "blue":
                return Color.parseColor("#00A1FF");
            case "transparent":
                return Color.argb(128, 255, 255, 255);
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

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public boolean isHovered() {
        return isHovered;
    }
}
