/**
 * @author Michael Lavoie
 */
package com.business;

public class Computer {
	// The number of levels in the search tree to generate. The number simply represents
	// how many moves ahead will be looked at and evaluated.
	private static final int NUM_OF_LEVELS = 4;
	
	// Alpha and beta are used for alpha-beta pruning that is implemented
	// for the minimax algorithm to decrease the number of nodes that are visited.
	private static final int ALPHA_DEFAULT = -10000;
	private static final int BETA_DEFAULT = 10000;
	
	private Player player;
	private Board board;
	private AI aILogic;
	
	public Computer(Player player, Board board) {
		this.player = player;
		this.board = board;
		aILogic = new AI(board);
	}
	
	/**
	 * Generates the best possible move for the NUM_OF_LEVELS.
	 */
	public void makeMove() {
			Move move = aILogic.getBestMove(player, ALPHA_DEFAULT, BETA_DEFAULT, NUM_OF_LEVELS);
			board.setMoveOnBoard(move, player);
	}
}
