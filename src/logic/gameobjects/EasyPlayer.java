package logic.gameobjects;

import java.util.Random;

import logic.Game;

/**
 * Estrategia de la inteligencia artifical con nivel de dificultad fácil.
 *
 */
public class EasyPlayer implements PlayerStrategy{

	private static final long serialVersionUID = 1L;
	public static final String TYPE = "Easy";
	
	private boolean UNOTried;
	
	public EasyPlayer() {
		this.UNOTried = false;
	}
	
	/**
	 * Metodo para escoger un color aleatorio en caso de aparición de un Add4Card o ChangeColorCard
	 * @param random se trata de un número entre 0-1.
	 * @return ColorUNO
	 */
	private ColorUNO selectColor(double random) {
		if (random < 0.25) {
			return ColorUNO.BLUE;
		}
		else if (random < 0.5) {
			return ColorUNO.RED;
		}
		else if (random < 0.75) {
			return ColorUNO.GREEN;
		}
		else {
			return ColorUNO.YELLOW;
		}
	}
	
	/**
	 *  Metodo que se encarga de devolver una carta entre todas las posibles a lanzar por el bot.
	 *  Su nivel de inteligencia es fácil, por lo que siempre devolverá la primera carta entre las posibles a lanzar.
	 *  @param game juego entero
	 *  @return carta que lanzará el bot.
	 */
	@Override
	public Card play(Game game) {	
		game.setCycleBool(true);
		if(game.getPlayerCards().size() == 2 && !UNOTried && game.getPlayerCards().possible(game.getCenterCard())) {
			if(game.getRandomNumber() > 0.7) {
				game.getCurrentPlayer().setUno(true);
				UNOTried = true;
				return null;
			}
		}
		UNOTried = false;
		for (int i = 0; i < game.getPlayerCards().size(); i++){
			if (game.getPlayerCards().get(i).checkSimilarity(game.getCenterCard())) {
				if (game.getPlayerCards().get(i).getColor() == null) {
					ColorUNO colorSelected = selectColor(game.getRandomNumber());
					game.getPlayerCards().get(i).setColor(colorSelected);
				}
				Card auxCard = game.getPlayerCards().get(i);
				game.throwCard(auxCard);
				game.setNextPlayer(true);
				return auxCard;
			}
		}
		if(game.isPossibleDraw()) {
			game.drawCard(game.getCurrentPlayer(), 1);
			game.setNextPlayer(false);
		}
		else {
			game.setNextPlayer(true);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return TYPE;
	}
	
	

}
