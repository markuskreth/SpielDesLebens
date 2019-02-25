package de.kreth.kata.spieldeslebens;

import javax.swing.SwingUtilities;

import de.kreth.kata.spieldeslebens.swing.GameFrame;

public class GameStart {

	public static void main(final String[] args) {

		Configuration config = Configuration.builder()
				.setWidth(30)
				.setHeight(30)
				.setFishCount(15)
				.setSharkCount(5)
				.setRockCount(5)
				.setPlanktonPerTick(500)
				.setReproductionPercent(40)
				.build();
		Board b = new Board(config);
		final GameFrame frame = new GameFrame(b);
		SwingUtilities.invokeLater(() -> frame.setVisible(true));

	}

}
