package com.example.achtung_die_kurve;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.io.IOException;
import java.net.ServerSocket;
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

    public void test() throws IOException {
        for (int i = 1; i < 6000; i++) {
            try {
                ServerSocket s = new ServerSocket(i);
                System.out.println(i);
            } catch (IOException ex) {
                continue; // try next port
            }
        }

        // if the program gets here, no port in the range was found
        throw new IOException("no free port found");
    }

    public void hostGameOnClick(View view, String option){
        openSetUsernamePopup(view, option);
    }

    @SuppressLint("ClickableViewAccessibility")
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
        popupWindow.setOutsideTouchable(false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final Button continueButton = popupView.findViewById(R.id.continueButton);
        final EditText usernameInput = popupView.findViewById(R.id.usernameInput);
        final ImageButton cancelButton = popupView.findViewById(R.id.cancelButton);

        //Continuebutton disablen, solang noch nichts eingegeben wurde
        continueButton.setEnabled(false);
        continueButton.setAlpha(0.6f);
        continueButton.setClickable(false);

        //maximale Länge von 15 Zeichen erlauben
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(15);
        usernameInput.setFilters(filters);

        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    continueButton.setEnabled(true);
                    continueButton.setAlpha(1.0f);
                    continueButton.setClickable(true);
                }else{
                    continueButton.setEnabled(false);
                    continueButton.setAlpha(0.6f);
                    continueButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        continueButton.setOnClickListener(view1 -> continueButtonOnClick(popupWindow, view, usernameInput, option));
    }

    public void openEnterPasswordPopup(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.enter_password_popup, null);

        // create the popup window
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int)(displayMetrics.widthPixels/1.5);
        int height = (int)(displayMetrics.heightPixels/1.1);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(false);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        final Button continueButton = popupView.findViewById(R.id.continueButton);
        final EditText passwordInput = popupView.findViewById(R.id.passwordInput);
        final ImageButton cancelButton = popupView.findViewById(R.id.cancelButton);

        //Continuebutton disablen, solang noch nichts eingegeben wurde
        continueButton.setEnabled(false);
        continueButton.setAlpha(0.6f);
        continueButton.setClickable(false);

        //maximale Länge von 15 Zeichen erlauben
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(15);
        passwordInput.setFilters(filters);

        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    continueButton.setEnabled(true);
                    continueButton.setAlpha(1.0f);
                    continueButton.setClickable(true);
                }else{
                    continueButton.setEnabled(false);
                    continueButton.setAlpha(0.6f);
                    continueButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        continueButton.setOnClickListener(view1 -> continueButtonOnClickPassword(popupWindow, view, passwordInput));
    }

    public void continueButtonOnClickPassword(PopupWindow popupWindow, View view, EditText passwordInput){

        popupWindow.dismiss();
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

        popupWindow.setOutsideTouchable(false);


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        final ImageButton cancelButton = popupView.findViewById(R.id.cancelButton);
        final CheckBox privateCheckbox = popupView.findViewById(R.id.privateCheckBox);
        final EditText gameNameInput = popupView.findViewById(R.id.gameNameInput);
        final TextView passwordText = popupView.findViewById(R.id.password);
        final EditText passwordInput = popupView.findViewById(R.id.passwordInput);
        final Button createGameButton = popupView.findViewById(R.id.createGameButton);

        //maximale Länge von 15 Zeichen erlauben
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(15);
        gameNameInput.setFilters(filters);
        passwordInput.setFilters(filters);

        createGameButton.setEnabled(false);
        createGameButton.setAlpha(0.6f);
        createGameButton.setClickable(false);

        gameNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    createGameButton.setEnabled(true);
                    createGameButton.setAlpha(1.0f);
                    createGameButton.setClickable(true);
                }else{
                    createGameButton.setEnabled(false);
                    createGameButton.setAlpha(0.6f);
                    createGameButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0 && gameNameInput.getText().length() > 0){
                    createGameButton.setEnabled(true);
                    createGameButton.setAlpha(1.0f);
                    createGameButton.setClickable(true);
                }else{
                    createGameButton.setEnabled(false);
                    createGameButton.setAlpha(0.6f);
                    createGameButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // add Listeners
        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
        privateCheckboxAddCheckedChangeListener(privateCheckbox, passwordText, passwordInput, createGameButton, gameNameInput);
        createGameButton.setOnClickListener(view1 -> createGameButtonOnClick(view, popupWindow, gameNameInput, passwordInput, privateCheckbox));
    }

    public void createGameButtonOnClick(View view, PopupWindow popupWindow, EditText gameNameInput, EditText passwordInput, CheckBox privateCheckbox){
        gameName = String.valueOf(gameNameInput.getText());
        password = String.valueOf(passwordInput.getText());
        Player myPlayer = new Player(username, true);
        Game myGame = new Game(gameName, isPrivate, password);
        Intent intent = new Intent(this, GameQueue.class);
        intent.putExtra("myGame", myGame);
        intent.putExtra("myPlayer", myPlayer);
        startActivity(intent);
        setContentView(R.layout.game_queue);
        popupWindow.dismiss();
    }



    public void privateCheckboxAddCheckedChangeListener(CheckBox privateCheckbox, TextView passwordText, EditText passwordInput, Button createGameButton, EditText gameNameInput){
        privateCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    isPrivate = true;
                    passwordText.setAlpha(1.0f);
                    passwordInput.setAlpha(1.0f);
                    passwordInput.setEnabled(true);
                    if(gameNameInput.getText().length() > 0 && passwordInput.getText().length() > 0){
                        createGameButton.setEnabled(true);
                        createGameButton.setAlpha(1.0f);
                        createGameButton.setClickable(true);
                    }else{
                        createGameButton.setEnabled(false);
                        createGameButton.setAlpha(0.6f);
                        createGameButton.setClickable(false);
                    }
                }else{
                    isPrivate = false;
                    passwordText.setAlpha(0.3f);
                    passwordInput.setAlpha(0.3f);
                    passwordInput.setEnabled(false);
                    if(gameNameInput.getText().length() > 0){
                        createGameButton.setEnabled(true);
                        createGameButton.setAlpha(1.0f);
                        createGameButton.setClickable(true);
                    }else{
                        createGameButton.setEnabled(false);
                        createGameButton.setAlpha(0.6f);
                        createGameButton.setClickable(false);
                    }
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

        continueButton.setAlpha(0.6f);
        continueButton.setClickable(false);
        continueButton.setEnabled(false);
        GameReceiver gameReceiver = new GameReceiver();
        ArrayList<Game> foundGames = gameReceiver.searchGames();
        foundGames.add(new Game("TestGame", false));

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
                tr.setOnClickListener(v -> tableRowOnClick(tr, continueButton));

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
        continueButton.setOnClickListener(v -> continueButtonOnClick(popupWindow, availableGames, foundGames));
        cancelButton.setOnClickListener(view1 -> popupWindow.dismiss());
    }

    public void joinGameOnClick(View view, String option){
        openSetUsernamePopup(view, option);
    }

    public void tableRowOnClick(TableRow tr, Button continueButton){
        tr.setAlpha(0.6f);
        continueButton.setAlpha(1.0f);
        continueButton.setClickable(true);
        continueButton.setEnabled(true);
    }

    public void continueButtonOnClick(PopupWindow popupWindow, TableLayout availableGames, ArrayList<Game> foundGames){

        int tableRowsNumber = availableGames.getChildCount();
        System.out.println(tableRowsNumber);
        Player myPlayer = new Player(username, false);
        for (int i = 0; i < tableRowsNumber; i++){
            if(availableGames.getChildAt(i).getAlpha() == 0.6f){
                GameReceiver gameReceiver = new GameReceiver();
                gameReceiver.sendPlayer(foundGames.get(i).getInetAddressTCP(), myPlayer, foundGames.get(i));
                //gameReceiver.initiateTCPConnection(foundGames.get(i).getInetAddressTCP(), myPlayer, foundGames.get(i));
                Intent intent = new Intent(this, GameQueue.class);
                intent.putExtra("myPlayer", myPlayer);
                intent.putExtra("myGame", foundGames.get(i));
                intent.putExtra("gameReceiver", gameReceiver);
                startActivity(intent);
                setContentView(R.layout.game_queue);
                popupWindow.dismiss();
                break;

            }
        }
    }
}