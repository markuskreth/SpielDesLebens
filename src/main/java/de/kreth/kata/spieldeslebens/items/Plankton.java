package de.kreth.kata.spieldeslebens.items;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Plankton extends AbstractLebewesen<Plankton> {

  public Plankton(final Point startPosition, int weight) {
    super(startPosition);
    this.weight = weight;
  }

  @Override
  public void eat(final AbstractLebewesen<?> lebewesen) {
    // Plankton isst nicht.
  }

  public int getWeight() {
    return weight;
  }

  @Override
  public String toString() {
    return "Plankton [" + super.toString() + "]";
  }

}
