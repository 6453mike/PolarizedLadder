package com.business;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Human {
	private Player player;
	private Board board;
	
	public Human(Player player, Board board) {
		this.player = player;
		this.board = board;
	}
	
	/**
	 * Attempts to make a move that the user has inputted and returns a success
	 * or failure boolean value.
	 * 
	 * @return Whether or not the move has been made successfully.
	 */
	public boolean makeMove() {
		Move humanMove = new Move();
		Scanner inputScanner = new Scanner(System.in);
		String playerInput = null;
		System.out.print("Enter move for " + player + "(Ex: G3): ");
		
		try {
			playerInput = inputScanner.next();
		}
		catch (NoSuchElementException nsee) { // This happens when the user presses Ctrl + z to end the game.
			System.out.println("\n\nGame has been ended");
			System.exit(0);
		}
		
		if (humanMove.setMove(playerInput)) {
			if (!board.setMoveOnBoard(humanMove, player)) {
				System.out.println("Move already taken!");
				makeMove();
			}
		}
		else {
			System.out.println("Invalid Move!");
			makeMove();
		}
		return true;
	}
}
