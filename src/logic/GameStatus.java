package logic;

import java.util.List;

import logic.gameobjects.CardStatus;
import logic.gameobjects.PlayerStatus;

/**
 * Adapter del juego para que lo reciban los observadores.
 *
 */
public interface GameStatus {

	/**
	 * Transforma el juego en una cadena legible por consola.
	 * 
	 * @return Cadena a imprimir en consola.
	 */
	public String gameToString();
	/**
	 * 
	 * @return Cadena de la carta central.
	 */
	public String getThrownCard();
	/**
	 * 
	 * @return Cadena de la carta central más legible para interfaz gráfica.
	 */
	public String getThrownCardGUI();
	
	/**
	 * 
	 * @return Nº de jugadores.
	 */
	public int getNumberOfPlayers();
	
	/**
	 * 
	 * @param name Nombre del jugador cuyo indice en la lista de jugadores queremos ver.
	 * @return Índice del jugador buscado.
	 */
	public int getPlayerIndex(String name);
	
	/**
	 * Comprobación de que el tamaño del montón de robar no es cero.
	 * 
	 * @return Se puede robar o no.
	 */
	public boolean ableToDraw();
	/**
	 * 
	 * @return Adapter de la carta central.
	 */
	public CardStatus getCenterCardStatus();
	
	/**
	 *  
	 * @return Adapter del jugador actual.
	 */
	public PlayerStatus getCurrentPlayerStatus();
	
	/**
	 *  
	 * @param index Índice del jugador buscado en la lista de jugadores del juego.
	 * @return Adapter del jugador buscado.
	 */
	public PlayerStatus getPlayerStatus(int index);
	
	/**
	 * 
	 * @return Lista de adapter de los jugadores.
	 */
	public List<PlayerStatus> getPlayersStatus();

}
