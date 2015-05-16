package com.business;

import java.util.ArrayList;
import java.util.List;

import com.business.Board;

public class AI {
	private Board board;
	
	// validMoves is used to store the moves from the VALID_MOVES
	// hash map to make iterating simpler.
	private List<String> validMoves;
	
	public AI (Board board) {
		this.board = board;
		validMoves = new ArrayList<String>(Move.VALID_MOVES.keySet());
	}
	
	/**
	 * This method generates a natural tree using recursion. It uses Minimax
	 * with Alpha-Beta pruning to find the best move without needing to traverse
	 * the entire tree.
	 * 
	 * @param player The current player to evaluate (MIN or MAX).
	 * @param alpha The current alpha score.
	 * @param beta The current beta score.
	 * @param level The number of levels left to traverse.
	 * @return The best move so far.
	 */
	public Move getBestMove(Player player, int alpha, int beta, int level) {
		Move myBestMove = new Move();
		Move opponentBestMove = null;
		Move tempMove = new Move();
				
		if (level == 0 || board.isFull() || board.hasPlayerWon(player.getNextPlayer())) { // We have reached the bottom. Now evaluate the gameboard.
			tempMove = new Move();
			tempMove.setScore(calculateCurrentScore(level));
			return tempMove;
		}
		
		if (player == Player.PLAYER_ONE) {
			myBestMove.setScore(alpha);
		}
		else {
			myBestMove.setScore(beta);
		}

		for (String validMove: validMoves) { // Try all valid moves on the board.
			tempMove.setMove(validMove);
			if (board.setMoveOnBoard(tempMove, player)) {
				opponentBestMove = getBestMove(player.getNextPlayer(), alpha, beta, level - 1); // Go a level deeper.
				board.removeMoveFromBoard(tempMove); // Undo changes to the gameboard.
				if ((player == Player.PLAYER_ONE) && ((opponentBestMove.getScore() > myBestMove.getScore()))) {
					myBestMove.setMove(tempMove.getMove());
					myBestMove.setScore(opponentBestMove.getScore());
					alpha = opponentBestMove.getScore();
				}
				else if ((player == Player.PLAYER_TWO) && (opponentBestMove.getScore() < myBestMove.getScore())) {
					myBestMove.setMove(tempMove.getMove());
					myBestMove.setScore(opponentBestMove.getScore());
					beta = opponentBestMove.getScore();
				}
				if (alpha >= beta) { // Prune the tree.
					return myBestMove;
				}
			}
		}
		return myBestMove;
	}
	
	/**
	 * This is the heuristic method. It calculates a score for the given current gameboard. The level
	 * helps calculate a score that is related to how deep we are in the tree. The less deep the better a score
	 * should be.
	 * 
	 * @param level The current level of the game search tree.
	 * @return
	 */
	private int calculateCurrentScore(int level) {
		if (board.isFull()) { // No one has an advantage.
			return 0;
		}
		if (board.hasPlayerWon(Player.PLAYER_ONE)) { // MAX has the advantage.
			return 100 * (level + 1);
		}
		if (board.hasPlayerWon(Player.PLAYER_TWO)) { // MIN has the advantage.
			return -100 * (level + 1);
		}
		return ((board.getNumOfLadders(Player.PLAYER_ONE, 4) * 2 + board.getNumOfLadders(Player.PLAYER_ONE, 3)) - (board.getNumOfLadders(Player.PLAYER_TWO, 4) * 2 + board.getNumOfLadders(Player.PLAYER_TWO, 3)));
	}
}
