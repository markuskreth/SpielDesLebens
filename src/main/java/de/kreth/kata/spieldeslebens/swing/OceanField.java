package de.kreth.kata.spieldeslebens.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.kreth.kata.spieldeslebens.items.Plankton;
import de.kreth.kata.spieldeslebens.ozean.Himmelsrichtung;
import de.kreth.kata.spieldeslebens.ozean.Point;

class OceanField extends JPanel {

	private static final long serialVersionUID = -6818972784243153586L;

	private static final Logger logger = LogManager.getLogger(OceanField.class);

	public static final BufferedImage FISCH;

	public static final BufferedImage SHARK;
	static {
		try {
			FISCH = ImageIO.read(OceanField.class.getResource("/fish.png"));
			SHARK = ImageIO.read(OceanField.class.getResource("/shark.gif"));
		}
		catch (IOException e) {
			throw new IllegalStateException("Unable to load Resource", e);
		}
	}

	private Icon icon = Icon.EMPTY;

	enum Icon {
		EMPTY, FISH, SHARK, ROCK;
	}

	transient Point point;

	private Character sign;

	private int state;

	private Plankton plankton;

	public OceanField() {
		setPreferredSize(new Dimension(30, 30));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createRaisedBevelBorder());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintIcon((Graphics2D) g);

	}

	private void paintIcon(Graphics2D g2d) {
		Image scaledInstance;
		switch (icon) {
		case EMPTY:
			if (sign != null) {
				switch (state) {
				case 4:
					g2d.setColor(Color.BLACK);
					break;
				case 3:
					g2d.setColor(Color.DARK_GRAY);
					break;
				case 2:
					g2d.setColor(Color.GRAY);
					break;
				case 1:
					g2d.setColor(Color.LIGHT_GRAY);
					break;
				}
				g2d.drawString(sign.toString(), 2, 18);
			}
			break;
		case FISH:
			scaledInstance = FISCH.getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH);
			g2d.drawImage(scaledInstance, 1, 1, this);
			break;
		case ROCK:
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
			g2d.setColor(Color.WHITE);
			g2d.drawString("R", 2, 18);
			break;
		case SHARK:
			scaledInstance = SHARK.getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_SMOOTH);
			g2d.drawImage(scaledInstance, 1, 1, this);
			break;
		default:
			break;
		}
		if (plankton != null) {
			g2d.drawString(String.valueOf(plankton.getWeight()), 15, 10);
		}
	}

	public void setSign(Himmelsrichtung richtung, int state) {

		if (state <= 0) {
			sign = null;
		}
		else {
			switch (richtung) {
			case NORDEN:
			case SUEDEN:
				sign = '|';
				break;
			case OSTEN:
			case WESTEN:
				sign = '-';
				break;
			case NORD_OST:
			case SUED_WEST:
				sign = '/';
				break;
			case SUED_OST:
			case NORD_WEST:
				sign = '\\';
				break;
			default:
				sign = null;
				break;
			}
		}
		this.state = state;
		repaint();
	}

	public void setEmpty() {
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

	public void setPlankton(Plankton item) {
		this.plankton = item;
	}

}
