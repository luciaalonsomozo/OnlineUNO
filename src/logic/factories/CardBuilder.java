package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Card;
import logic.gameobjects.ColorUNO;

/**
 * Constructor de las cartas del juego.
 *
 */
public abstract class CardBuilder extends Builder<Card> {

	/**
	 * Símbolo de la carta a crear.
	 */
	protected String symbol; 
	/**
	 * Color de la carta a crear.
	 */
	protected ColorUNO color;
	
	CardBuilder(String type) {
		super(type);
	}
	
	/**
	 * Parseo del JSON para facilitar el tratamiento de la información. 
	 * 
	 * @param data JSON a parsear.
	 */
	protected void parseJSON(JSONObject data) {
		symbol = data.getString("symbol");
		String colorAux = data.getString("color");
		if (!colorAux.equals("BLACK")) {
			color = ColorUNO.getColor(colorAux);
		}
		else {
			color = null;
		}
	}

	@Override
	protected abstract Card createTheInstance(JSONObject data);

}
