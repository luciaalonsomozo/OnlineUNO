package view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controls.Controller;
import logic.Game;
import logic.ObserverUNO;
import logic.gameobjects.Card;
import logic.gameobjects.EasyPlayer;
import logic.gameobjects.HardPlayer;
import logic.gameobjects.HumanPlayer;
import logic.gameobjects.MediumPlayer;
import logic.gameobjects.PlayerList;
import logic.gameobjects.PlayerStatus;
import logic.gameobjects.PlayerStrategy;

public class ChangeIADialog extends JDialog{

	private static final String message = "There aren't any AI players";
	private List<PlayerStatus> players;
	
	private Controller controller;
	
	public ChangeIADialog(JFrame frame, List<PlayerStatus> players, Controller controller) {
		super(frame, "Change difficulty of AI players");
		this.players = players;
		this.controller = controller;
		setModal(true);
		initGUI();
		pack();
		setVisible(true);
	}
	
	/**Di�logo que se abre en el panel de Settings cuando se selecciona el bot�n de cambiar la inteligencia artificial, 
	 * Al abrirse initGUI a�ade al dialogo X cajas de selecci�n en las que aparecen todas los tipos de player, (siendo X el n�mero 
	 * de jugadores de la partida actual), adem�s teine dos botones cancel (que cierra el di�logo sin realizar cambios) y accept (que cierra
	 * el di�logo realizando los cambios)*/
	private void initGUI() {
		this.setLocationRelativeTo(null);
		this.setPreferredSize(new Dimension(400, 400));
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		List<JComboBox<PlayerStrategy>> jcombos = new ArrayList<JComboBox<PlayerStrategy>>();
		
		add(new JLabel("Select the AI of the players: "), BorderLayout.NORTH);
		
		JLabel medium = new JLabel();
		medium.setLayout(new BoxLayout(medium, BoxLayout.Y_AXIS));
		
		for(int i = 0; i < players.size(); i++) {				
			JPanel auxBig = new JPanel();
			auxBig.setLayout(new BoxLayout(auxBig, BoxLayout.X_AXIS));
			JLabel aux = new JLabel(players.get(i).getName());
			auxBig.add(aux);
			Vector<PlayerStrategy> typesOfAI = new Vector<PlayerStrategy>();

			typesOfAI.add(new HumanPlayer());
			typesOfAI.add(new EasyPlayer());
			typesOfAI.add(new MediumPlayer());
			typesOfAI.add(new HardPlayer());
			
			JComboBox<PlayerStrategy> AISelection = new JComboBox<PlayerStrategy>(typesOfAI);
			int index;
			String pl = players.get(i).getStrategyString();
			if(pl.equals(HumanPlayer.TYPE))
				index = 0;
			else if(pl.equals(EasyPlayer.TYPE))
				index = 1;
			else if(pl.equals(MediumPlayer.TYPE)) 
				index = 2;
			else 
				index = 3;
			
			
			AISelection.setSelectedIndex(index);
			
			jcombos.add(AISelection);
			auxBig.add(AISelection);
			medium.add(auxBig);
			
		}
		medium.setVisible(true);
		add(medium, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel();
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((ActionEvent e) -> {
			setVisible(false);
			dispose();
		});
		buttons.add(cancelButton);
		
		JButton acceptButton = new JButton("Accept");
		buttons.add(acceptButton);
		acceptButton.addActionListener((ActionEvent e) -> {
			for(int i = 0; i < jcombos.size(); i++) {
				controller.changeIA(players.get(i), (PlayerStrategy) jcombos.get(i).getSelectedItem());
			}				
			setVisible(false);
			dispose();
		});
		add(buttons, BorderLayout.SOUTH);
	}
}
