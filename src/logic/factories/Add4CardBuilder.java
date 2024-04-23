package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Add4Card;
import logic.gameobjects.Card;

/**
 * Constructor de la carta de robar 4.
 *
 */
public class Add4CardBuilder extends CardBuilder{
	
	public Add4CardBuilder() {
		super(Add4Card.TYPE);
	}

	@Override
	protected Card createTheInstance(JSONObject data) {
		super.parseJSON(data);
		return new Add4Card();
	}

}
