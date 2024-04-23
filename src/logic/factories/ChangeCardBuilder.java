package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Card;
import logic.gameobjects.ChangeCard;

/**
 * Constructor de la carta de cambio de sentido. 
 *
 */
public class ChangeCardBuilder extends CardBuilder{
	
	public ChangeCardBuilder() {
		super(ChangeCard.TYPE);
	}

	@Override
	protected Card createTheInstance(JSONObject data) {
		super.parseJSON(data);
		return new ChangeCard(color);
	}
}
