package logic.factories;

import org.json.JSONObject;
import logic.gameobjects.PlayerStrategy;

/**
 * Constructor genérico de las estrategias de los jugadores.
 *
 */
public abstract class PlayerBuilder extends Builder<PlayerStrategy>{

	protected String name;
	
	PlayerBuilder(String type) {
		super(type);
	}

	@Override
	protected abstract PlayerStrategy createTheInstance(JSONObject data);
	
}
