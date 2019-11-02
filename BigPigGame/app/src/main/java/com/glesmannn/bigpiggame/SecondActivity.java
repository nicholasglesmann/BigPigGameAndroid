package com.glesmannn.bigpiggame;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private PigGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_activity);

        String player1Name = getIntent().getExtras().getString("player1name");
        String player2Name = getIntent().getExtras().getString("player2name");

        SecondFragment secondFragment = (SecondFragment)getFragmentManager()
                .findFragmentById(R.id.second_fragment);
        secondFragment.setPlayerNames(player1Name, player2Name);
    }

}
