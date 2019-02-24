package de.kreth.kata.spieldeslebens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.events.ItemEvent;
import de.kreth.kata.spieldeslebens.events.ItemListener;
import de.kreth.kata.spieldeslebens.events.ItemPositionEvent;
import de.kreth.kata.spieldeslebens.items.AbstractFisch;
import de.kreth.kata.spieldeslebens.items.AbstractLebewesen;
import de.kreth.kata.spieldeslebens.items.Felsen;
import de.kreth.kata.spieldeslebens.items.Fisch;
import de.kreth.kata.spieldeslebens.items.Hai;
import de.kreth.kata.spieldeslebens.items.WithPosition;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class Board {

	private final Logger logger = LogManager.getLogger(getClass());

	private final List<ItemListener> itemListeners = Collections.synchronizedList(new ArrayList<>());

	private final Executor eventExecutor = Executors.newSingleThreadExecutor();

	final Point upperLeftCorner;

	final Point lowerRightCorner;

	List<Fisch> fische;

	List<Hai> haie;

	List<Felsen> felsen;

	Map<Point, Integer> plankton;

	public Board() {
		upperLeftCorner = new Point(0, 0);
		lowerRightCorner = new Point(30, 30);

		fische = new ArrayList<>();
		haie = new ArrayList<>();
		felsen = new ArrayList<>();
		plankton = new HashMap<>();
	}

	public void add(final ItemListener l) {
		itemListeners.add(l);
	}

	private synchronized void fireEvent(ItemEvent<?> ev) {
		eventExecutor.execute(() -> {
			for (ItemListener l : itemListeners) {
				l.itemChange(ev);
			}
		});
	}

	public void add(final Fisch fisch) {
		fische.add(fisch);
		fireEvent(new ItemPositionEvent<Fisch>(fisch, Optional.empty()));
	}

	public void add(final Hai hai) {
		haie.add(hai);
		fireEvent(new ItemPositionEvent<Hai>(hai, Optional.empty()));
	}

	public void addFelsen(final Point felsen) {
		Felsen e = new Felsen(felsen);
		this.felsen.add(e);
		fireEvent(new ItemPositionEvent<Felsen>(e, Optional.empty()));

	}

	public Point getUpperLeftCorner() {
		return upperLeftCorner;
	}

	public Point getLowerRightCorner() {
		return lowerRightCorner;
	}

	public boolean isOccupied(final Point position) {
		if (position.getX() < upperLeftCorner.getX() || position.getX() > lowerRightCorner.getX()
				|| position.getY() <= upperLeftCorner.getY() || position.getY() >= lowerRightCorner.getY()) {
			return true;
		}
		for (final WithPosition fels : felsen) {
			if (fels.currentPosition().equals(position)) {
				return true;
			}
		}

		return getOccupation(position).isPresent();
	}

	public Optional<WithPosition> getOccupation(final Point position) {

		final List<AbstractLebewesen<?>> occupied = new ArrayList<>();
		occupied.addAll(fische);
		occupied.addAll(haie);
		for (final AbstractLebewesen<?> occupation : occupied) {
			if (occupation.isAlive() && occupation.currentPosition().equals(position)) {
				return Optional.of(occupation);
			}
		}
		return Optional.empty();
	}

	void withAll(Consumer<AbstractLebewesen<?>> consumer) {

		final List<AbstractLebewesen<?>> lebewesen = new ArrayList<>();
		lebewesen.addAll(fische);
		lebewesen.addAll(haie);
		for (final AbstractLebewesen<?> l : lebewesen) {
			consumer.accept(l);
		}
	}

	public synchronized void timerTick() throws InterruptedException {

		for (final Fisch f : new ArrayList<>(fische)) {
			fische.remove(f);
			fische.add(move(f));
		}
		for (final Hai h : new ArrayList<>(haie)) {
			haie.remove(h);
			haie.add(move(h));
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractFisch<? extends AbstractFisch<?, ?>, ?>> T move(T f) {
		Point old = f.currentPosition();
		f = (T) f.move(Board.this::isOccupied);
		if (!old.equals(f.currentPosition())) {

			fireEvent(new ItemPositionEvent<AbstractFisch<?, ?>>(f, Optional.of(old)));
			logger.debug(f + " moved from " + old);
		}
		return f;
	}

}
