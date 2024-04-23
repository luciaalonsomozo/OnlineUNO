package main;

import javax.swing.SwingUtilities;

import controls.ControllerLocal;
import logic.Game;
import view.gui.MainWindow;
import view.printer.GamePrinter;

//Client (and server) main

/**
 * Esta clase representa el main de nuestro juego, tanto para en las partidas en red el servidor o el cliente, como
 * para el juego en local.
 * 
 */
public class Main {

	/**
	 * El método interpreta la entrada para detectar si se desea jugar por consola o por interfaz gráfica del usuario.
	 * @param args Opción que ha elegido el usuario
	 */
	public static void main(String[] args) {
		try {
			String mode = args[0];
			if ("cli".equals(mode)) {
				Game game = Game.getInstance();
				ControllerLocal controller = new ControllerLocal(game);
				GamePrinter view = new GamePrinter(controller);
				view.run();
			}
			else {
				SwingUtilities.invokeLater(() -> new MainWindow());
			}
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("No mode introduced. Default is GUI.");
			SwingUtilities.invokeLater(() -> new MainWindow());	
		}
	}
	
}
