package controls.commads;

import controls.exceptions.GameException;
import logic.gameobjects.Card;
import logic.gameobjects.ColorUNO;
import logic.Game;

/**
 * Comando encargado de permitir a un jugador lanzar una carta
 *
 */
public class ThrowCardCommand extends Command {
	
	private static final String NAME = "throw";
	private static final String DESCRIPTION = "Throws the selected card. Arguments: id color";
	private static final String SHORTCUT = "t";
	
	/**
	 * Identificador de la carta a lanzar.
	 */
	private String id;
	
	/**
	 * Color de la carta a lanzar.
	 */
	private ColorUNO colorId;
	
	/**
	 * Carta a lanzar.
	 */
	private Card card;

	public ThrowCardCommand() {
		super(NAME, DESCRIPTION, SHORTCUT);
		card = null;
	}
	
	@Override 
	public Command parse(String[] input) throws GameException{
		if(matchCommandName(input[0])) {
			//Check if at least one card can be thrown
			if (input.length != 3) {
				throw new GameException("Insert a valid number of arguments!");
			}
			else {
				try {
					this.colorId = ColorUNO.getColor(input[2]);
					this.id = Card.getId(input[1]);
				}
				catch (IllegalArgumentException ex){
					throw new GameException("Insert valid arguments!", ex);
				}
				return this;
			}
		}
		return null;
	}
	
	@Override
	public boolean execute(Game game) throws GameException{
		if (!game.getPlayerCards().possible(game.getCenterCard())) {
			throw new GameException("You have no possible valid cards!");
		}
		try {
			card = game.getCurrentPlayer().getCards().getCard(id, colorId);
		}
		catch (GameException e) {
			throw new GameException(e.getMessage(), e);
		}
		if (game.getCenterCard() != null && !card.checkSimilarity(game.getCenterCard())) {
			throw new GameException("The card is not valid!");
		}
		game.throwCard(card);
		return true;
	}

}
