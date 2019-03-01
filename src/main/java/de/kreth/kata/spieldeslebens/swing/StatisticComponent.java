package de.kreth.kata.spieldeslebens.swing;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.kreth.kata.spieldeslebens.Statistik;

public class StatisticComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5287362007235996318L;

	private JLabel fischCount;

	private JLabel sharkCount;

	public StatisticComponent() {
		setLayout(new GridLayout(2, 2));

		JLabel fishLabel = new JLabel(
				new ImageIcon(OceanField.FISCH.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
		fischCount = new JLabel();
		JLabel haiLabel = new JLabel(
				new ImageIcon(OceanField.SHARK.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH)));
		sharkCount = new JLabel();

		add(fishLabel);
		add(fischCount);
		add(haiLabel);
		add(sharkCount);
	}

	public void update(Statistik statistik) {
		fischCount.setText(String.valueOf(statistik.getFishCount()));
		sharkCount.setText(String.valueOf(statistik.getSharkCount()));
	}
}
