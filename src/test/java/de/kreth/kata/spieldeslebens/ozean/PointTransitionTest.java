package de.kreth.kata.spieldeslebens.ozean;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class PointTransitionTest {

   @Test
   void movementDoesntChangeOrigin() {

      final Point p = new Point();
      assertEquals(0, p.getX());
      assertEquals(0, p.getY());
      int multiplicator = 1;
      for (Himmelsrichtung richtung : Himmelsrichtung.values()) {
         for (int i=0; i<multiplicator; i++) {
            assertNotEquals(p, p.move(richtung));
         }
         multiplicator++;
         assertEquals(0, p.getX());
         assertEquals(0, p.getY());
      }
   }
   
   @Test
   void testPointMovement() {
      Point p = new Point();
      assertEquals(0, p.getX());
      assertEquals(0, p.getY());
      
      Point point = p.move (Himmelsrichtung.NORDEN);
      assertEquals(0, point.getX());
      assertEquals(-1, point.getY());
      
      point = p.move (Himmelsrichtung.NORD_OST);
      assertEquals(1, point.getX());
      assertEquals(-1, point.getY());
      
      point = p.move (Himmelsrichtung.OSTEN);
      assertEquals(1, point.getX());
      assertEquals(0, point.getY());
      
      point = p.move (Himmelsrichtung.SUED_OST);
      assertEquals(1, point.getX());
      assertEquals(1, point.getY());
      
      point = p.move (Himmelsrichtung.SUEDEN);
      assertEquals(0, point.getX());
      assertEquals(1, point.getY());
      
      point = p.move (Himmelsrichtung.SUED_WEST);
      assertEquals(-1, point.getX());
      assertEquals(1, point.getY());
      
      point = p.move (Himmelsrichtung.WESTEN);
      assertEquals(-1, point.getX());
      assertEquals(0, point.getY());
      
      point = p.move (Himmelsrichtung.NORD_WEST);
      assertEquals(-1, point.getX());
      assertEquals(-1, point.getY());
   }

}
