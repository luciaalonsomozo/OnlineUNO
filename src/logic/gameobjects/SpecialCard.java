package logic.gameobjects;

/**
 * Clase abstracta que encapsula la funcionalidad de las cartas especiales.
 *
 */
public abstract class SpecialCard extends Card {
	
	private static final long serialVersionUID = 1L;

	public SpecialCard() {
	}
	
	public SpecialCard(ColorUNO color) {
		super(color);
	}
	
	/** 
	 * Se encarga de parsear el tipo de una carta y ver si coincide con alguna SpecialCard.
	 * @param input carta introducida por el usuario
	 * @return String
	 */
	public String parse(String input) {
		if (input.toUpperCase().equals(this.symbol)) {
			return input;
		}
		return null;
	}

}
