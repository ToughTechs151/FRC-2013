package org.nashua.tt151.devices;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class PWMOutDevice extends Device {
    public static enum PWMType {
    	/**
    	 * Identifier for a jaguar
    	 */
    	JAGUAR('J'),
    	/**
    	 * Identifier for a servo
    	 */
    	SERVO('S'),
    	/**
    	 * Identifier for a talon
    	 */
    	TALON('T'),
    	/**
    	 * Identifier for an unknown PWM device type
    	 */
    	UNKNOWN('U'),
    	/**
    	 * Identifier for an victor
    	 */
    	VICTOR('V');
    	
    	/**
    	 * Retrieve the PWM Type that corresponds with the shorthand
    	 * @param s The shorthand character
    	 * @return The PWM type that corresponds with the shorthand
    	 */
    	public static PWMType getFromShorthand(char s) {
    		for (PWMType at:PWMType.values()) {
    			if (at.getShorthand()==s) {
    				return at;
    			}
    		}
    		return PWMType.UNKNOWN;
    	}
		/**
    	 * Single character representation of the PWM Type
    	 */
    	private char shorthand;
    	
    	/**
    	 * Representation of a PWM Type using a single character
    	 * @param sh Character representation
    	 */
    	private PWMType(char sh) {
    		shorthand = sh;
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
    		return toS.length()<=3?toS:toS.substring(0, 3);
    	}
    }
	private String name = "Unknown";
	private int slot = 0;
	private PWMType type = PWMType.UNKNOWN;
	private int value = 0;
	public PWMOutDevice(int value, String name, int slot, PWMType type) {
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
	public PWMType getType() {
		return type;
	}
	public int getValue() {
		return value;
	}
	public void paintComponent(Graphics g) {
		g.setColor(value<128?Color.RED.darker().darker().darker():value>128?Color.GREEN.darker().darker().darker():Color.YELLOW.darker().darker().darker().darker());
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
		str = (""+type.getShorthand()).toUpperCase();
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
		if (type.equals(PWMType.SERVO)) {
			str = String.format("%.2f", value/1000.0);
		} else {
			str = String.format("%.2f (%d)", (value-128)/128.0, value);
		}
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
	}
}
