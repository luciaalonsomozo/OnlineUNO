package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Card;
import logic.gameobjects.SimpleCard;

/**
 * Constructor de las cartas simples.
 *
 */
public class SimpleCardBuilder extends CardBuilder{

	public SimpleCardBuilder() {
		super(SimpleCard.TYPE);
	}

	@Override
	protected Card createTheInstance(JSONObject data) {
		super.parseJSON(data);
		return new SimpleCard(symbol, color);
	}

}
