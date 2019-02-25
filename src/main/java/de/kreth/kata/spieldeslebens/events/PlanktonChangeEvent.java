package de.kreth.kata.spieldeslebens.events;

import java.util.Optional;
import de.kreth.kata.spieldeslebens.items.Plankton;

public class PlanktonChangeEvent extends ItemPositionEvent<Plankton> {

  public PlanktonChangeEvent(Plankton item) {
    super(item, Optional.empty());
  }

}
