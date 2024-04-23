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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controls.ControllerLocal;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;

@SuppressWarnings("serial")
public class LoadPanel extends JPanel implements ObserverUNO{

	public static final String LAYOUT_NAME = "LoadPanel";
	
	private ControllerLocal controller;
	private List<String> savesNames;
	private MainWindow root;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 2);
	
	public LoadPanel(MainWindow root, ControllerLocal c) {
		super(new BorderLayout());
		this.controller = c;
		this.root = root;
		this.savesNames = new ArrayList<String>();
		root.add(this);
		c.addObserver(this);
	}
	/**
	 * Creamos este panel para la acción de cargar una partida de UNO previamente guardada, para esto tenemos 
	 * un scroll que nos muestra todas las opciones de partidas que tenemos, tras seleccionar una, se nos abriría 
	 * el panel de juego con los datos de esa partida concreta.
	 * Además, tenemos el botón back que nos llevaría al panel inicial.*/
	private void initGUI() {
		JButton backButton = new JButton();
		ImageIcon imageIconBack = new ImageIcon("Images/back.jpg");
		Image image3  = imageIconBack.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconBack = new ImageIcon(image3);
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS ));
		
		backButton = new JButton(imageIconBack);
		backButton.setMaximumSize(new Dimension(160, 60));
		backButton.setPreferredSize(new Dimension(160, 60));
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				root.changePanel(InitialPanel.LAYOUT_NAME);	
			}						
		});
		
		JLabel title = new JLabel("Choose the game you want to play", SwingConstants.CENTER);
		title.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 20));
		title.setAlignmentX(CENTER_ALIGNMENT);
		upperPanel.add(Box.createRigidArea(new Dimension(100, 50)));
		upperPanel.add(title);
		upperPanel.add(Box.createRigidArea(new Dimension(150, 50)));
		upperPanel.add(backButton);
		upperPanel.setBackground(Color.white);
	
		JPanel saves = new JPanel();
		JPanel buttons = new JPanel();
		buttons.add(upperPanel);
		saves.setMaximumSize(new Dimension(600, 320));
		saves.setPreferredSize(new Dimension(600, 320));
		JButton auxBut;
		for (int i = 0; i < savesNames.size(); i++) {
			auxBut = new JButton(savesNames.get(i));
			auxBut.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, (int) (getRootPane().getHeight() * 0.02)));
			auxBut.setPreferredSize(new Dimension(400, 30));
			final int j = i;
			auxBut.addActionListener((ActionEvent a) -> {
				controller.load(j);
				root.changePanel(MainGamePanel.LAYOUT_NAME);
			});
			auxBut.setAlignmentX(CENTER_ALIGNMENT);
			saves.add(auxBut);
		}
		JScrollPane scroll = new JScrollPane(saves, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMaximumSize(new Dimension(600, 300));
		scroll.setPreferredSize(new Dimension(600, 300));
		saves.setBackground(Color.white);
		scroll.setBackground(Color.white);
		buttons.add(Box.createRigidArea(new Dimension(50, 50)));
		buttons.add(scroll);
		String title2 = "Load Game";
		
		buttons.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title2, TitledBorder.LEFT, 2));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS ));


		buttons.setMaximumSize(new Dimension(800, 500));
		buttons.setPreferredSize(new Dimension(800, 500));
		JPanel generalPanel = new JPanel(new GridBagLayout());
		
		generalPanel.add(buttons);
		buttons.setBackground(Color.white);
		generalPanel.setBackground(Color.white);
		this.add(generalPanel, BorderLayout.CENTER);
	}

	@Override
	public void notifyOnLoad(List<String> savesNames) {
		this.savesNames = savesNames;
		initGUI();
	}

	@Override
	public void notifyOnError(String error) {
		return;
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
	public void notifyOnSave() {
		return;
	}

	@Override
	public void notifyOnStart(int numPlayers) {
		return;
	}

	
}
