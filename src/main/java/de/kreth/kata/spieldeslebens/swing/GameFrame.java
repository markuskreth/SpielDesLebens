package de.kreth.kata.spieldeslebens.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.Board;
import de.kreth.kata.spieldeslebens.events.ItemDiedEvent;
import de.kreth.kata.spieldeslebens.events.ItemEvent;
import de.kreth.kata.spieldeslebens.events.ItemListener;
import de.kreth.kata.spieldeslebens.events.ItemPositionEvent;
import de.kreth.kata.spieldeslebens.items.Fisch;
import de.kreth.kata.spieldeslebens.items.Hai;
import de.kreth.kata.spieldeslebens.items.Plankton;
import de.kreth.kata.spieldeslebens.items.WithPosition;
import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class GameFrame extends JFrame implements ItemListener {

	final Logger logger = LogManager.getLogger(getClass());

	private static final long serialVersionUID = 1703618122365349251L;

	private final Board board;

	private GameTask task;

	private final Lock timerLock = new ReentrantLock();

	private final Timer timer;

	private JPanel boardPanel;

	private Map<Point, OceanField> pointToPanelMap = new HashMap<>();

	private JLabel fischCount;

	private JLabel sharkCount;

	private JSlider speedslider;

	public GameFrame(Board board) {
		super();
		timer = new Timer("Game Timer");
		this.board = board;
		board.add(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		createBoardPanel(board);
		add(boardPanel, BorderLayout.CENTER);

		JPanel buttonPanel = createButtons();
		speedslider = new JSlider(0, 100);
		speedslider.addChangeListener(this::speedChanged);
		JLabel speed = new JLabel("Geschwindigkeit");
		JPanel speedPanel = new JPanel(new FlowLayout());
		speedPanel.add(speed);
		speedPanel.add(speedslider);

		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		north.add(buttonPanel);
		north.add(speedPanel);

		add(north, BorderLayout.NORTH);
		pack();
	}

	private void speedChanged(ChangeEvent ev) {
		stop();
		start();
	}

	public JPanel createButtons() {
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JToggleButton startStop = new JToggleButton("Start", false);
		startStop.addActionListener(ev -> toggleGame(ev));
		buttonPanel.add(startStop);
		JLabel fishLabel = new JLabel(
				new ImageIcon(OceanField.FISCH.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
		fischCount = new JLabel();
		buttonPanel.add(fishLabel);
		buttonPanel.add(fischCount);
		JLabel haiLabel = new JLabel(
				new ImageIcon(OceanField.SHARK.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
		sharkCount = new JLabel();

		buttonPanel.add(haiLabel);
		buttonPanel.add(sharkCount);

		return buttonPanel;
	}

	private void toggleGame(ActionEvent ev) {
		JToggleButton source = (JToggleButton) ev.getSource();
		if (source.isSelected()) {
			source.setText("Stop");
			start();
		}
		else {
			source.setText("Start");
			stop();
		}
	}

	private void createBoardPanel(Board board) {
		Point upperLeftCorner = board.getUpperLeftCorner();
		Point lowerRightCorner = board.getLowerRightCorner();

		int height = lowerRightCorner.getY() + Math.abs(upperLeftCorner.getY());
		int width = lowerRightCorner.getX() + Math.abs(upperLeftCorner.getX());
		boardPanel = new JPanel(new GridLayout(height, width));

		logger.debug("Creating board with Size {}x{}", width, height);
		List<Point> points = new ArrayList<>();

		for (int x = upperLeftCorner.getX(); x < lowerRightCorner.getX(); x++) {
			for (int y = upperLeftCorner.getY(); y < lowerRightCorner.getY(); y++) {
				OceanField field = new OceanField();
				field.point = new Point(x, y);
				points.add(field.point);

				pointToPanelMap.put(field.point, field);
				boardPanel.add(field);
			}
		}
		Collections.sort(points);

		logger.trace("Created {} Fields. Max={}", points.size(), points.get(points.size() - 1));
	}

	@Override
	public void itemChange(ItemEvent<?> ev) {
		try {
			if (ev instanceof ItemPositionEvent<?>) {
				ItemPositionEvent<?> positionEvent = (ItemPositionEvent<?>) ev;
				positionEvent.getOldPosition()
						.ifPresent(p -> resetField(p, positionEvent.getItem().currentPosition()));
				WithPosition item = positionEvent.getItem();
				if (item instanceof Fisch) {
					pointToPanelMap.get(item.currentPosition()).setFish();
				}
				else if (item instanceof Hai) {
					pointToPanelMap.get(item.currentPosition()).setShark();
				}
				else if (item instanceof Plankton) {
					pointToPanelMap.get(item.currentPosition()).setPlankton((Plankton) item);
				}
			}
			else if (ev instanceof ItemDiedEvent<?>) {
				OceanField field = pointToPanelMap.get(((WithPosition) ev.getItem()).currentPosition());
				field.setEmpty();
			}
		}
		catch (NullPointerException e) {
			throw new RuntimeException("Error setting Item: " + ev, e);
		}

		fischCount.setText(String.valueOf(board.getFischeCount()));
		sharkCount.setText(String.valueOf(board.getHaieCount()));
	}

	private void resetField(Point old, Point current) {
		this.pointToPanelMap.get(old).setEmpty();
		timer.scheduleAtFixedRate(
				new OceanFieldFadeTask(this.pointToPanelMap.get(old), old.to(current)), 0,
				speedslider.getValue() * 10L);
	}

	public void start() {
		if (timerLock.tryLock()) {
			try {
				if (task == null && speedslider.getValue() > 0) {
					task = new GameTask();
					timer.scheduleAtFixedRate(task, 0, speedslider.getValue() * 10L);
				}
			}
			finally {
				timerLock.unlock();
			}
		}
	}

	public void stop() {
		if (timerLock.tryLock()) {
			try {
				if (task != null) {
					task.cancel();
					task = null;
				}
			}
			finally {
				timerLock.unlock();
			}
		}
	}

	class GameTask extends TimerTask {

		@Override
		public void run() {
			try {
				logger.trace("Timer executes Ticker");
				board.timerTick();
			}
			catch (final InterruptedException e) {
				logger.error("Timer Task not executed", e);
			}

		}

	}

	class OceanFieldFadeTask extends TimerTask {

		private final OceanField field;

		private final Himmelsrichtung direction;

		private int state;

		public OceanFieldFadeTask(OceanField field, Himmelsrichtung direction) {
			super();
			this.field = field;
			this.direction = direction;
			this.state = 4;
		}

		@Override
		public void run() {
			field.setSign(direction, state--);
			if (state < 0) {
				cancel();
			}
		}

	}
}
