package controls.commads;

import controls.exceptions.GameException;
import logic.Game;

/**
 * Clase abstracta del comando. Sus subclases se encargan de establecer todos las posibles
 * acciones que el usuario puede realizar.
 *
 */
public abstract class Command {

	/**
	 * Nombre del comando.
	 */
	protected final String name;
	/**
	 * Descripción del comando para facilitar la construcción de la ayuda.
	 */
	protected final String description;
	/**
	 * Abreviatura del comando. Para acelerar el parseo.
	 */
	protected final String shortcut;
	
	/**
	 * Lista de comandos disponibles para la realización del parseo
	 */
	protected static final Command[] AVAILABLE_COMMANDS = {
			new ThrowCardCommand(),
			new EndCommand(),
			new DrawCardCommand(),
			new SaveCommand(),
			new UNOCommand(),
			new HelpCommand()
	};
	
	/**
	 * Lista de comandos disponibles en el juego en red debido a que en este modo no todos 
	 * los comando están disponibles.
	 */
	protected static final Command[] NET_AVAILABLE_COMMANDS = {
			new ThrowCardCommand(),
			new UNOCommand(),
			new DrawCardCommand(),
			new EndCommand()
	};
	
	/**
	 * Constructor de un comando inicializando sus atributos principales.
	 * 
	 * @param name Nombre del comando
	 * @param description Descripción del comando
	 * @param shortcut Abreviatura del comando
	 */
	public Command(String name, String description, String shortcut) {
		this.name = name; 
		this.description = description;
		this.shortcut = shortcut;
	}
	
	/**
	 * Comprobación de si una cadena se corresponde con el comando
	 * 
	 * @param name Cadena a comprobar.
	 * @return La cadena es igual a la abreviatura del comando o es igual a su nombre.
	 */
	protected boolean matchCommandName(String name) {
		return this.shortcut.equalsIgnoreCase(name) || this.name.equalsIgnoreCase(name);
	}
	
	/**
	 * Método encargado de parsea un comando teniendo en cuenta sus posibles parámetros.
	 * 
	 * @param input Entrada a parsear
	 * @return Se devuelve el comando si coincide el parseo y, en caso contrario, se devuelve null.
	 * @throws GameException En caso de que la entrada no tenga el nº correcto de parámetros.
	 */
	protected Command parse(String[] input) throws GameException{
		if(matchCommandName(input[0])) {
			if(input.length == 1) {
				return this;
			}
			else {
				throw new GameException("The number of arguments is incorrect!");
			}
		}
		return null;		
	}
	
	/**
	 * Método para facilitar la visualización de ayuda.
	 * 
	 * @return Cadena con la información del comando.
	 */
	protected String getDescription() {
		return name.toUpperCase() + " [" + shortcut + "]: " + description; 
	}
	
	/**
	 * Recorre los comandos disponibles buscando uno cuyo parseo coincida con 
	 * la entrada
	 * 
	 * @param input Entrada a parsear
	 * @return Comando cuyo parseo coincida
	 * @throws GameException No hay ningún comando que coincida con el parseo
	 */
	public static Command getCommand(String[] input) throws GameException{
		//Possible future change
		Command result = null;
		for (int i = 0; i < AVAILABLE_COMMANDS.length && result == null; i++) {
			result = AVAILABLE_COMMANDS[i].parse(input);
		}
		if (result == null) {
			throw new GameException("Insert a valid command! (h for more information)");
		}
		return result;
	}
	
	/**
	 * Se recorren los comandos disponibles en el modo red buscando uno 
	 * que coincida con la entrada.
	 * 
	 * @param command Cadena a comparar con los comandos disponibles.
	 * @return True si es igual a algún comando disponible y false en caso contrario.
	 */
	public static boolean validNetCommand(String command) {
		for (Command c: NET_AVAILABLE_COMMANDS) {
			if(c.matchCommandName(command)) return true;
		}
		return false;
	}
	
	/**
	 * Ejecuta la acción del comando
	 * 
	 * @param game Juego sobre el que se debe ejecutar el comando.
	 * @return Devuelve si se debe pasar al siguiente jugador o no.
	 * @throws GameException Excepción que ocurre si hay algún error, en game, al ejecutar el comando.
	 */
	public abstract boolean execute(Game game) throws GameException;
	
}
