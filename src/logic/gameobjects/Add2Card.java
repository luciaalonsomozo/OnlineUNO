package logic.gameobjects;

import logic.Game;

/**
 * 
 * Carta especial que representa a los "chupate dos".
 *
 */
public class Add2Card extends SpecialCard {

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "Add2Card";
	static final String SYMBOL = "+2+";
	
	protected Add2Card() {
		this.symbol = SYMBOL;
	}
	
	public Add2Card(ColorUNO color) {
		super(color);
		this.symbol = SYMBOL;
	}
	
	/**
	 *  Obliga al siguiente jugador a robar dos cartas.
	 */
	@Override
	public void execute(Game game) {
		Player auxPlayer = game.nextPlayer();
		game.drawCard(auxPlayer, 2);
	}
	
	@Override 
	protected String getType() {
		return TYPE;
	}
	
}
