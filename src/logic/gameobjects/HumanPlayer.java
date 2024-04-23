package logic.gameobjects;

import logic.Game;

/**
 * Representa la estrategia de un jugador humano.
 */
public class HumanPlayer implements PlayerStrategy{

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "Human";
	
	@Override
	public Card play(Game game) {	
		game.setCycleBool(false);
		return null;
	}
	
	@Override
	public String toString() {
		return TYPE;
	}

}
