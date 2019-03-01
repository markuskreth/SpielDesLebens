package de.kreth.kata.spieldeslebens.items;

import java.util.function.Predicate;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Hai extends AbstractFisch<Hai, RichtungGen> {

	private AbstractLebewesen<?> eaten = null;

	public Hai(final Point startPosition) {
		this(startPosition, new RichtungGen());
	}

	private Hai(final Point startPosition, final RichtungGen richtungGen) {
		super(startPosition, richtungGen);
		weight = 5;
	}

	@Override
	public void eat(AbstractLebewesen<?> lebewesen) {
		super.eat(lebewesen);
		this.eaten = lebewesen;
	}

	@Override
	public Hai move(Predicate<Point> veto) {
		if (eaten == null) {
			return super.move(veto);
		}
		else {
			Hai createWithStartPoint = createWithStartPoint(eaten.currentPosition(), getRichtungGen());
			eaten = null;
			return createWithStartPoint;
		}
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
