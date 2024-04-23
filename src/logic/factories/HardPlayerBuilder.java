package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.HardPlayer;
import logic.gameobjects.PlayerStrategy;

/**
 * Constructor de la estrategia "Hard".
 *
 */
public class HardPlayerBuilder extends PlayerBuilder{

	public HardPlayerBuilder() {
		super(HardPlayer.TYPE);
	}

	@Override
	public PlayerStrategy createTheInstance(JSONObject info) {
		return new HardPlayer();
	}
	
}
