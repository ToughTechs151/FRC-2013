package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.UIManager;

import org.nashua.tt151.Dashboard;

public class StatusModule extends JPanel {
	public enum Status {
		DISCONNECTED, CONNECTED, HOPPER_JAM;
	}
	private Status state = Status.DISCONNECTED;
	public StatusModule() {
		setFont(UIManager.getFont("Label.font").deriveFont(28f));
	}
	public void setStatus(Status st) {
		state = st;
	}
	public void paintComponent(Graphics g) {
		String msg;
		switch (state) {
			case CONNECTED:
				g.setColor(Color.GREEN.darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "CONNECTED        CONNECTED        CONNECTED       CONNECTED";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
			case DISCONNECTED:
				g.setColor(Color.RED.darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				msg = "DISCONNECTED        DISCONNECTED        DISCONNECTED";
				g.setColor(Color.WHITE);
				g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				break;
			case HOPPER_JAM:
				msg = "HOPPER JAM!     HOPPER JAM!     HOPPER JAM!     HOPPER JAM!";
				if (Dashboard.getPaintFrame()<4) {
					g.setColor(Color.RED);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.WHITE);
					g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.RED);
					g.drawString(msg, getWidth()/2-g.getFontMetrics().stringWidth(msg)/2, getHeight()-10);
				}
				break;
		}
	}
}
