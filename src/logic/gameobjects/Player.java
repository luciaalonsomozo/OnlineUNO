package logic.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Game;
import logic.factories.BuilderBasedFactory;

/**
 * Clase que representa un jugador. Cada jugador presenta su lista de cartas,
 * su estrategia y su identificador.
 *
 */
public class Player implements Serializable, PlayerStatus{
	
	private static final long serialVersionUID = 1L;
	public static final int MAX_PLAYERS = 4;
	private String id;
	private CardList cardList;
	private PlayerStrategy st;
	private boolean uno = false;

	private static final int NUM_INITIAL_CARDS = 7;

	
	private int compareTo(Player a) {
		if(this.cardList.size() < a.cardList.size()) {
			return -1;
		}
		else if(this.cardList.size() > a.cardList.size()) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public Player(String id, PlayerStrategy st) {
		this.id = id;
		this.st = st;
		this.cardList = new CardList();
	}
	
	/**
	 * Atribuye a cada jugador sus cartas iniciales
	 * @param game juego entero
	 */
	public void initialize(Game game) {
		for(int i = 0; i < NUM_INITIAL_CARDS; i++) {
			cardList.add(game.getStack().get(0));
			game.getStack().remove(0);
		}
	}
	/**
	 * Roba un numero dado de cartas para cada jugador.
	 * 
	 * @param game juego entero 
	 * @param num numero de cartas a robar
	 */
	public void drawCards(Game game, int num) {
		for(int i = 0; i < num; i++) {
			Card auxCard = game.getStack().get(0);
			cardList.add(auxCard);
			game.getStack().remove(0);
		}
	}

	/**
	 * Hace que la estrategia de cada jugador seleccione la carta a lanzar.
	 * 
	 * @param game juego entero
	 * @return carta que el jugador decide lanzar.
	 */
	public Card play(Game game) {
		return st.play(game);
	}

	/**
	 * El jugador lanza una carta y se le elimina de su lista de cartas.
	 * @param card carta a borrar
	 */
	public void throwCard(Card card) {
		cardList.remove(card);
	}
	/**
	 * Carga la lista de cartas de un jugador.
	 * 
	 * @param cardListAux JSONArray de cartas a cargar
	 * @param factory Factoria que crea las instancias de las cartas
	 */
	public void load(JSONArray cardListAux, BuilderBasedFactory<Card> factory) {
		cardList.load(cardListAux, factory);
	}
	
	/**
	 * Detecta si un jugador ha ganado, es decir, se ha quedado sin cartas.
	 * @return Si el jugador ha ganado.
	 */
	public boolean win() {
		return cardList.size() == 0;
	}	
	/**
	 * Guarda la información de cada jugador en formato de JSONObject, guardando el tipo de strategia del jugador, su id y su lista de cartas.
	 * @return JSON que guarda el estado del jugador.
	 */
	public JSONObject report() {
		JSONObject result = new JSONObject();
		result.put("type", st.toString());
		result.put("id", id);
		result.put("cardhand", cardList.report());
		return result;
	}
	
	public CardList getCards() {
		return cardList;
	}
	
	//Check the winner
	public static Comparator<Player> cmpCards = new Comparator<Player> () {
		public int compare(Player a, Player b) {
			return a.compareTo(b);
		}
	};
	
	@Override
	public String toString() {
		return id.toUpperCase();
	}

	public boolean isHuman() {
		return st.toString().equals(HumanPlayer.TYPE);
	}

	public void setStrategy(PlayerStrategy strategy) {
		this.st = strategy;
	}
	
	public void setUno(boolean ok) {
		this.uno = ok;
	}
	
	@Override
	public boolean getUno() {
		return this.uno;
	}
	public int getInitialCards() {
		return NUM_INITIAL_CARDS;
	}

	public PlayerStrategy getStrategy() {
		return st;
	}

	public PlayerStatus status() {
		return this;
	}

	@Override
	public String getName() {
		return id.toUpperCase();
	}

	@Override
	public String getStrategyString() {
		return st.toString();
	}

	@Override
	public List<CardStatus> getCardsStatus() {
		List<CardStatus> res = new ArrayList<CardStatus>();
		for (int i = 0; i < cardList.size(); i++) {
			res.add(cardList.get(i).status());
		}
		return res;
	}

}

