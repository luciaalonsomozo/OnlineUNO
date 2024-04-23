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

@SuppressWarnings("serial")
public class JoinNet extends JDialog {
	
	private boolean correctConnection;
	private ControllerNet c;

	public JoinNet(JFrame frame, ControllerNet c) {
		super(frame, "Join online game");
		this.c = c;
		setModal(true);
		initGUI();
	}

	/**Este dialogo es usado para que el usuario se conecte a una sala de juego ya creada, se le pide en dos JTextField
	 * que introduzca el ip de la partida a la que quiere acceder y un nombre de usuario que ser con el que se unir a la 
	 * partida. Si no existe una partida con el ip escrito se mostrar un mensaje de error, en caso contrario se abrir
	 * una pantalla en la que se esperar a que el número de jugadores necesario se conecte. */
	private void initGUI() {		
		this.setLocationRelativeTo(null);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		add(new JLabel("Select the ip of the game: "));
		JTextField ip = new JTextField();
		add(ip);
		
		add(new JLabel("Write your name: "));
		JTextField name = new JTextField();
		add(name);
		
		JPanel buttons = new JPanel(new BorderLayout());
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((ActionEvent e) -> {
			correctConnection = false;
			setVisible(false);
			dispose();
		});
		buttons.add(cancelButton);
		
		JButton acceptButton = new JButton("Accept");
		buttons.add(acceptButton);
		acceptButton.addActionListener((ActionEvent e) -> {
			if (!"".equals(ip.getText()) && !"".equals(name.getText())) { 
				try {
					c.connectToServer(ip.getText(), name.getText());
					c.startReceiving();
					correctConnection = true;
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
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 200);
	}
	
	/**checkConnection determina si se ha realizado conexión con algún server o no.
	 * @return true si la conexión ha sido realizada correctamente
	 * @return false si no ha sido posible conectarse a un servidor
	 * */
	public boolean checkConnection() {
		pack();
		setVisible(true);
		return correctConnection;
	}

}
