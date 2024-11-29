package com.example.term_project;

public class GameSetting {
    private static int hexRadius = 100;
    private static HexTile[][] hexMap;
    private static int mapOffset_X = 0;
    private static int mapOffset_Y = 0;
    private static boolean initial = true;

    static {
        int rows = 15;  // 행 크기
        int cols = 15;  // 열 크기
        hexMap = new HexTile[rows][cols];  // 15x15 크기의 배열로 초기화
    }

    public static boolean isInitial() {
        return initial;
    }

    public static void setInitial(boolean initial) {
        GameSetting.initial = initial;
    }

    public static void setHexRadius(int hexRadius) {
        GameSetting.hexRadius = hexRadius;
    }

    public static int getHexRadius(){
        return hexRadius;
    }

    public static HexTile getHexTile(int row, int col) {
        return hexMap[row][col];
    }

    public static void setHexTile(int row, int col, HexTile tile) {
        hexMap[row][col] = tile;
    }

    public static int getMapOffset_X() {
        return mapOffset_X;
    }

    public static void setMapOffset_X(int mapOffset_X) {
        GameSetting.mapOffset_X = mapOffset_X;
    }

    public static int getMapOffset_Y() {
        return mapOffset_Y;
    }

    public static void setMapOffset_Y(int mapOffset_Y) {
        GameSetting.mapOffset_Y = mapOffset_Y;
    }
}
