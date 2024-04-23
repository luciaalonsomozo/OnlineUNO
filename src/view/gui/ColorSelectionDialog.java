package view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.gameobjects.ColorUNO;

@SuppressWarnings("serial")
public class ColorSelectionDialog extends JDialog {
	
	private String colorSelected;

	public ColorSelectionDialog(JFrame frame) {
		super(frame, "Select color");
		setModal(true);
		initGUI();
	}
	/**
	 * Este diálogo se abre durante una partida, cuando se lanza una carta en la que se tenga que seleccionar un color,
	 * es decir ChangeColorCard o Add4Card.
	 * Este diálogo contiene un JComoBox en el que se muestran los cuatro colores disponibles, además de dos botones, 
	 * cancel para poder seleccionar una carta distinta y accept para elegir el color seleccionado.*/
	private void initGUI() {		
		this.setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		add(new JLabel("Select the color of the card: "), BorderLayout.NORTH);
		Vector<String> colorNames = new Vector<String>();
		for (ColorUNO colorAux: ColorUNO.values()) {
			colorNames.add(colorAux.toString());
		}
		JComboBox<String> colorSelection = new JComboBox<String>(colorNames);
		add(colorSelection);
		
		JPanel buttons = new JPanel();
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((ActionEvent e) -> {
			colorSelected = null;
			setVisible(false);
			dispose();
		});
		buttons.add(cancelButton);
		
		JButton acceptButton = new JButton("Accept");
		buttons.add(acceptButton);
		acceptButton.addActionListener((ActionEvent e) -> {
			colorSelected = (String) colorSelection.getSelectedItem();
			setVisible(false);
			dispose();
		});
		add(buttons, BorderLayout.SOUTH);
	
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 200);
	}

	/**Devuelve el color seleccionado en el JComboBox
	 * @return color seleccionado*/
	public String askColor() {
		pack();
		setVisible(true);
		return colorSelected;
	}

}
