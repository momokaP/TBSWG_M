package com.example.term_project;

import android.graphics.Color;

public class ColorUtils {

    // 색상 문자열을 실제 색상값으로 변환
    public static int getColorFromName(String colorName) {
        switch (colorName) {
            case "Red":
                return Color.RED;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            case "Yellow":
                return Color.YELLOW;
            case "Cyan":
                return Color.CYAN;
            case "Magenta":
                return Color.MAGENTA;
            case "Orange":
                return Color.rgb(255, 165, 0); // Orange 색상 값
            case "Blue-Green":
                return Color.rgb(0, 184, 184); // Blue-Green 색상 값
            default:
                return Color.BLUE; // 기본값 Blue
        }
    }

    // 색상의 보색을 구하는 함수
    public static int invertColor(int color) {
        // Color.red(), Color.green(), Color.blue()는 색상 값을 각각 반환
        int red = 255 - Color.red(color);
        int green = 255 - Color.green(color);
        int blue = 255 - Color.blue(color);

        // 보색으로 구성된 새로운 색상 값 반환
        return Color.rgb(red, green, blue);
    }
}
