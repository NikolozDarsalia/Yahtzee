/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		for(int i = 0; i < 2; i++) {
			display.waitForPlayerToClickRoll(i);
			int[] dices = firstTry();
			changeResults(dices);
		}
		

	}
	
	

		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	
	
	
	private int[] firstTry() {
		int[] dices = new int[N_DICE];
		for(int n = 1; n <= dices.length; n++) {
			int value = rgen.nextInt(ONES, SIXES);
			dices[n-1] = value;
		}
		
		display.displayDice(dices);
		return dices;
	}
	
	
	private void changeResults(int[] dices) {
		display.waitForPlayerToSelectDice();
		for(int n = 1; n <= dices.length; n++) {
			if (display.isDieSelected(n)) {
				dices[n] = rgen.nextInt(ONES, SIXES);
			}
		}
		
		display.displayDice(dices);
	}
	
	
	private boolean hasSelected(int[] dices) {
		for(int n = 1; n <= dices.length; n++) {
			if (display.isDieSelected(n)) {
				dices[n] = rgen.nextInt(ONES, SIXES);
				return true;
			}
		}
		
		return false;
	}
	
	

}
