package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.Card;
import logic.gameobjects.ChangeColorCard;

/**
 * Constructor de la carta de cambio de color.
 *
 */
public class ChangeColorCardBuilder extends CardBuilder{
	
	public ChangeColorCardBuilder() {
		super(ChangeColorCard.TYPE);
	}

	@Override
	protected Card createTheInstance(JSONObject data) {
		super.parseJSON(data);
		return new ChangeColorCard();
	}

}
