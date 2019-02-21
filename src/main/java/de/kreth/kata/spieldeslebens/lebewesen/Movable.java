package de.kreth.kata.spieldeslebens.lebewesen;

public interface Movable<T extends Movable<?>> extends WithPosition {
   T move();
}
