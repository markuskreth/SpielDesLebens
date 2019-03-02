package de.kreth.kata.spieldeslebens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.events.ItemDiedEvent;
import de.kreth.kata.spieldeslebens.events.ItemEvent;
import de.kreth.kata.spieldeslebens.events.ItemListener;
import de.kreth.kata.spieldeslebens.events.ItemPositionEvent;
import de.kreth.kata.spieldeslebens.events.ItemReproductionEvent;
import de.kreth.kata.spieldeslebens.events.PlanktonChangeEvent;
import de.kreth.kata.spieldeslebens.items.AbstractFisch;
import de.kreth.kata.spieldeslebens.items.AbstractLebewesen;
import de.kreth.kata.spieldeslebens.items.Felsen;
import de.kreth.kata.spieldeslebens.items.Fisch;
import de.kreth.kata.spieldeslebens.items.Hai;
import de.kreth.kata.spieldeslebens.items.Plankton;
import de.kreth.kata.spieldeslebens.items.WithPosition;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class Board {

	private final Logger logger = LogManager.getLogger(getClass());

	private final List<ItemListener> itemListeners = Collections.synchronizedList(new ArrayList<>());

	private final Executor eventExecutor = Executors.newSingleThreadExecutor();

	private final Point upperLeftCorner;

	private final Point lowerRightCorner;

	private List<Fisch> fische;

	private List<Hai> haie;

	private List<Felsen> felsen;

	private Map<Point, Integer> plankton;

	private PointRandomizer planktonRandomizer;

	private int planktonPerTick;

	private int reproductionPercent;

	public Board(Configuration config) {
		upperLeftCorner = new Point(0, 0);
		lowerRightCorner = new Point(config.getWidth(), config.getHeight());
		planktonRandomizer = new PointRandomizer(config.getWidth(), config.getHeight());

		planktonPerTick = config.getPlanktonPerTick();
		reproductionPercent = config.getReproductionPercent();

		fische = new ArrayList<>();
		haie = new ArrayList<>();
		felsen = new ArrayList<>();
		plankton = new HashMap<>();
		config.getRocks().forEach(r -> addFelsen(r));
		config.getSharks().forEach(s -> add(s));
		config.getFishes().forEach(f -> add(f));
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

	public Statistik createStatistik() {
		return new Statistik.Builder()
				.setFishCount(fische.size())
				.setSharkCount(haie.size())
				.build();
	}

	private void add(final Fisch fisch) {
		fische.add(fisch);
		fireEvent(new ItemPositionEvent<Fisch>(fisch, Optional.empty()));
	}

	private void add(final Hai hai) {
		haie.add(hai);
		fireEvent(new ItemPositionEvent<Hai>(hai, Optional.empty()));
	}

	private void addFelsen(final Felsen e) {
		this.felsen.add(e);
		fireEvent(new ItemPositionEvent<Felsen>(e, Optional.empty()));
	}

	public void fireItemInitEvents() {
		for (Felsen e : felsen) {
			fireEvent(new ItemPositionEvent<Felsen>(e, Optional.empty()));
		}
		for (Hai hai : haie) {
			fireEvent(new ItemPositionEvent<Hai>(hai, Optional.empty()));
		}
		for (Fisch fisch : fische) {
			fireEvent(new ItemPositionEvent<Fisch>(fisch, Optional.empty()));
		}
	}

	public Point getUpperLeftCorner() {
		return upperLeftCorner;
	}

	public Point getLowerRightCorner() {
		return lowerRightCorner;
	}

	public boolean isOccupied(final Point position) {
		if (position.getX() < upperLeftCorner.getX()
				|| position.getX() >= lowerRightCorner.getX()
				|| position.getY() < upperLeftCorner.getY()
				|| position.getY() >= lowerRightCorner.getY()) {
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

		for (int i = 0; i < planktonPerTick; i++) {
			Point planktonItem = planktonRandomizer.create();
			if (!plankton.containsKey(planktonItem)) {
				plankton.put(planktonItem, 0);
			}
			plankton.put(planktonItem, plankton.get(planktonItem) + 1);
			fireEvent(new PlanktonChangeEvent(
					new Plankton(planktonItem, plankton.get(planktonItem).intValue())));
		}
		updateFish();
		updateShark();
	}

	private void updateShark() {
		for (final Hai h : new ArrayList<>(haie)) {
			if (h.isAlive()) {
				haie.remove(h);
				eatFish(h);

				if (new Random().nextInt(100 + 1) <= reproductionPercent) {
					List<Hai> children = reproduceItem(h);
					for (Hai child : children) {
						Hai movedHai = move(child);
						if (movedHai.isAlive()) {
							haie.add(movedHai);
						}
					}
				}
				else {
					Hai moved = move(h);
					if (moved.isAlive()) {
						haie.add(moved);
					}
					else {
						fireEvent(new ItemDiedEvent<>(h));
					}
				}
			}
			else {
				haie.remove(h);
				logger.debug("Died: {}", h);
				fireEvent(new ItemDiedEvent<>(h));
			}
		}
	}

	private void updateFish() {
		for (final Fisch f : new ArrayList<>(fische)) {
			if (f.isAlive()) {
				fische.remove(f);

				if (new Random().nextInt(100 + 1) <= reproductionPercent) {
					List<Fisch> children = reproduceItem(f);

					for (Fisch child : children) {
						Fisch moved = moveFisch(child);
						if (moved.isAlive()) {
							fische.add(moved);
						}
						else {
							logger.debug("Died: {}", f);
							fireEvent(new ItemDiedEvent<>(child));
						}
					}
				}
				else {
					Fisch moved = moveFisch(f);
					if (moved.isAlive()) {
						fische.add(moved);
					}
					else {
						logger.debug("Died: {}", f);
						fireEvent(new ItemDiedEvent<>(f));
					}
				}

			}
			else {
				fische.remove(f);
				logger.debug("Died: {}", f);
				fireEvent(new ItemDiedEvent<>(f));
			}
		}
	}

	private void eatFish(final Hai h) {
		for (Fisch f : fische) {
			if (Math.abs(f.currentPosition().getX() - h.currentPosition().getX()) <= 1
					&& Math.abs(f.currentPosition().getY() - h.currentPosition().getY()) <= 1) {
				h.eat(f);
				break;
			}
		}
	}

	private <T extends AbstractFisch<?, ?>> List<T> reproduceItem(final T f) {
		@SuppressWarnings("unchecked")
		List<T> children = (List<T>) f.fortpflanzen();
		logger.info("{} pflanzt sich fort -> {}", f, children);
		fireEvent(new ItemReproductionEvent<>(f, children));
		return children;

	}

	private Fisch moveFisch(final Fisch f) {
		Fisch move = move(f);
		Integer p = plankton.get(move.currentPosition());
		if (p != null) {
			Plankton planktionItem = new Plankton(move.currentPosition(), p);
			move.eat(planktionItem);
			plankton.put(move.currentPosition(), 0);
			fireEvent(new PlanktonChangeEvent(planktionItem));
		}
		return move;
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractFisch<? extends AbstractFisch<?, ?>, ?>> T move(final T creature) {
		Point old = creature.currentPosition();

		T moved = (T) creature.move(Board.this::isOccupied);
		moved.decreaseWeight();

		if (!old.equals(moved.currentPosition()) && moved.isAlive()) {
			fireEvent(new ItemPositionEvent<AbstractFisch<?, ?>>(moved, Optional.of(old)));
			logger.debug(moved + " moved from " + old);
		}
		else {
			fireEvent(new ItemDiedEvent<>(creature));
		}
		return moved;
	}

}
