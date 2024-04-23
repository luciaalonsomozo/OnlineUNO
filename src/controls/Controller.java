package controls;

import logic.ObserverUNO;
import logic.gameobjects.Player;
import logic.gameobjects.PlayerStatus;
import logic.gameobjects.PlayerStrategy;

/**
 * Interfaz del controlador del patrón MVC. Lleva a cabo la comunicación entre la vista y el modelo
 * del juego. 
 *
 */

public interface Controller {

	/**
	 * Método encargado de transmitir al modelo la acción llevada a cabo por el usuario.
	 * En el caso del juego en red, este método se utilizará para enviar la información al 
	 * servidor que, a su vez, la aplicarán a su Game.
	 * 
	 * @param input Array con la acción que el usuario ha realizado. Su parseo se realiza en los comandos
	 */
	public void userAction(String[] input);
	
	/**
	 * Método encargado de recoger la ayuda considerada necesaria por el controlador para facilitar 
	 * al usuario poder interacturar correctamente con él.
	 * 
	 * @return Cadena de texto con las instrucciones adecuadas.
	 */
	public String getHelp();
	
	/**
	 * Método para añadir un observador a la lista o estructura similar de almacenamiento de los observadores. 
	 * Esto es utilizado a la hora de implementar el patrón observador, imprescindible en el MVC.
	 * 
	 * @param obs Observador que se añadirá al conjunto de observadores.
	 */
	public void addObserver(ObserverUNO obs);
	
	/**	
	 * Método encargado de devolver el nombre del "player" del usuario que controla el controlador. 
	 * En el caso local será el usuario local y en el caso en red será el "player" del cliente.
	 * 
	 * @return Nombre, en mayúsculas, del jugador.
	 */
	public String getPlayer();
	
	/**
	 * Método encargado de terminar la partida de manera abrupta. 
	 */
	public void endGame();

	/**
	 * Método encargado de cambiar la inteligencia artificial para un jugador dado.
	 * En el caso en red no se podrá cambiar en medio de una partida.
	 * @param playerStatus Jugador al que se le desea cambiar la estrategia.
	 * @param st Nueva estrategia del jugador
	 */
	public void changeIA(PlayerStatus playerStatus, PlayerStrategy st);

	
}
