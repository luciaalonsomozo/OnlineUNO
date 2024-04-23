package controls.exceptions;

/**
 * Excepción creada para el manejo de errores referentes al juego.
 *
 */
@SuppressWarnings("serial")
public class GameException extends Exception {
	
	public GameException() {
		super();
	}

	public GameException (String message) {
		super (message);
	}
	
	public GameException (String message, Throwable cause) {
		super (message, cause);
	}
	
}
