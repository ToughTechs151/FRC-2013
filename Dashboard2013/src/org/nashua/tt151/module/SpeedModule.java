package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.UIManager;

import org.nashua.tt151.Dashboard;

public class SpeedModule extends JPanel {
	public enum Mode {
		TURBO, CREEP, NORMAL, AUTOMATED, DISABLED;
	}
	private Mode mode = Mode.DISABLED;
	public SpeedModule() {
		setFont(UIManager.getFont("Label.font").deriveFont(24f));
	}
	public void setMode(Mode md) {
		mode = md;
	}
	public void paintComponent(Graphics g) {
		String msg;
		switch (mode) {
			case CREEP:
				g.setColor(Color.RED.darker().darker().darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "CREEP MODE";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
			case NORMAL:
				g.setColor(Color.YELLOW.darker().darker().darker().darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "NORMAL SPEED";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
			case TURBO:
				g.setColor(Color.RED.darker().darker().darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "TURBO SPEED";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
			case AUTOMATED:
				g.setColor(Color.BLUE.darker().darker().darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "AUTOMATED";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
			case DISABLED:
				g.setColor(Color.LIGHT_GRAY.darker().darker().darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "DISABLED";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
		}
	}
}
