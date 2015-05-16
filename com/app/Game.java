/**
 * @author Michael Lavoie
 */
package com.app;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import com.business.Board;
import com.business.Computer;
import com.business.Human;
import com.business.Player;

public class Game {
	private static Board board;
	private static Object[] players;
	
	/**
	 * Initiate gameplay!
	 */
	public static void play() {
		board = new Board();
		players = new Object[2];
		Player currentPlayer = Player.PLAYER_ONE;
		boolean gameOver = false;
		
		// We begin the game.
		printWelcomeMessage();
		initializePlayers();
		board.printGameBoardToConsole();

		while (!gameOver) {			
			if (players[currentPlayer.getIndex()] instanceof Computer) {
				((Computer) players[currentPlayer.getIndex()]).makeMove();
			}
			else {
				((Human) players[currentPlayer.getIndex()]).makeMove();
			}
			
			board.printGameBoardToConsole();

			if (board.hasPlayerWon(currentPlayer)) {
				System.out.println("*****************************" + currentPlayer + " has won the game!!*****************************");
				gameOver = true;
			}
			else if (board.isFull()) {
				System.out.println("*****************************The game is a draw!!*****************************");
				gameOver = true;
			}	
			currentPlayer = currentPlayer.getNextPlayer();	
		}
	}
	
	public static void printWelcomeMessage() {
		System.out.println("***************************************************");
		System.out.println("************Welcome To Polarized Ladder************");
		System.out.println("***************************************************\n");
		System.out.println("Note: To end the game at any time, press Ctrl + Z (And then press enter if you are running the game from the command-line.)\n\n");
	}
	
	/**
	 * Set up the play type with the associated players (Ex: Human is Player 1 vs Computer is Player 2).
	 */
	public static void initializePlayers() {
		Scanner inputScanner = new Scanner(System.in);
		int playerInput = 1;
		Player chosenPlayer = null;
		System.out.print("Please enter the game play type (For Human vs Human type 1. For Human vs Computer type 2. For Computer vs Computer type 3.): ");
		
		try {
			playerInput = inputScanner.nextInt();
		}
		catch (InputMismatchException ime) {
			System.out.println("Invalid Option");
			initializePlayers();
			return;
		}
		catch (NoSuchElementException nsee) { // This happens when the user presses Ctrl + z to end the game.
			System.out.println("\n\nGame has been ended");
			System.exit(0);
		}

		// Create players based on the game type.
		switch (playerInput) {
		case 1: // Human vs Human.
			players[Player.PLAYER_ONE.getIndex()] = new Human(Player.PLAYER_ONE, board);
			players[Player.PLAYER_TWO.getIndex()] = new Human(Player.PLAYER_TWO, board);
			break;
		case 2: // Human vs Computer.
			chosenPlayer = choosePlayer();
			players[chosenPlayer.getNextPlayer().getIndex()] = new Computer(chosenPlayer.getNextPlayer(), board);
			players[chosenPlayer.getIndex()] = new Human(chosenPlayer, board);
			break;
		case 3: // Computer vs Computer.
			players[Player.PLAYER_ONE.getIndex()] = new Computer(Player.PLAYER_ONE, board);
			players[Player.PLAYER_TWO.getIndex()] = new Computer(Player.PLAYER_TWO, board);
			break;
		default: // Invalid Option.
			System.out.println("Invalid Option");
			initializePlayers();
		}	
	}
		
	/**
	 * If the game is to be played Human vs Computer then this method returns
	 * the chosen Player that the human will be.
	 * 
	 * @return The Player to be played by the Human.
	 */
	public static Player choosePlayer() {
		Scanner inputScanner = new Scanner(System.in);
		int playerInput = 0;
		System.out.print("Would you like to play first? (Enter 1 for yes; 0 for no): ");
		
		try {
			playerInput = inputScanner.nextInt();
		}
		catch (InputMismatchException ime) {
			System.out.println("Invalid Option");
			return choosePlayer();
			
		}
		catch (NoSuchElementException nsee) { // This happens when the user presses Ctrl + z to end the game.
			System.out.println("\n\nGame has been ended");
			System.exit(0);
		}
		
		switch (playerInput) {
		case 0:
			return Player.PLAYER_TWO;
		case 1:
			return Player.PLAYER_ONE;
		default:
			System.out.println("Invalid Option");
			return choosePlayer();	
		}
	}
	
	/**
	 * Begin App!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		play();
	}
}
