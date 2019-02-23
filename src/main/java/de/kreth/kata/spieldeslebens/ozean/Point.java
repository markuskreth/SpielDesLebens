package de.kreth.kata.spieldeslebens.ozean;

public class Point {

	private final int x;

	private final int y;

	/**
	 * Default Koordinate 0,0
	 */
	public Point() {
		this(0, 0);
	}

	public Point(final int x, final int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Point other = (Point) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	public Point move(final Himmelsrichtung richtung) {
		int x = this.x;
		int y = this.y;
		switch (richtung) {
		case NORDEN:
			y--;
			break;
		case NORD_OST:
			y--;
			x++;
			break;
		case NORD_WEST:
			y--;
			x--;
			break;
		case OSTEN:
			x++;
			break;
		case SUEDEN:
			y++;
			break;
		case SUED_OST:
			y++;
			x++;
			break;
		case SUED_WEST:
			y++;
			x--;
			break;
		case WESTEN:
			x--;
			break;
		default:
			break;

		}
		return new Point(x, y);
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

}
