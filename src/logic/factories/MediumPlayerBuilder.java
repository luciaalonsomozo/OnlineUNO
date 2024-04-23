package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.MediumPlayer;
import logic.gameobjects.PlayerStrategy;

/**
 * Constructor de la estrategia "Medium".
 *
 */
public class MediumPlayerBuilder extends PlayerBuilder{

	public MediumPlayerBuilder() {
		super(MediumPlayer.TYPE);
	}

	@Override
	public PlayerStrategy createTheInstance(JSONObject info) {
		return new MediumPlayer();
	}
	
}
