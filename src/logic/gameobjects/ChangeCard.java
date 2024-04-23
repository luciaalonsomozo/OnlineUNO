package logic.gameobjects;

import logic.Game;

/**
 * Carta especial que cambia el sentido del turno de jugadores.
 */
public class ChangeCard extends SpecialCard {
	
	private static final long serialVersionUID = 1L;
	public static final String TYPE = "ChangeCard";
	private static final String SYMBOL = "->";
	
	public ChangeCard () {
		this.symbol = SYMBOL;
	}
	
	public ChangeCard(ColorUNO color) {
		super(color);
		this.symbol = SYMBOL;
	}
	
	@Override 
	protected String getType() {
		return TYPE;
	}
	
	/**
	 * Se encarga de cambiar el orden de los jugadores.
	 * 
	 */
	@Override
	public void execute(Game game) {
		game.changeOrderPlayers();
	}
	
}
