package logic.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Comparator;
import java.util.List;

import controls.exceptions.GameException;
import logic.Game;
import logic.factories.BuilderBasedFactory;

/**
 * Clase que representa un montón de cartas, sea el de robar, el de lanzar, o la mano de cada jugador.
 * 
 */
public class CardList implements Serializable{
 
	private static final long serialVersionUID = 1L;
	/**
	 * Lista de cartas.
	 */
	private List <Card> cardList;
	
	/**
	 * Comparador de cartas para barajearlas.
	 */
	private transient static final Comparator<Card> comp = new Comparator<Card>() {
 			public int compare(Card arg0, Card arg1) {
 				if(arg0.getNumber()< arg1.getNumber()) return -1;
 				else if(arg0.getNumber() == arg1.getNumber()) return 0;
 				else return 1; };};
 
 	public CardList() {
 		cardList = new ArrayList<Card>();
 	}
 	
 	public CardList(CardList copied) {
 		cardList = new ArrayList<Card>(copied.cardList);
 	}
	
 	/**
 	 * Metodo que se encarga de barajar las cartas. Genera para cada carta una nueva posición.
 	 * 
 	 * @param game Permite que se pueda coger un número aleatorio.
 	 */
	public void shuffle(Game game) { //shuffles cards;
		List<Card> list = cardList;
		for(int i = 0; i < cardList.size(); i++) 
			cardList.get(i).updateRandom(game);		
		cardList.sort(comp);
	}
	
	/**
	 * 
	 * Detecta si entre una baraja (de un jugador) hay alguna carta posible a lanzar.
	 * 
	 * @param card Carta del centro del juego, que determinará si cualquier carta del jugador es similar a ella o no.
	 * @return Cierto si alguna carta de la lista es similar a la pasada por parámetro.
	 */
	public boolean possible(Card card) {
		//If centerCard is null all cards are valid
		if (card == null) return true;
		
		boolean result = false;
		int i = 0;
		while (!result && i < cardList.size()) {
			if (cardList.get(i).checkSimilarity(card)) {
				result = true;
			}
			i++;
		}
		return result;
	}
	
	/**
	 * Añade una carta al montón de cartas.
	 * 
	 * @param card Carta a añadir al montón.
	 */
	public void add(Card card) {
		cardList.add(card);
	}

	/**
	 * Elimina una carta del montón de cartas.
	 * 
	 * @param card Carta a borrar del montón.
	 */
	public void remove(Card card) {
		cardList.remove(card);
	}
	
	/**
	 * Elimina una carta del montón de una posición concreta.
	 * 
	 * @param i Índice en la lista de cartas de la carta a eliminar.
	 * @return Instancia de la carta eliminada de la lista.
	 */
	public Card remove(int i) {
		return cardList.remove(i);
	}

	/**
	 * Reseta el montón de cartas dejandolo vacío.
	 */
	public void clear() {
		cardList.clear();
	}
	
	/**
	 * Añade al montón las SimpleCards.
	 */
	public void addSimpleCards() {
		String symbol;
		for(int i = 0; i < 10; i++) {
			symbol = String.valueOf(i);
			for(ColorUNO c: ColorUNO.values()) {
				SimpleCard c1 = new SimpleCard(symbol, c);
				SimpleCard c2 = new SimpleCard(symbol, c);
				cardList.add(c1);
				cardList.add(c2);
			}					
		}
	}
	/**
	 * Añade al montón las SpecialCards.
	 */
	public void addSpecialCards() {
		for(ColorUNO c: ColorUNO.values()) {
			ForbiddenCard c1 = new ForbiddenCard(c);
			ForbiddenCard c2 = new ForbiddenCard(c);
			ChangeCard c3 = new ChangeCard(c);
			ChangeCard c4 = new ChangeCard(c);
			Add2Card c5 = new Add2Card(c);
			Add2Card c6 = new Add2Card(c);
			cardList.add(c1);
			cardList.add(c2);
			cardList.add(c3);
			cardList.add(c4);
			cardList.add(c5);
			cardList.add(c6);
		}
		
		for(int i = 0; i < 4; i++) {
			Add4Card c1 = new Add4Card();
			ChangeColorCard c2 = new ChangeColorCard();
			cardList.add(c1);
			cardList.add(c2);
		}
	}
	
	/**
	 * Se encarga de cargar una lista de cartas guardada anteriormente. 
	 * Genera la lista a través de un JSONArray.
	 * 
	 * @param cardListAux JSONArray que contiene la información de una lista de cartas.
	 * @param factory Factoría de cartas que parsea el tipo de cada carta para generarla.
	 */
	public void load(JSONArray cardListAux, BuilderBasedFactory<Card> factory) {
		JSONObject cardAux = new JSONObject();
		for (int i = 0; i < cardListAux.length(); i++) {
			cardAux = cardListAux.getJSONObject(i);
			cardList.add(factory.createInstance(cardAux));
		}	
	}

	/**
	 * Busca para una lista dada esa carta en concreto, dados un id y un color.
	 * 
	 * @param id Id de la carta a buscar en el montón
	 * @param color Color de la carta a buscar en el montón
	 * @return Carta del montón que coincida en color e identificador con los parámetros.
	 * @throws GameException La carta no se ha podido encontrar
	 */
	//Return (if possible) the card with the same id, color
	public Card getCard(String id, ColorUNO color) throws GameException {
		boolean ok = false;
		int i = 0;
		Card card = null;
		while(!ok && i < size()) {
			if (cardList.get(i).equals(id, color)) {
				card = cardList.get(i);
				ok = true;
			}
			i++;
		}
		if (card == null) {
			throw new GameException("Card not found!");
		}
		return card;		
	}
	
	/**
	 * Devuelve la carta de una posición en concreto de la lista.
	 * @param i Índice de la carta en la lista.
	 * @return Instancia de la carta
	 */
	public Card get(int i) {
		return cardList.get(i);
	}

	/**
	 * Guarda para una lista de cartas su estado.
	 * @return JSON con el estado de carta de la lista individualmente.
	 */
	public JSONArray report() {
		JSONArray cards = new JSONArray();
		for (Card i : cardList) {
			cards.put(i.report());
		}
		return cards;
	}
		
	public int size() {
		return cardList.size();
	}

	public List<Card> getCardList() {
		return cardList;
	}
	
}
