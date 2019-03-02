package de.kreth.kata.spieldeslebens.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.Board;
import de.kreth.kata.spieldeslebens.PointRandomizer;
import de.kreth.kata.spieldeslebens.events.ItemDiedEvent;
import de.kreth.kata.spieldeslebens.events.ItemEvent;
import de.kreth.kata.spieldeslebens.events.ItemListener;
import de.kreth.kata.spieldeslebens.events.ItemPositionEvent;
import de.kreth.kata.spieldeslebens.items.Felsen;
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

	private JSlider speedslider;

	private JToggleButton startStopToggle;

	private StatisticComponent statisticUI;

	private final PointRandomizer itemPointRandomizer;

	public GameFrame(Board board) {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timer = new Timer("Game Timer");
		this.board = board;
		board.add(this);
		itemPointRandomizer = new PointRandomizer(board.getLowerRightCorner().getX(),
				board.getLowerRightCorner().getY());
		this.setLayout(new BorderLayout());

		createBoardPanel(board);
		add(boardPanel, BorderLayout.CENTER);

		JComponent controls = createControls();
		statisticUI = new StatisticComponent();
		JPanel speedPanel = createSpeedSlider();

		JPanel northWest = new JPanel();
		northWest.setLayout(new BoxLayout(northWest, BoxLayout.Y_AXIS));
		northWest.add(controls);
		northWest.add(speedPanel);

		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
		north.add(northWest);
		north.add(statisticUI);

		add(north, BorderLayout.NORTH);
		board.fireItemInitEvents();
		pack();
	}

	public JPanel createSpeedSlider() {
		speedslider = new JSlider(0, 100);
		speedslider.addChangeListener(this::speedChanged);
		speedslider.setMajorTickSpacing(25);
		speedslider.setMinorTickSpacing(5);
		speedslider.setPaintTicks(true);
		speedslider.setPaintLabels(true);
		speedslider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (speedslider.getValue() < 1) {
					new GameTask().run();
				}
			}
		});
		JLabel speed = new JLabel("Geschwindigkeit");
		JPanel speedPanel = new JPanel(new FlowLayout());
		speedPanel.add(speed);
		speedPanel.add(speedslider);
		return speedPanel;
	}

	private void speedChanged(ChangeEvent ev) {
		if (startStopToggle.isSelected() == false) {
			logger.debug("Prepared Speed to " + speedslider.getValue());
		}
		stop();
		start();
	}

	public JComponent createControls() {
		startStopToggle = new JToggleButton("Start", false);
		startStopToggle.addActionListener(ev -> toggleGame(ev));
		startStopToggle.isSelected();

		JButton addFisch = new JButton("+",
				new ImageIcon(OceanField.FISCH.getScaledInstance(24, 24, BufferedImage.SCALE_SMOOTH)));
		addFisch.addActionListener(ev -> {
			Point startPosition = itemPointRandomizer.create();
			while (board.isOccupied(startPosition)) {
				startPosition = itemPointRandomizer.create();
			}
			board.add(new Fisch(startPosition));
		});
		JButton addShark = new JButton("+",
				new ImageIcon(OceanField.SHARK.getScaledInstance(24, 24, BufferedImage.SCALE_SMOOTH)));
		addShark.addActionListener(ev -> {
			Point startPosition = itemPointRandomizer.create();
			while (board.isOccupied(startPosition)) {
				startPosition = itemPointRandomizer.create();
			}
			board.add(new Hai(startPosition));
		});
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.add(startStopToggle);
		panel.add(addFisch);
		panel.add(new JLabel());
		panel.add(addShark);
		return panel;
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
				final OceanField field = new OceanField();
				field.point = new Point(x, y);
				field.setToolTipText(field.point.toString());
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
				OceanField oceanField = pointToPanelMap.get(item.currentPosition());
				if (item instanceof Fisch) {
					oceanField.setFish();
					oceanField.setToolTipText(oceanField.point.toString() + ": " + item);
				}
				else if (item instanceof Hai) {
					oceanField.setShark();
					oceanField.setToolTipText(oceanField.point.toString() + ": " + item);
				}
				else if (item instanceof Plankton) {
					oceanField.setPlankton((Plankton) item);
				}
				else if (item instanceof Felsen) {
					oceanField.setFelsen();
				}
			}
			else if (ev instanceof ItemDiedEvent<?>) {
				OceanField field = pointToPanelMap.get(((WithPosition) ev.getItem()).currentPosition());
				field.setEmpty();
				field.setToolTipText(field.point.toString());
			}
		}
		catch (NullPointerException e) {
			throw new RuntimeException("Error setting Item: " + ev, e);
		}

	}

	private void resetField(Point old, Point current) {
		this.pointToPanelMap.get(old).setEmpty();
		if (speedslider.getValue() > 0) {
			timer.scheduleAtFixedRate(
					new OceanFieldFadeTask(this.pointToPanelMap.get(old), old.to(current)), 0,
					speedslider.getValue() * 10L);
		}
		else {
			new OceanFieldFadeTask(this.pointToPanelMap.get(old), old.to(current)).run();
		}
	}

	public void start() {
		if (timerLock.tryLock()) {
			try {
				if (startStopToggle.isSelected() && speedslider.getValue() > 0) {
					task = new GameTask();
					long period = calcPeriod();
					logger.info("Starting Game with Speed " + period);
					timer.scheduleAtFixedRate(task, 0, period);
				}
			}
			finally {
				timerLock.unlock();
			}
		}
	}

	/**
	 * <pre>
	 * Expected	speed
	2000	1
	1500	10
	1000	20
	800	30
	600	40
	400	50
	300	60
	200	70
	100	80
	50	90
	25	100
	 * 
	 * </pre>
	 * 
	 * @return
	 */
	private long calcPeriod() {
		if (speedslider.getValue() > 90) {
			return 25L;
		}
		else if (speedslider.getValue() <= 2) {
			return 3000L;
		}
		else if (speedslider.getValue() <= 4) {
			return 2700L;
		}
		else if (speedslider.getValue() <= 6) {
			return 2300L;
		}
		else if (speedslider.getValue() <= 8) {
			return 1900L;
		}
		else if (speedslider.getValue() <= 10) {
			return 1500L;
		}
		else if (speedslider.getValue() <= 20) {
			return 1000L;
		}
		else if (speedslider.getValue() <= 30) {
			return 800L;
		}
		else if (speedslider.getValue() <= 40) {
			return 600L;
		}
		else if (speedslider.getValue() <= 50) {
			return 400L;
		}
		else if (speedslider.getValue() <= 60) {
			return 300L;
		}
		else if (speedslider.getValue() <= 70) {
			return 200L;
		}
		else if (speedslider.getValue() <= 80) {
			return 100L;
		}
		else if (speedslider.getValue() <= 90) {
			return 50L;
		}
		return 0;
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
				repaint();
				statisticUI.update(board.createStatistik());
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
