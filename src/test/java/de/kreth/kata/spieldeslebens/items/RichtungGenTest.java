package de.kreth.kata.spieldeslebens.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import de.kreth.kata.spieldeslebens.items.RichtungGen;
import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;


class RichtungGenTest {

   @Test
   void testDefaultRandomFindsAll() {
      RichtungGen gen = new RichtungGen();
      List<Himmelsrichtung> toFind = new ArrayList<>(Arrays.asList(Himmelsrichtung.values()));
      int count = 0;
      while (!toFind.isEmpty()) {
         Himmelsrichtung richtung = gen.next();
         assertNotNull(richtung);
         toFind.remove(richtung);
         count++;
      }
      assertTrue(count >= Himmelsrichtung.values().length); // Für den unwahrscheinlichen Fall, dass direkt jede Ziffer ohne Widerholung kommt.
   }

   @Test
   void testEachDirection() {

      final AtomicInteger rand = new AtomicInteger(0);
      RichtungGen gen = new RichtungGen(() -> rand.get());
      
      for (Himmelsrichtung richtung : Himmelsrichtung.values()) {
         rand.set(richtung.ordinal());
         Himmelsrichtung next = gen.next();
         assertNotNull(next);
         assertEquals(richtung, next);
      }
      
   }

}
