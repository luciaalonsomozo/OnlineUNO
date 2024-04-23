package view.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controls.Controller;
import controls.ControllerLocal;
import controls.ControllerNet;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	public static final String FONT_NAME = "Microsoft YaHei";
	
	private Controller controller = null;
	private CardLayout layout;
	private JPanel mainPanel;
	
	/**Inicializa la vista estableciendo el fondo blanco sobre el que se pintarón el resto de pantallas
	 * y mostrando en primer lugar el panel inicial*/
	private void initGUI() {
		//Set fullscreen
		this.setBackground(Color.WHITE);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    
	    //Other options
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		//Start panel
		InitialPanel iniPanel = new InitialPanel(this);
		layout.addLayoutComponent(iniPanel, InitialPanel.LAYOUT_NAME);
		
	}
	/**Se encarga de mostrar el panel correspondiente en cada momento
	 * */
	protected void changePanel(String panelName) {
		layout.show(mainPanel, panelName);
	}	
	/**
	 * Establece el controller del juego
	 * */
	protected void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**Crea los paneles necesarios para la vista cuando se utiliza un controlador local, es decir, todos los jugadores
	 * desde un mismo pc y no mediante la red.*/
	protected void createLocalPanels() {
		//New game(1): Number and name of players panels
		NumPlayersPanel numplayerspanel = new NumPlayersPanel(this, (ControllerLocal) controller);
		layout.addLayoutComponent(numplayerspanel, NumPlayersPanel.LAYOUT_NAME);
				
		UsersNamePanel usersPanel = new UsersNamePanel(this, (ControllerLocal) controller);
		layout.addLayoutComponent(usersPanel, UsersNamePanel.LAYOUT_NAME);
				
				
		//Save panel
		SavePanel savePanel = new SavePanel(this, (ControllerLocal) controller);
		layout.addLayoutComponent(savePanel, SavePanel.LAYOUT_NAME);
		
		
		//Load panel 
		LoadPanel loadPanel = new LoadPanel(this, (ControllerLocal) controller);
		layout.addLayoutComponent(loadPanel, LoadPanel.LAYOUT_NAME);
					
		createOtherPanels();
	}
	/**Crea los paneles que pueden ser utilizados tanto en el ControllerLocal como en el ControllerNet*/
	protected void createOtherPanels() {		
		//Settings panel
		MidSettingsPanel midSettingsPanel = new MidSettingsPanel(this, controller);
		layout.addLayoutComponent(midSettingsPanel, MidSettingsPanel.LAYOUT_NAME);
		
		//Main game panel
		MainGamePanel mGPanel = new MainGamePanel(this, controller);
		layout.addLayoutComponent(mGPanel, MainGamePanel.LAYOUT_NAME);

		
		//End game panel
		EndPanel endGame = new EndPanel(this, controller);
		layout.addLayoutComponent(endGame, EndPanel.LAYOUT_NAME);
	}
	
	/**Inicializa una pantalla general en la que irán apareciendo los diferentes paneles que hayan sido creados,
	 * adem�s inicia la GUI para poder pintar el panel inicial*/
	public MainWindow() {
		super("UNO");
		this.layout = new CardLayout();
		this.mainPanel = new JPanel(layout);
		setContentPane(mainPanel); 
		initGUI();
	}

	
}
