package com.example.term_project;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

// 육각형을 그릴 수 있는 클래스
public class HexagonTile {

    // 육각형의 한 변의 길이
    private float radius;

    // 생성자
    public HexagonTile(float radius) {
        this.radius = radius;
    }

    // 육각형의 각 꼭짓점 계산
    public PointF[] getHexagonVertices(float x, float y) {
        PointF[] points = new PointF[6];

        for (int i = 0; i < 6; i++) {
            float angle = (float)(2 * Math.PI / 6 * i);  // 6등분된 각도
            points[i] = new PointF(
                    x + radius * (float)Math.cos(angle),
                    y + radius * (float)Math.sin(angle)
            );
        }
        return points;
    }

    // 육각형 그리기
    public void drawHexagon(Canvas canvas, float x, float y, Paint paint) {
        PointF[] vertices = getHexagonVertices(x, y);
        Path path = new Path();
        path.moveTo(vertices[0].x, vertices[0].y);

        for (int i = 1; i < 6; i++) {
            path.lineTo(vertices[i].x, vertices[i].y);
        }

        path.close();
        canvas.drawPath(path, paint);
    }
}