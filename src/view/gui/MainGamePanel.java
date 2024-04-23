package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import controls.Controller;
import controls.ControllerLocal;
import controls.exceptions.GameException;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;
import logic.gameobjects.CardList;
import logic.gameobjects.CardStatus;
import logic.gameobjects.ColorUNO;
import logic.gameobjects.Player;
import logic.gameobjects.PlayerList;
import logic.gameobjects.PlayerStatus;

@SuppressWarnings("serial")
public class MainGamePanel extends JPanel implements ObserverUNO{

	public static final String LAYOUT_NAME = "MainGamePanel";

	private static final String HIDDEN_CARD_LOCATION = "Images/hiddenCard.jpg";
	private static final String HIDDEN_CARD_LOCATION_LEFT = "Images/hiddenCardleft.jpg";
	private static final String HIDDEN_CARD_LOCATION_RIGHT = "Images/hiddenCardright.jpg";
	private static final String HIDDEN_CARD_LOCATION_TOP = "Images/hiddenCardtop.jpg";
	
	private static double CARD_TO_WINDOW = 0.25;
	private static double FONT_TO_WINDOW = 0.015;
	private static double CARD_PROPORTION = 0.6;
	
	private static double CARD_PROPORTION_TOP_HEIGHT = 0.17;
	private static double CARD_PROPORTION_TOP_WIDTH = 0.55;
	
	private static double CARD_PROPORTION_LR_HEIGHT = 0.60;
	private static double CARD_PROPORTION_LR_WIDTH = 0.15;
	
	Controller controller;
	private MainWindow root;
	
	public MainGamePanel(MainWindow root, Controller c) {
		super(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.controller = c;
		this.root = root;
		root.add(this);
		c.addObserver(this);		
		initialGUI();
	}
	
	private void initialGUI() {
		JPanel layout = new JPanel(new GridBagLayout());
		layout.add(new JLabel("Waiting to start the game..."));
		add(layout);
	}

	private String generateLocation(String symbol, String color) {
		StringBuilder res = new StringBuilder();
		res.append("Images/");
		switch(symbol) {
		case "+2+": 
			symbol = "+2";
			break;
		case "+4+": 
			symbol = "+4";
			break;
		case "X": 
			symbol = "S";
			break;
		case "->":
			symbol = "R";
			break;
		case "C": 
			symbol = "CC";
			break;
		default: 
			break;
		}
		res.append(symbol);
		
		if (color != null) {
			switch(color) {
			case "YELLOW": 
				res.append("Amarillo");
				break;
			case "RED": 
				res.append("Rojo");
				break;
			case "GREEN": 
				res.append("Verde");
				break;
			case "BLUE": 
				res.append("Azul");
				break;
			default: 
				break;
			}
		}
		res.append(".jpg");
		return res.toString();
	}
	/**A�ade el boto�n del UNO a la esquina inferior derecha, al pulsar un jugador cuando le queden 2 cartas, se activar� 
	 * la l�gica del comando UNO
	 * @param parent panel general al que se a�ade el bot�n*/
	private void unoButton(JPanel parent) {
		ImageIcon imageIcon = new ImageIcon("Images/unoImage.jpg");
		Image image = imageIcon.getImage().getScaledInstance((int) (getParent().getHeight()*0.2), (int) (getParent().getHeight()*0.2), Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		JButton unoButton = new JButton(imageIcon);
		unoButton.setPreferredSize(new Dimension((int) (getParent().getHeight()*0.15), (int) (getParent().getHeight()*0.15)));
		unoButton.setMaximumSize(new Dimension((int) (getParent().getHeight()*0.15), (int) (getParent().getHeight()*0.15)));
		unoButton.setAlignmentX(RIGHT_ALIGNMENT);
		Border empty = BorderFactory.createEmptyBorder();
		unoButton.setBorder(empty);
		parent.add(unoButton, BorderLayout.EAST);
		unoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String [] input = {"u"};
				controller.userAction(input);
			}
		});
	}
	/**A�ade el boto�n de settings a la esquina superior derecha, al pulsar se abre el panel correspondiente
	 * @param parent panel general al que se a�ade el bot�n*/
	private void settingsButton(JPanel parent) {
		ImageIcon imageIcon = new ImageIcon("Images/settings.png");
		Image image = imageIcon.getImage().getScaledInstance((int) (getParent().getHeight()*0.05), (int) (getParent().getHeight()*0.05), Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		JButton settingsButton = new JButton(imageIcon);
		settingsButton.setPreferredSize(new Dimension((int)(getParent().getHeight()*0.05), (int)(getParent().getHeight()*0.05)));
		settingsButton.setAlignmentX(RIGHT_ALIGNMENT);
		Border empty = BorderFactory.createEmptyBorder();
		settingsButton.setBorder(empty);
		parent.add(settingsButton);
		settingsButton.setToolTipText("Settings");
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Open settings panel
				root.changePanel(MidSettingsPanel.LAYOUT_NAME);
			}
		});
	}
	/**A�ade el mont�n de cartas central con la carta que est� bocarriba en ese momento*/
	private void addTopCards(GameStatus game) {	
		int height = (int) (getRootPane().getHeight() * CARD_TO_WINDOW);
		int width = (int) (height * CARD_PROPORTION);
		JPanel top = new JPanel();
		add(Box.createRigidArea(new Dimension(30,30)));
		//CAMBIE ESTO
		add(top, BorderLayout.CENTER);
		top.setBackground(Color.WHITE);
		
		String symbol = game.getCenterCardStatus().getSymbol();
		String color = game.getCenterCardStatus().getColorString();
		String fileLocation = generateLocation(symbol, color);
		ImageIcon imageIcon = new ImageIcon(fileLocation);
		Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		JLabel centerCard = new JLabel(imageIcon);
		top.add(centerCard);
		
		if (game.ableToDraw()) {
			imageIcon = new ImageIcon(HIDDEN_CARD_LOCATION);
			image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(image);
			JButton drawCard = new JButton(imageIcon);
			drawCard.setPreferredSize(new Dimension(width, height));
			drawCard.setMaximumSize(new Dimension(width, height));
			drawCard.addActionListener((ActionEvent a) -> {
				String [] input = {"d"};
				controller.userAction(input);
			});
			top.add(drawCard);
		}
	}
	/**A�ade las cartas del jugador de la izquierda, el que esta a tres turnos del actual, las cartas son ajustadas al tama�o 
	 * de la pantalla de cada ordenador, y como m�ximo se monstrar�n al jugador actual 4 de estas, si resultan ser m�s, 
	 * aparece un indicador con el n�mero de cartas restantes . 
	 * Ademas se a�ade debajo de las cartas el nombre de usuario del jugador.
	 * @param player jugador correspondiente a esas cartas*/
	private void addLeftPlayerCards(PlayerStatus player) {

		
		int width = (int) (getRootPane().getHeight() * CARD_PROPORTION_LR_WIDTH);
		int height = (int) ((width) *  CARD_PROPORTION_LR_HEIGHT);
		//add(Box.createRigidArea(new Dimension(30,30)));
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		List<CardStatus> deck = player.getCardsStatus();
		
		ImageIcon imageIconCut = new ImageIcon(HIDDEN_CARD_LOCATION_LEFT);
		Image imageCut = imageIconCut.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		imageIconCut = new ImageIcon(imageCut);
		
		int i = 0;
		if(deck.size() > 5) {
			while(i < 4) {
				JLabel leftCards = new JLabel(imageIconCut);
				left.add(leftCards);
				i++;
			}
			left.add(Box.createRigidArea(new Dimension(1, 20)));
			int numberOfCards = deck.size() - 4;
			JLabel numberOfCardsLabel = new JLabel("+" + numberOfCards);
			left.add(numberOfCardsLabel);
		}
		else {
			for(int j = 0; j < deck.size(); j++) {
				JLabel leftCards = new JLabel(imageIconCut);
				left.add(leftCards);
			}
		}
		
		
		JLabel playerName = new JLabel(player.getName());
		left.add(playerName);
		left.setBackground(Color.WHITE);
		this.add(left, BorderLayout.WEST);
		
	}
	/**A�ade las cartas del jugador de la derecha, el que esta a un turnos del actual, las cartas son ajustadas al tama�o 
	 * de la pantalla de cada ordenador, y como m�ximo se monstrar�n al jugador actual 4 de estas, si resultan ser m�s, 
	 * aparece un indicador con el n�mero de cartas restantes . 
	 * Ademas se a�ade debajo de las cartas el nombre de usuario del jugador.
	 * @param player jugador correspondiente a esas cartas*/
	private void addRightPlayerCards(PlayerStatus player) {

		int width = (int) (getRootPane().getHeight() * CARD_PROPORTION_LR_WIDTH);
		int height = (int) ((width) *  CARD_PROPORTION_LR_HEIGHT);
		//add(Box.createRigidArea(new Dimension(30,30)));
		
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		List<CardStatus> deck = player.getCardsStatus();
		
		ImageIcon imageIconCut = new ImageIcon(HIDDEN_CARD_LOCATION_RIGHT);
		Image imageCut = imageIconCut.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		imageIconCut = new ImageIcon(imageCut);
		
		int i = 0;
		if(deck.size() > 5) {
			while(i < 4) {
				JLabel leftCards = new JLabel(imageIconCut);
				right.add(leftCards);
				i++;
			}
			right.add(Box.createRigidArea(new Dimension(1, 20)));
			int numberOfCards = deck.size() - 4;
			JLabel numberOfCardsLabel = new JLabel("+" + numberOfCards);
			right.add(numberOfCardsLabel);
		}
		else {
			for(int j = 0; j < deck.size(); j++) {
				JLabel leftCards = new JLabel(imageIconCut);
				right.add(leftCards);
			}
		}
		
		JLabel playerName = new JLabel(player.getName());
		right.add(playerName);
		right.setBackground(Color.WHITE);
		this.add(right, BorderLayout.EAST);
		
	}

	/**A�ade las cartas del jugador superior, el que esta a dos turnos del actual, las cartas son ajustadas al tama�o 
	 * de la pantalla de cada ordenador, y como m�ximo se monstrar�n al jugador actual 4 de estas, si resultan ser m�s, 
	 * aparece un indicador con el n�mero de cartas restantes . 
	 * Ademas se a�ade a la derecha de las cartas el nombre de usuario del jugador.
	 * @param player jugador correspondiente a esas cartas*/
	private void addTopPlayerCards(PlayerStatus player) {
		
		int height = (int) (getRootPane().getHeight() * CARD_PROPORTION_TOP_HEIGHT);
		int width = (int) (height * CARD_PROPORTION_TOP_WIDTH);
		
		JPanel top = new JPanel(new BorderLayout());
		JPanel topCards = new JPanel();
		topCards.setBackground(Color.WHITE);
		top.setBackground(Color.WHITE);
		topCards.setLayout(new BoxLayout(topCards, BoxLayout.X_AXIS));
		JPanel cards = new JPanel();
		cards.setBackground(Color.WHITE);
		int fontSize = (int) (getRootPane().getWidth() * FONT_TO_WINDOW);
		List<CardStatus> deck = player.getCardsStatus();
		
		ImageIcon imageIconCut = new ImageIcon(HIDDEN_CARD_LOCATION_TOP);
		Image imageCut = imageIconCut.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		imageIconCut = new ImageIcon(imageCut);

		
		int i = 0;
		if(deck.size() > 5) {
			while(i < 4) {
				JLabel topCard = new JLabel(imageIconCut);
				cards.add(topCard);
				i++;
			}
			topCards.add(cards);
			int numberOfCards = deck.size() - 4;
			JLabel numberOfCardsLabel = new JLabel("+" + numberOfCards);
			cards.add(numberOfCardsLabel);
		}
		else {
			for(int j = 0; j < deck.size(); j++) {
				JLabel leftCards = new JLabel(imageIconCut);
				cards.add(leftCards);
			}
			topCards.add(cards);
		}
		JLabel playerName = new JLabel(player.getName());
		playerName.setAlignmentX(RIGHT_ALIGNMENT);

		cards.add(playerName);
		top.add(topCards, BorderLayout.CENTER);

		JPanel sButton = new JPanel();
		sButton.setBackground(Color.WHITE);
		top.add(sButton, BorderLayout.EAST);
		settingsButton(sButton);
		
		this.add(top, BorderLayout.NORTH); 
		
	}
	
	/**A�ade las cartas del jugador actual, el que tiene el turno, las cartas son ajustadas al tama�o de la pantalla de 
	 * cada ordenador. Ademas se a�ade a la izquierda de las cartas el nombre de usuario del jugador.
	 * @param player jugador actual
	 * @param isCurrent booleanoq que lleva si es el turno del jugador 
	 * @param currentPlayer Nombre del jugador actual
	*/
	private void addPlayerHand(PlayerStatus player, boolean isCurrent, String currentPlayer) {
		
		JPanel parent = new JPanel(new BorderLayout());
		parent.setBackground(Color.WHITE);

		JLabel turn;
		if(isCurrent) {
			turn = new JLabel("Your turn");
		}
		else {
			turn = new JLabel(currentPlayer + " turn");
		}

		int fontSize = (int) (getRootPane().getHeight() * FONT_TO_WINDOW);
		turn.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, fontSize));
		
		parent.add(turn, BorderLayout.NORTH);
		
		JPanel hand = new JPanel();
		hand.setBackground(Color.WHITE);
		hand.setLayout(new BoxLayout(hand, BoxLayout.X_AXIS));
		JLabel name = new JLabel(player.getName());
		name.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, fontSize));
		hand.add(name);	
		//Invisible separator
		hand.add(Box.createHorizontalStrut(fontSize));
		
		JPanel cards = new JPanel(); 
		cards.setBorder(null);
		cards.setBackground(Color.WHITE);
		List<CardStatus> deck = player.getCardsStatus();
		JButton auxButton; String symbol; String color; String fileLocation;
		ImageIcon imageIcon; Image image;
		int height = (int) (getRootPane().getHeight() * CARD_TO_WINDOW);
		int width = (int) (height * CARD_PROPORTION);
		for (CardStatus c: deck) {
			symbol = c.getSymbol();
			color = c.getColorString();
			fileLocation = generateLocation(symbol, color); 
			imageIcon = new ImageIcon(fileLocation);
			image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(image);
			auxButton = new JButton(imageIcon);
			auxButton.setPreferredSize(new Dimension(width, height));
			auxButton.addActionListener((ActionEvent a) -> {
				try {
					String colorString = null;
					if (c.getColorString() == null) {
						ColorSelectionDialog askColorDiag = new ColorSelectionDialog((JFrame) SwingUtilities.getRoot(this));
						colorString = askColorDiag.askColor();
						if (colorString == null) {
							throw new GameException("No color selected");
						}
					}
					else {
						colorString = c.getColorString();
					}
					String [] input = {"t", c.getSymbol(), colorString};
					controller.userAction(input);
				}
				catch (GameException e) {
					JOptionPane.showMessageDialog(getParent(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				
			});
			cards.add(auxButton);
		}
		hand.add(cards);
		JScrollPane scroll = new JScrollPane(cards, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hand.add(scroll);
		unoButton(hand);
		parent.add(hand, BorderLayout.CENTER);
		this.add(parent, BorderLayout.SOUTH);
	}

	
	@Override
	public void notifyOnUpdate(GameStatus game) {
		
		int numberOfPlayers = game.getNumberOfPlayers();
		String playerName = controller.getPlayer();
		int index = game.getPlayerIndex(playerName);
		
		boolean isCurrent = false;

		if(playerName.equals(game.getCurrentPlayerStatus().getName())) {
			isCurrent = true;
		}
		
		removeAll();
		
		switch(numberOfPlayers) {
			case 2: 
				addTopPlayerCards(game.getPlayerStatus(index + 1));
				break;
			case 3:
				addTopPlayerCards(game.getPlayerStatus(index + numberOfPlayers - 1));
				addRightPlayerCards(game.getPlayerStatus(index + 1));
				break;
			case 4:
				addTopPlayerCards(game.getPlayerStatus(index + 2));
				addLeftPlayerCards(game.getPlayerStatus(index + numberOfPlayers - 1));
				addRightPlayerCards(game.getPlayerStatus(index + 1));
				break;
		}

		//Center card & Draw card
		addTopCards(game);
		
		//Player hand
		addPlayerHand(game.getPlayerStatus(index), isCurrent, game.getCurrentPlayerStatus().getName());	
		
		revalidate();
		repaint();

		if (!game.getCurrentPlayerStatus().getStrategyString().equals("Human")){
			//Message
			StringBuilder message = new StringBuilder();
			message.append("The player ").append(game.getCurrentPlayerStatus().getName()).append(" has ");
			if (game.getThrownCard() == null) {
				if (game.getCurrentPlayerStatus().getUno()) {
					message.append("pressed UNO!");
				}
				else {
					message.append("drawn a card!");
				}
			}
			else {
				message.append("thrown ").append(game.getThrownCardGUI());
			}
			JOptionPane.showMessageDialog(getParent(), message, "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	@Override
	public void notifyOnEnd(List<String> winners) {
		root.changePanel(EndPanel.LAYOUT_NAME);
	}

	@Override
	public void notifyOnError(String error) {
		JOptionPane.showMessageDialog(getParent(), error, "WARNING", JOptionPane.WARNING_MESSAGE);		
	}

	@Override
	public void notifyOnLoad(List<String> savesNames) {
		return;
	}

	@Override
	public void notifyOnSave() {
		JOptionPane.showMessageDialog(getParent(), "Game has been susscessfully saved!", "Information", JOptionPane.INFORMATION_MESSAGE);		
	}

	@Override
	public void notifyOnStart(int numPlayers) {
		return;		
	}
	
}
