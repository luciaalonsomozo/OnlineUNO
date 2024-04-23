package logic.gameobjects;

import java.io.Serializable;

import org.json.JSONObject;

import controls.exceptions.GameException;
import logic.Game;

/**
 * Clase abstracta que encapsula la funcionalidad de las cartas.
 * Implementa la interfaz de CardStatus para crear el patrón adapter.
 * Implementa la interfaz Serializable para la transmisión a través los sockets en conexión en red.
 *
 */
public abstract class Card implements Serializable, CardStatus{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Cota superior del máximo número de las SimpleCards.
	 */
	private static final int MAX_CARD = 9;
	
	/**
	 * Cota inferior del mínimo número de las SimpleCards.
	 */
	private static final int MIN_CARD = 0;
	
	/**
	 * Se trata de una lista de los tipos de SpecialCards en el juego.
	 */
	private static final SpecialCard[] AVAILABLE_SPECIAL_CARDS = {
			new Add2Card(), 
			new Add4Card(),
			new ChangeCard(),
			new ChangeColorCard(),
			new ForbiddenCard()
	};
	
	/**
	 * Símbolo de la carta, depende de su tipo.
	 */
	protected String symbol;
	
	/**
	 * Color de cada carta.
	 */
	protected ColorUNO color;
	
	/**
	 * Número usado para barajear las cartas.
	 */
	protected double number;
	
	public Card () {};
	
	public Card(ColorUNO color) {
		this.color = color;
		//updateRandom();
	}
	

	/**
	 * Para cada carta, devuelve su id interpretando la entrada del usuario.
	 * @param input String correspondiente a la carta introducida por teclado por el usuario
	 * @return id de la carta
	 * @throws GameException
	 */
	public static String getId(String input) throws GameException {
		String output = null;
		try {
			Integer intOutput = Integer.parseInt(input);
			if (intOutput > MAX_CARD && intOutput < MIN_CARD) {
				throw new GameException("The introduced card number is incorrect! (0-9)");
			}
			output = intOutput.toString();
		}
		catch (NumberFormatException e) {
			for (int i = 0; i < AVAILABLE_SPECIAL_CARDS.length && output == null; ++i) {
				output = AVAILABLE_SPECIAL_CARDS[i].parse(input);
			}
			if (output == null) { 
				throw new GameException("Insert a valid card!");
			}
		}
		return output;
	}
	/**
	 * Comprueba la similitud entre la carta del centro del juego y la carta que el usuario desee lanzar.
	 * Solo será correcta si coincide en color y / o número con la central.
	 * 
	 * @param card se trata de la carta del centro del juego
	 * @return Si la carta tiene el mismo símbolo o color.
	 */
	public boolean checkSimilarity(Card card) {
		return this.symbol.equals(card.symbol) || this.color.equals(card.color);
	}
	
	/**
	 * 
	 * Devuelve para cada carta su estado y características, compuesto por su tipo, su símbolo y su color.
	 * 
	 * @return JSON con el estado del juego.
	 */
	public JSONObject report() {
		JSONObject result = new JSONObject(); 
		result.put("type", getType());
		
		JSONObject interior = new JSONObject();
		interior.put("symbol", symbol);
		if (color == null) {
			interior.put("color", "BLACK");				
		}
		else {
			interior.put("color", color.toString());	
		}
		result.put("data", interior);		
		
		return result; 
	}
	
	public void updateRandom(Game g) {
		number = g.getRandomNumber();
	}

	protected boolean equals(String id, ColorUNO color2) {
		return symbol.equals(id.toUpperCase()) && color.equals(color2);
	}
	
	public double getNumber() {
		return number;
	}
	
	@Override
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String getColorString() {
		if (color == null) {
			return null;
		}
		return color.toString();
	}
	
	public ColorUNO getColor() {
		return color;
	}
	
	public void setColor(ColorUNO color) {
		this.color = color;
	}
	
	public String toString() {
		//Sets and then resets color
		return color.code() + symbol + "\u001B[0m";
	}
	
	public String guiString() {
		return symbol + ' ' + color;
	}
	
	protected abstract String getType();
	
	public abstract void execute(Game game);

	public CardStatus status() {
		return this;
	}

	public void clearColor() {
		return;		
	}
	
}
