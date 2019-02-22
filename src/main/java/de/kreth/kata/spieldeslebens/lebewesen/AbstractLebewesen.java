package de.kreth.kata.spieldeslebens.lebewesen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.kreth.kata.spieldeslebens.ozean.Point;

public abstract class AbstractLebewesen<THIS extends AbstractLebewesen<?>> implements WithPosition {

   private final Point currentPosition;
   private int weight;
   
   public AbstractLebewesen(Point startPosition) {
      super();
      this.currentPosition = startPosition;
   }

   @Override
   public Point currentPosition() {
      return currentPosition;
   }

   public void decreaseWeight() {
      weight--;
   }
   
   public void eat(AbstractLebewesen<?> lebewesen) {
      weight += lebewesen.weight;
      lebewesen.weight = 0;
   }
   
   public List<THIS> fortpflanzen() {
      List<THIS> kinder = new ArrayList<>();
      
      BigInteger[] divided = BigInteger.valueOf(weight).divideAndRemainder(BigInteger.TWO);

      kinder.add(createWithWeight(divided[0].intValue()));
      
      if (divided[1].equals(BigInteger.ZERO)) {
         kinder.add(createWithWeight(divided[0].intValue()));
      } else {
         kinder.add(createWithWeight(divided[0].intValue() + 1));
      }
      this.weight = 0;
      return kinder;
   }

   protected abstract THIS createWithWeight(int weight);
   
   public boolean isAlive() {
      return weight > 0;
   }

   @Override
   public String toString() {
      return "currentPosition=" + currentPosition + ", weight=" + weight;
   }
}
