package com.example.achtung_die_kurve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class GameQueue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_queue);

        Intent intent = getIntent();
        Game myGame = (Game) intent.getSerializableExtra("myGame");
        GamePublisher gamePublisher = new GamePublisher(myGame);
        gamePublisher.startPublishingGame();

        Spinner points_spinner = (Spinner) findViewById(R.id.points_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.points_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        points_spinner.setAdapter(adapter);

        //Items holen + onClickListener setzten (Farbe ändern und Boolean setzen)
        TextView fast_slow = findViewById(R.id.fast_slow);
        TextView thick_thin = findViewById(R.id.thick_thin);
        TextView more_less_holes = findViewById(R.id.more_less_holes);
        TextView reverse = findViewById(R.id.reverse);
        TextView no_wall = findViewById(R.id.no_wall);

        View.OnClickListener onItemClick = new View.OnClickListener() {
            @Override
            public void onClick(View item) {
                setItem((TextView) item);
            }
        };

        fast_slow.setOnClickListener(onItemClick);
        thick_thin.setOnClickListener(onItemClick);
        more_less_holes.setOnClickListener(onItemClick);
        reverse.setOnClickListener(onItemClick);
        no_wall.setOnClickListener(onItemClick);
    }
    boolean test = true; //nur zum probieren, funzt nit richtig
    private void setItem(TextView item){
        if(test){ //aktuellen Boolean holen
            item.setTextColor(Color.parseColor("#FF1212"));
            test = false;
        }else{
            item.setTextColor(Color.parseColor("#000000"));
            test = true;
        }
    }
}