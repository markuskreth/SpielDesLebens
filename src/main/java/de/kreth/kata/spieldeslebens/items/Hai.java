package de.kreth.kata.spieldeslebens.items;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Hai extends AbstractFisch<Hai, RichtungGen> {

	public Hai(final Point startPosition) {
		super(startPosition, new RichtungGen());
	}

	private Hai(final Point startPosition, final RichtungGen richtungGen) {
		super(startPosition, richtungGen);
	}

	@Override
	protected Hai createWithStartPoint(final Point startpoint, final RichtungGen richtungGen) {
		return new Hai(startpoint, richtungGen);
	}

	@Override
	public String toString() {
		return "Hai [" + super.toString() + "]";
	}

	@Override
	protected Hai copy() {
		return createWithStartPoint(this.currentPosition(), getRichtungGen());
	}

}
