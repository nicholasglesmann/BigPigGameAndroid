package com.glesmannn.bigpiggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    PigGame game;
    EditText player1NameEditText;
    EditText player2NameEditText;
    TextView player1ScoreTextView;
    TextView player2ScoreTextView;
    TextView playerTurnLabel;
    ImageView dieImage;
    TextView pointsForTurnTextView;
    TextView pointsForTurnLabel;
    Button rollDieButton;
    Button endTurnButton;
    Button newGameButton;
    private static final String P1_SCORE = "player_1_score";
    private static final String P2_SCORE = "player_2_score";
    private static final String T_POINTS = "turn_points";
    private static final String TURN = "player_turn";
    private static final String CURRENT_ROLL = "current_roll";
    private static final String GAME_STARTED = "game_started";
    int currentRoll = 0;
    boolean gameOverNextTurn = false;
    boolean gameStarted = false;

    public void setPlayerNames() {
        game.setPlayer1Name(player1NameEditText.getText().toString());
        game.setPlayer2Name(player2NameEditText.getText().toString());
    }

    public void rollDieClickEvent(View v) {
        currentRoll = game.rollDie();

        if(currentRoll == 8) {
            rollDieButton.setEnabled(false);
        }
        setPlayerNames();
        displayImage();
        displayPointsForTurn();
    }

    public void endTurnClickEvent(View v) {
        // enable the roll die button
        rollDieButton.setEnabled(true);

        // change player turn
        game.changeTurn();

        // update all views
        displayPlayerTurn();
        displayPointsForTurn();
        displayPlayerPoints();

        // if the game is fully over, call gameOver()
        if(gameOverNextTurn) {
            gameOver();
            return;
        }

        // sets a flag to end the game after the next time the end turn button is clicked
        if(game.getPlayer1Score() >= 100 || game.getPlayer2Score() >= 100) {

            pointsForTurnLabel.setText("LAST ROUND!!!");

            // set the end game next turn flag
            gameOverNextTurn = true;
        }

        // set the roll to 0
        currentRoll = 0;

        // update the image to blank die
        displayImage();
    }

    public void newGameClickEvent(View v) {
        rollDieButton.setEnabled(true);
        endTurnButton.setEnabled(true);
        player1NameEditText.setEnabled(false);
        player2NameEditText.setEnabled(false);
        game = new PigGame();
        game.startGame();
        pointsForTurnLabel.setText("Points For This Turn");
        setPlayerNames();
        displayPlayerTurn();
        displayPlayerPoints();
        displayPointsForTurn();

        // set the roll to 0
        currentRoll = 0;

        // update the image to blank die
        displayImage();
    }

    public void gameOver() {
        rollDieButton.setEnabled(false);
        endTurnButton.setEnabled(false);
        player1NameEditText.setEnabled(true);
        player2NameEditText.setEnabled(true);
        String winner;
        if(game.getPlayer1Score() > game.getPlayer2Score()) {
            if(game.getPlayer1Name().length() > 0)
                winner = game.getPlayer1Name() + " wins!";
            else
                winner = "Player 1 wins!";
        } else if (game.getPlayer2Score() > game.getPlayer1Score()) {
            if(game.getPlayer2Name().length() > 0)
                winner = game.getPlayer2Name() + " wins!";
            else
                winner = "Player 2 wins!";
        } else {
            winner = "It's a tie.";
        }

        game.endGame();
        gameOverNextTurn = false;
        pointsForTurnLabel.setText("Game over.");
        playerTurnLabel.setText(winner);

        // set the roll to 0
        currentRoll = 0;

        // update the image to blank die
        displayImage();
    }

    private void displayPlayerTurn() {
        String currentPlayer = game.getCurrentPlayer();
        if(game.getGameStarted()) {
            playerTurnLabel.setVisibility(TextView.VISIBLE);
            if(currentPlayer.length() > 0) {
                playerTurnLabel.setText(currentPlayer + "'s turn");
            } else if (game.getTurn() % 2 == 1){
                playerTurnLabel.setText("Player 1's turn");
            } else {
                playerTurnLabel.setText("Player 2's turn");
            }
        } else {
            playerTurnLabel.setVisibility(TextView.GONE);
        }
    }

    private void displayPointsForTurn() {
        pointsForTurnTextView.setText(String.format(Locale.US, "%d", game.getTurnPoints()));
    }

    private void displayPlayerPoints() {
        player1ScoreTextView.setText(String.format(Locale.US, "%d", game.getPlayer1Score()));
        player2ScoreTextView.setText(String.format(Locale.US, "%d", game.getPlayer2Score()));
    }

    private void displayImage() {
        int id = 0;

        switch(currentRoll){
            case 0:
                id = R.drawable.die8blank;
                break;
            case 1:
                id = R.drawable.die8side1;
                break;
            case 2:
                id = R.drawable.die8side2;
                break;
            case 3:
                id = R.drawable.die8side3;
                break;
            case 4:
                id = R.drawable.die8side4;
                break;
            case 5:
                id = R.drawable.die8side5;
                break;
            case 6:
                id = R.drawable.die8side6;
                break;
            case 7:
                id = R.drawable.die8side7;
                break;
            case 8:
                id = R.drawable.die8side8;
                break;
        }

        dieImage.setImageResource(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        dieImage = findViewById(R.id.dieImage);
        player1NameEditText = findViewById(R.id.player1NameEditText);
        player2NameEditText = findViewById(R.id.player2NameEditText);
        player1ScoreTextView = findViewById(R.id.player1ScoreTextView);
        player2ScoreTextView = findViewById(R.id.player2ScoreTextView);
        playerTurnLabel = findViewById(R.id.playerTurnLabel);
        pointsForTurnTextView = findViewById(R.id.pointsForTurnTextView);
        pointsForTurnLabel = findViewById(R.id.pointsForTurnLabel);
        rollDieButton = findViewById(R.id.rollDieButton);
        endTurnButton = findViewById(R.id.endTurnButton);
        newGameButton = findViewById(R.id.newGameButton);

        int p1Score = 0, p2Score = 0, tPoints = 0;
        int t = 1;

        if(savedInstanceState == null) {
            rollDieButton.setEnabled(false);
            endTurnButton.setEnabled(false);
        } else if(savedInstanceState != null) {
            p1Score = savedInstanceState.getInt(P1_SCORE);
            p2Score = savedInstanceState.getInt(P2_SCORE);
            tPoints = savedInstanceState.getInt(T_POINTS);
            t = savedInstanceState.getInt(TURN);
            currentRoll = savedInstanceState.getInt(CURRENT_ROLL);
            gameStarted = savedInstanceState.getBoolean(GAME_STARTED);
        }

        game = new PigGame(p1Score, p2Score, tPoints, t, currentRoll, gameStarted);

        if(!game.getGameStarted()) {
            rollDieButton.setEnabled(false);
            endTurnButton.setEnabled(false);
        } else {
            player1NameEditText.setEnabled(false);
            player2NameEditText.setEnabled(false);
        }

        displayImage();
        displayPointsForTurn();
        displayPlayerPoints();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setPlayerNames();
        displayPlayerTurn();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Toast t = Toast.makeText(this, String.format(Locale.US, "%d", game.getTurnPoints()), Toast.LENGTH_LONG);
        t.show();
        outState.putInt(P1_SCORE, game.getPlayer1Score());
        outState.putInt(P2_SCORE, game.getPlayer2Score());
        outState.putInt(T_POINTS, game.getTurnPoints());
        outState.putInt(TURN, game.getTurn());
        outState.putInt(CURRENT_ROLL, game.getCurrentRoll());
        outState.putBoolean(GAME_STARTED, game.getGameStarted());
        super.onSaveInstanceState(outState);
    }
}
