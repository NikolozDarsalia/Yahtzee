import java.util.ArrayList;

public class AutoPlayer implements YahtzeeConstants {
	
	private int[] dices = new int[6];
	private int tries;
	private int[][] score_board;
	private CategoryLogic category_logic;
			
	public AutoPlayer(int[] dices, int tries, int[][] score_board) {
		this.dices = dices;
		this.tries = tries;
		this.score_board = score_board;
	}
	
	
	public ArrayList <Integer> selectDices() {
		int category = isLower();
		if(category != -1) {
			return noProblemDices(category);
		}else {
			
		}
		
	}
	
	
	public int selectCategory() {
		return 1;
	}
	
	
	private ArrayList <Integer>  remainedCategories() {
		ArrayList <Integer> remained_categories = new ArrayList<Integer>();
		
		for(int category = 1; category <= N_CATEGORIES; category++) {
			if(category != UPPER_SCORE || category != LOWER_SCORE || category != TOTAL || category != UPPER_BONUS) {
				if(score_board[1][category-1] == Integer.MIN_VALUE) {
					remained_categories.add(category);
				}
			}
		}
		return remained_categories;
	}
	
	
	private int isLower(){
		
		for(int category : remainedCategories()) {
			if(category >= THREE_OF_A_KIND) {
				category_logic = new CategoryLogic(dices, category);
				int score = category_logic.getScore();
				if(score > 0) {
					return category;
				}
			}
		}
		return -1;
	}
	
	
	private double probability(int category, int tries_remained) {
		int positive = 0;
		int total = 0;
		int[] copy_dices = deepCopyOfDices();
		
		for(int dice = 1; dice <= copy_dices.length; dice++) {
			for(int value = 1; value <= SIXES; value++) {
				copy_dices[dice-1] = value;
				category_logic = new CategoryLogic(copy_dices, category);
				int score = category_logic.getScore();
				total ++;
				if(score > 0) {
					positive ++;
				}
				
			}
			if(positive > 0) {
				copy_dices[dice-1] = dices[dice-1];
				break;
			}
			else {
				
			}
		}
	


		double probability = tries_remained*(positive/total);
		return probability;
	}
	
	
	private ArrayList<Integer> noProblemDices(int category) {
		ArrayList<Integer> no_prob = new ArrayList<Integer>();
		int[] copy_dices = deepCopyOfDices();
		
		for(int i = 1; i <= dices.length; i++) {
			copy_dices[i-1] = -1;
			category_logic = new CategoryLogic(dices, category);
			int score = category_logic.getScore();
			if(score > 0) {
				no_prob.add(i);
			}
			copy_dices[i-1] = dices[i-1];
		}
		
		return no_prob;
		
	}
	
	
	private int[] deepCopyOfDices() {
		int[] copy_dices = new int[6];
		for(int i = 1; i <= dices.length; i++) {
			copy_dices[i-1] = dices[i-1];
		}
		
		return copy_dices;
	}
	
}
