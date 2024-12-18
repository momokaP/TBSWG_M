package com.example.term_project;

import android.graphics.Color;

public class ColorUtils {

    // 기본 밝기 및 어두운 색 비율
    private static final float LIGHTEN_FACTOR = 0.5f; // 50% 밝게
    private static final float DARKEN_FACTOR = 0.5f;  // 50% 어둡게

    // 색상 문자열을 실제 색상값으로 변환
    public static int getColorFromName(String colorName) {
        switch (colorName) {
            case "silver":
                return Color.parseColor("#C0C0C0");
            case "gray":
                return Color.parseColor("#808080");
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
            case "anti-Red":
                return invertColor(Color.RED); // Red의 보색
            case "anti-Green":
                return invertColor(Color.GREEN); // Green의 보색
            case "anti-Blue":
                return invertColor(Color.BLUE); // Blue의 보색
            case "anti-Yellow":
                return invertColor(Color.YELLOW); // Yellow의 보색
            case "anti-Cyan":
                return invertColor(Color.CYAN); // Cyan의 보색
            case "anti-Magenta":
                return invertColor(Color.MAGENTA); // Magenta의 보색
            case "anti-Orange":
                return invertColor(Color.rgb(255, 165, 0)); // Orange의 보색
            case "anti-Blue-Green":
                return invertColor(Color.rgb(0, 184, 184)); // Blue-Green의 보색
            default:
                return Color.BLUE; // 기본값 Blue
        }
    }

    // 색상의 보색을 구하는 함수
    public static int invertColor(int color) {
        int red = 255 - Color.red(color);
        int green = 255 - Color.green(color);
        int blue = 255 - Color.blue(color);

        return Color.rgb(red, green, blue);
    }

    // 색상을 밝게 만드는 함수
    public static int lightenColor(int color) {
        return lightenColor(color, LIGHTEN_FACTOR);
    }

    // 색상을 어둡게 만드는 함수
    public static int darkenColor(int color) {
        return darkenColor(color, DARKEN_FACTOR);
    }

    // 색상을 밝게 만드는 함수 (factor를 매개변수로 받는 함수)
    private static int lightenColor(int color, float factor) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        red = (int) Math.min(255, red + (255 - red) * factor);
        green = (int) Math.min(255, green + (255 - green) * factor);
        blue = (int) Math.min(255, blue + (255 - blue) * factor);

        return Color.rgb(red, green, blue);
    }

    // 색상을 어둡게 만드는 함수 (factor를 매개변수로 받는 함수)
    private static int darkenColor(int color, float factor) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        red = (int) Math.max(0, red - red * factor);
        green = (int) Math.max(0, green - green * factor);
        blue = (int) Math.max(0, blue - blue * factor);

        return Color.rgb(red, green, blue);
    }
}
