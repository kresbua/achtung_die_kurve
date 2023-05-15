package com.example.achtung_die_kurve;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends AppCompatActivity {
    private CanvasView canvasView;
    private Game myGame;
    private Player myPlayer;
    private Timer timer;
    private float collisionRadius;
    private boolean collision = false;
//test
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        //Game Objekt holen
        Intent intent = getIntent();
        myGame = (Game) intent.getSerializableExtra("myGame");

        //Player Objekt holen
        myPlayer = (Player) intent.getSerializableExtra("myPlayer");

        //Punkteanzeigen für Player holen
        final TextView player1_points = findViewById(R.id.player1_points);
        final TextView player2_points = findViewById(R.id.player2_points);
        final TextView player3_points = findViewById(R.id.player3_points);
        final TextView player4_points = findViewById(R.id.player4_points);

        //Punkteanzeigen text setzen
        if(myPlayer.isHost()){
            player1_points.setText(myPlayer.getUsername() + ":" + myPlayer.getPoints());
            player1_points.setTextColor(myPlayer.getColor());
        }else{
            //to be continued
        }

        Button left = findViewById(R.id.left);
        Button right = findViewById(R.id.right);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlayer.getDirectionX() >= 0 && myPlayer.getDirectionY() < 0) {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()+2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()+2);
                } else if (myPlayer.getDirectionX() > 0 && myPlayer.getDirectionY() >= 0) {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()-2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()+2);
                } else if (myPlayer.getDirectionX() <= 0 && myPlayer.getDirectionY() > 0) {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()-2);//hier -
                    myPlayer.setDirectionY(myPlayer.getDirectionY()-2);
                } else {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()+2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()-2);
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlayer.getDirectionX() <= 0 && myPlayer.getDirectionY() < 0) {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()-2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()+2);
                } else if (myPlayer.getDirectionX() < 0 && myPlayer.getDirectionY() >= 0) {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()+2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()+2);
                } else if (myPlayer.getDirectionX() >= 0 && myPlayer.getDirectionY() > 0) {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()+2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()-2);
                } else {
                    myPlayer.setDirectionX(myPlayer.getDirectionX()-2);
                    myPlayer.setDirectionY(myPlayer.getDirectionY()-2);
                }
            }
        });

        canvasView = findViewById(R.id.canvas);
        canvasView.setPaintColor(myPlayer.getColor());

        collisionRadius = myPlayer.getCircleSize() / 2.0f;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        float posX = myPlayer.getCurrentX() + myPlayer.getDirectionX();
                        float posY = myPlayer.getCurrentY() + myPlayer.getDirectionY();

                        drawPlayerCoordinates();
                        canvasView.addCircle(posX, posY);
                        myPlayer.setCurrentY((int) posY);
                        myPlayer.setCurrentX((int) posX);

                        //Kollisionserkennung mit den Rändern
                        if (myPlayer.getCurrentY() <= myPlayer.getCircleSize()/2 + 5) {
                            collision = true;
                        } else if (myPlayer.getCurrentY() >= canvasView.getHeight() - myPlayer.getCircleSize()/2 - 7) {
                            collision = true;
                        } else if (myPlayer.getCurrentX() >= canvasView.getWidth() - myPlayer.getCircleSize()/2 - 7) {
                            collision = true;
                        } else if (myPlayer.getCurrentX() <= myPlayer.getCircleSize()/2 + 7) {
                            collision = true;
                        }

                        //Kollisionserkennung mit sich selbst
                        PointF currentPoint = new PointF(myPlayer.getCurrentX(), myPlayer.getCurrentY());
                        float collisionRadius = myPlayer.getCircleSize() / 2.0f;

                        int numPointsToCheck = Math.max(0, myPlayer.getPointsXY().size() - 2); // Anzahl der Punkte, die überprüft werden sollen

                        for (int i = 0; i < numPointsToCheck; i++) {
                            PointF point = myPlayer.getPointsXY().get(i);
                            float distanceX = Math.abs(currentPoint.x - point.x);
                            float distanceY = Math.abs(currentPoint.y - point.y);

                            if (distanceX < collisionRadius + myPlayer.getCircleSize() && distanceY < collisionRadius + myPlayer.getCircleSize() && !isCloseToLastTwoPoints(currentPoint, myPlayer.getPointsXY())) {
                                // Kollision mit sich selbst erkannt
                                collision = true;
                                break;
                            }
                        }

                        myPlayer.addPoint(currentPoint);

                        if(collision){
                            canvasView.addCircle(posX, posY);
                            canvasView.clearCanvas();
                            myPlayer.getPointsXY().clear();
                            myPlayer.setCurrentX(200);
                            myPlayer.setCurrentY(800);
                            myPlayer.setDirectionX(0);
                            myPlayer.setDirectionY(-10);
                            collision = false;
                        }

                        System.out.println("Direction X: " + myPlayer.getDirectionX() + " DirectionY: " + myPlayer.getDirectionY());
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

    private void drawPlayerCoordinates() {
        List<Player> players = myGame.getPlayers();

        for (Player player : players) {
            float posX = player.getCurrentX();
            float posY = player.getCurrentY();
            canvasView.addCircle(posX, posY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }
}