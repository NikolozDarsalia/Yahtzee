import acm.util.ErrorException;

public class CategoryLogic implements YahtzeeConstants{
	
	private int[] dices = new int[6];
	private int category;
	
	/* Constructor */
	public CategoryLogic(int[] dices, int category) {
		this.dices = dices;
		this.category = category;
	}
	
	
	/* After selection of a category, this method will check if 
	 * the value of dices is appropriate to the selected category.
	 * If answer is positive, method will return generated score 
	 * by a logic of selected category. In other case, result of 
	 * this method will be 0. 
	 * */
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
	
	
	/* Score map is for calculate probabilities in AutoPlayer class.
	 * It's different from getScore method, because potential upper bonus
	 * effect is included in the calculation of upper category's results.
	 *  */
	public int scoresMap() {
		switch(category) {
		case 1: return getScore() + getScore()/63*35;
		case 2: return getScore() + getScore()/63*35;
		case 3: return getScore() + getScore()/63*35;
		case 4: return getScore() + getScore()/63*35;
		case 5: return getScore() + getScore()/63*35;
		case 6: return getScore() + getScore()/63*35;
		case 9: return getScore();
		case 10: return getScore();
		case 11: return getScore();
		case 12: return getScore();
		case 13: return getScore();
		case 14: return getScore();
		case 15: return getScore();
		default: throw new ErrorException("Tries: Illegal index");
		}
		
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
}
