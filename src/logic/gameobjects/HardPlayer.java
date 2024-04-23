package logic.gameobjects;


import logic.Game;

/** 
 * Estrategia de la inteligencia artificial con nivel de dificultad difícil.
 *
 */
public class HardPlayer implements PlayerStrategy{

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "Hard";
	
	private boolean UNOTried;
	
	public HardPlayer() {
		this.UNOTried = false;
	}
	
	/**
	 * Se encarga de comprobar si alguna carta de la lista de cartas que se pasa como parámetro es de un tipo en concreto.
	 * Consideramos los tipos como: Add4Card, Add2Card, ForbiddenCard, ChangeColorCard, ChangeCard, SimpleCard.
	 * @param availableCards lista de la cual se buscará si una se corresponde con el tipo.
	 * @param type tipo a buscar entre la lista.
	 * @return int devolvera el índice de la lista si se encuentra, y -1 si no se encuentra.
	 */
	private int searchEquivalence(CardList availableCards, String type) {
		for (int i = 0; i < availableCards.size(); i++) {
			if (availableCards.get(i).getType().equals(type)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Calcula el color con menos frecuencia en una lista dada.
	 * @param cards Lista de cartas a buscar.
	 * @return  ColorUNO con menos frecuencia.
	 */
	//Least common color
	private ColorUNO searchNoColor(CardList cards) {
		int[] colorFrecuency = new int[ColorUNO.values().length];
		for (int i = 0; i < cards.size(); i++) {
			ColorUNO colorAux = cards.get(i).getColor();
			if (colorAux != null) {
				++colorFrecuency[ColorUNO.valueOf(colorAux.toString()).ordinal()];
			}
		}
		int indexMin = 0, minFrecuency = colorFrecuency[0];
		
		for(int i = 1 ; i < colorFrecuency.length ; i++) {
			if(colorFrecuency[i] < minFrecuency) {
				indexMin = i;
				minFrecuency = colorFrecuency[i];
			}
		}
		
		return ColorUNO.values()[indexMin];
	}
	
	/**
	 * Comprueba si hay alguna carta del color dado en la lista.
	 * 
	 * @param them lista de cartas
	 * @param color color a buscar
	 * @return Cierto si existe al menos una carta en la lista de ese color.
	 */
	private boolean hasThisColor(CardList them, ColorUNO color) {
		for(int i = 0; i < them.size(); i++) {
			if(color.equals(them.get(i).color)) return true;
		}
		
		return false;
	}
	
	/**
	 * Comprueba si hay alguna carta del número dado en la lista.
	 * 
	 * @param them lista de cartas
	 * @param number número a buscar
	 * @return Cierto si existe al menos una carta en la lista con ese número.
	 */
	private boolean hasThisNumber(CardList them, String number) {
		for(int i = 0; i < them.size(); i++) {
			if(number.equals(them.get(i).getSymbol())) return true;
		}
		
		return false;
	}
	
	/**
	 * Comprueba si para una lista, y una carta, si un usuario lanzase la carta, habría alguna carta posible en la lista que se puediese lanzar en el siguiente turno.
	 * @param cardMine carta que se lanzaría
	 * @param them lista donde buscar una carta a lanzar
	 * @return Cierto si el jugador puede lanzar alguna carta.
	 */
	private boolean canThrow(Card cardMine, CardList them) {
		for(int i = 0; i < them.size(); i++) {
			if (them.get(i).checkSimilarity(cardMine)) { 
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Busca la carta que peor le convenga al siguiente jugador.
	 * Se trata de una dificultad difícil.
	 * @param game juego entero
	 * @return Carta que el bot decide lanzar.
	 */
	@Override
	public Card play(Game game) {
		game.setCycleBool(true);
		if(game.getPlayerCards().size() == 2 && !UNOTried && game.getPlayerCards().possible(game.getCenterCard())) {
			game.getCurrentPlayer().setUno(true);
			UNOTried = true;
			return null;
		}
		UNOTried = false;
		CardList availableCards = new CardList();
		int[] colorFrecuency = new int[ColorUNO.values().length];
		
		ColorUNO colorAux = null;
		for (int i = 0; i < game.getPlayerCards().size(); i++){
			if (game.getPlayerCards().get(i).checkSimilarity(game.getCenterCard())) {  
				availableCards.add(game.getPlayerCards().get(i));
			}
			colorAux = game.getPlayerCards().get(i).getColor();
			if (colorAux != null) {
				++colorFrecuency[ColorUNO.valueOf(colorAux.toString()).ordinal()];
			}
		}	
		if (availableCards.size() == 0) {
			if(game.isPossibleDraw()) {
				game.drawCard(game.getCurrentPlayer(), 1);
				game.setNextPlayer(false);
			}
			else {
				game.setNextPlayer(true);
			}
			return null;
		}
		int maxAux = 0;
		
		for (int i = 0; i < ColorUNO.values().length; i++) {
			if (colorFrecuency[i] > maxAux) {
				maxAux = colorFrecuency[i];
				colorAux = ColorUNO.values()[i];
			}
		}
		
		//Two players throw X
		if (game.getPlayerList().size() == 2) {
			int index = searchEquivalence(availableCards, ForbiddenCard.TYPE);
			if (index != -1) {
				Card auxCard = availableCards.get(index);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;			
			}
		}
		
		CardList nextPlayerCards = game.getPlayer((game.getIndexCurrentPlayer() + 1) % game.getPlayerList().size()).getCards();
	
		//Make them draw
		for (int i = 0; i < availableCards.size(); i++) {
			if (availableCards.get(i).getColor() != null && !canThrow(availableCards.get(i), nextPlayerCards)) {
				Card auxCard = availableCards.get(i);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;
			}
		}
		
		//+4, +2, X, ->, S, CC
		int index = searchEquivalence(availableCards, Add4Card.TYPE);
		
		if (index != -1) { 
			Card chosenOne = availableCards.get(index);
			ColorUNO chosenColor = searchNoColor(nextPlayerCards);
			
			if(availableCards.size() == 1) { // if you've got one available card you throw it with the least common color
				chosenOne.setColor(chosenColor);
				game.throwCard(chosenOne);
				game.setNextPlayer(true);
				return chosenOne;
			}
			if(!hasThisColor(nextPlayerCards, chosenColor)) { // if the next player doesn't have that color then you throw that color
				chosenOne.setColor(chosenColor);
				game.throwCard(chosenOne);
				game.setNextPlayer(true);
				return chosenOne;
			}	
			else { // if the next player has that frecuent color 
				index = searchEquivalence(availableCards, ForbiddenCard.TYPE); // we try to throw a forbbidden card
				if (index != -1) {
					Card auxCard = availableCards.get(index);
					game.throwCard(auxCard);
					game.setNextPlayer(true);
					return auxCard;			
				}
				else { //else we throw the least fecuent color
					chosenOne.setColor(chosenColor);
					game.throwCard(chosenOne);
					game.setNextPlayer(true);
					return chosenOne;
				}
			}
		}
		
		index = searchEquivalence(availableCards, ChangeColorCard.TYPE); // if we have a cc card if there is a color the next payer dousnt' have, we thorw it, else we throw a forbidden card. Else, we set color blue ranodmly
		if (index != -1) {
			Card chosenOne = availableCards.get(index);
			ColorUNO chosenColor = searchNoColor(nextPlayerCards);
			
			if(!hasThisColor(nextPlayerCards, chosenColor)) {
				chosenOne.setColor(chosenColor);
				game.throwCard(chosenOne);
				game.setNextPlayer(true);
				return chosenOne;
			}	
			else {
				index = searchEquivalence(availableCards, ForbiddenCard.TYPE);
				if (index != -1) {
					Card auxCard = availableCards.get(index);
					game.throwCard(auxCard);
					game.setNextPlayer(true);
					return auxCard;			
				}
			}

			chosenOne.setColor(ColorUNO.BLUE);
			game.throwCard(chosenOne);
			game.setNextPlayer(true);
			return chosenOne;
		}
		
		index = searchEquivalence(availableCards, Add2Card.TYPE);
		if (index != -1) {
			Card auxCard = availableCards.get(index);
			game.throwCard(auxCard);
			game.setNextPlayer(true);
			return auxCard;		
		}
		
		index = searchEquivalence(availableCards, ChangeCard.TYPE);
		if (index != -1) {
			Card auxCard = availableCards.get(index);
			game.throwCard(auxCard);
			game.setNextPlayer(true);
			return auxCard;							
		}
		
		index = searchEquivalence(availableCards, SimpleCard.TYPE);
		
		if(availableCards.size() == 1) {
			Card auxCard = availableCards.get(0);
			game.throwCard(auxCard);
			game.setNextPlayer(true);
			return auxCard;
		}
		else {
			boolean ok=false;
			for(int i = 0; i < availableCards.size(); i++) { // throws a card that the next cannot respond to 
				if(!hasThisColor(nextPlayerCards, availableCards.get(i).getColor()) && !hasThisNumber(nextPlayerCards, availableCards.get(i).getSymbol())) {
					ok = true;
					Card auxCard = availableCards.get(i);
					game.throwCard(auxCard);
					game.setNextPlayer(true);
					return auxCard;
				}
			}

			if(!ok){ // if he cannot throw a car so that the next cannot respond to, we throw our most frecuent color		
				//Most frequent color
				for (int i = 0; i < availableCards.size(); i++) { // return the most frecuant color
					if (availableCards.get(i).getColor() != null && availableCards.get(i).getColor().equals(colorAux)) {
						Card auxCard = availableCards.get(i);
						game.throwCard(auxCard);
						game.setNextPlayer(true);
						return auxCard;
					}
				}
				Card auxCard = availableCards.get(index);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;		
			}
			
		}
		
		Card auxCard = availableCards.get(0);
		game.throwCard(auxCard);
		game.setNextPlayer(true);
		return auxCard;
	}
	
	@Override
	public String toString() {
		return TYPE;
	}

}
