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
			THIS created = createWithStartPoint(newPosition, richtungGen);
			created.weight = this.weight;
			this.weight = 0;
			return created;
		}
		else {
			return (THIS) this;
		}
	}

	public boolean sollteFortpflanzen(int random, int fortpflanzen) {
		return weight > 3 && random <= fortpflanzen + weight;
	}

	@Override
	public List<THIS> fortpflanzen() {
		final List<THIS> kinder = new ArrayList<>();

		final BigInteger[] divided = BigInteger.valueOf(weight).divideAndRemainder(BigInteger.TWO);
		THIS copy = this.copy();
		copy.weight = divided[0].intValue();

		kinder.add(copy);
		copy = this.copy();

		if (divided[1].equals(BigInteger.ZERO)) {
			copy.weight = divided[0].intValue();
		}
		else {
			copy.weight = divided[0].intValue() + 1;
		}
		kinder.add(copy);
		return kinder;
	}

	protected DIR getRichtungGen() {
		return richtungGen;
	}

	protected abstract THIS copy();
}
