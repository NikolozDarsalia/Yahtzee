import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutoPlayer implements YahtzeeConstants {
	
	private int[] dices = new int[6];
	private int tries;
	private int[][] score_board;
	private CategoryLogic category_logic;
	
	private ArrayList<Integer> indexes;
	private ArrayList<Double> expected_values;
	
	
	/* This is the AutoPlayer constructor */
	public AutoPlayer(int[] dices, int tries, int[][] score_board) {
		this.dices = dices;
		this.tries = tries;
		this.score_board = score_board;
	}
	
	
	
	public ArrayList <Integer> selectDices() {
		int category = isLower()[0];
		int score = isLower()[1];
		if(category != -1) {
			return noProblemDices(category, score);
		}else {
			
		}
		
	}
	
	
	
	public int selectCategory() {
		int prev_score = 0;
		int answer = 0;
		
		for(int category : remainedCategories()) {
			category_logic = new CategoryLogic(dices, category);
			int score = category_logic.scoresMap();
			if(score > prev_score) {
				answer = category;
				prev_score = score;
			}
		}
		
		return answer;
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
	
	
	
	private int[] isLower(){
		int cat = -1;
		int score_old = 0;
		
		for(int category : remainedCategories()) {
			if(category >= THREE_OF_A_KIND) {
				category_logic = new CategoryLogic(dices, category);
				int score = category_logic.getScore();
				if(score > score_old) {
					cat = category;
					score_old = score;
				}
			}
		}
		
		int[] result = {cat,score_old};
		
		return result;
	}
	
	
	
	private void probability(int tries_remained) {
		int positive = 0;
		HashMap< ArrayList<Integer>, Map<Integer, Integer> > dict = new HashMap< ArrayList<Integer>, Map<Integer, Integer> >();  
		
		for(int category : remainedCategories()) {
	
			for(int fifth = 0; fifth <= 6; fifth++) {
				for(int fourth = 0; fourth <= 6; fourth++) {
					for(int third = 0; third <= 6; third++) {
						for(int second = 0; second <= 6; second++) {
							
							for(int first = 1; first <= 6; first++) {
								int[] new_dices = {first, second, third, fourth, fifth};
								ArrayList<Integer> selected_dices = selectedDices(new_dices);
								newDices(new_dices);
								
								category_logic = new CategoryLogic(new_dices, category);
								int score = category_logic.scoresMap();
								
								 Map<Integer, Integer> scores_p = dict.get(selected_dices);
								
								if(score > 0) {
									if (scores_p == null) {
										dict.put(selected_dices, new HashMap<>());
										dict.get(selected_dices).put(score, 1);
									}else {
										dict.get(selected_dices).put(score, dict.get(selected_dices).get(score) + 1);
									}
								}
								
							}
						}
					}
				}
			}
		}
		
		return getMaxExpectedValue(dict, )
	
	}
	

	
	private ArrayList<Integer> getMaxExpectedValue(HashMap< ArrayList<Integer>, Map<Integer, Integer> > dict, ArrayList<ArrayList <Integer>> selecteds){

		HashMap< ArrayList<Integer>, Double > expected_values_dict = new HashMap< ArrayList<Integer>, Double >();
		ArrayList<Integer> final_dices = new ArrayList<Integer>();
		double max_expected_value = 0;
		
		for(ArrayList <Integer> selected_dices : selecteds) {
			double expected_value = 0;
			
			for(int score : dict.get(selected_dices).keySet()) {
				expected_value += (double) score * dict.get(selected_dices).get(score) / Math.pow(6, selected_dices.size());
			}
			
			if (expected_value > max_expected_value) {
				final_dices = selected_dices;
				max_expected_value = expected_value; 
			}
	
		}
		
		return final_dices;
		
	}
		
	
	
	
	private ArrayList<Integer> selectedDices(int[] new_dices){
		ArrayList<Integer> selected_dices = new ArrayList<Integer>();
		
		for(int i = 1; i <= new_dices.length; i++) {
			if(new_dices[i-1] != 0)	selected_dices.add(i);
		}
		
		return selected_dices;
	}
	
	
	
	private int[] newDices(int[] new_dices) {
		for(int i = 1; i <= new_dices.length; i++) {
			if(new_dices[i-1] == 0)	new_dices[i-1] = dices[i-1];
		}
		
		return new_dices;
	}
	
	
	
	private ArrayList<Integer> noProblemDices(int category, int score) {
		ArrayList<Integer> no_prob = new ArrayList<Integer>();
		int[] copy_dices = deepCopyOfDices();

		for(int i = 1; i <= dices.length; i++) {
			copy_dices[i-1] = -1;
			category_logic = new CategoryLogic(dices, category);
			int new_score = category_logic.getScore();
			if(new_score == score) {
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
