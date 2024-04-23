package logic.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

/**
 * Implementación de la interfaz de la factoría basada en una lista de constructores.
 *
 * @param <T> Tipo de la factoría.
 */
public class BuilderBasedFactory<T> implements Factory<T> {

	/**
	 * Lista de constructores de la factoría.
	 */
	private List<Builder<T>> _builders;

	/**
	 * Se inicializa la lista de constructores.
	 * 
	 * @param builders Lista de constructores a utilizar.
	 */
	public BuilderBasedFactory(List<Builder<T>> builders) {
		_builders = new ArrayList<>(builders);
	}

	@Override
	public T createInstance(JSONObject info) {
		if (info != null) {
			for (Builder<T> bb : _builders) {
				T o = bb.createInstance(info);
				if (o != null)
					return o;
			}
		}

		throw new IllegalArgumentException("Invalid value for createInstance: " + info);
	}
}
