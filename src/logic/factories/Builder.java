package logic.factories;

import org.json.JSONObject;

/**
 * Clase abstracta de los constructores para permitir generalizarlo a distintos tipos.
 *
 * @param <T> Tipo de objeto a construir.
 */
public abstract class Builder<T> {
	/**
	 * Tipo de objecto que el construtor trata.
	 */
	protected String _type;

	/**
	 * Inicializa el tipo a la entrada. Si no hay entrada lanza una excepción.
	 * 
	 * @param type Tipo que el constructor tratará.
	 */
	Builder(String type) {
		if (type == null) {
			throw new IllegalArgumentException("Invalid type: " + type);
		}
		else {
			_type = type;
		}
	}

	/**
	 * Crea, si es posible, una instancia del objeto de su tipo en base a un JSON de entrada.
	 * 
	 * @param info JSON de entrada que servirá para construir el objecto.
	 * @return Instancia del objeto creado o, si no es posible crearlo, null.
	 */
	public T createInstance(JSONObject info) {

		T b = null;

		if (_type != null && _type.equals(info.getString("type"))) {
			b = createTheInstance(info.has("data") ? info.getJSONObject("data") : new JSONObject());
		}

		return b;
	}

	/**
	 * Crea la instancia del objecto.
	 * 
	 * @param data JSON con los datos para construir el objecto, es decir, completar los parámetros de entrada 
	 * 			   del constructor del objeto.
	 * @return Instancia del objeto.
	 */
	protected abstract T createTheInstance(JSONObject data);
}
