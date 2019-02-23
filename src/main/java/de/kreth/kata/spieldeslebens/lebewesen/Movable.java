package de.kreth.kata.spieldeslebens.lebewesen;

import java.util.function.Predicate;

import de.kreth.kata.spieldeslebens.ozean.Point;

public interface Movable<T extends Movable<?>> extends WithPosition {
	T move(Predicate<Point> veto);
}
