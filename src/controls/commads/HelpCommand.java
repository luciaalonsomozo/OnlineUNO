package controls.commads;

import java.util.ArrayList;
import java.util.List;

import logic.Game;

/**
 * Comando encargado de devolver ayuda para el usuario.
 *
 */
public class HelpCommand extends Command {

	private static final String NAME = "help";
	private static final String DESCRIPTION = "Shows the help menu";
	private static final String SHORTCUT = "h";
	
	/**
	 * Lista con los mensajes de ayuda.
	 */
	private static List<String> message;
	
	public HelpCommand() {
		super(NAME, DESCRIPTION, SHORTCUT);
		message = new ArrayList<String>();
	}

	/**
	 * Método para obtener la lista de ayuda. Si el mensaje ya ha sido creado, no se vuelve a crear.
	 * 
	 * @return Lista de ayuda.
	 */
	public static List<String> getListMessages() {
		if (message.isEmpty()) {
			for (Command c: Command.AVAILABLE_COMMANDS) {
				message.add(c.getDescription());
			}
		}
		return message;
	}
	
	/**
	 * Devuelve la ayuda como una sola cadena.
	 * 
	 * @return Cadena con la ayuda.
	 */
	public static String getMessage() {
		List<String> auxList = getListMessages();
		StringBuilder aux = new StringBuilder();
		for (String c: auxList) {
			aux.append(c);
			aux.append(System.lineSeparator());
		}
		String auxString = aux.toString();
		return auxString;
	}
	
	@Override
	public boolean execute(Game game) {
		String auxString = getMessage();
		game.gameError(auxString); 
		return false;
	}

}
