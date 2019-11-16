package com.glesmannn.bigpiggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Fragment;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class SecondFragment extends Fragment implements View.OnClickListener {


    // Game Logic
    PigGame game;
    int[] currentRolls = new int[] {0,0};

    // UI Widgets
    TextView player1NameLabel;
    TextView player2NameLabel;
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

    // This method is only called from the second activity
    public void setPlayerNames(String player1Name, String player2Name) {
        game.setPlayer1Name(player1Name);
        game.setPlayer2Name(player2Name);
    }

    // This method is only called from the first activity. I realize that it is redundant to do it this way
    // but it helps me understand how the data is being passed around
    public void setPlayerNamesFromFirstActivity(String player1Name, String player2Name) {
        this.setPlayerNames(player1Name, player2Name);
        displayPlayerNames();
        displayPlayerTurn();
    }

    public void rollDieClickEvent(View v) {

        currentRolls = game.rollDice();

        if(game.getPlayerDead()) {
            rollDieButton.setEnabled(false);
        }

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

    public void gameOver() {
        rollDieButton.setEnabled(false);
        endTurnButton.setEnabled(false);
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

    private void displayPlayerNames() {
        player1NameLabel.setText(game.getPlayer1Name());
        player2NameLabel.setText(game.getPlayer2Name());
    }

    private void displayPlayerTurn() {
        String currentPlayer = game.getCurrentPlayer();
        if(!currentPlayer.isEmpty()) {
            playerTurnLabel.setText(currentPlayer + "'s turn");
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initial Values
        int player1Score = 0, player2Score = 0, turnPoints = 0, turn = 1;
        boolean gameStarted = false, playerDead = false, gameOverNextTurn = false;
        String player1Name = "", player2Name = "";

        if(savedInstanceState != null) {
            // Recall Instance State Variables if a game is in progress
            player1Score = savedInstanceState.getInt(PLAYER1_SCORE);
            player2Score = savedInstanceState.getInt(PLAYER2_SCORE);
            turnPoints = savedInstanceState.getInt(TURN_POINTS);
            turn = savedInstanceState.getInt(TURN);
            currentRolls = savedInstanceState.getIntArray(CURRENT_ROLLS);
            gameStarted = savedInstanceState.getBoolean(GAME_STARTED);
            playerDead = savedInstanceState.getBoolean(PLAYER_DEAD);
            gameOverNextTurn = savedInstanceState.getBoolean(GAME_OVER_NEXT_TURN);
            player1Name = savedInstanceState.getString("player1name");
            player2Name = savedInstanceState.getString("player2name");
        }

        // Set preferences to the default values defined in preferences.xml
        // readAgain is false so the values will only be set once
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Create game logic with instance variables (or initial values if no game in progress)
        game = new PigGame(player1Score, player2Score, turnPoints, turn, currentRolls, gameStarted, gameOverNextTurn);
        game.setPlayerDead(playerDead);

        game.setPlayer1Name(player1Name);
        game.setPlayer2Name(player2Name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate the fragment layout
        View view = inflater.inflate(R.layout.second_fragment, container, false);

        // UI Widget References
        dieImage = view.findViewById(R.id.dieImage);
        dieImage2 = view.findViewById(R.id.dieImage2);
        player1NameLabel = view.findViewById(R.id.player1NameLabel);
        player2NameLabel = view.findViewById(R.id.player2NameLabel);
        player1ScoreTextView = view.findViewById(R.id.player1ScoreTextView);
        player2ScoreTextView = view.findViewById(R.id.player2ScoreTextView);
        playerTurnLabel = view.findViewById(R.id.playerTurnLabel);
        pointsForTurnTextView = view.findViewById(R.id.pointsForTurnTextView);
        pointsForTurnLabel = view.findViewById(R.id.pointsForTurnLabel);
        rollDieButton = view.findViewById(R.id.rollDieButton);
        endTurnButton = view.findViewById(R.id.endTurnButton);
        newGameButton = view.findViewById(R.id.newGameButton2);

        // Instantiate all arrays
        diceImages = new ImageView[]{dieImage,dieImage2};

        rollDieButton.setOnClickListener(this);
        endTurnButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // get updated preferences
        numDie = Integer.parseInt(prefs.getString("num_die", "1"));
        scoreToWin = Integer.parseInt(prefs.getString("score_to_win", "100"));
        endTurnNumber = Integer.parseInt(prefs.getString("end_turn_number", "8"));


        // update preferences in the game
        game.setNumDie(numDie);
        game.setScoreToWin(scoreToWin);
        game.setEndTurnNumber(endTurnNumber);

        player1NameLabel.setText(game.getPlayer1Name());
        player2NameLabel.setText(game.getPlayer2Name());

        // display stuff
        displayImage();
        displayPlayerTurn();
        displayPointsForTurn();
        displayPlayerPoints();


        // disable the roll die button if the player is dead but hasn't ended turn
        if(game.getPlayerDead()) {
            rollDieButton.setEnabled(false);
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PLAYER1_SCORE, game.getPlayer1Score());
        outState.putInt(PLAYER2_SCORE, game.getPlayer2Score());
        outState.putInt(TURN_POINTS, game.getTurnPoints());
        outState.putInt(TURN, game.getTurn());
        outState.putIntArray(CURRENT_ROLLS, game.getCurrentRolls());
        outState.putBoolean(GAME_STARTED, game.getGameStarted());
        outState.putBoolean(PLAYER_DEAD, game.getPlayerDead());
        outState.putBoolean(GAME_OVER_NEXT_TURN, game.getGameOverNextTurn());
        outState.putString("player1name", game.getPlayer1Name());
        outState.putString("player2name", game.getPlayer2Name());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_pig_game, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                Toast.makeText(getActivity(), "About", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected((item));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rollDieButton) {
            rollDieClickEvent(v);
        } else if (v.getId() == R.id.endTurnButton) {
            endTurnClickEvent(v);
        }
    }
}
