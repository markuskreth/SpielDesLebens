package de.kreth.kata.spieldeslebens.ozean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PointTransitionTest {

	@Test
	void movementDoesntChangeOrigin() {

		final Point p = new Point();
		assertEquals(0, p.getX());
		assertEquals(0, p.getY());
		int multiplicator = 1;
		for (Himmelsrichtung richtung : Himmelsrichtung.values()) {
			for (int i = 0; i < multiplicator; i++) {
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

		Point point = p.move(Himmelsrichtung.NORDEN);
		assertEquals(0, point.getX());
		assertEquals(-1, point.getY());

		point = p.move(Himmelsrichtung.NORD_OST);
		assertEquals(1, point.getX());
		assertEquals(-1, point.getY());

		point = p.move(Himmelsrichtung.OSTEN);
		assertEquals(1, point.getX());
		assertEquals(0, point.getY());

		point = p.move(Himmelsrichtung.SUED_OST);
		assertEquals(1, point.getX());
		assertEquals(1, point.getY());

		point = p.move(Himmelsrichtung.SUEDEN);
		assertEquals(0, point.getX());
		assertEquals(1, point.getY());

		point = p.move(Himmelsrichtung.SUED_WEST);
		assertEquals(-1, point.getX());
		assertEquals(1, point.getY());

		point = p.move(Himmelsrichtung.WESTEN);
		assertEquals(-1, point.getX());
		assertEquals(0, point.getY());

		point = p.move(Himmelsrichtung.NORD_WEST);
		assertEquals(-1, point.getX());
		assertEquals(-1, point.getY());
	}

	@Test
	void testPointRecovery() {
		Point p = new Point();
		assertEquals(0, p.getX());
		assertEquals(0, p.getY());

		Point point = p.move(Himmelsrichtung.NORDEN);
		assertEquals(Himmelsrichtung.NORDEN, p.to(point));

		point = p.move(Himmelsrichtung.SUEDEN);
		assertEquals(Himmelsrichtung.SUEDEN, p.to(point));

		point = p.move(Himmelsrichtung.WESTEN);
		assertEquals(Himmelsrichtung.WESTEN, p.to(point));

		point = p.move(Himmelsrichtung.OSTEN);
		assertEquals(Himmelsrichtung.OSTEN, p.to(point));

		point = p.move(Himmelsrichtung.NORD_OST);
		assertEquals(Himmelsrichtung.NORD_OST, p.to(point));

		point = p.move(Himmelsrichtung.NORD_WEST);
		assertEquals(Himmelsrichtung.NORD_WEST, p.to(point));

		point = p.move(Himmelsrichtung.SUED_OST);
		assertEquals(Himmelsrichtung.SUED_OST, p.to(point));

		point = p.move(Himmelsrichtung.SUED_WEST);
		assertEquals(Himmelsrichtung.SUED_WEST, p.to(point));

	}

}
