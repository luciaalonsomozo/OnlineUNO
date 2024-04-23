package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import controls.Controller;
import controls.ControllerLocal;
import controls.ControllerNet;
import logic.Game;

@SuppressWarnings("serial")
public class InitialPanel extends JPanel {
	
	public static final String LAYOUT_NAME = "IniPanel";
	private Image uno;
	private MainWindow root;
	
	public InitialPanel(MainWindow root) {
		super(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.root = root;
		root.add(this);
		initGUI();
	}
	
	/** 
	 * Carga las imágenes necesarias para cada uno de los iconos utilizados en los botones
	 * @param img nombre del archivo que contiene la imagen
	 * @return i objecto imagen resultante tras cargar
	 * */
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("Images/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	/** 
	 *Se encarga de realizar la estructura general del panel inicial, poniendo el icono del UNO en función del tamaño de
	 *la pantalla y los 4 botones que realizan las acciones necesarias, todos ellos tienen una clase aparte que
	 *permite ejecutar su lógica, dos de ellos utilizan un diálogo y los otros dos mandan al usuario a los paneles
	 *correspondientes
	 * */
	public void initGUI() {
		
		uno = loadImage("uno.png");
		
		JPanel _uno = new JPanel();
		
		_uno.setBackground(Color.WHITE);
		_uno.setLayout(new BorderLayout());
		
		//ImageIcon imageIcon = new ImageIcon("Images/uno.png");
		//Image imageUNO = imageIcon.getImage().getScaledInstance((int) (root.getWidth()*8), (int) (root.getHeight()*14), Image.SCALE_SMOOTH);
		//imageIcon = new ImageIcon(imageUNO);
		
		Image image1  = uno.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
		ImageIcon imageIconUno = new ImageIcon(image1);
		
		JLabel unoLabel = new JLabel(imageIconUno);
		//JLabel unoLabel = new JLabel(imageIcon);
		
		_uno.add(unoLabel, BorderLayout.NORTH);
		
		JPanel buttons = new JPanel();
		buttons.setBackground(Color.WHITE);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		//
		ImageIcon imageIconNewGame = new ImageIcon("Images/newgame.JPG");
		Image image  = imageIconNewGame.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
		imageIconNewGame = new ImageIcon(image);
		
		JButton newGame = new JButton(imageIconNewGame);
		newGame.setMaximumSize(new Dimension(180, 60));
		newGame.setPreferredSize(new Dimension(180, 60));
		newGame.setToolTipText("Start new game");
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				root.setController(new ControllerLocal(Game.reset()));
				root.createLocalPanels();
				root.changePanel(NumPlayersPanel.LAYOUT_NAME);
			}
		});
		newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(newGame, BorderLayout.CENTER);
		
		buttons.add(Box.createRigidArea(new Dimension(10,10)));
		
		//new Load Button
		ImageIcon imageIconLoadGame = new ImageIcon("Images/loadgame.jpg");
		Image image2  = imageIconLoadGame.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
		imageIconLoadGame = new ImageIcon(image2);
		
		JButton load = new JButton(imageIconLoadGame);
		load.setMaximumSize(new Dimension(180, 60));
		load.setPreferredSize(new Dimension(180, 60));
		load.setToolTipText("Load a game");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControllerLocal c = new ControllerLocal(Game.reset());
				root.setController(c);
				root.createLocalPanels(); 
				if(c.wantToLoad()) {
					root.changePanel(LoadPanel.LAYOUT_NAME);
				}
			}
		});

		load.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(load);	
		
		buttons.add(Box.createRigidArea(new Dimension(10,10)));
		
		//new join Button
		ImageIcon imageIconServerGame = new ImageIcon("Images/session.jpg");
		Image imageServer  = imageIconServerGame.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
		imageIconServerGame = new ImageIcon(imageServer);
		
		JButton server = new JButton(imageIconServerGame);
		server.setMaximumSize(new Dimension(180, 60));
		server.setPreferredSize(new Dimension(180, 60));
		server.setToolTipText("Create an online session");
		server.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControllerNet c = new ControllerNet();
				root.setController(c);		
				root.createOtherPanels();
				MenuServerDialog mS = new MenuServerDialog((JFrame) root, c);
				if (mS.confirm()) {
					root.changePanel(MainGamePanel.LAYOUT_NAME);
				}
			}
		});

		server.setAlignmentX(Component.CENTER_ALIGNMENT);

		buttons.add(server);
		
		buttons.add(Box.createRigidArea(new Dimension(10,10)));

		//new join Button
		ImageIcon imageIconJoinGame = new ImageIcon("Images/Join.jpg");
		Image imageJoin  = imageIconJoinGame.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
		imageIconJoinGame = new ImageIcon(imageJoin);
		
		JButton join = new JButton(imageIconJoinGame);
		join.setMaximumSize(new Dimension(180, 60));
		join.setPreferredSize(new Dimension(180, 60));
		join.setToolTipText("Join a game");
		join.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControllerNet c = new ControllerNet();
				root.setController(c);		
				root.createOtherPanels();
				JoinNet joinNetDialog = new JoinNet((JFrame) root, c);
				if (joinNetDialog.checkConnection()) {
					root.changePanel(MainGamePanel.LAYOUT_NAME);
				}
			}
		});

		join.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(join);	
		buttons.add(Box.createRigidArea(new Dimension(40, 20)));
					
		_uno.add(buttons, BorderLayout.SOUTH);
		this.add(_uno, BorderLayout.CENTER);
	
	}
	
}
