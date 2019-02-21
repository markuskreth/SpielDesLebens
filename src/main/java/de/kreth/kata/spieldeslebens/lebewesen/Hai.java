package de.kreth.kata.spieldeslebens.lebewesen;

import de.kreth.kata.spieldeslebens.ozean.Point;

public class Hai extends AbstractFisch<Hai, RichtungGen> {

   public Hai(Point startPosition) {
      super(startPosition, new RichtungGen());
   }

   private Hai(Point startPosition, RichtungGen richtungGen) {
      super(startPosition, richtungGen);
   }

   @Override
   protected Hai createWithStartPoint(Point startpoint, RichtungGen richtungGen) {
      return new Hai(startpoint, richtungGen);
   }

   @Override
   public String toString() {
      return "Hai [" + super.toString() + "]";
   }

}
