package de.kreth.kata.spieldeslebens.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.ozean.Point;

class OceanField extends JPanel {

	private static final long serialVersionUID = -6818972784243153586L;

	private static final Logger logger = LogManager.getLogger(OceanField.class);

	private Icon icon = Icon.EMPTY;

	enum Icon {
		EMPTY, FISH, SHARK, ROCK;
	}

	transient Point point;

	public OceanField() {
		setPreferredSize(new Dimension(30, 30));
		setBorder(BorderFactory.createRaisedBevelBorder());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintIcon((Graphics2D) g);

	}

	private void paintIcon(Graphics2D g2d) {

		switch (icon) {
		case EMPTY:
			break;
		case FISH:
			g2d.drawString("F", 2, 18);
			break;
		case ROCK:
			g2d.drawString("R", 2, 18);
			break;
		case SHARK:
			g2d.drawString("S", 2, 18);
			break;
		default:
			break;
		}
	}

	public void makeEmpty() {
		logger.trace("Clearing " + point);
		icon = Icon.EMPTY;
		repaint();
	}

	public void setFish() {

		logger.trace("Setting FISH at " + point);
		icon = Icon.FISH;
		repaint();
	}

	public void setShark() {
		logger.trace("Setting SHARK at " + point);
		icon = Icon.SHARK;
		repaint();
	}

	public Point getPoint() {
		return point;
	}

}