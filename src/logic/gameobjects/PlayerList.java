package logic.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Game;
import logic.factories.BuilderBasedFactory;

/**
 * Contiene la lista de jugadores del juego. 
 *
 */
public class PlayerList implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Player> playerList;
	 
	public PlayerList() {
		playerList = new ArrayList<Player>();
	}
	 
	public PlayerList(List<String> names, List<String> type, BuilderBasedFactory<PlayerStrategy> playerFactory) {
		playerList = new ArrayList<Player>();
		
		for (int i = 0; i < names.size(); i++) {
			Player player;	
			JSONObject jo = new JSONObject();
			jo.put("type", type.get(i));
			player = new Player(names.get(i), playerFactory.createInstance(jo));
			playerList.add(player);
		}
	}

	public void initialize(Game game) {
		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).initialize(game);
		}
	}

	public void sort(Comparator<Player> cmp) {
		playerList.sort(cmp);
	}
	/**
	 * Revierte el orden de los jugadores para que los turnos vayan en sentido contrario.
	 * 
	 * @param currentPlayer indice de la lista del jugador actual antes del cambio de sentido.
	 * @return Entero que representa el índice del jugador actual después del cambio de sentido.
	 */
	public int changeOrder(int currentPlayer) {
		 Collections.reverse(playerList);
		 return playerList.size() - currentPlayer - 1;
	}

	/**
	 * Se encarga de cargar una lista de jugadores.
	 * 
	 * @param playerListAux JSONArray que contiene la lista de jugadores
	 * @param factory Factoría que interpreta las cartas de cada jugador
	 * @param playerFactory Factoría que interpreta el tipo de estrategia de cada jugador.
	 */
	public void load(JSONArray playerListAux, BuilderBasedFactory<Card> factory, BuilderBasedFactory<PlayerStrategy> playerFactory) {
		Player playerAux;
		for (int i = 0; i < playerListAux.length(); i++) {
			
			JSONObject jo = playerListAux.getJSONObject(i);
			
			playerAux = new Player(jo.getString("id"), playerFactory.createInstance(jo));
			playerAux.load(playerListAux.getJSONObject(i).getJSONArray("cardhand"), factory);
			playerList.add(playerAux);
			
		}
	}
	 
	public Player get(int i) {
		Player auxPlayer = playerList.get(i);
		return auxPlayer;
	}
	
	public int size() {
		return playerList.size();
	}
	
	/**
	 * Se encarga de generar el JSON con el estado de los jugadores en el momento de guardar la partida.
	 * @return JSON con el estado de los jugadores.
	 */
	public JSONArray report() {
		JSONArray players = new JSONArray();
		for (Player p: playerList) {
			players.put(p.report());
		}
		return players;
	}

	public Player getPlayer(int i) {
		int j = i % playerList.size();
		return playerList.get(j);
	}

	/**
	 * Utilizado para poder iterar en los bucles con iteradores y no números. 
	 * 
	 * @return Lista de jugadores.
	 */
	public List<Player> getPlayerList() {
		return playerList;
	}
}
