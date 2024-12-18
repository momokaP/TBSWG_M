package com.example.term_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gameSettings.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "settings";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COLOR = "color";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 SQL
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_COLOR + " TEXT);";
        db.execSQL(CREATE_TABLE);

        // 기본값 삽입 SQL
        String INSERT_DEFAULTS = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_COLOR + ") " +
                "VALUES ('Default Name', 'Red');";
        db.execSQL(INSERT_DEFAULTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        // 기본값 삽입 (기본값이 없을 경우)
        String checkQuery = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(checkQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // 기본값 삽입
                String INSERT_DEFAULTS = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + ", " + COLUMN_COLOR + ") " +
                        "VALUES ('Default Name', 'Red');";
                db.execSQL(INSERT_DEFAULTS);
            }
            cursor.close();
        }
    }

    // 설정 값 읽어오기
    public String[] getSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] settings = new String[2]; // {name, color}
        String query = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
            int colorColumnIndex = cursor.getColumnIndex(COLUMN_COLOR);

            if (nameColumnIndex != -1 && colorColumnIndex != -1) {
                settings[0] = cursor.getString(nameColumnIndex);
                settings[1] = cursor.getString(colorColumnIndex);
            } else {
                // 컬럼이 없으면 기본값 설정
                settings[0] = ""; // 또는 null로 처리 가능
                settings[1] = "";
            }

            cursor.close();
        }
        return settings;
    }
}
