package me.finnbueno.earthshove.util;

/**
 * @author Finn Bon
 */
public interface ConfigAdapter<T> {

	default String serialize(T object) {
		return object.toString();
	}

	T build(Object confValue);

}
