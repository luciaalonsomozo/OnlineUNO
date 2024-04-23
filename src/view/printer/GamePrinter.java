package view.printer;

import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;
import logic.gameobjects.EasyPlayer;
import logic.gameobjects.HardPlayer;
import logic.gameobjects.HumanPlayer;
import logic.gameobjects.MediumPlayer;
import logic.gameobjects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controls.ControllerLocal;
public class GamePrinter implements ObserverUNO {
	
	private static final String WELCOME_MSG = "Welcome to UNO";
	private static final String NEW_GAME_MSG = "Creating new game...";
	private static final String LOAD_MSG = "Which one do you want to load?";
	private static final String PLAYERS_MSG = "How many players are going to play?: ";
	private static final String GAME_SAVED_MSG = "Game saved successfully!";
	private static final String WRONG_NUMBER = "The number must be between ";
	private static final String PLAYERS_NAMES = "Introduce the names of the players:";
	private static final String WRONG_NAMES_PLAYERS = "The name has already been chosen:";
	private static final String TYPE_PLAYERS = "Introduce the type of player (Human, Easy, Medium, Hard): ";
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 4;
	private Scanner scanner;
	private ControllerLocal controller;
	
	public GamePrinter(ControllerLocal c) {
		this.scanner = new Scanner(System.in);
		this.controller = c;
		c.addObserver(this);
	}

	public void run() {
		showInitialMenu();
	}
	
	private int askNumber(int min, int max) {
		int num = -1;
		try {
			String s = scanner.nextLine();
			num = Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
			System.out.print(WRONG_NUMBER + min + " and " + max + '\n');
			num = askNumber(min, max);
		}
		if (num < min || num > max) {
			System.out.print(WRONG_NUMBER + ' ' + min + " and " + max + '\n');
			num = askNumber(min, max);
		}
		return num;
	}
	
	public void showInitialMenu() {
		System.out.print(WELCOME_MSG + '\n');
		System.out.print("Do you want to load a save?\n");
		String auxLoadQuestion = scanner.nextLine();
		boolean goingToLoad = "YES".equals(auxLoadQuestion.toUpperCase());
		if (goingToLoad) {
			goingToLoad = controller.wantToLoad();
		}
		if (!goingToLoad) {
			System.out.println(NEW_GAME_MSG);
			System.out.println("Posible commands: ");
			String help = controller.getHelp();
			System.out.println(help);
			notLoaded();
		}
	}
			
	public void notLoaded() {	
		System.out.print(PLAYERS_MSG + '\n');
		int num = askNumber(MIN_PLAYERS, MAX_PLAYERS);
		controller.setNumPlayers(num);
	}

	@Override
	public void notifyOnStart(int numPlayers) {
		System.out.print(PLAYERS_NAMES + '\n');
		List<String> names = new ArrayList<String>();
		List<String> type = new ArrayList<String>();
		
		String aux = null; boolean correctName = true;
		int i = 0;
		while (i < numPlayers) {
			correctName = true;
			System.out.print("Player " + (i+1) + ": ");
			aux = scanner.nextLine();
			for (int ii = 0; ii < i && correctName; ii++) {
				String auxName = names.get(ii);
				if (aux.equals(auxName)) {
					System.out.print(WRONG_NAMES_PLAYERS + '\n');
					correctName = false;
				}
			}
			if (correctName) { 
				names.add(aux);
				boolean typeB = false;
				while(!typeB) {
					System.out.print(TYPE_PLAYERS);
					aux = scanner.nextLine();
					if(!aux.equals(EasyPlayer.TYPE) && !aux.equals(MediumPlayer.TYPE) && !aux.equals(HardPlayer.TYPE) &&  !aux.equals(HumanPlayer.TYPE)) {
						System.out.println("Invalid type!");
					}
					else typeB = true;
					
				}
				type.add(aux);
				i++; 
			}
		}		
		controller.initialize(names, type);
	}

	@Override
	public void notifyOnLoad(List<String> savesNames) {
		for (int i = 0; i < savesNames.size(); i++) {
			System.out.println((i + 1) + ". " + savesNames.get(i));
		}
		System.out.println(LOAD_MSG);
		int index = askNumber(1, savesNames.size());
		controller.load(index - 1);
	}
	
	@Override
	public void notifyOnUpdate(GameStatus gameSt) {
		System.out.println(gameSt.gameToString());
		
		int index = gameSt.getPlayerIndex(gameSt.getCurrentPlayerStatus().getName());
		
		if (!gameSt.getPlayerStatus(index).getStrategyString().equals("Human")) {
			if (gameSt.getThrownCard() == null) {
				if (gameSt.getCurrentPlayerStatus().getUno()) {
					System.out.println("The player has pressed UNO!");
				}
				else {
					System.out.println("The player has drawn!");
				}
			} 
			else {
				System.out.print("Selected card: ");
				System.out.println(gameSt.getThrownCard());
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			String[] input = askForCommand();
			controller.userAction(input);
		}
	}
	
	@Override
	public void notifyOnSave() {
		System.out.println(GAME_SAVED_MSG);
	}

	@Override
	public void notifyOnEnd(List<String> winners) {
		if (winners.size() > 0) {
			System.out.print("The player " + winners.get(0) + " is the winner!\n");
		}
		System.out.print("The game has ended.");
	}
	
	@Override
	public void notifyOnError(String errorMessage) {
		System.out.println(errorMessage);
	}

	public String[] askForCommand () {
		String input;
		System.out.print("Action > ");
		input = scanner.nextLine();
		
		String [] params = input.toLowerCase().trim().split(" ");
		return params;
	}

	public void showError(String message) {
		System.out.println(message);
	}


}
