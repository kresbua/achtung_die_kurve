package com.example.achtung_die_kurve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {
    private Paint circlePaint;
    private List<PointF> points;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.FILL);

        points = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (PointF point : points) {
            canvas.drawCircle(point.x, point.y, 10, circlePaint);
        }
    }

    public void addCircle(float x, float y) {
        points.add(new PointF(x, y));
        invalidate();
    }

    public void clearCanvas() {
        points.clear();
        invalidate();
    }
    public void setPaintColor(int color){
        circlePaint.setColor(color);
    }
}