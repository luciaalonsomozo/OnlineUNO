package controls.commads;

import java.io.IOException;

import controls.exceptions.GameException;
import logic.Game;

/**
 * Comando encargado de guardar la partida
 *
 */
public class SaveCommand extends Command {
	
	private static final String NAME = "save";
	private static final String DESCRIPTION = "Saves the current game";
	private static final String SHORTCUT = "s";
	
	/**
	 * Identificador de la partida guardada.
	 */
	private String _ID;

	public SaveCommand() {
		super(NAME, DESCRIPTION, SHORTCUT);
	}

	@Override
	protected Command parse(String[] input) throws GameException{
		if(matchCommandName(input[0])) {
			if(input.length == 2) {
				_ID = input[1];
				return this;
			}
			else {
				throw new GameException("The number of arguments is incorrect!");
			}
		}
		return null;		
	}
	
	@Override
	public boolean execute(Game game) throws GameException{
		try {
			game.save(_ID);
		}
		catch (IOException e) {
			throw new GameException("File not found", e);
		}
		return false;
	}

}
