package com.example.achtung_die_kurve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.List;

public class CanvasView extends View {

    private Paint paint;
    private Canvas canvas;
    private Player player;
    private int canvasWidth;
    private int canvasHeight;
    private boolean drawCircle = false;

    private List<Pair<Float, Float>> coordinates;
    private int circleRadius;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(0);
        player = new Player("username", "ip", false);
    }

    //Hier nur Zeichnen was ganz am Anfang ist
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        paint.setColor(Color.GRAY);
        canvas.drawRect((float)0, (float)0, canvasWidth, canvasHeight, paint);

        if(drawCircle){
            List<Pair<Float, Float>> coordinates = player.getCoordinates();

            paint.setColor(Color.WHITE);


            for(int i = 0; i < coordinates.size(); i++){
                canvas.drawCircle(coordinates.get(i).first, coordinates.get(i).second, circleRadius, paint);
                Log.d("INFO3:", "Drew x: " + coordinates.get(i).first + ", y: " + coordinates.get(i).second);
            }

        }
    }
    public void addCoordinates(float x, float y){
        player.addCoordinates(x, y);
        drawCircle = true;
        circleRadius = 10;
        invalidate();
    }

}