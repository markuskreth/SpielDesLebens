package de.kreth.kata.spieldeslebens.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;

public class RichtungGenWithPreferedDirection extends RichtungGen {

	@SafeVarargs
	private static <E> List<E> join(final List<E> list, final E... preferedDirections) {
		List<E> result = new ArrayList<>(list);
		result.addAll(Arrays.asList(preferedDirections));
		return result;
	}

	private final List<Himmelsrichtung> preferedDirections;

	public RichtungGenWithPreferedDirection() {
		this(Collections.emptyList());
	}

	RichtungGenWithPreferedDirection(final List<Himmelsrichtung> preferedDirections) {
		super(() -> {
			final Random random = new Random();
			return random.nextInt(Himmelsrichtung.values().length + preferedDirections.size() - 1);
		});
		this.preferedDirections = Collections.unmodifiableList(new ArrayList<>(preferedDirections));
	}

	RichtungGenWithPreferedDirection(final RichtungGenWithPreferedDirection old) {
		this(join(old.preferedDirections, new RichtungGen().next()));
	}

	@Override
	protected Himmelsrichtung next(final int index) {
		if (index < Himmelsrichtung.values().length) {
			return super.next(index);
		}
		return preferedDirections.get(index - Himmelsrichtung.values().length);
	}
}
