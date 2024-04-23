package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Card;
import logic.gameobjects.ForbiddenCard;

/**
 * Constructor de la carta de prohibido.
 *
 */
public class ForbiddenCardBuilder extends CardBuilder{
	
	public ForbiddenCardBuilder() {
		super(ForbiddenCard.TYPE);
	}

	@Override
	protected Card createTheInstance(JSONObject data) {
		super.parseJSON(data);
		return new ForbiddenCard(color);
	}

}
