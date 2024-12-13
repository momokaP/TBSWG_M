package com.example.term_project;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameSetting {
    private static int hexRadius = 100;
    private static int unitRadius = 70;
    private static HexTile[][] hexMap;
    private static Unit[][] unitMap;

    private static Map<String, User> users = new HashMap<>();
    private static Unit selectedunit;
    private static int mapOffset_X = 0;
    private static int mapOffset_Y = 0;
    private static boolean initial = true;

    private static final int rows = 9;
    private static final int cols = 9;
    private static final int howManyUser = 2;

    private static final int range = 1;

    static {
        hexMap = new HexTile[rows][cols];  // 9x9 크기의 배열로 초기화
        unitMap = new Unit[rows][cols];
    }

    // 사용자 추가 또는 수정
    public static void addUser(String name, User user) {
        users.put(name, user);
    }

    // 사용자 조회
    public static User getUser(String name) {
        return users.get(name);
    }

    // 사용자 삭제
    public static void removeUser(String name) {
        users.remove(name);
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

    public static void setUnitRadius(int unitRadius) {
        GameSetting.unitRadius = unitRadius;
    }

    public static int getUnitRadius(){
        return unitRadius;
    }

    public static Unit getUnit(int row, int col) {
        return unitMap[row][col];
    }

    public static void setUnit(int row, int col, Unit unit) {
        unitMap[row][col] = unit;
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

    public static Unit getSelectedunit() {
        return selectedunit;
    }

    public static void setSelectedunit(Unit selectedunit) {
        GameSetting.selectedunit = selectedunit;
    }

    // 행, 열을 큐브 좌표로 변환
    private static int[] cubeCoordinates(int row, int col) {
        int q = col - (row / 2);
        int r = row;
        int s = -q - r;
        return new int[]{q, r, s};
    }

    // 큐브 거리 계산
    private static int cubeDistance(int[] a, int[] b) {
        return Math.max(
                Math.max(Math.abs(a[0] - b[0]), Math.abs(a[1] - b[1])),
                Math.abs(a[2] - b[2])
        );
    }

    public static boolean isValidMove(Unit unit, int targetRow, int targetCol) {
        int currentRow = unit.getRow();
        int currentCol = unit.getCol();

        int[] unitCube = cubeCoordinates(currentRow, currentCol);
        int[] tileCube = cubeCoordinates(targetRow, targetCol);
        int distance = cubeDistance(unitCube, tileCube);

        boolean isAdjacent = distance <= range;

        // 유효한 인접 타일이고 해당 위치에 유닛이 없으면 true 반환
        return isAdjacent && (getUnit(targetRow, targetCol) == null);
    }

    public static void moveUnit(Unit unit, int targetRow, int targetCol) {
        if (isValidMove(unit, targetRow, targetCol)) {
            int currentRow = unit.getRow();
            int currentCol = unit.getCol();

            // 기존 위치에서 유닛 제거
            setUnit(currentRow, currentCol, null);

            // 유닛 정보 업데이트
            unit.setRow(targetRow);
            unit.setCol(targetCol);

            // 새로운 위치에 유닛 배치
            setUnit(targetRow, targetCol, unit);

            // 점령 처리
            HexTile targetTile = getHexTile(targetRow, targetCol);
            if (targetTile != null && !Objects.equals(targetTile.getUser(), unit.getUser())) {
                if(targetTile.getUser() != null && targetTile.getUser() != unit.getUser()){
                    System.out.println(targetTile.getUser()+" "+unit.getUser());
                    GameSetting.getUser(targetTile.getUser()).addHowManyTiles(-1);
                    // TextView에 텍스트 설정
                    if(targetTile.getUser() == "user1"){
                        int howmManyTiles = GameSetting.getUser(targetTile.getUser()).getHowManyTiles();
                        MainActivity.howmany_tile.setText("점령한 타일 수 : " + howmManyTiles);
                    }
                }
                targetTile.setUser(unit.getUser());
                targetTile.setColor(unit.getColor());
                getUser(unit.getUser()).addHowManyTiles(1);
            }
        } else {
            // 잘못된 이동 시 로그 출력 (디버깅용)
            System.out.println("Invalid move to row: " + targetRow + ", col: " + targetCol);
        }
    }

    public static void highlightMovableTiles(Unit unit) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                HexTile tile = getHexTile(row, col);
                if (tile != null) {
                    // 이동 가능 여부 설정
                    boolean movable = isValidMove(unit, row, col);
                    tile.setMovable(movable);
                }
            }
        }
    }

    public static void resetMovableTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                HexTile tile = getHexTile(row, col);
                if (tile != null) {
                    tile.setMovable(false);
                }
            }
        }
    }

    public static void resetHoverTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                HexTile tile = getHexTile(row, col);
                if (tile != null) {
                    tile.setHovered(false);
                }
            }
        }
    }

}
