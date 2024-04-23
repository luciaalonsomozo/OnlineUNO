package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controls.ControllerLocal;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;
import logic.gameobjects.Player;

@SuppressWarnings("serial")
public class UsersNamePanel extends JPanel implements ObserverUNO{
	
	public static final String LAYOUT_NAME = "UsersPanel";
	
	private ControllerLocal controller;
	private List<String> playersName;
	private List<String> typePlayers;
	private int numberOfPlayers;
	private MainWindow root;

	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 2);
	
	String[] typesOfPlayer = { "Human", "Easy", "Medium", "Hard"};
	
	public UsersNamePanel(MainWindow root, ControllerLocal c) {
		super(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.controller = c;
		this.playersName = new ArrayList<String>();
		this.typePlayers = new ArrayList<String>();
		this.root = root;
		root.add(this);
		c.addObserver(this);
	}
	
	/**En este panel, según el número de jugadores seleccionado en el anterior panel, se mostrarí una pantalla con
	 * textFields para poder introducir el nombre de cada uno de los jugadores, así como un menu en el que se
	 * podrá seleccionar el tipo de jugador que es cada uno de ellos, humano, o dentro de ser un bot, con nivel fácil, 
	 * medio o difícil.
	 * Este panel tiene también un botón continuo para crear la partida con todas las opciones seleccionadas.*/
	private void initGUI() {
		JPanel buttons = new JPanel();
		String title = "Users name";
		buttons.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title, TitledBorder.LEFT, 2));
		buttons.setMaximumSize(new Dimension(800, 500));
		buttons.setPreferredSize(new Dimension(800, 500));

		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS ));
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS ));
		
		
		JLabel newGame = new JLabel("Introduce the names of the players:");
		newGame.setBackground(Color.WHITE);
		newGame.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 20));
		newGame.setHorizontalAlignment(0);
		
		upperPanel.add(newGame);
		upperPanel.add(Box.createRigidArea(new Dimension(200, 20)));
		//upperPanel.add(backButton);
		upperPanel.setBackground(Color.WHITE);
		
		List<JTextField> txtFields = new ArrayList<JTextField>();
		List<JComboBox> comboBoxList = new ArrayList<JComboBox>();
		JPanel data = new JPanel();
		data.setBackground(Color.WHITE);
		
		data.setLayout(new BoxLayout(data, BoxLayout.Y_AXIS));
		data.setPreferredSize(new Dimension(600,400));
		for (int i = 0; i < numberOfPlayers; i++) {
			JPanel askUser = new JPanel();
			//askUser.setBackground(Color.BLACK);
			askUser.setLayout(new BoxLayout(askUser, BoxLayout.Y_AXIS));
			
			
			JLabel usersName = new JLabel("Insert name");
			usersName.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 20));
			askUser.add(usersName);
			
			JTextField txt = new JTextField("User Name " + (i + 1));
			txt.setPreferredSize(getMinimumSize());
			txt.setSize(getParent().getWidth()*(numberOfPlayers)/4, getParent().getHeight());
			txtFields.add(txt);
			askUser.add(txtFields.get(i));
			
			JPanel choose = new JPanel(new BorderLayout());
			JLabel type = new JLabel("Choose type of player");
			type.setAlignmentX(LEFT_ALIGNMENT);
			type.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 20));
			
			JComboBox<String> typeOfPlayers = new JComboBox<String>(this.typesOfPlayer);
			typeOfPlayers.setAlignmentX(LEFT_ALIGNMENT);
			comboBoxList.add(typeOfPlayers);
			typeOfPlayers.setSelectedIndex(0);
			typeOfPlayers.setBackground(Color.WHITE);
			
			choose.add(type, BorderLayout.NORTH);
			choose.add(typeOfPlayers, BorderLayout.CENTER);
			
			JPanel eachUser = new JPanel();
			eachUser.setLayout(new BoxLayout(eachUser, BoxLayout.X_AXIS));
			eachUser.setBackground(Color.WHITE);
			
			eachUser.add(Box.createRigidArea(new Dimension(40,10)));
			askUser.setBackground(Color.WHITE);
			eachUser.add(askUser);
			eachUser.add(Box.createRigidArea(new Dimension(80,10)));
			choose.setBackground(Color.WHITE);
			eachUser.add(choose);
			eachUser.add(Box.createRigidArea(new Dimension(40,10)));
			data.add(eachUser);
			data.setBackground(Color.WHITE);
		}
		
		ImageIcon imageIconContinue = new ImageIcon("Images/continue.JPG");
		Image image  = imageIconContinue.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconContinue = new ImageIcon(image);
		
		JButton continueButton = new JButton(imageIconContinue);
		continueButton.setMaximumSize(new Dimension(160, 60));
		continueButton.setPreferredSize(new Dimension(160, 60));
		
		continueButton.setAlignmentX(CENTER_ALIGNMENT);
		continueButton.addActionListener((ActionEvent e) -> {
			boolean valid = true;
			//Check the fields have text
			//Check the names are different
			for (int i = 0; i < numberOfPlayers; i++) {
				if ("".equals(txtFields.get(i).getText())) {
					valid = false;
				}
				for (int ii = i + 1; ii < numberOfPlayers; ii++) {
					if (txtFields.get(i).getText().equals(txtFields.get(ii).getText())) {
						valid = false;
					}
				}
			}
			if (valid) {
				for (int i = 0; i < numberOfPlayers; i++) {
					playersName.add(txtFields.get(i).getText());
					typePlayers.add((String) comboBoxList.get(i).getSelectedItem());
				}
				controller.initialize(playersName, typePlayers);
				root.changePanel(MainGamePanel.LAYOUT_NAME);
			}
			else {
				JOptionPane.showMessageDialog(getParent(), "Invalid names!");
			}
		});
		JPanel lowerPanel;
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS ));
		lowerPanel.add(Box.createRigidArea(new Dimension(500, 20)));
		lowerPanel.add(continueButton);
		lowerPanel.setBackground(Color.WHITE);
		
		buttons.add(upperPanel);
		buttons.add(Box.createRigidArea(new Dimension(40, 20)));
		buttons.add(data, BorderLayout.CENTER);
		buttons.add(Box.createRigidArea(new Dimension(40, 20)));
		buttons.add(lowerPanel);

		//data.add(next, BorderLayout.SOUTH);
		
		JPanel generalPanel = new JPanel(new GridBagLayout());
		
		generalPanel.add(buttons);
		
		buttons.setBackground(Color.white);
		generalPanel.setBackground(Color.white);
		this.add(generalPanel, BorderLayout.CENTER);


	}

	@Override
	public void notifyOnStart(int numPlayers) {
		this.numberOfPlayers = numPlayers;
		initGUI();
	}

	@Override
	public void notifyOnEnd(List<String> winners) {
		return;
	}

	@Override
	public void notifyOnUpdate(GameStatus game) {
		return;
	}

	@Override
	public void notifyOnLoad(List<String> savesNames) {
		return;
	}

	@Override
	public void notifyOnError(String error) {
		//JOptionPane.showMessageDialog(getParent(), error);
	}

	@Override
	public void notifyOnSave() {
		return;
	}
	
}
