package logic;

import java.util.List;

/**
 * Interfaz del observador del patrón observador.
 *
 */
public interface ObserverUNO {
	
	/**
	 * Notificación llamada al antes de iniciar la partida. Sirve para establecer el nº de jugadores 
	 * que habrá.
	 * 
	 * @param numPlayers Nº de jugadores que habrá en el juego.
	 */
	public void notifyOnStart(int numPlayers);
	
	/**
	 * Notificación que se llama cuando se va a cargar una partida. Se utiliza para 
	 * presentar al jugador los nombres de las partidas guardadas.
	 * 
	 * @param savesNames Listas de nombres de las partidas guardadas que se pueden cargar.
	 */
	public void notifyOnLoad(List<String> savesNames);
	
	/**
	 * Notificación que se llama en el momento en que el juego se actualiza. Se utiliza para
	 * presentar al usuario con la información más nueva del juego.
	 * 
	 * @param gameSt Fachada del juego recien actualizado.
	 */
	public void notifyOnUpdate(GameStatus gameSt);
	
	/**
	 * Notificación llamada cuando se produce un guardado exitoso. Se utiliza para indicar al
	 * usuario que el juego se ha guardado correctamente.
	 */
	public void notifyOnSave();
	
	/**
	 * Notificación llamada cuando el juego finaliza. Se permite al observador ver los ganadores 
	 * de la partida.
	 * 
	 * @param winnersNames Lista de ganadores de la partida. 
	 */
	public void notifyOnEnd(List<String> winnersNames);
	
	/**
	 * Notificación llamada cuando ocurre un error en el modelo. Se utiliza para indicar al 
	 * usuario que ha ocurrido dicho error.
	 * 
	 * @param error Mensaje del error.
	 */
	public void notifyOnError(String error);
	
}
