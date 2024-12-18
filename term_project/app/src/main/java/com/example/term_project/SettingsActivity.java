package com.example.term_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private EditText nameEditText;
    private Spinner colorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEditText = findViewById(R.id.nameEditText);
        colorSpinner = findViewById(R.id.colorSpinner);

        // Spinner에 색상 옵션 추가
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        // 데이터베이스 생성/열기
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        // 기본값 확인 및 삽입
        checkAndInsertDefaultSettings();

        // 기존 설정 값 불러오기
        loadSettingsFromDatabase();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 입력한 이름과 색상 저장
                String name = nameEditText.getText().toString();
                String color = colorSpinner.getSelectedItem().toString();
                saveSettingsToDatabase(name, color);
            }
        });
    }

    // 데이터베이스에서 설정 값 읽어오기
    private void loadSettingsFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String[] settings = dbHelper.getSettings();

        if (settings[0] != null) {
            nameEditText.setText(settings[0]);

            // TextView에 이름 설정
            TextView nameTextView = findViewById(R.id.nameTextView);
            nameTextView.setText("이름: " + settings[0]);

            String color = settings[1];

            // 색상 설정
            ArrayAdapter adapter = (ArrayAdapter) colorSpinner.getAdapter();
            int position = adapter.getPosition(color);
            colorSpinner.setSelection(position);

            // 색상 원 업데이트
            ImageView colorCircle = findViewById(R.id.colorCircle);
            int colorValue = ColorUtils.getColorFromName(color);
            colorCircle.setImageDrawable(new ColorDrawable(colorValue)); // 원에 색 적용
        }
    }

    // 데이터베이스에 설정 저장
    private void saveSettingsToDatabase(String name, String color) {
        // 테이블의 모든 데이터를 삭제
        database.delete(DatabaseHelper.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("color", color);

        // 기존 데이터가 있으면 업데이트, 없으면 새로 삽입
        long result = database.insertWithOnConflict("settings", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            // 삽입 실패 처리
            final Toast toast = Toast.makeText(SettingsActivity.this, "저장 실패", Toast.LENGTH_SHORT);
            toast.show();
            new Handler().postDelayed(toast::cancel, 1000);
        } else {
            // 성공적으로 저장됨
            final Toast toast = Toast.makeText(SettingsActivity.this, "저장 완료: " + name + ", " + color, Toast.LENGTH_SHORT);
            toast.show();
            new Handler().postDelayed(toast::cancel, 1000);
        }

        loadSettingsFromDatabase();
    }

    // 기본값 확인 및 삽입
    private void checkAndInsertDefaultSettings() {
        SQLiteDatabase db = database;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                // 기본값 삽입
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAME, "Default Name");
                values.put(DatabaseHelper.COLUMN_COLOR, "Red");
                db.insert(DatabaseHelper.TABLE_NAME, null, values);
            }
            cursor.close();
        }
    }
}