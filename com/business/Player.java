/**
 * @author Michael Lavoie
 */
package com.business;

public enum Player {
	// Player constants with associated indices that are used to refer to players in an array.
	PLAYER_ONE(0),
	PLAYER_TWO(1);
	
	private int index;
	
	private Player(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Player getNextPlayer() {
		Player players[] = Player.values();  
        int ordinal = this.ordinal();  
        ordinal = ++ordinal % players.length;  
        return players[ordinal]; 
	}
	
}
