package logic;

/**
 * Interfaz del observador del patrón Observador.
 *
 */
public interface ObservableUNO {

	/**
	 * Añade un observador al conjunto de observadores.
	 * 
	 * @param obs Observador a añadir.
	 */
	public void addObserver(ObserverUNO obs);
	
	/**
	 * Elimina un observador del conjunto de observadores.
	 * 
	 * @param obs Observador a eliminar.
	 */
	public void removeObserver(ObserverUNO obs);
	
}
