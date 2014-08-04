package org.nashua.tt151.devices;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Analog Device Panel
 * @author Brian Ashworth
 * @version 1.0
 */
public class AnalogDevice extends Device {
	/**
     * Analog Device Type
     * @author Brian Ashworth
     * @version 1.0
     */
    public static enum AnalogType {
    	/**
    	 * Identifier for an accelerometer
    	 */
    	ACCELEROMETER('A', Color.YELLOW.darker().darker().darker().darker().darker(), Color.WHITE),
    	/**
    	 * Identifier for a gyro
    	 */
    	GYRO('G', Color.BLUE.darker().darker().darker(), Color.WHITE),
    	/**
    	 * Identifier for a potentiometer
    	 */
    	POTENTIOMETER('P', Color.GREEN.darker().darker().darker(), Color.WHITE),
    	/**
    	 * Identifier for an unknown analog device type
    	 */
    	UNKNOWN('U', Color.RED.darker().darker().darker(), Color.WHITE);
    	
    	/**
    	 * Retrieve the Analog Type that corresponds with the shorthand
    	 * @param s The shorthand character
    	 * @return The analog type that corresponds with the shorthand
    	 */
    	public static AnalogType getFromShorthand(char s) {
    		for (AnalogType at:AnalogType.values()) {
    			if (at.getShorthand()==s) {
    				return at;
    			}
    		}
    		return AnalogType.UNKNOWN;
    	}
    	
    	private Color bg;
		private Color fg;
		/**
    	 * Single character representation of the Analog Type
    	 */
    	private char shorthand;
    	
    	/**
    	 * Representation of a Analog Type using a single character
    	 * @param sh Character representation
    	 */
    	private AnalogType(char sh, Color bg, Color fg) {
    		shorthand = sh;
    		this.fg = fg;
    		this.bg = bg;
    	}
    	
    	public Color getBackgroundColor() {
    		return bg;
    	}
    	
    	public Color getForegroundColor() {
    		return fg;
    	}
    	
    	/**
    	 * Retrieves the shorthand character for the device
    	 * @return The shorthand character for the device
    	 */
    	public char getShorthand() {
    		return shorthand;
    	}
    	public String toString() {
    		String toS = super.toString().substring(0, 1).toUpperCase()+super.toString().substring(1).toLowerCase();
    		return toS.length()<=5?toS:toS.substring(0, 5);
    	}
    }
	private String name = "Unknown";
	private int slot = 0;
	private AnalogType type = AnalogType.UNKNOWN;
	private int value = 0;
	public AnalogDevice(int value, String name, int slot, AnalogType type) {
		this.value = value;
		this.name = name;
		this.slot = slot;
		this.type = type;
		setSize(getDeviceWidth(), getDeviceHeight());
	}
	public String getName() {
		return name;
	}
	public int getSlot() {
		return slot;
	}
	public AnalogType getType() {
		return type;
	}
	public int getValue() {
		return value;
	}
	public void paintComponent(Graphics g) {
		g.setColor(type.getBackgroundColor().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(type.getBackgroundColor());
		g.fillRect(1, 1, getWidth()-2, getHeight()-2);
		g.setColor(type.getForegroundColor());
		String str;
		FontMetrics fm = g.getFontMetrics();
		int y = fm.getHeight();
		str = name;
		g.drawString(str, 5, y);
		str = ""+slot;
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
		y += fm.getHeight();
		str = (""+type.getShorthand()).toUpperCase();
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
		if (type.equals(AnalogType.GYRO)) {
			if (value==value%360) {
				str=String.format("%d\u00b0", value);
			} else {
				str=String.format("%d\u00b0 (%d\u00b0)", value, value%360);
			}
		} else if (type.equals(AnalogType.ACCELEROMETER)) {
			str=String.format("%d G", value);
		} else {
			str = String.format("%d", value);
		}
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
	}
}
