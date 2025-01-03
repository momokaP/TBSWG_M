package com.example.term_project;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameSetting {
    private static Context appContext;

    public static void setContext(Context context) {
        appContext = context.getApplicationContext();
    }

    public static String getCOLOR() {
        return COLOR;
    }

    public static void setCOLOR(String COLOR) {
        GameSetting.COLOR = COLOR;
    }

    public static String getNAME() {
        return NAME;
    }

    public static void setNAME(String NAME) {
        GameSetting.NAME = NAME;
    }

    private static String COLOR;
    private static String NAME;

    private static int hexRadius = 100;
    private static int unitRadius = 70;
    private static HexTile[][] hexMap;
    private static Unit[][] unitMap;

    private static Map<String, User> users = new HashMap<>();
    private static List<String> userOrder = new ArrayList<>();
    private static Unit selectedunit;

    public static HexTile getSelectedProductionTile() {
        return selectedProductionTile;
    }

    public static void setSelectedProductionTile(HexTile selectedProductionTile) {
        GameSetting.selectedProductionTile = selectedProductionTile;
    }

    private static HexTile selectedProductionTile;
    private static int mapOffset_X = 0;
    private static int mapOffset_Y = 0;
    private static boolean initial = true;

    private static final int rows = 9;
    private static final int cols = 9;
    private static final int howManyUser = 2;

    private static final int range = 1;

    private static int cost = 2;
    private static int computerAiCost = 1;

    private static String WhoAmI = "user1";

    public static String getWhoIsEnemy() {
        return WhoIsEnemy;
    }

    public static void setWhoIsEnemy(String whoIsEnemy) {
        WhoIsEnemy = whoIsEnemy;
    }

    private static String WhoIsEnemy = "user2";

    private static int turn = 2;

    public static String getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(String currentPlayer) {
        GameSetting.currentPlayer = currentPlayer;
    }

    private static String currentPlayer;

    public static String getWhoAmI() {
        return WhoAmI;
    }

    public static void setWhoAmI(String whoAmI) {
        WhoAmI = whoAmI;
    }

    static {
        hexMap = new HexTile[rows][cols];  // 9x9 크기의 배열로 초기화
        unitMap = new Unit[rows][cols];
    }

    // 사용자 추가 또는 수정
    public static void addUser(String name, User user) {
        users.put(name, user);
        userOrder.add(name);
    }

    // 사용자 조회
    public static User getUser(String name) {
        return users.get(name);
    }

    public static int getTurn() {
        return turn;
    }

    public static void addTurn(int turn) {
        GameSetting.turn += turn;
    }

    // 사용자 삭제
    public static void removeUser(String name) {
        users.remove(name);
    }

    public static int getCost() {
        return cost;
    }

    public static void multiplyCost(int cost) {
        GameSetting.cost *= cost;
    }

    public static int getComputerAiCost() {
        return computerAiCost;
    }

    public static void multiplyComputerAiCost(int computerAiCost) {
        GameSetting.computerAiCost *= computerAiCost;
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

    public static boolean isValidAttack(Unit unit, int targetRow, int targetCol) {
        int currentRow = unit.getRow();
        int currentCol = unit.getCol();

        int[] unitCube = cubeCoordinates(currentRow, currentCol);
        int[] tileCube = cubeCoordinates(targetRow, targetCol);
        int distance = cubeDistance(unitCube, tileCube);

        boolean isAdjacent = distance <= range;

        // 유효한 인접 타일이고 해당 위치에 유닛이 있으면 true 반환
        return isAdjacent && (getUnit(targetRow, targetCol) != null);
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

            // 타일 점령 처리
            HexTile targetTile = getHexTile(targetRow, targetCol);
            if (targetTile != null && !Objects.equals(targetTile.getUser(), unit.getUser())) {
                if(targetTile.getUser() != null && targetTile.getUser() != unit.getUser()){
                    // 다른 유저가 점령한 타일로 이동하면

                    // 타일을 원래 점령하던 유저는 HowManyTiles 1 감소
                    GameSetting.getUser(targetTile.getUser()).addHowManyTiles(-1);

                    unit.addHealth(-1);
                    // 체력이 0 이하인 경우 유닛 제거
                    if (unit.isDead()) {
                        setUnit(targetRow, targetCol, null); // 유닛 제거
                    }

                    // TextView에 텍스트 설정
                    if(targetTile.getUser() == getWhoAmI()){
                        // 타일을 원래 점령하던 유저가 나라면
                        int howmManyTiles = GameSetting.getUser(targetTile.getUser()).getHowManyTiles();
                        MainActivity.howmany_tile.setText("점령한 타일 수 : " + howmManyTiles);
                    }
                }
                targetTile.setUser(unit.getUser());
                targetTile.setColor(unit.getColor());
                //targetTile.setUnitMoved(true);
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
                    if(row==unit.getRow() && col==unit.getCol())
                        continue;
                    // 이동 가능 여부 설정
                    boolean movable = isValidMove(unit, row, col);
                    boolean attackable = isValidAttack(unit, row, col);
                    tile.setMovable(movable);
                    tile.setAttackable(attackable);
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

    public static void resetAttackableTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                HexTile tile = getHexTile(row, col);
                if (tile != null) {
                    tile.setAttackable(false);
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

    public static void nextTurn(){
        addTurn(1);

        // 다음 유저로 턴 넘기기
        //currentPlayer = getNextPlayer();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Unit unit = getUnit(row, col);
                if (unit != null) {
                    unit.setMovable(true);
                    unit.setDamaged(false);
                }
            }
        }

        // 게임 종료 조건 확인
        checkGameEnd();
    }

    public static String getNextPlayer() {
        int currentIndex = userOrder.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % userOrder.size();
        return userOrder.get(nextIndex);
    }

    public static void checkGameEnd() {
        // 유저들 각각의 상태를 확인
        for (String userName : users.keySet()) {
            User user = users.get(userName);

            // 1. 타일 점령 수가 41개 이상인 경우
            if (user.getHowManyTiles() >= 41) {
                endGame(userName, "타일 점령 수가 41개를 초과했습니다.");
                return;
            }

            // 2. 유저가 보유한 유닛이 없는 경우
            boolean hasUnits = false;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Unit unit = getUnit(row, col);
                    if (unit != null && Objects.equals(unit.getUser(), userName)) {
                        hasUnits = true;
                        break;
                    }
                }
                if (hasUnits) break;
            }

            if (!hasUnits) {
                // 현재 유닛이 없는 유저가 userName
                // 상대 유저를 승자로 설정
                for (String opponentName : users.keySet()) {
                    if (!Objects.equals(opponentName, userName)) {
                        endGame(opponentName, userName + "의 유닛이 모두 사라졌습니다.");
                        return;
                    }
                }
            }
        }
    }

    // 게임 종료 처리
    private static void endGame(String winnerName, String reason) {
        System.out.println("Game Over! Winner: " + winnerName + " - " + reason);
        MainActivity.showGameEndDialog(winnerName, reason);
    }

    public static void initialize(String[] settings){
        //System.out.println("초기화초기화초기화초기화초기화초기화초기화");
        setNAME(settings[0]);
        setCOLOR(settings[1]);

        //System.out.println(getNAME());
        //System.out.println(getCOLOR());
    }

    public static void reset() {
        // 1. 게임 관련 상태 초기화
        hexMap = new HexTile[rows][cols];  // 새로운 9x9 맵 배열 초기화
        unitMap = new Unit[rows][cols];     // 새로운 유닛 맵 배열 초기화
        users.clear();                      // 유저 리스트 초기화
        for (String userName : users.keySet()) {
            User user = users.get(userName);
            if(user!=null) user.reset();
        }
        userOrder.clear();                  // 유저 순서 리스트 초기화
        selectedunit = null;                // 선택된 유닛 초기화
        selectedProductionTile = null;      // 선택된 생산 타일 초기화
        mapOffset_X = 0;                    // 맵 오프셋 초기화
        mapOffset_Y = 0;                    // 맵 오프셋 초기화
        initial = true;                     // 초기 상태 플래그 설정

        // 2. 게임 설정 관련 초기화
        cost = 2;                           // 기본 비용
        computerAiCost = 1;                 // AI의 기본 비용
        WhoAmI = "user1";                   // 현재 사용자 설정
        turn = 2;                           // 첫 번째 턴으로 설정
        currentPlayer = null;
    }

}
