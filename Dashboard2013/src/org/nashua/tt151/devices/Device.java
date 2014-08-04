package org.nashua.tt151.devices;

import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class Device extends JPanel {
	public static int getDeviceHeight() {
		return 40;
	}
	public static int getDeviceWidth() {
		return 120;
	}
	public int getSlot() {
		return 1;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
