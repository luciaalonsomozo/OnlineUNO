package logic;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.factories.*;
import logic.gameobjects.Card;
import logic.gameobjects.CardList;
import logic.gameobjects.CardStatus;
import logic.gameobjects.Player;
import logic.gameobjects.PlayerList;
import logic.gameobjects.PlayerStatus;
import logic.gameobjects.PlayerStrategy;
import storage.Storage;

/**
 * Clase que representa el modelo del juego y contiene toda su lógica.
 *
 */
public class Game implements ObservableUNO, Serializable, GameStatus{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Atributo que se encarga de gestionar si una partida ha finalizado o no.
	 */
	private boolean end;
	/**
	 * Atributo para decidir si el ciclo de update debe continuar o no.
	 */
	private boolean cycleBool;
	/**
	 * Atributo que detecta si hay cartas posibles a robar.
	 */
	private boolean possibleDraw;
	
	/**
	 * Atributo que gestiona el paso de un jugador al siguiente. Si un usario roba carta, en vez de lanzarla,
	 * el turno no debe cambiar.
	 */
	private boolean nextPlayer;
	
	/**
	 * Atributo que gestiona si algún jugador a lanzado un prohibido.
	 */
	private boolean jumpPlayer;
	
	/**
	 * Índice en la lista de jugadores del jugador actual.
	 */
	private int currentPlayer;
	
	/**
	 * Carta lanzada.
	 */
	private Card thrownCard;
	
	/**
	 * Atributo que representa la lista de jugadores.
	 */
	private PlayerList playerList;
	
	/**
	 * Atributo que representa la última carta de la pila. Determina si el usuario puede lanzar una carta o no.
	 */
	private Card centerCard;
	
	/**
	 * Atributo que representa el montón de robar cartas.
	 */
	private CardList stack; 
	
	/**
	 * Atributo que representa el montón de cartas ya lanzadas.
	 */
	private CardList pile; 
	/**
	 * Atributo que representa la factoría de cartas, cuya finalidad es crear las instancias de estas. 
	 */
	private transient BuilderBasedFactory<Card> factory;
	
	/**
	 * Atributo que representa la factoría de las estrategias de los jugadores, cuya finalidad es crear las instancias de las estrategias de cada jugador.
	 */
	private transient BuilderBasedFactory<PlayerStrategy> playerFactory;
	
	/**
	 * Atributo que representa a la clase encargada de la gestión de cargar y guardar partida.
	 */
	private transient Storage st;
	
	/**
	 * Lista de observadores del juego.
	 */
	private transient List<ObserverUNO> observers;
	
	/**
	 * Lista con formato JSON que guarda los nombres y ficheros de las partidas ya guardadas.
	 */
	private transient List<JSONObject> saves;
	
	/**
	 * Posee una propia instancia de si mismo para aplicar el patrón singleton.
	 */
	private static Game game;
	
	/**
	 * Atributo random cuya finalidad es generar números aleatorios.
	 */
	private Random random;	
	
	/**
	 * 
	 * Se usa para la aplicación del patrón Singleton.
	 * Si todavia no hay instancia del juego, la crea, y si no, devuelve la ya existente.
	 * Evita que se cree más de una instancia.
	 * 
	 * @return Game
	 */
	public static Game getInstance() {
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	

	//Private methods

	/**
	 * 
	 * Inicializa los atributos del juego: los montones de cartas, el final de la partida, el jugadorActual.
	 * Se crean las dos factorias importantes del juego, la de los jugadores y la de las cartas.
	 * Para la de jugadores se crea una lista de builders con los posibles tipos de jugadores.
	 * Para la de cartas, se añaden los tipos de cartas. 
	 */
	private Game () {
		end = false; possibleDraw = true; cycleBool = true; nextPlayer = false; jumpPlayer = false;
		currentPlayer = 0;
		stack = new CardList();
		pile = new CardList();
		observers = new ArrayList<ObserverUNO>();
		random = new Random();
		
		List<Builder<Card>> builderList = new ArrayList<>();
		builderList.add(new SimpleCardBuilder());
		builderList.add(new ChangeCardBuilder());
		builderList.add(new ChangeColorCardBuilder());
		builderList.add(new ForbiddenCardBuilder());
		builderList.add(new Add2CardBuilder());
		builderList.add(new Add4CardBuilder());
		factory = new BuilderBasedFactory<Card>(builderList);	
		
		List<Builder<PlayerStrategy>> builderListPlayer = new ArrayList<>();
		builderListPlayer.add(new HumanPlayerBuilder());
		builderListPlayer.add(new EasyPlayerBuilder());
		builderListPlayer.add(new MediumPlayerBuilder());
		builderListPlayer.add(new HardPlayerBuilder());
		
		playerFactory = new BuilderBasedFactory<PlayerStrategy>(builderListPlayer);
		
		st = new Storage(this);
	}
	
	/**
	 * 
	 * Inicializa el montón de cartas del centro donde los usuarios lanzarán sus cartas.
	 * Selecciona la centerCard como la primera carta del montón de robar cartas.
	 * Barajea el montón de robar cartas.
	 * 
	 */
	private void initializeCenterCard() {
		centerCard = stack.remove(stack.size() - 1);
		if (centerCard.getColor() == null) {
			stack.add(centerCard);
			stack.shuffle(this);
			initializeCenterCard();			
		}
		
		centerCard.execute(this);
		pile.add(centerCard);
	}

	/**
	 * 
	 * Si no hay más cartas para robar impide robar y se vuelcan los montones. En caso contrario se fija en si el jugador puede tirar alguna de
	 * sus cartas, en caso afirmativo, impide robar.
	 * 
	 */
	private void updatePossibleDraw() {
		if (stack.size() == 0) {
			possibleDraw = false;
			//Remove centerCard
			pile.remove(pile.size() - 1);
			
			//Create new stack
			pile.shuffle(this);
			for(Card c : pile.getCardList()) {
				c.clearColor();
			}
			stack = new CardList(pile);
			
			//Create new pile
			pile.clear();
			pile.add(centerCard);
		}
		else {
			possibleDraw = !getPlayerCards().possible(centerCard);
		}
	}
	
		
	//MOST IMPORTANT
	
	/**
	 * 
	 * Se encarga de resetear el juego, creando una nueva instancia.
	 * 
	 * @return Game
	 */
	public static Game reset() {
		game = new Game();
		return game;
	}
	
	/**
	 * 
	 * Crea el montón de robar cartas, añadiendole todas las simples y especiales.
	 * Las barajea.
	 * 
	 */
	public void initializeMonton() {
		stack.addSimpleCards();
		stack.addSpecialCards();
		stack.shuffle(this);
	}	
	
	/**
	 * 
	 * Inicializa la lista de jugadores, el monton y la carta del medio.
	 * 
	 * @param names Lista de nombre de los jugadores
	 * @param type Lista de tipo de jugadores (humano, easy, medium, hard)
	 */
	public void initialize(List<String> names, List<String> type) {
		playerList = new PlayerList(names, type, playerFactory);
		initializeMonton();
		initializeCenterCard();
		playerList.initialize(this);
	}
	
	/**
	 * Método encargado de actualizar el estado del juego. 
	 * En primer lugar comprobamos si se ha finalizado la partida.
	 * Si se debe pasar al siguiente jugador y la partida continua, se comprueba si ha presionado el 
	 * botón UNO. Tras esto, se pasa al siguiente jugador y si se debe dar la vuelta a los 
	 * montones. 
	 * 
	 * Después se actualiza la posibilidad de robar carta. 
	 * 
	 * Por último se notifica a los observadores de la actualización y, si se da el caso, 
	 * se termina la partida.
	 * 
	 */
	public void update() {	
		possibleDraw = true;
		for (Player p: playerList.getPlayerList()) {
			if (p.win()) {
				end = true;
			}
		}
		if (nextPlayer && !end) {
			if(playerList.get(currentPlayer).getCards().size() == 1) {
				if(!playerList.get(currentPlayer).getUno()) {
					playerList.get(currentPlayer).drawCards(game, 1);
					gameError("The player " + playerList.get(currentPlayer).getName() + " did not pressed UNO!");
				}
				playerList.get(currentPlayer).setUno(false);
			}
			int jumpInt = jumpPlayer? 1 : 0;
			currentPlayer = (currentPlayer + 1 + jumpInt) % playerList.size();
			jumpPlayer = false;
			
			nextPlayer = false;
		}		
		updatePossibleDraw();

		if (!end) {
			thrownCard = playerList.get(currentPlayer).play(game);
			for (ObserverUNO obs: observers) {
				obs.notifyOnUpdate(this);
			}
		}
		else {
			finish();
		}
	}

	/**
	 * Método que controla el ciclo del juego.
	 */
	public void cycle() {
		while(cycleBool) {
			update();
		}
	}

	/**
	 * 
	 * Inicializa el juego, cargando la lista de jugadores, sus cartas, el numero del jugador actual, la carta central, los montones principales.
	 * 
	 * @param index Se trata del indice del fichero a cargar. Empieza en 0 y llega hasta el ultimo fichero guardado.
	 * @throws IOException
	 */
	public void load(int index) throws IOException {
		JSONObject input = saves.get(index);
		stack = new CardList();
		pile = new CardList();
		random = new Random();
		playerList = new PlayerList();
		possibleDraw = input.getBoolean("possibledraw");	
		
		JSONArray playerListAux = input.getJSONArray("players");
		playerList.load(playerListAux, factory, playerFactory);
		currentPlayer = input.getInt("currentplayer");
		
		//What if it is empty?
		if (input.getJSONObject("centercard").equals(new JSONObject())) {
			centerCard = null;
		}
		else {	
			centerCard = factory.createInstance(input.getJSONObject("centercard"));
		}
		
		JSONArray cardListAux = input.getJSONArray("stack");
		stack.load(cardListAux, factory);
		
		cardListAux = input.getJSONArray("pile");
		pile.load(cardListAux, factory);
	}
	
	
	//Auxiliary methods
	
	/**
	 * 
	 * Se encarga de la logica de que un jugador lance una carta.
	 * Pone esa carta como nueva centerCard, la borra del montón del jugador, y la añade a la lista de cartas lanzadas.
	 * 
	 * @param card Carta a lanzar.
	 */
	public void throwCard(Card card) {
		Player auxPlayer = playerList.get(currentPlayer);
		auxPlayer.throwCard(card);
		card.execute(this);
		centerCard = card;
		pile.add(card);
	}
	/**
	 * Su objetivo es implementar la lógica de lanzar un cambio de sentido. La lista de jugadores debe invertirse de orden.
	 */
	public void changeOrderPlayers() {
		currentPlayer = playerList.changeOrder(currentPlayer);
	}
	
	/**
	 * Su objetivo es implementar la lógica de lanzar un prohibido. Salta a un jugador, actualizando el currentPlayer.
	 */
	public void jumpPlayer() {
		jumpPlayer = true;
	}	

	/**
	 *
	 * Se encarga de gestionar que un jugador robe un cierto número de cartas.
	 * 
	 * Calculamos el minimo entre el numero de cartas a robar por el jugador y el numero de cartas restantes del monton de robar cartas, así, si debe robar más cartas
	 * de las posibles, solo robe las que pueda.
	 * 
	 * @param player Se trata del jugador que debe robar carta.
	 * @param amount Es el número de cartas que debe robar.
	 */
	public void drawCard(Player player, int amount) {
		if (amount <= stack.size()) {
			player.drawCards(this, amount);
		}
		else {
			//Remove centerCard
			pile.remove(pile.size() - 1);
			
			//We add the remaining drawable cards to the pile which is 
			//then going to be transformed into the stack again.
			for (int i = 0; i < stack.size(); i++) {
				pile.add(stack.get(i));
			}
			pile.shuffle(this);
			
			for(Card c : pile.getCardList()) {
				c.clearColor();
			}
			stack = new CardList(pile);
			
			//Create new pile
			pile.clear();
			pile.add(centerCard);
		}

		if (stack.size() == 0 && pile.size() > 1) {
			//Remove centerCard
			pile.remove(pile.size() - 1);
			
			//Create new stack
			pile.shuffle(this);
			for(Card c : pile.getCardList()) {
				c.clearColor();
			}
			stack = new CardList(pile);
			
			//Create new pile
			pile.clear();
			pile.add(centerCard);
		}
	}
	
	/**
	 *  Finaliza el juego.
	 */
	public void endGame() {
		end = true;
	}
	
	/**
	 * 
	 * Gestiona si se debe pasar de jugador o no. Si en un turno el jugador solo roba cartas, no debería pasar el siguiente.
	 * Solo hasta que haya lanzado.
	 * 
	 * @param update Decide si se cambia de jugador o no.
	 */
	public void setNextPlayer(boolean update) {
		nextPlayer = update;
	}

	
	//Basic notifications

	/**
	 * 
	 * Se encarga de guardar y notificar a los observadores de que se ha guardado el juego.
	 * 
	 * @param id Nombre del juego a guardar.
	 * @throws IOException
	 */
	public void save(String id) throws IOException {
		st.save(id);	
		for (ObserverUNO obs: observers) {
			obs.notifyOnSave();
		}
	}

	/**
	 *  
	 *  Se encarga de leer la lista de posibles partidas a cargar y notificar a los observadores. 
	 * 
	 * @throws IOException
	 */
	public void loadAvailables() throws IOException {
		saves = st.loadAvailables();
		for(ObserverUNO obs: observers) {
			obs.notifyOnLoad(game.getSaves());
		}
	}

	/**
	 * 
	 * Notifica a los observadores de un error.
	 * 
	 * @param errorMessage Se trata del mensaje del error. 
	 * 
	 */
	public void gameError(String errorMessage) {
		for (ObserverUNO obs: observers) {
			obs.notifyOnError(errorMessage);
		}
	}

	/**
	 * Notifica a los jugadores de que el juego ha comenzado.
	 * 
	 * @param numPlayers número de jugadores totales
	 */
	public void setNumPlayers(int numPlayers) {
		for(ObserverUNO obs : observers) {
			obs.notifyOnStart(numPlayers);
		}
	}
	
	/**
	 *  
	 *  Genera la lista de ganadores, de menor a mayor por número de cartas.
	 *  Notifica a los observadores de que el juego ha terminado. 
	 *  
	 */
	public void finish() {
		cycleBool = false;
		playerList.sort(Player.cmpCards);
		List<String> winners = new ArrayList<String>();
		for (int i = 0; i < playerList.size(); i++) {
			winners.add(playerList.get(i).toString());
		}
		for (ObserverUNO obs: observers) {
			obs.notifyOnEnd(winners);
		}
	}
	
	/**
	 *  Añade un observador a la lista de observadores para que se le pueda notificar.
	 */
	public void addObserver(ObserverUNO obs) {
		observers.add(obs);
	}
	
	/**
	 * Borra un observador de la lista de observadores.
	 */
	public void removeObserver(ObserverUNO obs) {
		observers.remove(obs);
	}
	
	/**
	 * Cambia el estado del bucle de updates. Por ejemplo, lo activa cuando un usuario 
	 * ha introducido una acción correcta.
	 * 
	 * @param cycleB Nuevo estado.
	 */
	public void setCycleBool(boolean cycleB) {
		this.cycleBool = cycleB;
	}
	
	
	//GETTERS:
	
	/**
	 * Genera un JSON que contiene el estado del juego, se incluyen todos los atributos del game.
	 * 
	 * @return JSONObject 
	 */
	
	public JSONObject report() {
		JSONObject result = new JSONObject();
		result.put("status", end);
		result.put("possibledraw", possibleDraw);
		result.put("players", playerList.report());
		result.put("currentplayer", currentPlayer);
		if (centerCard == null) {
			result.put("centercard", new JSONObject());
		}
		else {
			result.put("centercard", centerCard.report());
		}
		result.put("stack", stack.report());
		result.put("pile", pile.report());
		return result;
	}
	
	public List<String> getSaves() {
		List<String> res = new ArrayList<String>();
		String id;
		for (JSONObject save: saves) {
			id = save.getString("id");
			res.add(id);
		}
		return res;
	}
	
	public CardList getStack() {
		return stack;
	}
	
	public CardList getPlayerCards() {
		return playerList.get(currentPlayer).getCards();
	}
	
	public Card getCenterCard() {
		return centerCard;
	}
	
	public Player getCurrentPlayer() {
		return playerList.get(currentPlayer);
	}
	
	public double getRandomNumber() {
		return random.nextDouble();
	}
	
	public boolean isPossibleDraw() {
		return possibleDraw;
	}

	public Player nextPlayer() {
		return playerList.get((currentPlayer + 1) % playerList.size());
	}
	
	public PlayerList getPlayerList() {
		return playerList;
	}
	
	public Player getPlayer(int i) {
		return playerList.getPlayer(i);
	}
	
	public int getIndexCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean hasEnded() {
		return end;
	}


	@Override
	public String gameToString() {
		StringBuilder result = new StringBuilder();
		if (getCenterCard() != null) {
			result.append(getCenterCard().toString()).append('\n');
		}
		else {
			result.append('\n');
		}
		
		result.append(getCurrentPlayer().toString()).append(": ");
		for (int i = 0; i < getCurrentPlayer().getCards().size(); i++) {
			result.append(getCurrentPlayer().getCards().get(i).toString()).append(' ');
		}
		
		result.append("\n");
		result.append("You can still draw ").append(getStack().size()).append(" cards.");
		
		return result.toString();
	}


	@Override
	public String getThrownCard() {
		if (thrownCard == null) {
			return null;
		}
		return thrownCard.toString();
	}


	@Override
	public String getThrownCardGUI() {
		if (thrownCard == null) {
			return null;
		}
		return thrownCard.guiString();
	}

	@Override
	public int getNumberOfPlayers() {
		return playerList.size();
	}
	
	@Override
	public int getPlayerIndex(String name) {
		for (int i = 0; i < playerList.size(); i++) {
			if (playerList.get(i).getName().equals(name.toUpperCase())) {
				return i;
			}
		}
		return -1;
	}
	
	
	@Override
	public boolean ableToDraw() {
		return stack.size() > 0;
	}


	@Override
	public CardStatus getCenterCardStatus() {
		return centerCard.status();
	}


	@Override
	public PlayerStatus getCurrentPlayerStatus() {
		return playerList.get(currentPlayer).status();
	}

	@Override
	public PlayerStatus getPlayerStatus(int index) {
		return playerList.get(index % playerList.size()).status();
	}


	@Override
	public List<PlayerStatus> getPlayersStatus() {
		List<PlayerStatus> resT = new ArrayList<PlayerStatus>();
		for (int i = 0; i < playerList.size(); i++) {
			resT.add(playerList.get(i).status());
		}
		return resT;
	}


	public void changeIA(Player player, PlayerStrategy st2) {
		
		player.setStrategy(st2);
		
	}



}
