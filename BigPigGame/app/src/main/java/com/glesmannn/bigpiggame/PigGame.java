package com.glesmannn.bigpiggame;

import java.util.Random;

public class PigGame {
    // player variables
	private int player1Score;
	private int player2Score;
	private String player1Name = "";
	private String player2Name = "";

	// current turn variables
	private int turn;
    private int turnPoints;
    private int[] currentRolls;
	private boolean gameStarted;
	private boolean gameOverNextTurn;
    private boolean playerDead;

    // preference variables
	private int numDie;
	private int scoreToWin;
	private int endTurnNumber;

    // random number generator
    private Random rand = new Random();
	
	public PigGame(int player1Score, int player2Score, int turnPoints, int turn, int[] currentRolls, boolean gameStarted, boolean gameOverNextTurn)
	{
		this.player1Score = player1Score;
		this.player2Score = player2Score;
		this.turnPoints = turnPoints;
		this.turn = turn;
		this.currentRolls = currentRolls;
		this.gameStarted = gameStarted;
		this.gameOverNextTurn = gameOverNextTurn;
	}

	// Getters and Setters for Player variables
	public void setPlayer1Name(String n)
	{
		if(n.isEmpty()) {
			player1Name = "Player 1";
		} else {
			player1Name = n;
		}
	}
	public String getPlayer1Name()
	{
		return player1Name;
	}
	public void setPlayer2Name(String n)
	{
		if (n.isEmpty()) {
			player2Name = "Player 2";
		} else {
			player2Name = n;
		}
	}
	public String getPlayer2Name()
	{
		return player2Name;
	}
	public int getPlayer1Score()
    {
        return player1Score;
    }
    public int getPlayer2Score()
    {
        return player2Score;
    }

    // Getters and Setters for Preferences
    public int getNumDie() { return numDie; }
    public void setNumDie(int numDie)
    {
	    this.numDie = numDie;
		for(int i = 1; i < numDie; i++) {
			currentRolls[i] = 0;
		}
	}

    public int getScoreToWin() { return scoreToWin; }
    public void setScoreToWin(int scoreToWin) { this.scoreToWin = scoreToWin; }
    public int getEndTurnNumber() { return endTurnNumber; }
    public void setEndTurnNumber(int endTurnNumber) { this.endTurnNumber = endTurnNumber; }

    // Getters and Setters for Current Turn variables
    public boolean getGameStarted() { return gameStarted; }
    public boolean getGameOverNextTurn() { return gameOverNextTurn; }
    public void setGameOverNextTurn(boolean gameOverNextTurn) { this.gameOverNextTurn = gameOverNextTurn; }
    public int getTurn() { return turn; }
    public int getTurnPoints() { return turnPoints; }
    public int[] getCurrentRolls() { return currentRolls; }
    public boolean getPlayerDead() { return playerDead; }
    public void setPlayerDead(boolean playerDead) { this.playerDead = playerDead; }







    public void togglePlayerDead() { playerDead = !playerDead; }



	
	public void resetGame()
	{
		player1Score = 0;
		player2Score = 0;
		turnPoints = 0;
		turn = 1;
	}

	public int[] rollDice()
	{
	    // create new array to store each die roll
	    currentRolls = new int[numDie];

	    // roll each die
	    for(int i = 0; i < numDie; i++) {
            int roll = rand.nextInt(8) + 1;

            // add the score to turn points
            turnPoints += roll;

            // add the die roll to the rolls array
            currentRolls[i] = roll;
        }

	    // after all die have been rolled, loop through the array looking for a roll matching
        // the endTurnNumber. If found, player should lose all points for the turn
	    for(int i = 0; i < currentRolls.length; i++) {
	        if(currentRolls[i] == endTurnNumber) {
                turnPoints = 0;
                togglePlayerDead();
                break;
            }
        }
		return currentRolls;
	}
	


	public String getCurrentPlayer()
	{
		if(turn % 2 == 1)
			return player1Name;
		else
			return player2Name;
	}


	
	public int changeTurn()
	{
		if(turn % 2 == 1)
			player1Score += turnPoints;
		else
			player2Score += turnPoints;
		
		turnPoints = 0;
		setPlayerDead(false);
		
		turn++;
		return turn;
	}

	public void startGame() {
		gameStarted = true;
	}

	public void endGame() {
		gameStarted = false;
	}


	
	public String checkForWinner()
	{
       String winnerMessage = "";
        if (player1Score >= scoreToWin || player2Score >= scoreToWin) {
            if (player2Score > player1Score) {
                winnerMessage = String.format("%s wins!", player2Name);
            }
            // Player 1 can only win after player 2 has had thier turn
            // This is so both players can have an equal number of turns
            else if (player1Score > player2Score && turn % 2 == 1) {
                winnerMessage = String.format("%s wins!", player1Name);
            } else if (player1Score == player2Score) {
                winnerMessage = "Tie";
            }
        }
		return winnerMessage;
	}
	
}
