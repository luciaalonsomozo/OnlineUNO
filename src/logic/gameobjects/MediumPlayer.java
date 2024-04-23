package logic.gameobjects;

import java.util.Random;

import logic.Game;

/**
 * Estrategia de la inteligencia artificial con nivel de dificultad medio.
 *
 */
public class MediumPlayer implements PlayerStrategy{

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "Medium";
	
	private boolean UNOTried;
	
	public MediumPlayer() {
		this.UNOTried = false;
	}
	
	private int searchEquivalence(CardList availableCards, String type) {
		for (int i = 0; i < availableCards.size(); i++) {
			if (availableCards.get(i).getType().equals(type)) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Metodo que se encarga de devolver una carta entre todas las posibles a lanzar por el bot.
	 * Su nivel de inteligencia es medio.
	 * @param game Juego entero
	 * @return Carta que el bot va a lanzar.
	 */
	@Override
	public Card play(Game game) {
		game.setCycleBool(true);
		if(game.getPlayerCards().size() == 2 && !UNOTried && game.getPlayerCards().possible(game.getCenterCard())) {
			if(game.getRandomNumber() > 0.2) {
				game.getCurrentPlayer().setUno(true);
				UNOTried = true;
				return null;
			}
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
		
		int nextPlayerCardsSize = game.getPlayer((game.getIndexCurrentPlayer() + 1) % game.getPlayerList().size()).getCards().size();
		if (nextPlayerCardsSize <= 2) {
			//+4, +2, X, ->, S, CC
			int index = searchEquivalence(availableCards, Add4Card.TYPE);
			if (index != -1) {
				Card chosenOne = availableCards.get(index);
				chosenOne.setColor(colorAux);
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
			
			index = searchEquivalence(availableCards, ForbiddenCard.TYPE);
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
			if (index != -1) {
				//Most frequent color
				for (int i = 0; i < availableCards.size(); i++) {
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
			
			//CC
			Card chosenOne = availableCards.get(0);
			chosenOne.setColor(colorAux);
			game.throwCard(chosenOne);
			game.setNextPlayer(true);
			return chosenOne;			
		}
		else {
			//S, ->, CC, X, +2, +4
			int index = searchEquivalence(availableCards, SimpleCard.TYPE);
			if (index != -1) {
				for (int i = 0; i < availableCards.size(); i++) {
					if (availableCards.get(i).getType().equals(SimpleCard.TYPE)
						&& availableCards.get(i).getColor().equals(colorAux)) {
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
			
			index = searchEquivalence(availableCards, ChangeCard.TYPE);
			if (index != -1) {
				Card auxCard = availableCards.get(index);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;						
			}
			
			index = searchEquivalence(availableCards, ChangeColorCard.TYPE);
			if (index != -1) {
				Card chosenOne = availableCards.get(index);
				chosenOne.setColor(colorAux);
				game.throwCard(chosenOne);
				game.setNextPlayer(true);
				return chosenOne;
			}
			
			index = searchEquivalence(availableCards, ForbiddenCard.TYPE);
			if (index != -1) {
				Card auxCard = availableCards.get(index);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;		
			}
			
			index = searchEquivalence(availableCards, Add2Card.TYPE);
			if (index != -1) {
				Card auxCard = availableCards.get(index);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;		
			}
			
			//+4
			Card chosenOne = availableCards.get(0);
			chosenOne.setColor(colorAux);
			game.throwCard(chosenOne);
			game.setNextPlayer(true);
			return chosenOne;
		}
	
	}
	
	@Override
	public String toString() {
		return TYPE;
	}

}
