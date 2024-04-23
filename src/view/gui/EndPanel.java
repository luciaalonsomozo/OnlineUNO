package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import controls.Controller;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;

@SuppressWarnings("serial")
public class EndPanel extends JPanel implements ObserverUNO{

	public static final String LAYOUT_NAME = "EndPanel";

	private double PODIUM_TO_WINDOW = 1.1;
	private double PODIUM_PROPORTION = 1.5;

	private List<String> playerList;
	private MainWindow root;
	private Controller c;
	
	public EndPanel(MainWindow root, Controller c) {
		super(new BorderLayout());
		this.root = root;
		root.add(this);
		this.c = c;
		c.addObserver(this);
		initGUI();
	}
	/**
	 * Mediante la clase Graphics realizamos el podium de ganadores, primero ponemos como fondo del panel la foto de un 
	 * podium vacio, y después mediante las coordenadas ajustadas al tamaño de la pantalla de cada ordenador ponemos los
	 * nombres de usuario de los ganadores. Además, añadimos dos botones, uno de ellos que manda al usuario a la pantalla
	 * inicial del juego y el otro cierra la aplicación.
	 * */
	public void initGUI(){
		//repaint();
		//paintComponent(this.getGraphics());

		JPanel buttons = new JPanel();
		
		buttons.setBackground(Color.WHITE);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		//Exit button
		ImageIcon imageIconExit = new ImageIcon("Images/Exit.jpg");
		Image image2  = imageIconExit.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
		imageIconExit = new ImageIcon(image2);
		JButton exit = new JButton(imageIconExit);
//		exit.setMaximumSize(new Dimension((int) (this.getRootPane().getWidth()/8), this.getRootPane().getHeight()/13));
//		exit.setPreferredSize(new Dimension((int) (this.getRootPane().getWidth()/8), this.getRootPane().getHeight()/13));
		exit.setMaximumSize(new Dimension(180, 60));
		exit.setPreferredSize(new Dimension(180, 60));
		exit.setToolTipText("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
//		buttons.add(Box.createRigidArea(new Dimension((int) (this.getRootPane().getWidth()/3), this.getRootPane().getHeight()/20)));
		buttons.add(Box.createRigidArea(new Dimension(550, 60)));

		//exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(exit);	
//		buttons.add(Box.createRigidArea(new Dimension(this.getRootPane().getWidth()/10,this.getRootPane().getHeight()/10)));
		buttons.add(Box.createRigidArea(new Dimension(100, 100)));

		//Menu button
		
		ImageIcon imageIconMenu = new ImageIcon("Images/endgame.jpg");
		Image image4  = imageIconMenu.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
		imageIconMenu = new ImageIcon(image4);
		JButton menu = new JButton(imageIconMenu);
//		menu.setMaximumSize(new Dimension((int) (this.getRootPane().getWidth()/8), this.getRootPane().getHeight()/13));
//		menu.setPreferredSize(new Dimension((int) (this.getRootPane().getWidth()/8), this.getRootPane().getHeight()/13));
		menu.setMaximumSize(new Dimension(180, 60));
		menu.setPreferredSize(new Dimension(180, 60));
		menu.setToolTipText("End Game");
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.endGame();
				root.changePanel(InitialPanel.LAYOUT_NAME);				
			}	
		});

		buttons.add(menu);	
		this.add(buttons, BorderLayout.SOUTH);
	}
	
	/** 
	 * Carga las imágenes necesarias para cada uno de los iconos utilizados en los botones
	 * @param s nombre del archivo que contiene la imagen
	 * @return i objecto imagen resultante tras cargar
	 * */
	private Image loadImage(String s) {
		Image i = null;
		try {
			return ImageIO.read(new File("Images/" + s));
		}
		catch (IOException e){
			
		}
		return i;
	}
	
	/**Se encarga de la acción específica de pintar los nombres de usuario de los ganadores
	 * @param graphics utilizado para pintar
	 * */
	@Override 
	public void paintComponent(Graphics graphics) {
		if (playerList != null) {
			Graphics2D g = (Graphics2D) graphics;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
			// clear with a background color
			g.setBackground(Color.white);
			g.clearRect(0, 0, getRootPane().getWidth(), getRootPane().getHeight());
			
			Image podium = loadImage("podium.jpg");
			
			int height = (int) (getRootPane().getHeight() * PODIUM_TO_WINDOW);
			int width = (int) (height * PODIUM_PROPORTION);
			int xPos = (int) (getRootPane().getWidth() / 2 - width / 2);
			int yPos = (int) (getRootPane().getHeight() / 2 - height / 2);
			
			g.drawImage(podium, xPos, yPos, width, height, this);
			
			g.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, (int) (height * 0.05)));
			
			g.drawString(playerList.get(1), (int) (xPos + (width/4)), (int) (yPos + (height * 0.39)));
			g.drawString(playerList.get(0), (int) (xPos + (width/2)), (int) (yPos + (height * 0.27)));
			if (playerList.size() > 2) {
				g.drawString(playerList.get(2), (int) (xPos + (width * 0.64)),(int) (yPos + (height * 0.49)));
			}
		}
		
	}

	@Override
	public void notifyOnEnd(List<String> winnersNames) {
		this.playerList = winnersNames;
		initGUI();
	}

	@Override
	public void notifyOnStart(int numPlayers) {
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
		return;
	}

	@Override
	public void notifyOnSave() {
		return;
	}

}
