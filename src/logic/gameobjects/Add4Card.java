package logic.gameobjects;

import logic.Game;

/**
 * Carta especial que representa a los "chupate cuatro".
 *
 */
public class Add4Card extends SpecialCard{

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "Add4Card";
	public static final String SYMBOL = "+4+";
	
	public Add4Card() {
		super(null);
		this.symbol = SYMBOL;
	}
	
	/**
	 * Comprueba la similitud entre la carta del centro y un +4, que es siempre correcto pues
	 * el +4 se puede lanzar en cualquier momento de la partida.
	 */
	@Override
	public boolean checkSimilarity(Card card) {
		return true;
	}

	@Override 
	protected boolean equals(String id, ColorUNO color2) {
		//Used to change the color
		if (symbol.equals(id.toUpperCase())) {
			this.color = color2;
			return true;
		}
		return false;
	}

	/**
	 * Obliga al siguiente jugador a robar cuatro cartas.
	 */
	@Override
	public void execute(Game game) {
		Player auxPlayer = game.nextPlayer();
		game.drawCard(auxPlayer, 4);
	}
	
	@Override
	public String toString() {
		if (color == null) {
			return "\u001B[30m" + symbol + "\u001B[0m";
		}
		else {
			return color.code() + symbol + "\u001B[0m";
		}
	}
	
	@Override 
	protected String getType() {
		return TYPE;
	}
	
	@Override 
	public void clearColor() {
		color = null;
	}
	
}

