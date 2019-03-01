package de.kreth.kata.spieldeslebens.items;

import de.kreth.kata.spieldeslebens.ozean.Point;

public abstract class AbstractLebewesen<THIS extends AbstractLebewesen<?>> implements WithPosition {

	private final Point currentPosition;

	protected int weight;

	public AbstractLebewesen(final Point startPosition) {
		super();
		this.currentPosition = startPosition;
		weight = 2;
	}

	@Override
	public Point currentPosition() {
		return currentPosition;
	}

	public void decreaseWeight() {
		weight--;
	}

	public void eat(final AbstractLebewesen<?> lebewesen) {
		weight += lebewesen.weight;
		lebewesen.weight = 0;
	}

	public boolean isAlive() {
		return weight > 0;
	}

	@Override
	public String toString() {
		return "currentPosition=" + currentPosition + ", weight=" + weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPosition == null) ? 0 : currentPosition.hashCode());
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractLebewesen<?> other = (AbstractLebewesen<?>) obj;
		if (currentPosition == null) {
			if (other.currentPosition != null) {
				return false;
			}
		}
		else if (!currentPosition.equals(other.currentPosition)) {
			return false;
		}
		if (weight != other.weight) {
			return false;
		}
		return true;
	}

}
