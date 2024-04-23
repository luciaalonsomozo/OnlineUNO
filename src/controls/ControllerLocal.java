package controls;

import controls.exceptions.GameException;
import logic.Game;
import logic.ObserverUNO;
import logic.gameobjects.Player;
import logic.gameobjects.PlayerStatus;
import logic.gameobjects.PlayerStrategy;

import java.io.IOException;
import java.util.List;

import controls.commads.*;

/**
 * Implementación del controlador para el caso de jugar de manera local.
 *
 */
public class ControllerLocal implements Controller {

	/**
	 * Atributo que mantiene la instancia del modelo para modificarlo
	 */
	private Game game; 
	
	/**
	 * Constructor que toma una referencia del modelo para modificarlo
	 * 
	 * @param game
	 */
	public ControllerLocal (Game game) {
		this.game = game;
	}
	
	@Override
	public void userAction(String[] input) {
		try {
			//Possible future change
			Command c = Command.getCommand(input);
			boolean update = c.execute(game);
			game.setNextPlayer(update);
			game.setCycleBool(true);
			game.cycle();
		}
		catch (GameException ex){
			game.gameError(ex.getMessage());
			game.setCycleBool(true);
			game.cycle();
		}
	}
	
	/**
	 * Método encargado de cargar la lista de partidas guardadas
	 * 
	 * @return Si no es posible cargar las partidas, false. Si todo funciona 
	 * 		   correctamente, true.
	 */
	public boolean wantToLoad() {
		try {
			game.loadAvailables();
			return true;
		}
		catch(IOException e) {
			game.gameError(e.getMessage());
			return false;
		}
	}

	/**
	 * Establece en el modelo el nº de jugadores del modelo
	 * 
	 * @param numPlayers Nº de jugadores a establecer
	 */
	public void setNumPlayers(int numPlayers) {
		game.setNumPlayers(numPlayers);
	}
	
	/**
	 * Inicializa los parámetros iniciales del juego y lo actualiza para iniciarlo
	 * 
	 * @param names Nombres de los jugadores
	 * @param type Estrategias que utilizan los jugadores
	 */
	public void initialize(List<String> names, List<String> type) {
		game.initialize(names, type);
		game.setCycleBool(true);
		game.cycle();
	}
	
	/**
	 * Método para cargar una cierta partida
	 * 
	 * @param index Índice de la partida que se va a cargar
	 */
	public void load(int index) {
		try {
			game.load(index);
			game.setCycleBool(true);
			game.cycle();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void addObserver(ObserverUNO obs) {
		game.addObserver(obs);
	}

	@Override
	public String getHelp() {
		String aux = HelpCommand.getMessage();
		return aux;		
	}
	
	/**
	 * Método utilizado para indicar al modelo que ha ocurrido un error. 
	 * Su aplicación más habitual es permitir notificar a los observadores que ha 
	 * ocurrido el error.
	 * 
	 * @param message Mensaje del error.
	 */
	public void gameError(String message) {
		game.gameError(message);
	}

	@Override
	public String getPlayer() {
		return game.getCurrentPlayer().toString();
	}

	/**
	 * Check if the current player is human.
	 * 
	 * @return Current player is human.
	 */
	public boolean currentPlayerHuman() {
		return game.getCurrentPlayer().getStrategy().toString().equals("Human");
	}

	/**
	 * Método encargado de terminar el juego.
	 */
	@Override
	public void endGame() {
		game.endGame();
		game.setCycleBool(true);
		game.cycle();
	}

	/**
	 * Método que cambia la inteligenic artificial de un jugador dado.
	 */
	public void changeIA(PlayerStatus player, PlayerStrategy st) {	
		game.changeIA((Player) player, st);	
	}
}
