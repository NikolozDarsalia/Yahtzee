
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
	
}
