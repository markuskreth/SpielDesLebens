package de.kreth.kata.spieldeslebens.events;

@FunctionalInterface
public interface ItemListener {

	void itemChange(ItemEvent<?> ev);

}
