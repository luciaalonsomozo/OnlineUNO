package controls.commads;

import logic.Game;

/**
 * Comando encargado de finalizar la partida.
 *
 */
public class EndCommand extends Command {
	
	private static final String NAME = "finish";
	private static final String DESCRIPTION = "Finish the game in the current state";
	private static final String SHORTCUT = "f";

	public EndCommand() {
		super(NAME, DESCRIPTION, SHORTCUT);
	}
	
	@Override
	public boolean execute(Game game) {
		game.endGame();
		return false;
	}
	
}
