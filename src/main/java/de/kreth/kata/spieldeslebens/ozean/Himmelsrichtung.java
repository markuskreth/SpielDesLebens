package de.kreth.kata.spieldeslebens.ozean;

public enum Himmelsrichtung {

	NORDEN(0), NORD_OST(45), OSTEN(90), SUED_OST(135), SUEDEN(180), SUED_WEST(225), WESTEN(270), NORD_WEST(315);

	private int degrees;

	private Himmelsrichtung(final int degrees) {
		this.degrees = degrees;
	}

	public int getDegrees() {
		return degrees;
	}

	@Override
	public String toString() {
		return name() + " " + degrees + "°";
	}
}
