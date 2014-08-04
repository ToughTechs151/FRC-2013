package org.nashua.tt151.devices;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class EmptyDevice extends Device {
	private int slot;
	public EmptyDevice(int slot) {
		this.slot = slot;
		setSize(getDeviceWidth(), getDeviceHeight());
	}
	public int getSlot() {
		return slot;
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.LIGHT_GRAY.darker().darker());
		String str;
		FontMetrics fm = g.getFontMetrics();
		int y = fm.getHeight();
		str = ""+slot;
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
	}
}