package com.glesmannn.bigpiggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Game Logic
    PigGame game;
    int[] currentRolls = new int[] {0,0};

    // UI Widgets
    EditText player1NameEditText;
    EditText player2NameEditText;
    TextView player1ScoreTextView;
    TextView player2ScoreTextView;
    TextView playerTurnLabel;
    ImageView dieImage;
    ImageView dieImage2;
    ImageView[] diceImages;
    TextView pointsForTurnTextView;
    TextView pointsForTurnLabel;
    Button rollDieButton;
    Button endTurnButton;
    Button newGameButton;

    // Constants
    private static final String PLAYER1_SCORE = "player_1_score";
    private static final String PLAYER2_SCORE = "player_2_score";
    private static final String GAME_STARTED = "game_started";
    private static final String GAME_OVER_NEXT_TURN = "game_over_next_turn";
    private static final String TURN = "player_turn";
    private static final String TURN_POINTS = "turn_points";
    private static final String CURRENT_ROLLS = "current_rolls";
    private static final String PLAYER_DEAD = "player_dead";

    // Preferences
    SharedPreferences prefs;
    int numDie;
    int scoreToWin;
    int endTurnNumber;

    public void setPlayerNames() {
        game.setPlayer1Name(player1NameEditText.getText().toString());
        game.setPlayer2Name(player2NameEditText.getText().toString());
    }

    public void rollDieClickEvent(View v) {
        currentRolls = game.rollDice();

        if(game.getPlayerDead()) {
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
        if(game.getGameOverNextTurn()) {
            gameOver();
            return;
        }

        // sets a flag to end the game after the next time the end turn button is clicked
        if(game.getPlayer1Score() >= game.getScoreToWin() || game.getPlayer2Score() >= game.getScoreToWin()) {

            pointsForTurnLabel.setText("LAST ROUND!!!");

            // set the end game next turn flag
            game.setGameOverNextTurn(true);
        }

        // set the rolls to 0
        for(int i = 0; i < currentRolls.length; i++) {
            currentRolls[i] = 0;
        }

        // update the image to blank die
        displayImage();
    }

    public void newGameClickEvent(View v) {
        rollDieButton.setEnabled(true);
        endTurnButton.setEnabled(true);
        player1NameEditText.setEnabled(false);
        player2NameEditText.setEnabled(false);
        game.resetGame();
        game.startGame();
        pointsForTurnLabel.setText("Points For This Turn");
        setPlayerNames();
        displayPlayerTurn();
        displayPlayerPoints();
        displayPointsForTurn();

        // set the rolls to 0
        for(int i = 0; i < currentRolls.length; i++) {
            currentRolls[i] = 0;
        }

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
        game.setGameOverNextTurn(false);
        pointsForTurnLabel.setText("Game over.");
        playerTurnLabel.setText(winner);

        // set the rolls to 0
        for(int i = 0; i < currentRolls.length; i++) {
            currentRolls[i] = 0;
        }

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
        // set image view visibilities based on number of dice
        switch(numDie) {
            case 1:
                dieImage.setVisibility(ImageView.VISIBLE);
                dieImage2.setVisibility(ImageView.GONE);
                break;
            default:
                dieImage.setVisibility(ImageView.VISIBLE);
                dieImage2.setVisibility(ImageView.VISIBLE);
                break;
        }
        int id = 0;
        for(int i = 0; i < currentRolls.length; i++) {
            switch(currentRolls[i]){
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
            diceImages[i].setImageResource(id);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // UI Widget References
        dieImage = findViewById(R.id.dieImage);
        dieImage2 = findViewById(R.id.dieImage2);
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

        // Instantiate all arrays
        diceImages = new ImageView[]{dieImage,dieImage2};

        // Initial Values
        int player1Score = 0, player2Score = 0, turnPoints = 0, turn = 1;
        boolean gameStarted = false, playerDead = false, gameOverNextTurn = false;

        if(savedInstanceState == null) {
            // Disable these buttons if there isn't a game currently in progress
            rollDieButton.setEnabled(false);
            endTurnButton.setEnabled(false);
        } else if(savedInstanceState != null) {
            // Recall Instance State Variables if a game is in progress
            player1Score = savedInstanceState.getInt(PLAYER1_SCORE);
            player2Score = savedInstanceState.getInt(PLAYER2_SCORE);
            turnPoints = savedInstanceState.getInt(TURN_POINTS);
            turn = savedInstanceState.getInt(TURN);
            currentRolls = savedInstanceState.getIntArray(CURRENT_ROLLS);
            gameStarted = savedInstanceState.getBoolean(GAME_STARTED);
            playerDead = savedInstanceState.getBoolean(PLAYER_DEAD);
            gameOverNextTurn = savedInstanceState.getBoolean(GAME_OVER_NEXT_TURN);
        }

        // Set preferences to the default values defined in preferences.xml
        // readAgain is false so the values will only be set once
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Create game logic with instance variables (or initial values if no game in progress)
        game = new PigGame(player1Score, player2Score, turnPoints, turn, currentRolls, gameStarted, gameOverNextTurn);
        game.setPlayerDead(playerDead);

        if(!game.getGameStarted()) {
            rollDieButton.setEnabled(false);
            endTurnButton.setEnabled(false);
        } else {
            player1NameEditText.setEnabled(false);
            player2NameEditText.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get updated preferences
        numDie = Integer.parseInt(prefs.getString("num_die", "1"));
        scoreToWin = Integer.parseInt(prefs.getString("score_to_win", "100"));
        endTurnNumber = Integer.parseInt(prefs.getString("end_turn_number", "8"));

        // make breakfast
        String toastText;
        toastText = "NumDie = " + numDie;
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

        toastText = "ScoreToWin = " + scoreToWin;
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

        toastText = "endTurnNumber = " + endTurnNumber;
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

        // update preferences in the game
        game.setNumDie(numDie);
        game.setScoreToWin(scoreToWin);
        game.setEndTurnNumber(endTurnNumber);

        // display stuff
        displayImage();
        setPlayerNames();
        displayPlayerTurn();
        displayPointsForTurn();
        displayPlayerPoints();

        // disable the roll die button if the player is dead but hasn't ended turn
        if(game.getPlayerDead()) {
            rollDieButton.setEnabled(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Toast t = Toast.makeText(this, String.format(Locale.US, "%d", game.getTurnPoints()), Toast.LENGTH_LONG);
        // t.show();
        outState.putInt(PLAYER1_SCORE, game.getPlayer1Score());
        outState.putInt(PLAYER2_SCORE, game.getPlayer2Score());
        outState.putInt(TURN_POINTS, game.getTurnPoints());
        outState.putInt(TURN, game.getTurn());
        outState.putIntArray(CURRENT_ROLLS, game.getCurrentRolls());
        outState.putBoolean(GAME_STARTED, game.getGameStarted());
        outState.putBoolean(PLAYER_DEAD, game.getPlayerDead());
        outState.putBoolean(GAME_OVER_NEXT_TURN, game.getGameOverNextTurn());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.big_pig_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected((item));
        }
    }
}
