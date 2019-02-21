package de.kreth.kata.spieldeslebens.lebewesen;

import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;
import de.kreth.kata.spieldeslebens.ozean.Point;

public abstract class AbstractFisch<THIS extends AbstractFisch<?, ?>, DIR extends RichtungGen> 
   extends AbstractLebewesen 
   implements Movable<THIS> {

   private final DIR richtungGen;
   
   public AbstractFisch(Point startPosition, DIR richtungGen) {
      super(startPosition);
      this.richtungGen = richtungGen;
   }

   @Override
   public THIS move() {
      Himmelsrichtung himmelsrichtung = richtungGen.next();
      Point newPosition = currentPosition().move(himmelsrichtung);
      return createWithStartPoint(newPosition, richtungGen);
   }

   protected abstract THIS createWithStartPoint(Point startpoint, DIR richtungGen);
}
