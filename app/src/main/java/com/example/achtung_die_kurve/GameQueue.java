package com.example.achtung_die_kurve;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class GameQueue extends AppCompatActivity {

    private Game myGame;
    private Player myPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_queue);

        //Spieler holen
        Intent intent = getIntent();
        myPlayer = (Player) intent.getSerializableExtra("myPlayer");

        //Game-Objekt holen
        myGame = (Game) intent.getSerializableExtra("myGame");

        //Spieler dem Spiel hinzufügen
        myGame.addPlayer(myPlayer);

        if(myPlayer.isHost()){
            //Game für andere publishen
            GamePublisher gamePublisher = new GamePublisher(myGame);
            gamePublisher.startPublishingGame();

            //Items + Booleans zur Hashmap hinzufügen
            myGame.getItems().put("fast_slow", true);
            myGame.getItems().put("thick_thin", true);
            myGame.getItems().put("more_less_holes", true);
            myGame.getItems().put("reverse", true);
            myGame.getItems().put("no_wall", true);
        }

        //Spieler-Textviews holen
        final TextView host = findViewById(R.id.player1);
        final TextView player2 = findViewById(R.id.player2);
        final TextView player3 = findViewById(R.id.player3);
        final TextView player4 = findViewById(R.id.player4);

        //Spieler-Namen setzen
        switch(myGame.getPlayers().size()){
            case 1:
                //Host Username setzen
                host.setText(myPlayer.getUsername());
                break;
            case 2:
                player2.setText(myPlayer.getUsername());
                break;
            case 3:
                player3.setText(myPlayer.getUsername());
                break;
            case 4:
                player4.setText(myPlayer.getUsername());
                break;
        }

        final Spinner points_spinner = (Spinner) findViewById(R.id.points_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.points_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        points_spinner.setAdapter(adapter);

        //nur Host darf Punkte einstellen
        if (!myPlayer.isHost()) {
            points_spinner.setEnabled(false); // Deaktiviere den Spinner für Nicht-Hosts
        }

        //Item-Textviews holen
        final TextView fast_slow = findViewById(R.id.fast_slow);
        final TextView thick_thin = findViewById(R.id.thick_thin);
        final TextView more_less_holes = findViewById(R.id.more_less_holes);
        final TextView reverse = findViewById(R.id.reverse);
        final TextView no_wall = findViewById(R.id.no_wall);

        //onClickListener setzten
        fast_slow.setOnClickListener(view -> onItemClick(fast_slow));
        thick_thin.setOnClickListener(view -> onItemClick(thick_thin));
        more_less_holes.setOnClickListener(view -> onItemClick(more_less_holes));
        reverse.setOnClickListener(view -> onItemClick(reverse));
        no_wall.setOnClickListener(view -> onItemClick(no_wall));

        //Farben Elemente holen
        final Button light_green = findViewById(R.id.light_green);
        final Button yellow = findViewById(R.id.yellow);
        final Button orange = findViewById(R.id.orange);
        final Button red = findViewById(R.id.red);
        final Button pink = findViewById(R.id.pink);
        final Button blue = findViewById(R.id.blue);

        //onClickListener setzen
        light_green.setOnClickListener(view -> setColor("#1CFF06"));
        yellow.setOnClickListener(view -> setColor("#FFFF00"));
        orange.setOnClickListener(view -> setColor("#db7800"));
        red.setOnClickListener(view -> setColor("#FF1212"));
        pink.setOnClickListener(view -> setColor("#FF06FB"));
        blue.setOnClickListener(view -> setColor("#006CFF"));

        //Start-Button holen
        Button start = findViewById(R.id.start_game);

        //onClickListener setzen
        start.setOnClickListener(view -> onStartClick());
    }
    private void onItemClick(TextView item){
        //nur der Host kann Items aktivieren/deaktivieren
        if(myPlayer.isHost()){
            switch(getResources().getResourceEntryName(item.getId())){
                case "fast_slow":
                    if(myGame.getItems().get("fast_slow")){
                        myGame.getItems().put("fast_slow", false);
                        item.setTextColor(Color.parseColor("#FF1212"));
                    }else{
                        myGame.getItems().put("fast_slow", true);
                        item.setTextColor(Color.parseColor("#000000"));
                    }
                    break;
                case "thick_thin":
                    if(myGame.getItems().get("thick_thin")){
                        myGame.getItems().put("thick_thin", false);
                        item.setTextColor(Color.parseColor("#FF1212"));
                    }else{
                        myGame.getItems().put("thick_thin", true);
                        item.setTextColor(Color.parseColor("#000000"));
                    }
                    break;
                case "more_less_holes":
                    if(myGame.getItems().get("more_less_holes")){
                        myGame.getItems().put("more_less_holes", false);
                        item.setTextColor(Color.parseColor("#FF1212"));
                    }else{
                        myGame.getItems().put("more_less_holes", true);
                        item.setTextColor(Color.parseColor("#000000"));
                    }
                    break;
                case "reverse":
                    if(myGame.getItems().get("reverse")){
                        myGame.getItems().put("reverse", false);
                        item.setTextColor(Color.parseColor("#FF1212"));
                    }else{
                        myGame.getItems().put("reverse", true);
                        item.setTextColor(Color.parseColor("#000000"));
                    }
                    break;
                case "no_wall":
                    if(myGame.getItems().get("no_wall")){
                        myGame.getItems().put("no_wall", false);
                        item.setTextColor(Color.parseColor("#FF1212"));
                    }else{
                        myGame.getItems().put("no_wall", true);
                        item.setTextColor(Color.parseColor("#000000"));
                    }
                    break;
            }
        }
    }

    private void setColor(String stringColor){
        int color = Color.parseColor(stringColor);
        TextView textView = null;

        switch(myGame.getPlayers().size()){
            case 1:
                textView = findViewById(R.id.player1);
                break;
            case 2:
                textView = findViewById(R.id.player2);
                break;
            case 3:
                textView = findViewById(R.id.player3);
                break;
            case 4:
                textView = findViewById(R.id.player4);
                break;
        }

        GradientDrawable backgroundGradient = (GradientDrawable) textView.getBackground();
        backgroundGradient.setStroke(2, color);
        textView.setTextColor(color);
    }

    private void onStartClick(){
        //nur der Host kann das Spiel starten
        if(myPlayer.isHost()){
            Intent intent = new Intent(this, GameScreen.class);
            intent.putExtra("myGame", myGame);
            startActivity(intent);
            setContentView(R.layout.game_screen);
        }
    }

}