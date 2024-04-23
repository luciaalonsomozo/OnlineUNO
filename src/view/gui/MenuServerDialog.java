package view.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

import controls.ControllerNet;
import logic.gameobjects.Player;
import main.Server;

public class MenuServerDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private static final String message = "There aren't any AI players";
	private JFrame frame;
	private ControllerNet c;
	private boolean confirm;
	
	public MenuServerDialog(JFrame frame, ControllerNet c) {
		super(frame, "Configurations of the game");
		this.frame = frame;
		this.c = c;
		this.confirm = false;
		setModal(true);
		initGUI();
	}
	
	/**Este diálogo se abre al pulsar el botón new Session en el panel inicial, en este aparece en la parte superior, 
	 * un JSpinner en el que se puede elegir el número de jugadores (de 2 a 4) y según lo seleccionado en este 
	 * apareceran debajo los correspondientes JComboBox para seleccionar el tipo de cada uno de ellos.
	 * Además, implementa dos botones, cancel, para cerrar el diálogo y volver al panel inicial y el botón accept para
	 * crear el servidor con las opciones selccionadas.*/
	private void initGUI() {
		this.setLocationRelativeTo(null);
		this.setPreferredSize(new Dimension(400, 400));
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		add(new JLabel("Configurate the game: "), BorderLayout.NORTH);
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		JPanel numPlayers = new JPanel();
		JLabel numPlayersText = new JLabel("Select the number of players");
		numPlayers.add(numPlayersText);
		
		JSpinner numPlayersSpinner = new JSpinner();
		numPlayersSpinner.setModel(new SpinnerNumberModel(2, 2, Player.MAX_PLAYERS, 1));
		numPlayers.add(numPlayersSpinner);
		content.add(numPlayers);
		
		JPanel medium = new JPanel();
		JPanel buttons = new JPanel();
		
		//Default number of players is 2
		addCenterStuff(content, medium, buttons, 2);
		
		JComponent comp = numPlayersSpinner.getEditor();
	    JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
	    DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
	    formatter.setCommitsOnValidEdit(true);
	    numPlayersSpinner.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	        	addCenterStuff(content, medium, buttons, (Integer) numPlayersSpinner.getValue());
	        	revalidate();
	        	repaint();
	        }
	    });

		add(content, BorderLayout.CENTER);
	
	}
	
	private void addCenterStuff(JPanel content, JPanel medium, JPanel buttons, Integer numberOfPlayers) {
		medium.removeAll();
		buttons.removeAll();
		medium.setLayout(new BoxLayout(medium, BoxLayout.Y_AXIS));
		
		List<JComboBox<String>> jcombos = new ArrayList<JComboBox<String>>();
		
		medium.add(new JLabel("Select the type of the players: "));
		
		for(int i = 1; i < numberOfPlayers; i++) {				
			JPanel auxBig = new JPanel();
			JLabel auxText = new JLabel("Player " + (i + 1));
			auxBig.add(auxText);
			Vector<String> typesOfAI = new Vector<String>();

			typesOfAI.add("Human");
			typesOfAI.add("Easy");
			typesOfAI.add("Medium");
			typesOfAI.add("Hard");
			
			JComboBox<String> AISelection = new JComboBox<String>(typesOfAI);
			
			jcombos.add(AISelection);
			auxBig.add(AISelection);
			medium.add(auxBig);
		}
		content.add(medium);

		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((ActionEvent ell) -> {
			setVisible(false);
			dispose();
		});
		buttons.add(cancelButton);
		
		JButton acceptButton = new JButton("Accept");
		buttons.add(acceptButton);
		acceptButton.addActionListener((ActionEvent el) -> {
			List<String> AIs = new ArrayList<String>();
			for(int i = 0; i < jcombos.size(); i++) {
				String strategy = (String) jcombos.get(i).getSelectedItem();
				if (!strategy.equals("Human")) {
					AIs.add(strategy);
				}
			}		
			
			
			
			//Initialize server
			Server ser = new Server(numberOfPlayers, AIs);
			ser.start();
			
			//Initialize client
			NetName net = new NetName(frame, c, ser);
			if(net.getOK()) {
				confirm = true;
			}
			else {
				ser.closeServer();
			}
			
			setVisible(false);
			dispose();
		});
		add(buttons, BorderLayout.SOUTH);
	}
	
	
	/**
	 * Devuelve un booleano según si se ha conseguido crear la red o no, esto depende de 
	 * si el nombre introducido es correcto o no
	 * @return true si se ha cargado correctamente la red
	 * @return false si no se ha conseguido cargar */
	public boolean confirm() {
		pack();
		setVisible(true);
		return confirm;
	}
}


