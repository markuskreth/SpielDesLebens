package de.kreth.kata.spieldeslebens.items;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Fisch extends AbstractFisch<Fisch, RichtungGenWithPreferedDirection> {

	public Fisch(final Point startPosition) {
		super(startPosition, new RichtungGenWithPreferedDirection());
	}

	public Fisch(final Point startPosition, final RichtungGenWithPreferedDirection richtungGen) {
		super(startPosition, richtungGen);
	}

	@Override
	protected Fisch createWithStartPoint(final Point startpoint, final RichtungGenWithPreferedDirection richtungGen) {
		return new Fisch(startpoint, new RichtungGenWithPreferedDirection(richtungGen));
	}

	@Override
	public String toString() {
		return "Fisch [" + super.toString() + "]";
	}

	@Override
	protected Fisch copy() {
		return createWithStartPoint(this.currentPosition(), getRichtungGen());
	}

}
