package view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controls.ControllerLocal;

@SuppressWarnings("serial")
public class SavePanel extends JPanel {

	public static final String LAYOUT_NAME = "SavePanel";
	
	private ControllerLocal controller;
	private MainWindow root;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 2);
	
	public SavePanel(MainWindow root, ControllerLocal controller) {
		super(new BorderLayout());
		this.controller = controller;
		this.root = root;
		root.add(this);
		initGUI();
	}
	
	/**Een este panel se realiza la acción de guardar la partida actual en un fichero que posteriormete pueda ser
	 * abierto, contiene un JTextField para poder darle un nombre reconocible a la partida y dos botones, cancel y continue,
	 * el primero para cancelar la acción de guardar y volver al panel de Settings y el continue para guardar la partida 
	 * y volver al panel de juego.
	 * */
	
	private void initGUI() {
		
		JLabel ini = new JLabel("Introduce the name of the game");
		ini.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 20));
		ini.setAlignmentX(Component.CENTER_ALIGNMENT);
		ini.setBackground(Color.white);
		ini.setMaximumSize(new Dimension(500, 30));
		ini.setPreferredSize(new Dimension(500, 30));
		
		JLabel ini2 = new JLabel("you want to save: ");
		ini2.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, 20));
		ini2.setAlignmentX(Component.CENTER_ALIGNMENT);
		ini2.setBackground(Color.white);
		ini2.setMaximumSize(new Dimension(500, 30));
		ini2.setPreferredSize(new Dimension(500, 30));
		
		JPanel text = new JPanel();
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS ));
		
		JTextField txt = new JTextField("");
		txt.setMaximumSize(new Dimension(500, 30));
		txt.setPreferredSize(new Dimension(500, 30));
		txt.setBackground(Color.white);
		
		text.add(ini);
		text.add(ini2);
		text.add(Box.createRigidArea(new Dimension(50, 30)));
		text.add(txt);
		text.setBackground(Color.white);
		
		
		ImageIcon imageIconCancel = new ImageIcon("Images/cancel.jpg");
		Image image  = imageIconCancel.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconCancel = new ImageIcon(image);
		
		JButton cancelButton = new JButton(imageIconCancel);
		cancelButton.setMaximumSize(new Dimension(160, 60));
		cancelButton.setPreferredSize(new Dimension(160, 60));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {					
				root.changePanel(MidSettingsPanel.LAYOUT_NAME);	
			}
		});
		
		ImageIcon imageIconContinue = new ImageIcon("Images/continue.JPG");
		Image image2  = imageIconContinue.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH);
		imageIconContinue = new ImageIcon(image2);
		
		JButton continueButton = new JButton(imageIconContinue);
		continueButton.setMaximumSize(new Dimension(160, 60));
		continueButton.setPreferredSize(new Dimension(160, 60));
		continueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {					
				if (txt.getText() == "") {
					JOptionPane.showMessageDialog(getParent(), "No name introduced");
				}
				else {
					String[] input = {"s", txt.getText()};
					controller.userAction(input);
					root.changePanel(MainGamePanel.LAYOUT_NAME);	
				}					
			}
		});
		
		
		JPanel buttons = new JPanel();
		String title = "Save Game";
		buttons.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title, TitledBorder.LEFT, 2));
		
		
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS ));
		buttons.add(Box.createRigidArea(new Dimension(50, 50)));
		buttons.add(text);
		buttons.add(Box.createRigidArea(new Dimension(50, 30)));
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
		options.add(cancelButton);
		options.add(Box.createRigidArea(new Dimension(150, 20)));
		options.add(continueButton);
		options.setBackground(Color.white);
		buttons.add(Box.createRigidArea(new Dimension(70, 70)));
		buttons.add(options, BorderLayout.SOUTH);
		buttons.setMaximumSize(new Dimension(800, 500));
		buttons.setPreferredSize(new Dimension(800, 500));

		JPanel generalPanel = new JPanel(new GridBagLayout());
		
		generalPanel.add(buttons);
		
		buttons.setBackground(Color.white);
		generalPanel.setBackground(Color.white);
		this.add(generalPanel, BorderLayout.CENTER);
		//this.add(Box.createRigidArea(new Dimension(1, 50)));
		
		
	}
	
}
