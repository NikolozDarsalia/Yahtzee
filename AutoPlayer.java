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
	
	
	public int[] selectDices() {
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
	
}
