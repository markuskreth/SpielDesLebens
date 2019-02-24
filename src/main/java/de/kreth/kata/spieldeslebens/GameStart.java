package de.kreth.kata.spieldeslebens;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.items.Fisch;
import de.kreth.kata.spieldeslebens.items.Hai;
import de.kreth.kata.spieldeslebens.ozean.Point;
import de.kreth.kata.spieldeslebens.swing.GameFrame;

public class GameStart {

	private static final Logger LOGGER = LogManager.getLogger(GameStart.class);

	public static void main(final String[] args) {

		LOGGER.trace("trace");
		LOGGER.debug("debug");
		LOGGER.info("info");
		LOGGER.warn("warn");

		Board b = new Board();
		Fisch fisch = new Fisch(new Point(20, 20));
		b.add(fisch);
//		fisch = new Fisch(new Point(10, 20));
//		b.add(fisch);
		fisch = new Fisch(new Point(15, 15));
		b.add(fisch);
		b.add(new Hai(new Point(7, 7)));

		final GameFrame frame = new GameFrame(b);
		SwingUtilities.invokeLater(() -> frame.setVisible(true));

	}

}
