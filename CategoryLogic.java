import acm.util.ErrorException;

public class CategoryLogic implements YahtzeeConstants{
	
	private int[] dices = new int[N_DICE];
	private int category;
	
	
	/* This is the CategoryLogic constructor */
	public CategoryLogic(int[] dices, int category) {
		this.dices = dices;
		this.category = category;
	}
	

	
	/**
	 * After selection of a category in Yatzee class, 
	 * this method will check if the value of dices 
	 * is appropriate to the selected category.
	 * 
	 * (Dices massive and category number are instance
	 *  variables of this class, which will get their 
	 *  meanings in Yahtzee class by constructor).
	 * 
	 * 
	 * @return integer value of score by category.
	 * 
	 */
	public int getScore() {
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
	
	
	
	/**
	 * This method is for upper categories. It sums values of the dicees,
	 * which are equals to category number selected by player.
	 * For example, category number 1 is for ones, number 2 for twos, etc. 
	 * 
	 * 
	 * @param dices
	 *  		massive of 5 integer values - each of them from 1 to 6.
	 *  
	 * @param category
	 * 			integer value of category.
	 * 
	 * @return integer value - score of specific upper category.
	 * 
	 */
	private int upperCategoryScore(int[] dices, int category) {
		int score = 0;
		for(int dice: dices) {
			if(dice == category) {
				score += dice;
			}
		}
		
		return score;
	}
	
	
	
	
	/**
	 * This method checks if any kinds of value is repeated n times in 
	 * a dices massive. 
	 * 
	 * It's for Three of a Kind, Four of a Kind and Yatzee categories.
	 * In first two of them, getScore method will return sum of all 5 values.
	 * Yatzee means that all five dices have same values and the result in this case
	 * will be fixed 50, not sum of all values.
	 * 
	 * 
	 * @param dices
	 *	 		massive of 5 integer values - each of them from 1 to 6.
	 * 
	 * @param n
	 * 			integer value - describes how many times repeated value is searched.
	 * 
	 * @return integer value - value which is repeated n times in a dices
	 * 		                 massive. If none of values is repeated n times,
	 * 						 then returns 0.
	 * 
	 */
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
	
	
	
	/**
	 * Full House means that there are only two kinds of values, one of them
	 * is repeated three times and another one - two times. 
	 * This method checks if the massive of dices is full house.
	 * 
	 * 
	 * @param dices
	 * 			massive of 5 integer values - each of them from 1 to 6.
	 * 
	 * @return integer value - 25, if the massive of dices is full house,
	 * 		  				  else 0.
	 */
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
	
	
	
	/**
	 * There are two categories about straight - small and and large straight.
	 * First of them means that there are 4 straight values in a massive,
	 * second - all 5 values are straight. 
	 * This method checks if n values are in a straight in dices massive. For
	 * that it uses sorter method, which is described below.
	 * 
	 * 
	 * @param dices
	 * 			massive of 5 integer values - each of them from 1 to 6.
	 * 
	 * @param n
	 * 		    integer value - describes how many straight values is searched.
	 * 
	 * @return integer value -  (n-1) * 10, If there are n neighbor values in sorted massive, 
	 * 						where every next value is 1 point greater then previous one, 
	 * 						else 0.
	 * 
	 */
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
	
	
	
	/**
	 * This method is created for forStraight method and it sorts dices massive
	 * by selection sorting algorithm.
	 * 
	 * 
	 * @param dices
	 * 			massive of 5 integer values - each of them from 1 to 6.
	 * 
	 * @return massive of 5 integer values - ascending sorted version of dices massive.
	 * 
	 */
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
	
	
	
	/**
	 * Chance is the last category of the game. It sums all five values of dices massive.
	 * This method is also used for three of a kind and four of a kind cases in
	 * getScore method. 
	 * 
	 * @param dices
	 * 			massive of 5 integer values - each of them from 1 to 6.
	 * 
	 * @return integer value - sum of all five values of dices massive.
	 * 
	 */
	private int chance(int[] dices) {
		int score = 0;
		for(int dice: dices) {
			score += dice;
		}
		return score;
	}
	
}
