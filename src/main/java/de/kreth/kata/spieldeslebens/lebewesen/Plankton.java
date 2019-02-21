package de.kreth.kata.spieldeslebens.lebewesen;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Plankton extends AbstractLebewesen {

   public Plankton(Point startPosition) {
      super(startPosition);
   }

   @Override
   public void eat(AbstractLebewesen lebewesen) {
      // Plankton isst nicht.
   }

   @Override
   public String toString() {
      return "Plankton [" + super.toString() + "]";
   }

}
