package de.kreth.kata.spieldeslebens.items;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.kreth.kata.spieldeslebens.items.RichtungGenWithPreferedDirection;
import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;


class RichtungGenWithPreferedDirectionTest {

   @Test
   void testNext() {
      RichtungGenWithPreferedDirection r = new RichtungGenWithPreferedDirection(
            Arrays.asList(Himmelsrichtung.NORDEN, Himmelsrichtung.NORDEN, Himmelsrichtung.SUEDEN));
      
      assertEquals(Himmelsrichtung.SUEDEN, r.next(Himmelsrichtung.values().length + 2));

      assertEquals(Himmelsrichtung.NORDEN, r.next(Himmelsrichtung.values().length + 1));

      assertEquals(Himmelsrichtung.NORDEN, r.next(Himmelsrichtung.values().length));

      assertEquals(Himmelsrichtung.NORD_WEST, r.next(Himmelsrichtung.values().length - 1));
      
      assertEquals(Himmelsrichtung.NORDEN, r.next(0));
   }

}
