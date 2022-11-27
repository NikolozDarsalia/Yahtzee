import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutoPlayer implements YahtzeeConstants {
	
	private int[] dices = new int[N_DICE];
	private int tries;
	private int player;
	private int[][] score_board;
	private CategoryLogic category_logic;
	
	
	
	/* This is the AutoPlayer constructor */
	public AutoPlayer(int[] dices, int tries, int[][] score_board, int player) {
		this.dices = dices;
		this.tries = tries;
		this.score_board = score_board;
		this.player = player;
	}

	

	/**
	 * This is a public method of AutoPlayer class, which is used
	 * after first tries of computer. It calculates which dices might be
	 * changed to get more score.
	 * 
	 * @return ArrayList of integers, each of them describes which dice will
	 * 		be changed.
	 * 
	 */
	public ArrayList <Integer> selectDices() {
		int category = isLower();

		if(category != -1) {
			return noProblemDices(category);
		}else {
			return chooseByExpectedValues(tries);
		}

	}
	
	
	
	/**
	 * This public method is used after all three tries of computer. It selects
	 * the category, which will get the maximum score based on the final dices
	 * of tries. If value of dices is not appropriate for any categories, answer of this
	 * method will be the first available category.
	 * 
	 * @return integer value - category number.
	 * 
	 */
	public int selectCategory() {
		int prev_score = 0;
		int answer = 0;
		
		for(int category : remainedCategories()) {
			category_logic = new CategoryLogic(dices, category);
			int score = category_logic.getScore();
			if(score >= prev_score) {
				if(!(score == prev_score && (category == CHANCE || category <= SIXES))) {
					answer = category;
					prev_score = score;
				}
			}
		}
		
		if( answer == 0) {
			answer = remainedCategories().get(0);
		}
		
		return answer;
	}
	
	
	
	/**
	 * It checks the empty cells of score_board for computer's categories, to
	 * show which categories are available.
	 * 
	 * @return ArrayList of integers - list of available categories.
	 * 
	 */
	private ArrayList <Integer>  remainedCategories() {
		ArrayList <Integer> remained_categories = new ArrayList<Integer>();
		
		for(int category = 1; category <= CHANCE; category++) {
			if(category != UPPER_SCORE && category != UPPER_BONUS) {
				if(score_board[player - 1][category - 1] == Integer.MIN_VALUE) {
					remained_categories.add(category);
				}
			}
		}
		return remained_categories;
	}
	
	
	
	/**
	 * This method checks if the existing value of dices are appropriate for
	 * any lower categories (without chance). 
	 * 
	 * @return integer value - category number, if the answer of this method is positive,
	 * 						   -1, if the dices is not appropriate for any lower categories.
	 */
	private int isLower(){
		int cat = -1;
		int score_old = 0;
		
		for(int category : remainedCategories()) {
			if(category >= THREE_OF_A_KIND && category < CHANCE) {
				category_logic = new CategoryLogic(dices, category);
				int score = category_logic.getScore();
				if(score >= score_old) {
					cat = category;
					score_old = score;
				}
			}
		}

		if(score_old == 0) cat = -1;
		
		return cat;
	}
	
	
	
	/**
	 * If the answer of isLower method is not -1, this method will be ran. 
	 * It finds dices, which will not be necessary for get the category returned
	 * by isLower method. 
	 * 
	 * 
	 * @param category - integer value (category number).
	 * 
	 * @return ArrayList of integer values, each of them describes dices, which can be 
	 * randomised. 
	 * 
	 */
	private ArrayList<Integer> noProblemDices(int category) {
		ArrayList<Integer> no_prob = new ArrayList<Integer>();
		int[] copy_dices = deepCopyOfDices();

		for(int i = 1; i <= dices.length; i++) {
			for(int x = i; x <= dices.length; x++) {
				copy_dices[i-1] = -1;
				copy_dices[x-1] = -1;
				
				category_logic = new CategoryLogic(copy_dices, category);
				
				int new_score = category_logic.getScore();
				
				if(new_score > 0) {
					
					if(x == i) {
						if(no_prob.size() == 1) {
							no_prob.clear();
						}
						no_prob.add(i);
						
					}else { 
						no_prob.clear();
						no_prob.add(i);
						no_prob.add(x);
						
						return no_prob;
					}	
				}
				
				copy_dices = deepCopyOfDices();
				
			}
		}
		
		return no_prob;
		
	}
	
	
	
	/**
	 * 
	 * 
	 * @return
	 * 
	 */
	private int[] deepCopyOfDices() {
		int[] copy_dices = new int[N_DICE];
		
		for(int i = 1; i <= dices.length; i++) {
			copy_dices[i-1] = dices[i-1];
		}
		
		return copy_dices;
	}
	
	
	
	/**
	 * 
	 * 
	 * @param tries_remained - integer value, which describes the number of remained tries
	 * 						 for computer.
	 * 
	 * 
	 * @return
	 * 
	 */
	private ArrayList<Integer> chooseByExpectedValues(int tries_remained) {
		
		Map< ArrayList<Integer>, Map<Integer, Integer> > probabilities_map = new HashMap< ArrayList<Integer>, Map<Integer, Integer> >();  
		
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
								int score = category_logic.getScore();
								
								Map<Integer, Integer> scores_p = probabilities_map.get(selected_dices);
								
								if(score > 0) {
									if (scores_p == null) {
										probabilities_map.put(selected_dices, new HashMap<>());
										probabilities_map.get(selected_dices).put(score, 1);
									}else if (probabilities_map.get(selected_dices).get(score) == null) {
										probabilities_map.get(selected_dices).put(score, 1);
									}else {
										probabilities_map.get(selected_dices).put(score, probabilities_map.get(selected_dices).get(score) + 1);
									}
								}
								
							}
						}
					}
				}
			}
		}
		
		Set<ArrayList <Integer>> set_selecteds = probabilities_map.keySet();
		ArrayList<ArrayList <Integer>>  arr = new ArrayList<>(set_selecteds);
		for (ArrayList<Integer> i : set_selecteds) {
			arr.add(i);
		}
            
		return getMaxExpectedValueSelection(probabilities_map, arr, tries_remained);
	
	}
	
	
					
	/**
	 * 
	 * 		
	 * @param probabilities_map
	 * 
	 * 
	 * @param selecteds
	 * 
	 * 
	 * @param tries_remained
	 * 
	 * @return
	 * 
	 */
	private ArrayList<Integer> getMaxExpectedValueSelection(Map< ArrayList<Integer>, Map<Integer, Integer> > probabilities_map, ArrayList<ArrayList <Integer>> selecteds, int tries_remained){

		ArrayList<Integer> final_dices = new ArrayList<Integer>();
		double max_expected_value = 0;
		
		for(ArrayList <Integer> selected_dices : selecteds) {
			double expected_value = 0;
			
			for(int score : probabilities_map.get(selected_dices).keySet()) {
				double probability_non = 1 - (double) probabilities_map.get(selected_dices).get(score) / Math.pow(6, selected_dices.size());
				double probability = (double) probabilities_map.get(selected_dices).get(score) / Math.pow(6, selected_dices.size());
				if(tries_remained == 2) {
					expected_value += score * (probability + probability * probability_non);
				}else {
					expected_value += score * probability;
				}
				
			}
			
			if (expected_value > max_expected_value) {
				final_dices = selected_dices;
				max_expected_value = expected_value; 
			}
	
		}
		
		return final_dices;
		
	}
		
	
	
	
	/**
	 * 
	 * 
	 * 
	 * @param new_dices
	 * 
	 * 
	 * @return
	 * 
	 */
	private ArrayList<Integer> selectedDices(int[] new_dices){
		ArrayList<Integer> selected_dices = new ArrayList<Integer>();
		
		for(int i = 1; i <= new_dices.length; i++) {
			if(new_dices[i-1] != 0)	selected_dices.add(i);
		}
		
		return selected_dices;
	}
	
	
	/**
	 * 
	 * 
	 * @param new_dices
	 * 
	 * 
	 * @return
	 * 
	 */
	private int[] newDices(int[] new_dices) {
		for(int i = 1; i <= new_dices.length; i++) {
			if(new_dices[i-1] == 0)	new_dices[i-1] = dices[i-1];
		}
		
		return new_dices;
	}
	
	
	
}
