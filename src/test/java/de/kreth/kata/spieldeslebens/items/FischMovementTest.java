package de.kreth.kata.spieldeslebens.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.kreth.kata.spieldeslebens.ozean.Point;

class FischMovementTest {

	@BeforeEach
	void initTest() {

	}

	@Test
	void tesTransitionChangedt() {
		Fisch fisch = new Fisch(new Point());
		Fisch nextPoint = fisch.move(p -> false);
		assertNotNull(nextPoint);
		assertNotEquals(fisch.currentPosition(), nextPoint.currentPosition());

	}

	@Test
	void tesTransitionVeto() {
		Fisch fisch = new Fisch(new Point());
		Fisch nextPoint = fisch.move(p -> true);
		assertNotNull(nextPoint);
		assertEquals(fisch.currentPosition(), nextPoint.currentPosition());

	}

}
