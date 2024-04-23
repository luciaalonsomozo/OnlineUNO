package logic.gameobjects;

import java.io.Serializable;

import logic.Game;

/**
 * Interfaz para representar la estrategia de un jugador.
 * @author lucia
 *
 */
public interface PlayerStrategy extends Serializable{
	
	public Card play(Game game);

}
