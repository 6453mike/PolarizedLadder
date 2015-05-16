/**
 * @author Michael Lavoie
 */
package com.business;

import com.business.Move;
import com.business.Player;

public class Board {
	// The first element in playerMoves is a long-bitstring representing player one's moves.
	// The second element in playerMoves is a long-bitstring representing player two's moves.
	private long[] playersMoves;
	
	// winningPattern1 and neutralizePattern1 hold the bit patterns that are considered a win and a neutralized situation respectively.
	// Note: This will be explained in a report accompanied with the source files.
	private long[] winningPattern1;
	private long[] neutralizePattern1;
	
	// winningPattern2 and neutralizePattern2 hold the bit patterns that are considered a win and a neutralized situation respectively.
	// Note: This will be explained in a report accompanied with the source files.
	private long[] winningPattern2;
	private long[] neutralizePattern2;
	
	
	public Board() {
		playersMoves = new long[2];
		winningPattern1 = new long[5];
		winningPattern2 = new long[5];
		neutralizePattern1 = new long[3];
		neutralizePattern2 = new long[3];	
	}
	
	/**
	 * Reinitializes all of the bit literal patterns.
	 */
	private void initializePatterns() {
		winningPattern1[0] = 0B1_00000000011_0000000000011L; // Left shift 8 times to check for win.
		winningPattern1[1] = 0B1_000000011_00000000011_0000000000000L; // Left shift 6 times to check for win.
		winningPattern1[2] = 0B1_0000011_000000011_00000000000_0000000000000L; // Left shift 4 times to check for win.
		winningPattern1[3] = 0B1_00011_0000011_000000000_00000000000_0000000000000L; // Left shift 2 times to check for win.
		winningPattern1[4] = 0B1_011_00011_0000000_000000000_00000000000_0000000000000L; // Use as is to check for win.
		
		winningPattern2[0] = 0B1_00000000110_0000000011000L; // The same number of shifts as the winningPattern1 array.
		winningPattern2[1] = 0B1_000000110_00000011000_0000000000000L;
		winningPattern2[2] = 0B1_0000110_000011000_00000000000_0000000000000L;
		winningPattern2[3] = 0B1_00110_0011000_000000000_00000000000_0000000000000L;
		winningPattern2[4] = 0B1_110_11000_0000000_000000000_00000000000_0000000000000L;
		
		// Only use these patterns for shift numbers between 2 and max inclusive and for winningPattern[i], where i > 0 and i < 4.
		neutralizePattern1[0] = 0B1_000000000_00000010000_0000000000000L;
		neutralizePattern1[1] = 0B1_0000000_000010000_00000000000_0000000000000L;
		neutralizePattern1[2] = 0B1_00000_0010000_000000000_00000000000_0000000000000L;
		
		// Only use these patterns for shift numbers between 0 and max - 2 inclusive and for winningPattern[i], where i > 0 and i < 4..
		neutralizePattern2[0] = 0B100_000000000_00000000100_0000000000000L;
		neutralizePattern2[1] = 0B100_0000000_000000100_00000000000_0000000000000L;
		neutralizePattern2[2] = 0B100_00000_0000100_000000000_00000000000_0000000000000L;
	}
	
	/**
	 * Check to see if the given player has won the game.
	 * 
	 * @param player The player to check for a winning status.
	 * @return Whether the game has been won by the specified player.
	 */
	public boolean hasPlayerWon(Player player) {
		int shiftCount = 0;
		initializePatterns();
		
		for (int i = 0; i < winningPattern1.length; i++) {
			do {
				if (numberOfSetBits(winningPattern1[i] & playersMoves[player.getIndex()]) == 5) { // There might be a winner.
					if (i == 0 || i == 4 || ((i > 0) && (i < 4) && (shiftCount < 2))) { // Can't be neutralized.
						return true;
					}
					// Check if the winning pattern is neutralized by the other player.
					if (numberOfSetBits(neutralizePattern1[i - 1] & playersMoves[player.getNextPlayer().getIndex()]) != 2) {
						return true;
					}
				}
				if (numberOfSetBits(winningPattern2[i] & playersMoves[player.getIndex()]) == 5) { // There might be a winner.
					if (i == 0 || i == 4 || ((i > 0) && (i < 4) && (shiftCount > ((8 - (2 * i)) - 2)))) { // Can't be neutralized.
						return true;
					}
					// Check if the winning pattern is neutralized by the other player.
					if (numberOfSetBits(neutralizePattern2[i - 1] & playersMoves[player.getNextPlayer().getIndex()]) != 2) {
						return true;
					}
				}
				
				// Shift the patterns over by 1 bit to check for a win at the next adjacent position.
				winningPattern1[i] = winningPattern1[i] << 1;
				winningPattern2[i] = winningPattern2[i] << 1;
				if ((i != 0) && (i < 4)) {
					if (shiftCount >= 2) {
						neutralizePattern1[i - 1] = neutralizePattern1[i - 1] << 1;
					}
					if (shiftCount <= ((8 - (2 * i)) - 2)) {
						neutralizePattern2[i - 1] = neutralizePattern2[i - 1] << 1;
					}
				}
				shiftCount++;
			}
			while (shiftCount <= (8 - (2 * i)));
			shiftCount = 0;
		}
		return false;
	}
	
	/**
	 * Sets the specified move on the gameboard for the specified player and returns a success
	 * or fail boolean value.
	 * 
	 * @param move The move to be set on the gameboard.
	 * @param player The player to set the move for.
	 * @return A success value.
	 */
	public boolean setMoveOnBoard(Move move, Player player) {
		if (!isMoveTaken(move)) {
			playersMoves[player.getIndex()] = setBit(playersMoves[player.getIndex()], move.getIndex());
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the given move from the board if it exists.
	 * 
	 * @param move The move to be removed.
	 */
	public void removeMoveFromBoard(Move move) {
		for (int i = 0; i < playersMoves.length; i++) {
			if (getBit(playersMoves[i], move.getIndex())) {
				playersMoves[i] = unsetBit(playersMoves[i], move.getIndex());
			}
		}
	}
	
	/**
	 * Returns true or false if the board is full or not full respectively.
	 * 
	 * @return Whether the board is full.
	 */
	public boolean isFull() {
		if ((playersMoves[Player.PLAYER_ONE.getIndex()] | playersMoves[Player.PLAYER_TWO.getIndex()]) == 0b1111111111111111111111111111111111111111111111111L) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if the given move has already been taken by either Player one or Player two.
	 * 
	 * @param move The move to be checked.
	 * @return Whether the move was taken or not.
	 */
	public boolean isMoveTaken(Move move) {
		if (getBit(playersMoves[Player.PLAYER_ONE.getIndex()], move.getIndex()) || getBit(playersMoves[Player.PLAYER_TWO.getIndex()], move.getIndex())) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Check and return whether the given bit literal has a bit set at the specified index.
	 * 
	 * @param bitLiteral The bit literal to be checked.
	 * @param index The index to be checked.
	 * @return Whether the specified bit index was set.
	 */
	private boolean getBit(long bitLiteral, int index) {
		return (((bitLiteral >> index) & 0b1L) == 1)? true : false;
	}
	
	/**
	 * Sets the bit at the specified index of the bitLiteral.
	 * 
	 * @param bitLiteral The bit literal to be modified.
	 * @param index The index of the bit to be set.
	 * @return The modified bit literal.
	 */
	private long setBit(long bitLiteral, int index) {
		return bitLiteral | (0b1L << index);
	}
	
	/**
	 * clears the bit at the specified index of the bitLiteral.
	 * 
	 * @param bitLiteral The bit literal to be modified.
	 * @param index The index of the bit to be unset.
	 * @return The modified bit literal.
	 */
	private long unsetBit(long bitLiteral, int index) {
		return bitLiteral & ~(0b1L << index);
	}
	
	/**
	 * Counts the number of set bits in a bit literal.
	 * @param bitLiteral
	 * @return
	 */
	private int numberOfSetBits(long bitLiteral) {
		return Long.bitCount(bitLiteral);
	}

	public void printGameBoardToConsole() {
		char gameBoard[] = new char[91]; // The visual representation of the game board (91 is a 7 by 13 grid).
		String letterLabels = "ABCDEFGHIJKLM"; // These letters will be printed on the x-axis.
		
		for (int i = 0; i < 91; i++) { // Initialize all elements to the space character for simplicity of presenting the board.
			gameBoard[i] = ' ';
		}	
		int m = 13, k = 55, bitIndex = 0;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < ((2 * i) + 1); j++) {
				bitIndex = ((((12 * i) + 6) + j) + (k - m));
				if (getBit(playersMoves[Player.PLAYER_ONE.getIndex()], bitIndex)) {
					gameBoard[(((12 * i) + 6) + j)] = 'o';
				}
				else if (getBit(playersMoves[Player.PLAYER_TWO.getIndex()], bitIndex)) {
					gameBoard[(((12 * i) + 6) + j)] = 'x';
				}
				else {
					gameBoard[(((12 * i) + 6) + j)] = '.';
				}
			}
			k = k - m;
			m += 2;
		}		
		for (int row = 0; row < 7; row++) { // Print out the pretty version of the game board.
			System.out.print("\n" + (7 - row) + "  ");
			for (int col = 0; col < 13; col++) {
				System.out.print(gameBoard[(13 * row) + col] + "  ");
			}
		}
		System.out.print("\n   ");
		for (int i = 0; i < 13; i++) {
			System.out.print(letterLabels.charAt(i) + "  ");
		}
		System.out.println("\n");
	}

	/**
	 * This method will count the number of partial ladders with numberOfTokens set for a specific player on the
	 * gameboard. Only ladders that have a possibility of a win are considered. If the other player has a token set
	 * on the possible ladder, the partial ladder will not be counted. For example, if numberOfTokens is set to 4
	 * when this method is called, the method will count all occurrences of ladders with only 1 token left to complete
	 * the ladder and that 1 token must be empty to be counted. Neutralization is not counted because it does not contribute
	 * to a win.
	 * 
	 * @param player The player to be considered for the counting of ladders.
	 * @param numberOfTokens The number of tokens that should be in a ladder.
	 * @return The number of ladders on the entire gameboard.
	 */
	public int getNumOfLadders(Player player, int numberOfTokens) {
		int ladderCounter = 0;
		int shiftCount = 0;
		initializePatterns();
		
		for (int i = 0; i < winningPattern1.length; i++) {
			do {
				if ((numberOfSetBits(winningPattern1[i] & playersMoves[player.getIndex()]) == numberOfTokens) &&
					(numberOfSetBits(winningPattern1[i] & playersMoves[player.getNextPlayer().getIndex()]) == 0)) {
					if (i == 0 || i == 4 || ((i > 0) && (i < 4) && (shiftCount < 2))) { // Can't be neutralized.
						ladderCounter++;
					}
					else if (numberOfSetBits(neutralizePattern1[i - 1] & playersMoves[player.getNextPlayer().getIndex()]) != 2) { // Check if the winning pattern is neutralized by the other player.
						ladderCounter++;
					}
				}
				if ((numberOfSetBits(winningPattern2[i] & playersMoves[player.getIndex()]) == numberOfTokens) &&
					(numberOfSetBits(winningPattern2[i] & playersMoves[player.getNextPlayer().getIndex()]) == 0)) {
					if (i == 0 || i == 4 || ((i > 0) && (i < 4) && (shiftCount > ((8 - (2 * i)) - 2)))) { // Can't be neutralized.
						ladderCounter++;
					}
					else if (numberOfSetBits(neutralizePattern2[i - 1] & playersMoves[player.getNextPlayer().getIndex()]) != 2) { // Check if the winning pattern is neutralized by the other player.
						ladderCounter++;
					}
				}
				winningPattern1[i] = winningPattern1[i] << 1;
				winningPattern2[i] = winningPattern2[i] << 1;
				if ((i != 0) && (i < 4)) {
					if (shiftCount >= 2) {
						neutralizePattern1[i - 1] = neutralizePattern1[i - 1] << 1;
					}
					if (shiftCount <= ((8 - (2 * i)) - 2)) {
						neutralizePattern2[i - 1] = neutralizePattern2[i - 1] << 1;
					}
				}
				shiftCount++;
			}
			while (shiftCount <= (8 - (2 * i)));
			shiftCount = 0;
		}
		return ladderCounter;
	}
}
