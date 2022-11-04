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
		for(int i = 1; i <= nPlayers; i++) {
			display.waitForPlayerToClickRoll(i);
			int[] dices = firstTry();
			
			for(int x = 1; x < 3; x++) {
				if(hasSelected(dices)){
					changeResults(dices);
				}else {
					break;
				}
			}
			
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
		for(int n = 1; n <= dices.length; n++) {
			if (display.isDieSelected(n-1)) {
				dices[n-1] = rgen.nextInt(ONES, SIXES);
				display.displayDice(dices);
			}
		}
	}
	
	
	private boolean hasSelected(int[] dices) {
		display.waitForPlayerToSelectDice();
		for(int n = 1; n <= dices.length; n++) {
			if (display.isDieSelected(n-1)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	private int getScore(int[] dices) {
		int category = display.waitForPlayerToSelectCategory();
		int score = 0;
		if(category < UPPER_SCORE) {
			score = upperCategoryScore(dices, category);
		}else if(category == THREE_OF_A_KIND) {
			
		}
		return category;
	}
	
	
	private int upperCategoryScore(int[] dices, int category) {
		int score = 0;
		for(int dice: dices) {
			if(dice == category) {
				score += dice;
			}
		}
		
		return score;
	}
	
	private int 
}
