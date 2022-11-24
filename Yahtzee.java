/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.applet.*;
import java.util.ArrayList;

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
		int against_computer = dialog.readInt("Enter 1 if you want to play agains computer.");
		
		if(against_computer == 1) nPlayers = 2;
		else nPlayers = dialog.readInt("Enter number of players");
		
		playerNames = new String[nPlayers];
		
		for (int i = 1; i <= nPlayers; i++) {
			if(against_computer == 1 && i == 2) {
				playerNames[i - 1] = "Dr.Sala";
			}else {
				playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
			}
		}
		
		rsr_start.play();
		
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		
		playGame(score_board, against_computer);
		
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
	private void playGame(int[][] score_board, int against_computer) {
		score_board = new int[nPlayers][N_CATEGORIES];
		fillScoreBoard(score_board);
		
		while(!gameOver(score_board)) {
			for(int player = 1; player <= nPlayers; player++) {
				
//				if (against_computer == 1 && player == 2) {
					display.printMessage(playerNames[player-1] + "'s turn!");
					int[] dices = firstTry();
//					int[] dices = {3,3,3,3,5}; 
					display.displayDice(dices);
					
					for(int tries = 2; tries > 0; tries --) {
//						pause(10000);
						AutoPlayer auto = new AutoPlayer(dices, tries, score_board, player);
						dices = changeResultsAuto(dices, auto.selectDices());
					}
					
//					pause(10000);
					AutoPlayer auto = new AutoPlayer(dices, 0, score_board, player);
					int category = auto.selectCategory();
					addScore(dices, player, category, score_board);
					
					
//				}else {
//					display.printMessage(playerNames[player-1] + "'s turn! Click \"Roll Dice\" button to roll the dice.");
//					display.waitForPlayerToClickRoll(player);
//					int[] dices = firstTry();
//					
//					for(int x = 1; x < 3; x++) {
//						if(hasSelected(dices)){
//							changeResults(dices);
//						}else {
//							break;
//						}
//					}
//					
//					int category = display.waitForPlayerToSelectCategory();
//					addScore(dices, player, category, score_board);
//				}

			}
		}
		
		calculateUpperBonus(score_board);
		calculateTotal(score_board);
		isWinner(score_board);
		rsr_sound.play();

	}
	
	
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
//	private AutoPlayer auto;
	private int[][] score_board;
	private CategoryLogic category_logic;
	private AudioClip rsr_sound = MediaTools.loadAudioClip("Rsr.au");
	private AudioClip rsr_start = MediaTools.loadAudioClip("rsr_start.au");
	
	
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
				
			}
		}
		
		display.displayDice(dices);
	}
	
	
	private int[] changeResultsAuto(int[] dices, ArrayList<Integer> selected_dices) {
		for(int dice_n : selected_dices) {
			
			dices[dice_n - 1] = rgen.nextInt(ONES, SIXES);
				
		}
		
		
		display.displayDice(dices);
		
		return dices;
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
		
		category_logic = new CategoryLogic(dices, category);
		int score = category_logic.getScore();
		
		display.updateScorecard(category, player, score);
		score_board[player - 1][category - 1] = score;
		
		calculateTotal(score_board);
		calculateUpperScore(score_board);
		calculateLowerScore(score_board);
		
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
