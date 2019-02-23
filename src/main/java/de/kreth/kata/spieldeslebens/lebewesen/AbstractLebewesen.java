package de.kreth.kata.spieldeslebens.lebewesen;

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
}
