package de.kreth.kata.spieldeslebens.items;

import java.util.List;

public interface Fortpflanzend<T> extends Cloneable {

	/**
	 * Liefert alle entstandenen Nachkommen eines Lebewesens durch einen Fortpflanzungsmechanismus.
	 * 
	 * @return
	 */
	List<T> fortpflanzen();

}
