package com.glesmannn.bigpiggame;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    private PigGame game;

    public PigGame getGame() { return game; }

    public void setGame(PigGame game) { this.game = game; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_activity);
    }

    public void setPlayerNamesFromFirstActivity(String player1Name, String player2Name) {
        SecondFragment secondFragment = (SecondFragment)getFragmentManager().findFragmentById(R.id.second_fragment);
        secondFragment.setPlayerNamesFromFirstActivity(player1Name, player2Name);
    }


}
