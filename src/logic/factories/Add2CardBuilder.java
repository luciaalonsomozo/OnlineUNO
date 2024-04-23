package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Add2Card;
import logic.gameobjects.Card;

/**
 * Constructor de la carta de robar 2.
 *
 */
public class Add2CardBuilder extends CardBuilder{
	
	public Add2CardBuilder() {
		super(Add2Card.TYPE);
	}

	@Override
	protected Card createTheInstance(JSONObject data) {
		super.parseJSON(data);
		return new Add2Card(color);
	}
	
}
