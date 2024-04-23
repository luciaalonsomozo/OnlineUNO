package logic.factories;

import org.json.JSONObject;

/**
 * Interfaz del factory method para crear un objeto en base a un JSON de entrada
 *
 * @param <T> Tipo, generalmente abstracto, de los objetos que se generarán por la factoría.
 */
public interface Factory<T> {
	
	public T createInstance(JSONObject info);

}
