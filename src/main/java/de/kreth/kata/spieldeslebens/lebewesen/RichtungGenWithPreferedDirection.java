package de.kreth.kata.spieldeslebens.lebewesen;

import java.util.*;

import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;

public class RichtungGenWithPreferedDirection extends RichtungGen {

   private final List<Himmelsrichtung> preferedDirections;

   public RichtungGenWithPreferedDirection() {
      this(Collections.emptyList());
   }
   

   RichtungGenWithPreferedDirection(RichtungGenWithPreferedDirection old) {
      this(join(old.preferedDirections, new RichtungGen().next()));
   }
   
   @SafeVarargs
   private static <E> List<E> join(List<E> list, E... preferedDirections) {
      list.addAll(Arrays.asList(preferedDirections));
      return list;
   }
   
   RichtungGenWithPreferedDirection(List<Himmelsrichtung> preferedDirections) {
      super(() -> {
         Random random = new Random();
         return random.nextInt(Himmelsrichtung.values().length + preferedDirections.size());
      });
      this.preferedDirections = preferedDirections;
   }
   
   @Override
   protected Himmelsrichtung next(int index) {
      if (index < Himmelsrichtung.values().length) {
         return super.next(index);
      }
      return preferedDirections.get(index - Himmelsrichtung.values().length);
   }
}
