package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controls.ControllerLocal;
import controls.commads.Command;
import logic.Game;
import logic.GameStatus;
import logic.ObserverUNO;
import logic.gameobjects.Card;

/**
 * Clase servidor del juego. 
 * La clase es un hilo pues se ejecuta conjuntamente con un cliente que será el host. 
 * A su vez implementa un observador puesto que cuando el modelo notifique de algún cambio
 * el servidor deberá enviar la información referente a dicha notificación a sus clientes
 * para que estos a su vez se la envíen a sus observadores que, en la mayoría de ocasiones, 
 * serán sus vistas.
 *
 */
public class Server extends Thread implements ObserverUNO
{
	/**
	 * Nombre del usuario host.
	 */
	private String admin;
	/**
	 * Nº de jugadores, incluyendo IAs que no tienen cliente asociado.
	 */
	private int numberPlayers;
	/**
	 * Comprobación del final del servidor.
	 */
	private boolean end;
	/**
	 * Socket del servidor
	 */
	private ServerSocket listener;
	/**
	 * Lista con las dificultades de las IAs. 
	 * También es de gran importancia su tamaño para determinar 
	 * el nº de clientes.
	 */
	private List<String> AIHardness;
	/**
	 * Referencia al modelo
	 */
	private Game game;
	/**
	 * Controlador para facilitar el control del modelo
	 */
	private ControllerLocal c;
	/**
	 * Diccionario que asocia a un jugador el flujo de entrada de su cliente 
	 * en el servidor
	 */
	private Map<String, ObjectInputStream> insMap;
	/**
	 * Diccionario que asocia a un jugador el flujo de salida de su cliente 
	 * en el servidor
	 */
	private Map<String, ObjectOutputStream> outsMap;
	/**
	 * Lista con los flujos de entrada de los clientes. 
	 */
	private List<ObjectInputStream> insList;
	/**
	 * Lista con los flujos de salida de los clientes. 
	 */
	private List<ObjectOutputStream> outsList;
	/**
	 * Dirección IP que deben introduccir los clientes para unirse.
	 */
	private String ipAdress;
	
    /**
     * Establece el nº de usuarios y el nº y tipo de las IAs.
     * 
     * @param numPlayers Nº de jugadores totales.
     * @param AIHardness Tipo de IAs.
     */
    public Server(int numPlayers, List<String> AIHardness) {
    	this.numberPlayers = numPlayers;
    	this.AIHardness = AIHardness;
    	this.end = false;
    }
    
    /**
     * Método encargado de "escuchar" y luego ejecutar las acciones que los clientes 
     * desean, siempre y cuando sea su turno.
     */
    private void listen() {
    	while(!end) {
    		String currenPlayerName = c.getPlayer();
    		//See user action
    		if(c.currentPlayerHuman()) {
				try {
		    		String[] command = (String[]) insMap.get(currenPlayerName).readObject();
		    		
		    		if(Command.validNetCommand(command[0])) {
		    			c.userAction(command);
		    		}
		    		else {
		    			c.gameError("Introduce valid command");
		    		}
				} catch (Exception e) {
					errorEnd();
				}
    		}
    	}
    }
    
    /**
     * Método encargado de cerrar el servidor en caso de que algún cliente cierre
     * su conexión de manera abrupta.
     */
	private void errorEnd() {
		for (int i = 0; i < outsList.size(); i++) {
			try {
				//Send to only currentPlayer?
				outsList.get(i).writeObject("ERROR");
				outsList.get(i).writeObject("Game has finished caused by an error");
				outsList.get(i).flush();
				outsList.get(i).reset();
			} catch (IOException e) {
				System.out.println("Player " + i + " has left.");
			}
		}
		c.endGame();
	}

	@Override
	public void run()
    {
    	this.insMap = new HashMap<String, ObjectInputStream>();
    	this.outsMap = new HashMap<String, ObjectOutputStream>();
    	this.insList = new ArrayList<ObjectInputStream>();
        this.outsList = new ArrayList<ObjectOutputStream>();
        try 
        {
        	listener = new ServerSocket(8901, 2, InetAddress.getByName(null));
        	ipAdress = listener.getInetAddress().getHostName();
            System.out.println("UNO Server is Running in ip: " + listener.getInetAddress());
            List<String> playerNames = new ArrayList<String>();
            List<String> playerTypes = new ArrayList<String>();
            
            //Removed infinite loop
        	game = Game.reset();
        	game.addObserver(this);
        	this.c = new ControllerLocal(game);
        	
        	//Human players
        	for (int i = 0; i < numberPlayers - AIHardness.size(); i++) {
        		Socket auxSock = listener.accept();
        		boolean correctName = true;
        		ObjectInputStream in = new ObjectInputStream(auxSock.getInputStream());
        		ObjectOutputStream out = new ObjectOutputStream(auxSock.getOutputStream());
        		try {
					String name = (String) in.readObject();
					for (int ii = 0; ii < playerNames.size(); ii++) {
						if (playerNames.get(ii).equals(name)) {
							correctName = false;
						}
					}
					if (correctName) {
	            		insMap.put(name, in);
	            		insList.add(in);
	            		outsMap.put(name, out);
	            		outsList.add(out);
	            		playerNames.add(name);
	            		playerTypes.add("Human");
	            		System.out.println("Server accepted player " + name); 
					}
					else {
						i--;
						in.close();
						out.close();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
        	}
        	
        	for (int i = 0; i < AIHardness.size(); i++) {
        		String name = AIHardness.get(i) + " AI " + (i+1);
        		playerNames.add(name);
        		playerTypes.add(AIHardness.get(i));
        		System.out.println("Server added " + name);
        	}
        	
        	//First player is admin?
        	admin = playerNames.get(0);
        	
        	c.initialize(playerNames, playerTypes);
        	listen();
        } 
        catch(IOException e)
        {
        	e.printStackTrace();
        }
        finally 
        {
        	try 
        	{
        		if(listener != null)
        			listener.close();
    		} catch (IOException e) {}
        }
    }

	@Override
	public void notifyOnStart(int numPlayers) {
		//Nothing
	}

	@Override
	public void notifyOnLoad(List<String> savesNames) {
		//Nothing
	}

	@Override
	public void notifyOnUpdate(GameStatus g) {
		//Send game
		for (int i = 0; i < outsList.size(); i++) {
			try {
				outsList.get(i).writeObject("UPDATE");
				outsList.get(i).writeObject(g);
				outsList.get(i).flush();
				outsList.get(i).reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void notifyOnSave() {
		//Nothing
	}
	
	public void closeServer() {
	  	for (int i = 0; i < insList.size(); i++) {
	  		try {
				insList.get(i).close();
				outsList.get(i).close();
			} catch (Exception e) {
				//
			}
	   	}
	   	try {
			listener.close();
		} catch (Exception e) {
	   		//
		}
	   	System.out.println("Server closed");
    	end = true;
    }

	@Override
	public void notifyOnEnd(List<String> winnersNames) {
		//Send winners
		for (int i = 0; i < outsList.size(); i++) {
			try {
				outsList.get(i).writeObject("END");
				outsList.get(i).writeObject(winnersNames);
				outsList.get(i).flush();
				outsList.get(i).reset();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			insMap.get(admin).readObject();
		} catch (Exception e) {
			closeServer();
		}
		closeServer();
	}

	@Override
	public void notifyOnError(String error) {
		//Send error
		try {
			if(c.currentPlayerHuman()) {
				outsMap.get(c.getPlayer()).writeObject("ERROR");
				outsMap.get(c.getPlayer()).writeObject(error);
				outsMap.get(c.getPlayer()).flush();
				outsMap.get(c.getPlayer()).reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método para devolver la dirección a la que el cliente debe unirse.
	 * 
	 * @return Dirección IP.
	 */
	public String getIpHost() {
		return ipAdress;
	}
	
	/**
     * Método main para testear el servidor.
     * 
     */
    public static void main(String[] args) throws Exception 
    {
    	Server server = new Server(2, new ArrayList<String>());
    	server.start();
    }

}