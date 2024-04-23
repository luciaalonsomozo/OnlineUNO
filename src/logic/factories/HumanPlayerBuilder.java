package logic.factories;

import org.json.JSONObject;

import logic.gameobjects.HumanPlayer;
import logic.gameobjects.PlayerStrategy;

/**
 * Constructor de la estrategia "Human".
 *
 */
public class HumanPlayerBuilder extends PlayerBuilder{

	public HumanPlayerBuilder() {
		super(HumanPlayer.TYPE);
	}

	@Override
	public PlayerStrategy createTheInstance(JSONObject info) {
		return new HumanPlayer();
	}
	
}