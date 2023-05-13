package com.example.achtung_die_kurve;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends AppCompatActivity {
    private CanvasView canvasView;
    private Timer timer;

    private int circleSize = 10;
    private int currentY = 800;
    private int currentX = 200;
    private int directionX = 0;
    private int directionY = -10;
    private List<PointF> points;

    private float collisionRadius;
    private boolean collision = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        points = new ArrayList<>();

        Button left = findViewById(R.id.left);
        Button right = findViewById(R.id.right);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (directionX >= 0 && directionY < 0) {
                    directionX += 2;
                    directionY += 2;
                } else if (directionX > 0 && directionY >= 0) {
                    directionX -= 2;
                    directionY += 2;
                } else if (directionX <= 0 && directionY > 0) {
                    directionX -= 2;
                    directionY -= 2;
                } else {
                    directionX += 2;
                    directionY -= 2;
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (directionX <= 0 && directionY < 0) {
                    directionX -= 2;
                    directionY += 2;
                } else if (directionX < 0 && directionY >= 0) {
                    directionX += 2;
                    directionY += 2;
                } else if (directionX >= 0 && directionY > 0) {
                    directionX += 2;
                    directionY -= 2;
                } else {
                    directionX -= 2;
                    directionY -= 2;
                }
            }
        });

        canvasView = findViewById(R.id.canvas);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        float posX = currentX + directionX;
                        float posY = currentY + directionY;

                        canvasView.addCircle(posX, posY);
                        currentY = (int) posY;
                        currentX = (int) posX;

                        //Kollisionserkennung mit den Rändern
                        if (currentY <= circleSize/2 + 5) {
                            collision = true;
                        } else if (currentY >= canvasView.getHeight() - circleSize/2 - 7) {
                            collision = true;
                        } else if (currentX >= canvasView.getWidth() - circleSize/2 - 7) {
                            collision = true;
                        } else if (currentX <= circleSize/2 + 7) {
                            collision = true;
                        }

                        //Kollisionserkennung mit sich selbst
                        PointF currentPoint = new PointF(currentX, currentY);
                        float collisionRadius = circleSize / 2.0f;

                        int numPointsToCheck = Math.max(0, points.size() - 2); // Anzahl der Punkte, die überprüft werden sollen

                        for (int i = 0; i < numPointsToCheck; i++) {
                            PointF point = points.get(i);
                            float distanceX = Math.abs(currentPoint.x - point.x);
                            float distanceY = Math.abs(currentPoint.y - point.y);

                            if (distanceX < collisionRadius + circleSize && distanceY < collisionRadius + circleSize && !isCloseToLastTwoPoints(currentPoint, points)) {
                                // Kollision mit sich selbst erkannt
                                collision = true;
                                break;
                            }
                        }

                        points.add(currentPoint);

                        if(collision){
                            canvasView.addCircle(posX, posY);
                            canvasView.clearCanvas();
                            points.clear();
                            currentX = 200;
                            currentY = 800;
                            directionX = 0;
                            directionY = -10;
                            collision = false;
                        }
                    }
                });
            }
        }, 1000, 75); // Start nach 1 Sekunde, wiederhole alle 75 Millisekunden
    }

    private float calculateDistance(PointF point1, PointF point2) {
        float dx = point2.x - point1.x;
        float dy = point2.y - point1.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private boolean isCloseToLastTwoPoints(PointF currentPoint, List<PointF> points) {
        int numPoints = points.size();
        if (numPoints < 2) {
            return false; // Wenn weniger als 2 Punkte vorhanden sind, gibt es keine Kollision
        }

        PointF lastPoint = points.get(numPoints - 1);
        PointF secondLastPoint = points.get(numPoints - 2);

        float distanceToLastPoint = calculateDistance(currentPoint, lastPoint);
        float distanceToSecondLastPoint = calculateDistance(currentPoint, secondLastPoint);

        // Überprüfen, ob der Abstand zu den letzten beiden Punkten kleiner als der Kollisionsradius ist
        return distanceToLastPoint < collisionRadius && distanceToSecondLastPoint < collisionRadius;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }
}