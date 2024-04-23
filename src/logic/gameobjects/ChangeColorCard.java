package logic.gameobjects;

import logic.Game;

/**
 * Carta especial que se encarga de cambiar el color actual de la partida.
 * @author lucia
 *
 */
public class ChangeColorCard extends SpecialCard {

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "ChangeColorCard";
	public static final String SYMBOL = "C";

	public ChangeColorCard() {
		super(null);
		this.symbol = SYMBOL;
	}
	
	@Override
	public boolean checkSimilarity(Card card) {
		return true;
	}
	
	/**
	 * Devuelve siempre cierto pues esta carta no tiene restricciones a lanzarse.
	 * @return Similitud entre dos cartas.
	 */
	@Override 
	protected boolean equals(String id, ColorUNO color2) {
		//Used to change the color
		if (symbol.equals(id.toUpperCase())) {
			this.color = color2;
			return true;
		}
		return false;
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
	
	//Color has already been changed
	@Override
	public void execute(Game game) {
		return;
	}
	
	@Override 
	public void clearColor() {
		color = null;
	}
	
}
	
