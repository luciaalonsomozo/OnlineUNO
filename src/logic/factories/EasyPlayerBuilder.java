package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.EasyPlayer;
import logic.gameobjects.PlayerStrategy;

/**
 * Constructor de la estrategia "Easy".
 *
 */
public class EasyPlayerBuilder extends PlayerBuilder{

	public EasyPlayerBuilder() {
		super(EasyPlayer.TYPE);
	}

	@Override
	public PlayerStrategy createTheInstance(JSONObject info) {
		return new EasyPlayer();
	}
	
}
