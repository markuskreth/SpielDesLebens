package de.kreth.kata.spieldeslebens.lebewesen;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Fisch extends AbstractFisch<Fisch, RichtungGenWithPreferedDirection> {

   public Fisch(Point startPosition) {
      super(startPosition, new RichtungGenWithPreferedDirection());
   }

   public Fisch(Point startPosition, RichtungGenWithPreferedDirection richtungGen) {
      super(startPosition, richtungGen);
   }

   @Override
   protected Fisch createWithStartPoint(Point startpoint, RichtungGenWithPreferedDirection richtungGen) {
      return new Fisch(startpoint, new RichtungGenWithPreferedDirection(richtungGen));
   }

   @Override
   public String toString() {
      return "Fisch [" + super.toString() + "]";
   }

}
