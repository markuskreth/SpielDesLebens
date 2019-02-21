package de.kreth.kata.spieldeslebens.lebewesen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.kreth.kata.spieldeslebens.ozean.Point;


class FischMovementTest {

   @BeforeEach
   void initTest() {
      
   }
   
   @Test
   void test() {
      Fisch fisch = new Fisch(new Point());
      Fisch nextPoint = fisch.move();
      assertNotNull(nextPoint);
      
   }

}
