package com.example.achtung_die_kurve;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends AppCompatActivity {
    private CanvasView canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_screen);

        canvasView = findViewById(R.id.canvas);

        //Beispiel liste aus Koordinaten
        List<Pair<Float, Float>> coordinates = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            coordinates.add(new Pair(300f, 700f-i*5));
        }

        Log.d("INFO1", coordinates.toString());

        Button drawCircleButton = findViewById(R.id.stop);
        drawCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < coordinates.size(); i++){
                    canvasView.addCoordinates(coordinates.get(i).first, coordinates.get(i).second);
                    Log.d("INFO2: ", "added x: " + coordinates.get(i).first + ", y: " + coordinates.get(i).second);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}