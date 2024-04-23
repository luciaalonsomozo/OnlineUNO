package logic.gameobjects;

import java.util.List;

/**
 * Clase creada para implementar el patrón adapter de un jugador.
 *
 */
public interface PlayerStatus {
	
	/**
	 * Devuelve el nombre de un jugador.
	 * @return Nombre del jugador
	 */
	public String getName();
	
	/**
	 * Devuelve el nombre de un jugador.
	 * @return Nombre del jugador
	 */
	public boolean getUno();
	
	/**
	 *  Devuelve el tipo de estrategia de un jugador.
	 * @return Tipo de estrategia del jugador.
	 */
	public String getStrategyString();
	
	/**
	 * Devuelve la lista de cartas de un jugador.
	 * @return Cartas del jugador.
	 */
	public List<CardStatus> getCardsStatus();

}
