package de.kreth.kata.spieldeslebens;

import javax.swing.SwingUtilities;

import de.kreth.kata.spieldeslebens.lebewesen.Fisch;
import de.kreth.kata.spieldeslebens.ozean.Point;
import de.kreth.kata.spieldeslebens.swing.GameFrame;

public class GameStart {

	public static void main(final String[] args) {

		Board b = new Board();
		Fisch fisch = new Fisch(new Point(-5, -5));
		b.add(fisch);

		final GameFrame frame = new GameFrame(b);
		SwingUtilities.invokeLater(() -> frame.setVisible(true));

	}

}
