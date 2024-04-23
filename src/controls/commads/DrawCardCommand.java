package controls.commads;

import controls.exceptions.GameException;
import logic.Game;
import logic.gameobjects.Player;

/**
 * Comando encargado de robar una carta.
 *
 */
public class DrawCardCommand extends Command {
	
	private static final String NAME = "draw";
	private static final String DESCRIPTION = "Draws a card from the stack";
	private static final String SHORTCUT = "d";

	public DrawCardCommand() {
		super(NAME, DESCRIPTION, SHORTCUT);
	}

	@Override
	public boolean execute(Game game) throws GameException {
		if (game.isPossibleDraw()) {
			Player auxPlayer = game.getCurrentPlayer();
			game.drawCard(auxPlayer, 1);
			return false;
		}
		throw new GameException("You can't draw more cards!");
	}

}
