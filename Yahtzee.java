/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;


import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	
	/* In run method, user inputs the number of players and then 
	 * the names of the players in dialog bar, until the YahtzeeDisplay graphics
	 * program will start, because it needs this information to draw specific score's 
	 * table on canvas. Also there is created score_board matrix variable in
	 * this method, which is for calculating player's points in the game, so
	 * the playGame method has a parameter for that. finally, the game will start by
	 * the playGame method.
	 * */
	public void run() {
		
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		
		rsr_sound.play();
		
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		
//		play("C:\\Users\\User\\Desktop\\assignment5\\Assignment5\\rsr.au");
		
		
		
//		int[][] score_board = new int[nPlayers][N_CATEGORIES];
//		fillScoreBoard(score_board);
//		
//		playGame(score_board);
		
		
	}
	

	

	
	/* This method describes the progress of the game. Until the result
	 * of gameOver method is false, each player have a first try and then two
	 * chances to change the result of randomly generated dices,
	 * if they don't select any dices on their second try, 
	 * there will be no third try. Then they choose the category and earn appropriate
	 * score for selected category and all these are happened substitutionally between
	 * players. After the gameOver method will return true, this method  
	 * will calculate upper bonuses, then final total scores of players and 
	 * finally, prints the name of winner.
	 * */
	private void playGame(int[][] score_board) {

		while(!gameOver(score_board)) {
			for(int player = 1; player <= nPlayers; player++) {
				display.printMessage(playerNames[player-1] + "'s turn! Click \"Roll Dice\" button to roll the dice.");
				display.waitForPlayerToClickRoll(player);
				int[] dices = firstTry();
				
				for(int x = 1; x < 3; x++) {
					if(hasSelected(dices)){
						changeResults(dices);
					}else {
						break;
					}
				}
				
				int category = display.waitForPlayerToSelectCategory();
				addScore(dices, player, category, score_board);
			}
		}
		
		calculateUpperBonus(score_board);
		calculateTotal(score_board);
		isWinner(score_board);
//		rsr_sound.play();

	}
	
	
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private AudioClip rsr_sound = MediaTools.loadAudioClip("rsr.au");
	
//	private AudioClip rsr_sound = MediaTools.loadAudioClip("rsr.au");
	
	/* After the player click the roll button this method
	 * will generate massive with five random integer values from
	 * 1 to 6, shows it as dices on the canvas and returns it as a massive.  
	 * */
	private int[] firstTry() {
		int[] dices = new int[N_DICE];
		for(int n = 1; n <= dices.length; n++) {
			int value = rgen.nextInt(ONES, SIXES);
			dices[n-1] = value;
		}
		
		display.displayDice(dices);
		return dices;
	}
	
	
	/* After the first try, player has an option to select dices, which
	 * they want to change. This method checks if there is any selected dices,
	 * after the player enters to roll again button, if there is any selected dices
	 * method's result is true, in other case the result is false.
	 *  */
	private boolean hasSelected(int[] dices) {
		display.waitForPlayerToSelectDice();
		for(int n = 1; n <= dices.length; n++) {
			if (display.isDieSelected(n-1)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/* If hasSelected() method's result is true, this method
	 * will change selected dices values randomly in the dices massive
	 * and adds it on canvas. 
	 * */
	private void changeResults(int[] dices) {
		for(int n = 1; n <= dices.length; n++) {
			if (display.isDieSelected(n-1)) {
				dices[n-1] = rgen.nextInt(ONES, SIXES);
				display.displayDice(dices);
			}
		}
	}
	
	
	/* score_board matrix is created in run method to control scores of players,
	 * it contains 17 rows and its number of columns depends on number of players.
	 * Its rows contain categories, upper bonus, upper and lower scores and total score
	 * values. This method gives that matrix the beginning form to start filling of it.
	 * after this method, all the values of matrix will be equal to Integer.MIN_VALUE.  
	 * */
	private int[][] fillScoreBoard(int[][] score_board){
		for(int player = 1; player <= nPlayers; player++) {
			for(int category = 1; category <= N_CATEGORIES; category++) {
				score_board[player-1][category-1] = Integer.MIN_VALUE;
			}
		}
		return score_board;
	}
	
	
	/* after the final dices will be generated, player needs to select
	 * category to earn scores. This method needs 4 arguments - final version of dices 
	 * massive, player number, category number, and matrix of score's board
	 * and it checks if the values of dices are appropriate for selected category.
	 * If answer is positive, player will get score by the logic of specific category,
	 * in other situation, the score will be 0. 
	 * Each player doesn't have an option to choose same categories again. 
	 * Finally, after this function, player's score for selected category, 
	 * sum of player's upper and lower scores and total score 
	 * will be changed in score_board matrix and also adds on canvas.
	 * */
	private void addScore(int[] dices, int player, int category, int[][] score_board) {

		while(score_board[player - 1][category - 1] != Integer.MIN_VALUE) {
			category = display.waitForPlayerToSelectCategory();
		}
		
		int score = getScore(dices, category);
		
		display.updateScorecard(category, player, score);
		score_board[player - 1][category - 1] = score;
		
		calculateTotal(score_board);
		calculateUpperScore(score_board);
		calculateLowerScore(score_board);
		
	}



	
	/* After selection of a category, this method will check if 
	 * the value of dices is appropriate to the selected category.
	 * If answer is positive, method will return generated score 
	 * by a logic of selected category. In other case, result of 
	 * this method will be 0. 
	 * */
	private int getScore(int[] dices, int category) {
		int score = 0;
		
		if(category < UPPER_SCORE) {
			score = upperCategoryScore(dices, category);
		}else if(category == THREE_OF_A_KIND) {
			if(nOfAKind(dices, 3) != 0) score = chance(dices);
		}else if(category == FOUR_OF_A_KIND) {
			if(nOfAKind(dices, 4) != 0) score = chance(dices);
		}else if(category == FULL_HOUSE){
			score = fullHouse(dices);
		}else if(category == SMALL_STRAIGHT){
			score = forStraight(dices, 4);
		}else if(category == LARGE_STRAIGHT){
			score = forStraight(dices, 5);
		}else if(category == YAHTZEE){
			if(nOfAKind(dices, 5) != 0) score = 50;
		}else if(category == CHANCE){
			score = chance(dices); 
		}
		return score;
	}
	
	
	/* This method is for upper categories. It needs two argument - 
	 * massive of dices and category number and returns sum of values,
	 * which are equals to category number selected by player.
	 * For example, category number 1 is for ones, number 2 for twos, etc. 
	 * */
	private int upperCategoryScore(int[] dices, int category) {
		int score = 0;
		for(int dice: dices) {
			if(dice == category) {
				score += dice;
			}
		}
		
		return score;
	}
	
	
	/* This method checks if any kinds of value is repeated n times in 
	 * a dices massive. It has two arguments - massive of dices and integer argument
	 * n. If any kinds of value is repeated n times in a massive,
	 * method will return the value, which will be repeated n times, in other situation,
	 * the result will be 0. 
	 * This method is for Three of a Kind, Four of a Kind and Yatzee categories.
	 * In first two of them, getScore method will return sum of all 5 values.
	 * Yatzee means that all five dices have same values and the result in this case
	 * will be fixed 50, not sum of all values.
	 * */
	private int nOfAKind(int[] dices, int n) {
		int count = 0;
		
		for(int i = 1; i <= dices.length-(n-1); i++) {
			for(int x = i; x <= dices.length; x++) {
				
				if(dices[x-1] == dices[i-1]) {
					count ++;
					if(count == n) {
						return dices[x-1];
					}
				}
			}
			
			count = 0;
		}
		
		return 0;
	}
	
	
	/* Full House means that there are only two kinds of values, one of them
	 * is repeated three times and another one - two times. This method checks if
	 * the massive of dices is full house, if it's right than the result will be
	 * 25 point, if it's not - 0. 
	 * */
	private int fullHouse(int[] dices) {
		int three_of_a_kind = nOfAKind(dices, 3);
		
		if(three_of_a_kind == 0) {
			return 0;
		}else {
			int dice_for_3 = three_of_a_kind;
			int dice_for_2 = 0;
			
			for(int dice: dices) {
				
				if (dice == dice_for_2) {
					return 25;
				}else if(dice != dice_for_3) {
					dice_for_2 = dice;
				}
				
			}
			
			return 0;
		}
	}
	
	
	/* There are two categories about straight - small and and large straight.
	 * First of them means that there are 4 straight values in a massive,
	 * second - all 5 values are straight. 
	 * This method checks if n values are in a straight in dices massive. For
	 * that it uses sorter method, which is described below. If in sorted massive
	 * there are n neighbor values, where every next value is 1 point greater
	 * then previous one, method will return (n-1) * 10, in other case,
	 * result will be 0.
	 * */
	private int forStraight(int[] dices, int n) {
		int count = 1;
		int [] sorted_dices = sorter(dices);
		
		for(int i = 1; i < sorted_dices.length; i++) {
			int prev = sorted_dices[i-1];
			for(int x = i + 1; x <= sorted_dices.length; x++) {
				
				if(sorted_dices[x-1] - prev == 1) {
					count ++;
					prev = sorted_dices[x-1];
					if(count == n) return (n-1)*10;
				}
			}
			
			count = 1;
		}
		
		return 0;
	}
	
	
	/* This method is created for forStraight method. It needs a massive typed
	 * argument and returns ascending sorted version of it. For that selection
	 * sorting algorithm is used. 
	 * */
	private int[] sorter(int[] dices) {
		
		for(int i = 1; i < dices.length; i++) {
			int index = i - 1; 
			int min = dices[i-1];
			for(int x = i + 1; x <= dices.length; x++) {
				if (dices[x-1] <= min) {
					index = x - 1;
					min = dices[x-1];
				}
				
			}
			dices[index] = dices[i-1];
			dices[i-1] = min;
		}

		return dices;

	}
	
	
	/* Chance is the last category of the game. If the player choose that 
	 * category, this method will return sum of all five values of dices massive.
	 * This method is also used for three of a kind or four of a kind cases in
	 * getScore method. 
	 * */
	private int chance(int[] dices) {
		int score = 0;
		for(int dice: dices) {
			score += dice;
		}
		return score;
	}
	
	
	/* After a player gets a score for selected category,
	 * this method will count the sum of scores for each player 
	 * which includes upper scores, lower scores and upper bonus.
	 * In calculation won't be included Integer.MIN_VALUE scores, because
	 * it's kinds of scores means that this category hasn't filled yet.
	 * finally, total values will add on canvas and write into score_board.
	 *  */
	private void calculateTotal(int[][] score_board) {
		
		for(int player = 1; player <= nPlayers; player++) {
			int total = 0;
			
			for(int category = 1; category <= N_CATEGORIES; category++) {
				
				if(category != UPPER_SCORE && category != LOWER_SCORE && category != TOTAL) {
					if(score_board[player-1][category-1] != Integer.MIN_VALUE) {
						total += score_board[player-1][category-1];
					}
				}
			}
			score_board[player - 1][TOTAL - 1] = total;
			display.updateScorecard(TOTAL, player, total);
		}
		
	}
	
	
	/* This method calculates sum of upper category scores for each player
	 * after a player gets a score for selected category.
	 * In calculation won't be included Integer.MIN_VALUE scores, because
	 * it's kinds of scores means that this category hasn't filled yet.
	 * Upper categories includes - ones, twos, threes, fours, fives and sixes.
	 * finally, upper scores will add on canvas and write into score_board.
	 * */
	private void calculateUpperScore(int[][] score_board) {
		
		for(int player = 1; player <= nPlayers; player++) {
			int total = 0;
			
			for(int category = 1; category < UPPER_SCORE; category++) {
				
				if(score_board[player-1][category-1] != Integer.MIN_VALUE) {
					total += score_board[player-1][category-1];
				}
				
			}
			score_board[player - 1][UPPER_SCORE - 1] = total;
			display.updateScorecard(UPPER_SCORE, player, total);
		}
	}
	
	
	/* This method calculates sum of lower category scores for each player
	 * after a player gets a score for selected category.
	 * In calculation won't be included Integer.MIN_VALUE scores, because
	 * it's kinds of scores means that this category hasn't filled yet.
	 * Lower categories are: three of a kind, four of a kind,
	 * full house, small straight, large straight, yahtzee and chance.
	 * finally, lower scores will add on canvas and write into score_board.
	 * */
	private void calculateLowerScore(int[][] score_board) {
		
		for(int player = 1; player <= nPlayers; player++) {
			int total = 0;
			
			for(int category = THREE_OF_A_KIND; category < LOWER_SCORE; category++) {
				
				if(score_board[player-1][category-1] != Integer.MIN_VALUE) {
					total += score_board[player-1][category-1];
				}
				
			}
			score_board[player - 1][LOWER_SCORE - 1] = total;
			display.updateScorecard(LOWER_SCORE, player, total);
		}
		
	}
	
	
	/* This method checks if every cell of score_board matrix, except of 
	 * upper bonuses, is filled, or not. If the answer is positive the method
	 * will return true, in other case, it will return false.
	 * */
	private boolean gameOver(int[][] score_board) {
		for(int player = 1; player <= nPlayers; player++) {
			for(int category = 1; category <= N_CATEGORIES; category++) {
				if(category != UPPER_BONUS) {
					if(score_board[player-1][category-1] == Integer.MIN_VALUE) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	
	/* After the gameOver() method will return true, this method will calculate
	 * if the sum of upper category values is more than 62, or not for each player,
	 * if it's more than 62, this method will add upper bonus for a player on canvas
	 * and also in score_board matrix.
	 * */
	private void calculateUpperBonus(int[][] score_board) {
		for(int player = 1; player <= nPlayers; player++) {
			if(score_board[player-1][UPPER_SCORE - 1] >= 63) {
				score_board[player-1][UPPER_BONUS - 1] = 35;
				display.updateScorecard(UPPER_BONUS, player, 35);
			}
		}
	}
	
	
	/* Finally, after the calculation of final bonuses, final scores will be visible
	 * on canvas and in score_board matrix, and this method will print the message
	 * about which player won the game. The winner will be a player, who has firstly
	 * received the maximum score. 
	 * */
	private void isWinner(int[][] score_board) {
		int total_score = 0;
		int winner = 0;
		for(int player = 1; player <= nPlayers; player++) {
			if (score_board[player-1][TOTAL-1] > total_score) {
				total_score = score_board[player-1][TOTAL-1];
				winner = player;
			}
		}
		display.printMessage("The winner is " + playerNames[winner - 1] + "!");
		
	}
	
}
