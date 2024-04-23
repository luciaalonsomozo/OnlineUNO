package controls.commads;

import controls.exceptions.GameException;
import logic.Game;

/**
 * Comando encargado de simular el botón UNO para el caso de que el jugador vaya a lanzar una 
 * carta para quedarse a una de la victoria.
 *
 */
public class UNOCommand extends Command {
	
	private static final String NAME = "uno";
	private static final String DESCRIPTION = "Changes the status of UNO";
	private static final String SHORTCUT = "u";

	public UNOCommand() {
		super(NAME, DESCRIPTION, SHORTCUT);
	}

	@Override
	public boolean execute(Game game) throws GameException {
		if (game.getCurrentPlayer().getCards().size() > 2 ) {
			throw new GameException("You have more than 2 cards!");
		}
		if (!game.getPlayerCards().possible(game.getCenterCard())) {
			throw new GameException("You have no possible cards to throw!");
		}
		if (game.getCurrentPlayer().getUno()) {
			throw new GameException("You already pressed uno!");
		}
		game.getCurrentPlayer().setUno(true);
		return false;
	}

}
