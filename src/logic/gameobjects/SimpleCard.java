package logic.gameobjects;

import logic.Game;

/**
 * Clase que representa las cartas simples del juego. Cartas que presentan un identificador que 
 * es un número entre el 0 y el 9, y un color.
 *
 */
public class SimpleCard extends Card {

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "SimpleCard";
	
	public SimpleCard(String numberID, ColorUNO color) {
		super(color);
		this.symbol = numberID;
	}
	
	@Override 
	protected String getType() {
		return TYPE;
	}
	
	//Normal cards don't do anything!
	@Override
	public void execute(Game game) { return; }
	
}
