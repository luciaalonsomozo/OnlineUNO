package controls;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import controls.commads.HelpCommand;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;
import logic.gameobjects.Player;
import logic.gameobjects.PlayerStatus;
import logic.gameobjects.PlayerStrategy;

/**
 * Implementación del controlador para el juego en red. Su utilización consiste principalmente 
 * en la comunicación con el servidor, tanto el envío de la información referente a 
 * la acción realizada por el usuario de este cliente como "escuchar" las actualizaciones del 
 * servidor
 * 
 */
public class ControllerNet implements Controller {
	
	/**
	 * Socket del cliente conectado al servidor
	 */
	private Socket socket;
	/**
	 * Flujo de entrada de los objetos procedentes del servidor
	 */
	private ObjectInputStream in;
	/**
	 * Flujo de salida de los objetos con destino el servidor
	 */
	private ObjectOutputStream out;
	/**
	 * Cadena con el nombre del jugador asociado a este cliente
	 */
	private String playerName;
	/**
	 * Lista de observadores. Principalmente serán los elementos de la vista del cliente.
	 */
	private List<ObserverUNO> obs;

	/**
	 * Construye el cliente inicializando la lista de observadores a un ArrayList vacío.
	 */
	public ControllerNet() {
		this.obs = new ArrayList<ObserverUNO>();
	}
	
	/**
	 * Método encargado de recibir la información del servidor. 
	 * La información se divide en dos partes, en primer lugar, el servidor envía 
	 * la notificación que ha causado el envío del mensaje y, por otro lado, se 
	 * envía la información que los observadores del cliente requieren (que depende 
	 * del tipo de notificación).
	 * 
	 * Este método debe ejecutarse en un hilo diferente del principal debido a que las 
	 * notificaciones del servidor pueden llegar en cualquier momento.
	 * 
	 * En caso de que haya un error en el envío de los datos se cierra la conexión.
	 */
	@SuppressWarnings("unchecked")
	private void receiveData() {
		boolean end = false;
		try {
			do {
				String notify = (String) in.readObject();
				if (notify.equals("UPDATE")) {
					GameStatus game = (GameStatus) in.readObject();
					for (int i = 0; i < obs.size(); i++) {
						obs.get(i).notifyOnUpdate(game);
					}
				}
				else if(notify.equals("END")) {
					List<String> winners = new ArrayList<String>();
					winners = (List<String>) in.readObject();
					for (int i = 0; i < obs.size(); i++) {
						obs.get(i).notifyOnEnd(winners);
					}
					end = true;
				}
				else {
					//ERROR
					String error = (String) in.readObject();
					for (int i = 0; i < obs.size(); i++) {
						obs.get(i).notifyOnError(error);	
					}
				}
			} while(!end);
		}
		catch(Exception ex) {
			closeConnection();
		}
	}	

	/**
	 * Método encargado de cerrar la conexión con el servidor
	 * Notifica a sus observadores de que la conexión se ha perdido.
	 */
	private void closeConnection() {
		try {
	        in.close();
	        out.close();
	        socket.close();
	        for (int i = 0; i < obs.size(); i++) {
				obs.get(i).notifyOnError("Connection lost... Exit the current game.");	
			}
        } 
		catch(Exception e) {
		}	
	}

	/**
	 * Método encargado de realizar la conexión con el servidor.
	 * 
	 * @param ip Dirección IP a la que el cliente se ha de conectar
	 * @param name Nombre del jugador que se va a conectar al servidor
	 * @throws UnknownHostException Excepción lanzada en caso de que no sea posible encontrar el host
	 * @throws IOException Excepción lanzada en otro caso.
	 */
	public void connectToServer(String ip, String name) throws UnknownHostException, IOException
	{
        // Make connection and initialize streams
        //socket = new Socket(InetAddress.getByName("localhost"), 8901);
        socket = new Socket(InetAddress.getByName(ip), 8901);
        out = new ObjectOutputStream(socket.getOutputStream());
        playerName = name.toUpperCase();
        out.writeObject(playerName);
        in = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * Método encargado de iniciar el hilo para recibir información del servidor.
	 */
	public void startReceiving() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				receiveData();
			}
		});
		
		t.start();
	}
	
	@Override
	public void userAction(String[] input) {
		try {
			out.writeObject(input);
			out.reset();
		} catch (IOException e) {
			closeConnection();
		}
	}
	

	@Override
	public String getHelp() {
		return HelpCommand.getMessage();
	}

	@Override
	public void addObserver(ObserverUNO o) {
		obs.add(o);
	}

	@Override
	public String getPlayer() {
		return playerName;
	}

	@Override
	public void endGame() {
		closeConnection();
	}
	
	/**
	 * Método que muestra mensaje de error sobre intentar cambiar la inteligencia artifical en una partida en red.
	 */

	@Override
	public void changeIA(PlayerStatus playerStatus, PlayerStrategy st) {
	
		for (int i = 0; i < obs.size(); i++) {
			obs.get(i).notifyOnError("You can't changeAI in server mode. Start a new session.");	
		}
		
	}


}
