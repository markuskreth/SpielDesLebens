package de.kreth.kata.spieldeslebens;

import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.kreth.kata.spieldeslebens.swing.GameFrame;

public class GameStart {

  private static final Logger LOGGER = LogManager.getLogger(GameStart.class);

  public static void main(final String[] args) {

    LOGGER.trace("trace");
    LOGGER.debug("debug");
    LOGGER.info("info");
    LOGGER.warn("warn");
    LOGGER.error("error");

    Configuration config = Configuration.builder().setWidth(30).setHeight(30).setFishCount(15)
        .setSharkCount(5).setRockCount(5).setPlanktonPerTick(350).build();
    Board b = new Board(config);
    final GameFrame frame = new GameFrame(b);
    SwingUtilities.invokeLater(() -> frame.setVisible(true));

  }

}
