package de.kreth.kata.spieldeslebens.items;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Felsen implements WithPosition {

	private final Point position;

	public Felsen(Point position) {
		super();
		this.position = position;
	}

	@Override
	public Point currentPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		Felsen other = (Felsen) obj;
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		}
		else if (!position.equals(other.position)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Felsen [" + position + "]";
	}

}
