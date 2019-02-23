package de.kreth.kata.spieldeslebens.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.Board;
import de.kreth.kata.spieldeslebens.events.ItemEvent;
import de.kreth.kata.spieldeslebens.events.ItemListener;
import de.kreth.kata.spieldeslebens.events.ItemPositionEvent;
import de.kreth.kata.spieldeslebens.lebewesen.WithPosition;
import de.kreth.kata.spieldeslebens.ozean.Point;

public class GameFrame extends JFrame implements ItemListener {

	private final Logger logger = LogManager.getLogger(getClass());

	private static final long PERIOD = 2000L;

	private static final long serialVersionUID = 1703618122365349251L;

	private final Board board;

	private GameTask task;

	private final Lock timerLock = new ReentrantLock();

	private final Timer timer;

	private JPanel boardPanel;

	private Map<Point, OceanField> pointToPanelMap = new HashMap<>();

	public GameFrame(Board board) {
		super();
		timer = new Timer("Game Timer");
		this.board = board;
		board.add(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		createBoardPanel(board);
		add(boardPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JToggleButton startStop = new JToggleButton("Start", false);
		startStop.addActionListener(ev -> toggleGame(ev));
		buttonPanel.add(startStop);
		add(buttonPanel, BorderLayout.NORTH);
		pack();
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

	public void createBoardPanel(Board board) {
		boardPanel = new JPanel(new GridLayout(board.getMaxY() + Math.abs(board.getMiny()),
				board.getMaxX() + Math.abs(board.getMinX())));

		for (int x = board.getMinX(); x <= board.getMaxX(); x++) {
			for (int y = board.getMiny(); y <= board.getMaxY(); y++) {
				OceanField field = new OceanField();
				field.point = new Point(x, y);
				pointToPanelMap.put(field.point, field);
				boardPanel.add(field);
			}
		}
	}

	@Override
	public void itemChange(ItemEvent<?> ev) {
		if (ev instanceof ItemPositionEvent<?>) {
			ItemPositionEvent<?> positionEvent = (ItemPositionEvent<?>) ev;
			positionEvent.getOldPosition().ifPresent(p -> resetField(p));
			WithPosition item = positionEvent.getItem();
			pointToPanelMap.get(item.currentPosition()).setFish();
		}
	}

	private void resetField(Point p) {
		this.pointToPanelMap.get(p).makeEmpty();
	}

	public void start() {
		if (timerLock.tryLock()) {
			try {
				if (task == null) {
					task = new GameTask();
					timer.scheduleAtFixedRate(task, PERIOD, PERIOD);
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

	private static class OceanField extends JLabel {

		private static final long serialVersionUID = -6818972784243153586L;

		private static final Color EMPTY = Color.LIGHT_GRAY;

		private static final Color FISH = Color.BLUE;

		private static final Color ROCK = Color.BLACK;

		private static final Color SHARK = Color.ORANGE;

		private transient Point point;

		public OceanField() {
			setBackground(EMPTY);
			setPreferredSize(new Dimension(20, 20));
		}

		public void makeEmpty() {
			setBackground(EMPTY);
		}

		public void setFish() {
			setBackground(FISH);
		}

		public Point getPoint() {
			return point;
		}

	}
}
