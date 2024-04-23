package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controls.ControllerLocal;

public class NumPlayersPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final String LAYOUT_NAME = "NumPlayersPanel";

	//private static final Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 1);
	
	private static final String TWO_PLAYERS = "2 players";
	private static final String THREE_PLAYERS = "3 players";
	private static final String FOUR_PLAYERS = "4 players";
	
	private ControllerLocal controller;
	private MainWindow root;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 2);
	
	public NumPlayersPanel(MainWindow root, ControllerLocal c) {
		super(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.controller = c;
		this.root = root;
		root.add(this);
		initGUI();
	}
	/**Devuelve el texto del botón seleccionado entre un grupo de botones.
	 * @param buttonGroup grupo de botones, en nuestro caso botones con el numero de jugadores (de 2 a 4)
	 * @return texto del botón seleccionado, null en caso de no seleccionar nada*/
	public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	/**En este panel tenemos un grupo de botones para seleccionar el número de jugadores que queremos en nuestra partida
	 * asi como un botón back que llevaría la usuario al panel inicial y un botón de continue que una vez selccionado 
	 * el número de jugadores deseado llevaría al usuario a un panel en el que elegiría el nombre y el tipo de cada
	 * uno de ellos*/
	private void initGUI() {
		//PanelBackground global = new PanelBackground("Images/fondo1.jpg");
		//add(global);
		JButton backButton = new JButton();
		ImageIcon imageIconCancel = new ImageIcon("Images/back.jpg");
		Image image3  = imageIconCancel.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconCancel = new ImageIcon(image3);
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS ));
		
		backButton = new JButton(imageIconCancel);
		backButton.setMaximumSize(new Dimension(160, 60));
		backButton.setPreferredSize(new Dimension(160, 60));
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				root.changePanel(InitialPanel.LAYOUT_NAME);	
			}					
		});
		
		
		
		JLabel title = new JLabel("Choose the number of players:");
		this.setBackground(Color.WHITE);
		title.setAlignmentX(CENTER_ALIGNMENT);
		int fontSize = 40 /*(int) (getRootPane().getHeight() * 0.1)*/;
		title.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 30));
		
		upperPanel.add(title);
		upperPanel.add(Box.createRigidArea(new Dimension(100, 30)));
		upperPanel.add(backButton);
		
		/*JPanel outsidePanel = new JPanel(new GridBagLayout());
		outsidePanel.setBackground(Color.WHITE);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.WHITE);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		*/
		
		int buttonFontSize = 30;
		Font buttonFont = new Font(MainWindow.FONT_NAME, Font.BOLD, buttonFontSize);
		JRadioButton two_players = new JRadioButton(TWO_PLAYERS, false);
		two_players.setAlignmentX(CENTER_ALIGNMENT);
		two_players.setFont(buttonFont);
		two_players.setBackground(Color.WHITE);
		
		JRadioButton three_players = new JRadioButton(THREE_PLAYERS, false);
		three_players.setAlignmentX(CENTER_ALIGNMENT);	
		three_players.setFont(buttonFont);
		three_players.setBackground(Color.WHITE);
		
		JRadioButton four_players = new JRadioButton(FOUR_PLAYERS, false);
		four_players.setAlignmentX(CENTER_ALIGNMENT);
		four_players.setFont(buttonFont);
		four_players.setBackground(Color.WHITE);
		
		ButtonGroup bgroup = new ButtonGroup();
		
		bgroup.add(two_players);
		bgroup.add(three_players);
		bgroup.add(four_players);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS ));
		
		buttonsPanel.add(two_players);
		buttonsPanel.add(three_players);
		buttonsPanel.add(four_players);
	
		ImageIcon imageIconContinue = new ImageIcon("Images/continue.JPG");
		Image image  = imageIconContinue.getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH);
		imageIconContinue = new ImageIcon(image);
		
		JButton continueButton = new JButton(imageIconContinue);
		continueButton.setMaximumSize(new Dimension(180, 60));
		continueButton.setPreferredSize(new Dimension(180, 60));
		
		continueButton.setAlignmentX(CENTER_ALIGNMENT);
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Set number of players
				int numPlayers = 0;
				String selectedButton = getSelectedButtonText(bgroup);
				if (selectedButton == null) {
					JOptionPane.showMessageDialog(getParent(), "No number selected", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				else {
					switch(selectedButton) {
					case TWO_PLAYERS: {
						numPlayers = 2;
						break;
					}
					case THREE_PLAYERS: {
						numPlayers = 3;
						break;
					}
					case FOUR_PLAYERS: {
						numPlayers = 4;
						break;
					}
					default: {
						numPlayers = 0;
						break;
					}
					}
					if (numPlayers >= 2 && numPlayers <= 4) {
						controller.setNumPlayers(numPlayers);
						root.changePanel(UsersNamePanel.LAYOUT_NAME);					
					}					
				}
			}
		});
		JPanel buttons = new JPanel();
		String title2 = "Number of players";
		buttons.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title2, TitledBorder.LEFT, 2));
		buttons.setMaximumSize(new Dimension(800, 500));
		buttons.setPreferredSize(new Dimension(800, 500));
		
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS ));
		buttons.add(Box.createRigidArea(new Dimension(50, 50)));
		upperPanel.setBackground(Color.white);
		buttons.add(upperPanel);
		buttons.add(Box.createRigidArea(new Dimension(50, 30)));
		buttons.add(buttonsPanel);
		buttons.add(Box.createRigidArea(new Dimension(50, 80)));
		buttons.add(continueButton);

		JPanel generalPanel = new JPanel(new GridBagLayout());
		
		generalPanel.add(buttons);
		
		buttons.setBackground(Color.white);
		generalPanel.setBackground(Color.white);
		this.add(generalPanel, BorderLayout.CENTER);
		//buttons.add(Box.createRigidArea(new Dimension(50, 30)));
		
	}	

}
