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
import de.kreth.kata.spieldeslebens.lebewesen.AbstractFisch;
import de.kreth.kata.spieldeslebens.lebewesen.AbstractLebewesen;
import de.kreth.kata.spieldeslebens.lebewesen.Fisch;
import de.kreth.kata.spieldeslebens.lebewesen.Hai;
import de.kreth.kata.spieldeslebens.lebewesen.WithPosition;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class Board {

	private final Logger logger = LogManager.getLogger(getClass());

	private final List<ItemListener> itemListeners = Collections.synchronizedList(new ArrayList<>());

	private final Executor eventExecutor = Executors.newSingleThreadExecutor();

	final int minX;

	final int miny;

	final int maxX;

	final int maxY;

	List<Fisch> fische;

	List<Hai> haie;

	List<Point> felsen;

	Map<Point, Integer> plankton;

	public Board() {
		minX = -20;
		miny = -20;
		maxX = 20;
		maxY = 20;
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
		this.felsen.add(felsen);
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMinX() {
		return minX;
	}

	public int getMiny() {
		return miny;
	}

	public boolean isOccupied(final Point position) {
		if (position.getX() <= minX || position.getX() >= maxX || position.getY() <= miny || position.getY() >= maxY) {
			return true;
		}
		for (final Point fels : felsen) {
			if (fels.equals(position)) {
				return true;
			}
		}

		return false;
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
