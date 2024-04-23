package view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controls.Controller;
import controls.ControllerNet;
import logic.gameobjects.ColorUNO;
import main.Server;

@SuppressWarnings("serial")
public class NetName extends JDialog {
	
	private ControllerNet c;
	private Server s;
	boolean ok = false;

	public NetName(JFrame frame, ControllerNet c, Server s) {
		super(frame, "Join online game");
		this.c = c;
		this.s = s;
		setModal(true);
		initGUI();
	}
	
	/**Este diálogo se abre tras iniciar una sesión de juego online, únicamente tiene un JTextField en el que el usuario introduce
	 * el nombre que querrá usar en la partida. */
	private void initGUI() {		
		this.setLocationRelativeTo(null);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		add(new JLabel("Write your name: "));
		JTextField name = new JTextField();
		add(name);
		
		JPanel buttons = new JPanel(new BorderLayout());
		
		JButton acceptButton = new JButton("Accept");
		buttons.add(acceptButton);
		acceptButton.addActionListener((ActionEvent e) -> {
			if (!"".equals(name.getText())) { 
				ok = true;
				try {
					c.connectToServer(s.getIpHost(), name.getText());
					c.startReceiving();
					JOptionPane.showMessageDialog(getParent(), "The ip to connect is: " + s.getIpHost(), "IP INFORMATION", JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
					dispose();
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(getParent(), "ERROR CONNECTING TO SERVER", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(getParent(), "INTRODUCE A NAME", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		});
		add(buttons);

	}
	
	/**Devueleve un booleano según si se ha introducido un nombre no vacío.
	 * @return true si el nombre no es vacío
	 * @return false si el nombre es vacío*/
	public boolean getOK() {
		
		pack();
		setVisible(true);
		return ok;
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 200);
	}

}
