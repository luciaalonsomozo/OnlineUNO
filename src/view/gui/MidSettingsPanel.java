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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controls.Controller;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;
import logic.gameobjects.Player;
import logic.gameobjects.PlayerList;
import logic.gameobjects.PlayerStatus;


@SuppressWarnings("serial")
public class MidSettingsPanel extends JPanel implements ObserverUNO{

	public static final String LAYOUT_NAME = "MidSettingsPanel";
	
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 2);

	private Controller c;
	private JSlider _sound;
	private JSlider _music;
	private JButton applyButton;
	private JButton endButton;
	private JButton saveButton;
	private JButton exitButton;
	private JButton changeIAButton;
	private JButton cancelButton;
	private MainWindow root;
	private List<PlayerStatus> players;
	
	public MidSettingsPanel(MainWindow root, Controller c) {
		super(new BorderLayout());
		this.root = root;
		this.c = c;
		root.add(this);
		c.addObserver(this);
		initGUI();
	}
	
	/**Este panel contiene todos los ajustes o acciones que pueden realizarse en mitad de una partida.
	 * Tenemos:
	 * - Save Game: para poder guardar la partida actual en un fichero y poder cargarla m�s tarde, se abre otro JPanel que
	 * nos permite realizar esta acci�n.
	 * - End Game: termina la partida y lleva al usuario a la pantalla inicial de la aplicaci�n.
	 * - Change AI: cambiar la inteligencia artificial, se abre un di�logo para cambiar el modo de juego de alguno de los
	 * jugadores.
	 * - Cancel: cierra este panel y vuelve a la partida que se estaba jugando.
	 * - Apply: se vuelve a la partida actual con todos los cambios realizados.
	 * - Exit: se cierra la palicaci�n.
	 * Todos estos botones son dispuestos de una manera l�gica dentro de un JPanel.*/
	private void initGUI() {
		this.setBackground(Color.white);
		
		JPanel generalPanel = new JPanel(new GridBagLayout());

		
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		centerPanel.setBackground(Color.white);
		
		ImageIcon imageIconSaveGame = new ImageIcon("Images/savegame.jpg");
		Image image1  = imageIconSaveGame.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconSaveGame = new ImageIcon(image1);
		
		saveButton = new JButton(imageIconSaveGame);
		saveButton.setMaximumSize(new Dimension(160, 60));
		saveButton.setPreferredSize(new Dimension(160, 60));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				root.changePanel(SavePanel.LAYOUT_NAME);						
			}					
		});
		centerPanel.add(saveButton);
		
		
		ImageIcon imageIconEndGame = new ImageIcon("Images/endgame.jpg");
		Image image4  = imageIconEndGame.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconEndGame = new ImageIcon(image4);
		
		endButton = new JButton(imageIconEndGame);
		endButton.setMaximumSize(new Dimension(160, 60));
		endButton.setPreferredSize(new Dimension(160, 60));
		endButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] input = {"f"};
				c.userAction(input);
				c.endGame();
				root.changePanel(InitialPanel.LAYOUT_NAME);						
			}					
		});

		centerPanel.add(Box.createRigidArea(new Dimension(30, 50)));
		centerPanel.add(endButton);
		
		
		JPanel lowerPanel = new JPanel(new BorderLayout());
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		lowerPanel.setBackground(Color.white);
		
		ImageIcon imageIconCancel = new ImageIcon("Images/cancel.jpg");
		Image image3  = imageIconCancel.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconCancel = new ImageIcon(image3);
		
		
		cancelButton = new JButton(imageIconCancel);
		cancelButton.setMaximumSize(new Dimension(160, 60));
		cancelButton.setPreferredSize(new Dimension(160, 60));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				root.changePanel(MainGamePanel.LAYOUT_NAME);	
			}					
		});
		
		
		lowerPanel.add(cancelButton, BorderLayout.WEST);
		lowerPanel.setBackground(Color.white);
		
		ImageIcon imageIconApply = new ImageIcon("Images/apply.JPG");
		Image image2  = imageIconApply.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconApply = new ImageIcon(image2);
		
		applyButton = new JButton(imageIconApply);
		applyButton.setMaximumSize(new Dimension(160, 60));
		applyButton.setPreferredSize(new Dimension(160, 60));
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				root.changePanel(MainGamePanel.LAYOUT_NAME);						
			}					
		});
		lowerPanel.add(Box.createRigidArea(new Dimension(30, 50)));
		lowerPanel.add(applyButton, BorderLayout.EAST);
		
		//exitButton= new JButton();
		
		JPanel upperPanel = new JPanel(new BorderLayout());
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.setBackground(Color.white);
		
		imageIconApply = new ImageIcon("Images/Exit.jpg");
		image2  = imageIconApply.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconApply = new ImageIcon(image2);
		
		
		exitButton = new JButton(imageIconApply);
		exitButton.setMaximumSize(new Dimension(160, 60));
		exitButton.setPreferredSize(new Dimension(160, 60));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);						
			}					
		});
		
		upperPanel.add(Box.createRigidArea(new Dimension(550, 50)));
		upperPanel.add(exitButton, BorderLayout.EAST);
		
		
		
		//changeIAButton = new JButton();
		imageIconApply = new ImageIcon("Images/ChangeAI.jpg");
		image2  = imageIconApply.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconApply = new ImageIcon(image2);
		
		
		changeIAButton = new JButton(imageIconApply);
		changeIAButton.setMaximumSize(new Dimension(160, 60));
		changeIAButton.setPreferredSize(new Dimension(160, 60));
		changeIAButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new ChangeIADialog((JFrame) SwingUtilities.getRoot(MidSettingsPanel.this), players, c);
				
			}					
		});
		
		centerPanel.add(Box.createRigidArea(new Dimension(30, 50)));
		centerPanel.add(changeIAButton);
		
		String title = "Settings";
		buttonsPanel.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title, TitledBorder.LEFT, 2));
		buttonsPanel.setMaximumSize(new Dimension(800, 500));
		buttonsPanel.setPreferredSize(new Dimension(800, 500));
		buttonsPanel.add(upperPanel, BorderLayout.NORTH);
		buttonsPanel.add(Box.createRigidArea(new Dimension(200, 100)));
		buttonsPanel.add(centerPanel, BorderLayout.CENTER);
		buttonsPanel.add(Box.createRigidArea(new Dimension(200, 150)));
		buttonsPanel.add(lowerPanel, BorderLayout.SOUTH);
	
		generalPanel.add(buttonsPanel);
		buttonsPanel.setBackground(Color.white);
		generalPanel.setBackground(Color.white);
		this.add(generalPanel, BorderLayout.CENTER);
	
	}
	@Override
	public void notifyOnStart(int numPlayers) {
		return;				
	}
	@Override
	public void notifyOnLoad(List<String> savesNames) {
		return;		
	}
	@Override
	public void notifyOnUpdate(GameStatus gameSt) {
		this.players = gameSt.getPlayersStatus();
	}
	@Override
	public void notifyOnSave() {
		return;				
	}
	@Override
	public void notifyOnEnd(List<String> winnersNames) {
		return;				
	}
	@Override
	public void notifyOnError(String error) {
		return;				
	}

}
