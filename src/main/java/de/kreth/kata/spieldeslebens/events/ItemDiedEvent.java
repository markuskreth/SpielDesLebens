package de.kreth.kata.spieldeslebens.events;

import de.kreth.kata.spieldeslebens.items.WithPosition;

public class ItemDiedEvent<T extends WithPosition> extends ItemEvent<T> {

	public ItemDiedEvent(T item) {
		super(item);
	}

}
