/**
 * @author Michael Lavoie
 */
package com.business;

import java.util.HashMap;

public class Move {
	// The VALID_MOVES hash map is a static helper variable. It maps
	// each move to a corresponding index in a binary string representing the board
	// in the Board class. It is public and can therefore be used in other classes for
	// convenience. For example, the hash map can be used to iterate over all possible valid moves
	// or it can be used to validate an inputted move.
	public static final HashMap<String, Integer> VALID_MOVES;
	static {
		VALID_MOVES = new HashMap<String, Integer>();
		VALID_MOVES.put("A1", 0);
		VALID_MOVES.put("B1", 1);
		VALID_MOVES.put("B2", 13);
		VALID_MOVES.put("C1", 2);
		VALID_MOVES.put("C2", 14);
		VALID_MOVES.put("C3", 24);
		VALID_MOVES.put("D1", 3);
		VALID_MOVES.put("D2", 15);
		VALID_MOVES.put("D3", 25);
		VALID_MOVES.put("D4", 33);
		VALID_MOVES.put("E1", 4);
		VALID_MOVES.put("E2", 16);
		VALID_MOVES.put("E3", 26);
		VALID_MOVES.put("E4", 34);
		VALID_MOVES.put("E5", 40);
		VALID_MOVES.put("F1", 5);
		VALID_MOVES.put("F2", 17);
		VALID_MOVES.put("F3", 27);
		VALID_MOVES.put("F4", 35);
		VALID_MOVES.put("F5", 41);
		VALID_MOVES.put("F6", 45);
		VALID_MOVES.put("G1", 6);
		VALID_MOVES.put("G2", 18);
		VALID_MOVES.put("G3", 28);
		VALID_MOVES.put("G4", 36);
		VALID_MOVES.put("G5", 42);
		VALID_MOVES.put("G6", 46);
		VALID_MOVES.put("G7", 48);
		VALID_MOVES.put("H1", 7);
		VALID_MOVES.put("H2", 19);
		VALID_MOVES.put("H3", 29);
		VALID_MOVES.put("H4", 37);
		VALID_MOVES.put("H5", 43);
		VALID_MOVES.put("H6", 47);
		VALID_MOVES.put("I1", 8);
		VALID_MOVES.put("I2", 20);
		VALID_MOVES.put("I3", 30);
		VALID_MOVES.put("I4", 38);
		VALID_MOVES.put("I5", 44);
		VALID_MOVES.put("J1", 9);
		VALID_MOVES.put("J2", 21);
		VALID_MOVES.put("J3", 31);
		VALID_MOVES.put("J4", 39);
		VALID_MOVES.put("K1", 10);
		VALID_MOVES.put("K2", 22);
		VALID_MOVES.put("K3", 32);
		VALID_MOVES.put("L1", 11);
		VALID_MOVES.put("L2", 23);
		VALID_MOVES.put("M1", 12);
	}
	
	// The string representation of 'this' move object (Ex: A1).
	private String move;
	
	// The score that this move gives for the game board when
	// the Minimax algorithm is being run.
	private int score;
	
	public boolean setMove(String move) {
		if (VALID_MOVES.containsKey(move)) {
			this.move = move;
			return true;
		}
		return false;
	}
	
	public String getMove() {
		return move;
	}
	
	/**
	 * Retrieves the index for the move stored in 'this' move object.
	 * 
	 * @return The index to be used to access a bit in a bitstring representing the gameboard.
	 */
	public int getIndex() {
		return VALID_MOVES.get(move);
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}