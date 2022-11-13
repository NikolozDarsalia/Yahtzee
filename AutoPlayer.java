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
		return dices;
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
		
		category_logic = new CategoryLogic(dices, category);
		int score = category_logic.getScore();
		total ++;
		if(score > 0) {
			positive ++;
		}
		return 0.1;
	}
	
	private int noProblemDices(int category) {
		int[] copy_dices = deepCopyOfDices();
		
		for(int i = 1; i <= dices.length; i++) {
			
			copy_dices[i-1] 
		}
		return 1;
		
	}
	
	private double probWhileFirstIsLower() {
		
		return 0.1;
	}
	
	private int[] deepCopyOfDices() {
		int[] copy_dices = new int[6];
		for(int i = 1; i <= dices.length; i++) {
			copy_dices[i-1] = dices[i-1];
		}
		
		return copy_dices;
	}
	
}
