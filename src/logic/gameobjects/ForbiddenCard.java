package logic.gameobjects;

import logic.Game;

/**
 * Carta especial que representa el prohibido, el salto de jugador.
 * @author lucia
 *
 */
public class ForbiddenCard extends SpecialCard {
	
	private static final long serialVersionUID = 1L;
	public static final String TYPE = "ForbiddenCard";
	static final String SYMBOL = "X";
	
	public ForbiddenCard() {
		this.symbol = SYMBOL;
	}
	
	public ForbiddenCard(ColorUNO color) {
		super(color);
		this.symbol = SYMBOL;
	}
	
	@Override 
	protected String getType() {
		return TYPE;
	}

	/**
	 * Se encarga de saltar el turno a un jugador.
	 */
	public void execute(Game game) {
		game.jumpPlayer();
	}
	
}
