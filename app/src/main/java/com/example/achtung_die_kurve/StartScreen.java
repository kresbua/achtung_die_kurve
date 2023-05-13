package com.example.achtung_die_kurve;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.security.Identity;
import java.util.ArrayList;


public class StartScreen extends AppCompatActivity {

    private String username;
    private String gameName;
    private String password;

    private boolean isPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        final Button hostGame = findViewById(R.id.hostGameButton);
        final Button joinGame = findViewById(R.id.joinGameButton);

        hostGame.setOnClickListener(view -> hostGameOnClick(getWindow().getDecorView().findViewById(android.R.id.content), "host"));
        joinGame.setOnClickListener(view -> joinGameOnClick(getWindow().getDecorView().findViewById(android.R.id.content), "join"));

    }


    public void hostGameOnClick(View view, String option){
        openSetUsernamePopup(view, option);
    }

    public void openSetUsernamePopup(View view, String option){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.set_username_popup, null);

        // create the popup window
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int)(displayMetrics.widthPixels/1.5);
        int height = (int)(displayMetrics.heightPixels/1.1);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final Button continueButton = popupView.findViewById(R.id.continueButton);
        final EditText usernameInput = popupView.findViewById(R.id.usernameInput);
        final ImageButton cancelButton = popupView.findViewById(R.id.cancelButton);

        // add Listeners
        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
        continueButton.setOnClickListener(view1 -> continueButtonOnClick(popupWindow, view, usernameInput, option));


    }

    public void continueButtonOnClick(PopupWindow popupWindow, View view, EditText usernameInput, String option){

        if(option.equals("host")){
            openCreateGamePopup(view);
        }else if(option.equals("join")){
            openJoinGamePopup(view);
        }
        username = String.valueOf(usernameInput.getText());
        System.out.println("-----------USERNAME: " + username + " ---------------");
        popupWindow.dismiss();
    }


    public void openCreateGamePopup(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.create_game_popup, null);

        // create the popup window
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int)(displayMetrics.widthPixels/1.5);
        int height = (int)(displayMetrics.heightPixels/1.1);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        final ImageButton cancelButton = popupView.findViewById(R.id.cancelButton);
        final CheckBox privateCheckbox = popupView.findViewById(R.id.privateCheckBox);
        final EditText gameNameInput = popupView.findViewById(R.id.gameNameInput);
        final TextView passwordText = popupView.findViewById(R.id.password);
        final EditText passwordInput = popupView.findViewById(R.id.passwordInput);
        final Button createGameButton = popupView.findViewById(R.id.createGameButton);


        // add Listeners
        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
        privateCheckboxAddCheckedChangeListener(privateCheckbox, passwordText, passwordInput);
        createGameButton.setOnClickListener(view1 -> createGameButtonOnClick(popupWindow, gameNameInput, passwordInput));
    }

    public void createGameButtonOnClick(PopupWindow popupWindow, EditText gameNameInput, EditText passwordInput){
        gameName = String.valueOf(gameNameInput.getText());
        password = String.valueOf(passwordInput.getText());
        Game myGame = new Game(gameName, isPrivate, password);
        Player myPlayer = new Player(username, true);
        Intent intent = new Intent(this, GameQueue.class);
        intent.putExtra("myGame", myGame);
        intent.putExtra("myPlayer", myPlayer);
        startActivity(intent);
        setContentView(R.layout.game_queue);
        popupWindow.dismiss();
    }


    public void privateCheckboxAddCheckedChangeListener(CheckBox privateCheckbox, TextView passwordText, EditText passwordInput){
        privateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    isPrivate = true;
                    passwordText.setAlpha(1.0f);
                    passwordInput.setAlpha(1.0f);
                    passwordInput.setFocusable(true);
                    passwordInput.setFocusable(View.FOCUSABLE);
                }else{
                    isPrivate = false;
                    passwordText.setAlpha(0.3f);
                    passwordInput.setAlpha(0.3f);
                    passwordInput.setFocusable(false);
                    passwordInput.setFocusable(View.NOT_FOCUSABLE);
                }
            }
        });
    }

    private void openJoinGamePopup(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.join_game_popup, null);

        // create the popup window
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int)(displayMetrics.widthPixels/1.5);
        int height = (int)(displayMetrics.heightPixels/1.1);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        final ImageButton cancelButton = popupView.findViewById(R.id.cancelButton);
        final TableLayout availableGames = popupView.findViewById(R.id.availableGames);
        final Button continueButton = popupView.findViewById(R.id.continueButton);

        GameReceiver gameReceiver = new GameReceiver();
        ArrayList<Game> foundGames = gameReceiver.searchGames();
        Typeface retroFont = getResources().getFont(R.font.retro_gaming);

        if(foundGames != null){ //Games available
            for(Game game : foundGames){
                TableRow tr = new TableRow(this);
                tr.setBackgroundResource(R.drawable.custom_tablerow);
                TableRow.LayoutParams tlparams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.FILL_PARENT);
                tr.setLayoutParams(tlparams);
                tr.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

                TableRow.LayoutParams tvparams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.FILL_PARENT);

                TextView gameName = new TextView(this);
                gameName.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                gameName.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                gameName.setLayoutParams(tvparams);
                gameName.setTypeface(retroFont);
                gameName.setText(game.getGameName());
                gameName.setTextColor(getResources().getColor(R.color.neon_blue));
                tr.addView(gameName);// add the column to the table row here

                TextView status = new TextView(this);
                status.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                status.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                status.setLayoutParams(tvparams);
                status.setTypeface(retroFont);
                status.setText(game.privacyStatusToString());
                status.setTextColor(getResources().getColor(R.color.neon_blue));
                tr.addView(status);

                TextView playerNumber = new TextView(this);
                playerNumber.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                playerNumber.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                playerNumber.setLayoutParams(tvparams);
                playerNumber.setTypeface(retroFont);
                playerNumber.setText(game.getPlayerNumber() + "/4");
                playerNumber.setTextColor(getResources().getColor(R.color.neon_blue));
                tr.addView(playerNumber);
                availableGames.addView(tr);
            }
        }
        continueButton.setOnClickListener(v -> continueButtonOnClick(popupWindow));
        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
    }

    public void joinGameOnClick(View view, String option){
        openSetUsernamePopup(view, option);
    }

    public void continueButtonOnClick(PopupWindow popupWindow){
        Player myPlayer = new Player(username, false);
        Intent intent = new Intent(this, GameQueue.class);
        intent.putExtra("myPlayer", myPlayer);
        startActivity(intent);
        setContentView(R.layout.game_queue);
        popupWindow.dismiss();
    }
}