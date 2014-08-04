package org.nashua.tt151.devices;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class RelayDevice extends Device {
	public static enum Direction {
		BACKWARD(-1, '-'), BOTH(0, 'B'), FORWARD(1, '+');
		public static Direction getDirectionForID(int id) {
			for (Direction d:values()) {
				if (d.id==id) {
					return d;
				}
			}
			return BOTH;
		}
		private int id;
		private char sh;
		private Direction(int id, char sh) {
			this.id = id;
			this.sh = sh;
		}
		public int getID() {
			return id;
		}
		public char getShorthand() {
			return sh;
		}
	}
	public static enum Value {
		BACKWARD(-1, Color.RED.darker().darker()), FORWARD(1, Color.GREEN.darker().darker()), OFF(0, Color.YELLOW.darker().darker().darker());
		public static Value getValueForID(int id) {
			for (Value v:values()) {
				if (v.id==id) {
					return v;
				}
			}
			return OFF;
		}
		private Color bg;
		private int id;
		private Value(int id, Color bg) {
			this.id = id;
			this.bg = bg;
		}
		public Color getBackgroundColor() {
			return bg;
		}
		public int getID() {
			return id;
		}
	}
	private Direction dir;
	private String name = "Unknown";
	private int slot = 0;
	private Value value;
	public RelayDevice(Value value, String name, int slot, Direction d) {
		this.value = value;
		this.name = name;
		this.slot = slot;
		this.dir = d;
		setSize(getDeviceWidth(), getDeviceHeight());
	}
	public Direction getDirection() {
		return dir;
	}
	public String getName() {
		return name;
	}
	public int getSlot() {
		return slot;
	}
	public Value getValue() {
		return value;
	}
	public void paintComponent(Graphics g) {
		g.setColor(value.getBackgroundColor().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(value.getBackgroundColor());
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
		str = ""+dir.getShorthand();
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
		str = ""+value;
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
	}
}
