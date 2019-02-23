package de.kreth.kata.spieldeslebens.lebewesen;

import java.util.Random;
import java.util.function.IntSupplier;

import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;

public class RichtungGen {

	private final IntSupplier rand;

	/**
	 * {@link RichtungGen} mit gleicher Wahrscheinlichkeit aller {@link Himmelsrichtung}en
	 */
	public RichtungGen() {
		this(() -> {
			final Random random = new Random();
			return random.nextInt(Himmelsrichtung.values().length);
		});
	}

	/**
	 * 
	 * @param rand Muss gueltige Zahl generierten, die eindeutig in eine {@link Himmelsrichtung} gewandelt werden kann.
	 */
	RichtungGen(final IntSupplier rand) {
		super();
		this.rand = rand;
	}

	public Himmelsrichtung next() {
		return next(rand.getAsInt());
	}

	protected Himmelsrichtung next(final int index) {
		return Himmelsrichtung.values()[index];
	}

}
