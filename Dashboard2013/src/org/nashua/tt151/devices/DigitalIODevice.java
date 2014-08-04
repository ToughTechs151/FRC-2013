package org.nashua.tt151.devices;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class DigitalIODevice extends Device {
	public static int getDeviceHeight() {
		return 20;
	}
	private String name = "Unknown";
	private int slot = 0;
	private boolean value = false;
	public DigitalIODevice(boolean value, String name, int slot) {
		this.value = value;
		this.name = name;
		this.slot = slot;
		setSize(getDeviceWidth(), getDeviceHeight());
	}
	public String getName() {
		return name;
	}
	public int getSlot() {
		return slot;
	}
	public boolean getValue() {
		return value;
	}
	public void paintComponent(Graphics g) {
		g.setColor(value ? Color.GREEN.darker().darker().darker() : Color.RED.darker().darker().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(g.getColor().brighter());
		g.fillRect(1, 1, getWidth()-2, getHeight()-2);
		g.setColor(Color.WHITE);
		String str;
		FontMetrics fm = g.getFontMetrics();
		int y = fm.getHeight();
		str = name;
		g.drawString(str, 5, y);
		str = ""+slot;
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
		y += fm.getHeight();
		str = value?"ON":"OFF";
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
	}
}
