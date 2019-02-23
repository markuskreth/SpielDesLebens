package de.kreth.kata.spieldeslebens.events;

import java.util.Objects;
import java.util.Optional;

import de.kreth.kata.spieldeslebens.lebewesen.WithPosition;
import de.kreth.kata.spieldeslebens.ozean.Point;

/**
 * Die Position eines Lebewesens hat sich geändert. {@link #getOldPosition()} liefert kein Ergebnis, wenn das Objekt neu
 * entstanden ist.
 * @author markus
 *
 * @param <T>
 */
public class ItemPositionEvent<T extends WithPosition> extends ItemEvent<T> {

	private Optional<Point> oldPosition;

	public ItemPositionEvent(T item, Optional<Point> oldPosition) {
		super(item);
		Objects.requireNonNull(oldPosition);
		this.oldPosition = oldPosition;
	}

	public Optional<Point> getOldPosition() {
		return oldPosition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((oldPosition == null) ? 0 : oldPosition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ItemPositionEvent<?> other = (ItemPositionEvent<?>) obj;

		if (!oldPosition.equals(other.oldPosition)) {
			return false;
		}
		return true;
	}

}
