package de.kreth.kata.spieldeslebens.items;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;
import de.kreth.kata.spieldeslebens.ozean.Point;

public abstract class AbstractFisch<THIS extends AbstractFisch<?, ?>, DIR extends RichtungGen>
		extends AbstractLebewesen<THIS> implements Movable<THIS>, Fortpflanzend<THIS> {

	private final DIR richtungGen;

	public AbstractFisch(final Point startPosition, final DIR richtungGen) {
		super(startPosition);
		this.richtungGen = richtungGen;
	}

	protected abstract THIS createWithStartPoint(Point startpoint, DIR richtungGen);

	@SuppressWarnings("unchecked")
	@Override
	public THIS move(final Predicate<Point> veto) {
		final Himmelsrichtung himmelsrichtung = richtungGen.next();
		final Point newPosition = currentPosition().move(himmelsrichtung);
		if (!veto.test(newPosition)) {
			return createWithStartPoint(newPosition, richtungGen);
		}
		else {
			return (THIS) this;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<THIS> fortpflanzen() {
		final List<THIS> kinder = new ArrayList<>();

		final BigInteger[] divided = BigInteger.valueOf(weight).divideAndRemainder(BigInteger.TWO);
		THIS copy = this.copy();
		copy.weight = divided[0].intValue();

		kinder.add(copy);

		if (divided[1].equals(BigInteger.ZERO)) {
			this.weight = divided[0].intValue();
		}
		else {
			this.weight = divided[0].intValue() + 1;
		}
		kinder.add((THIS) this);
		return kinder;
	}

	protected DIR getRichtungGen() {
		return richtungGen;
	}

	protected abstract THIS copy();
}
