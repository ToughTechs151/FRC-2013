package org.nashua.tt151.devices;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import org.nashua.tt151.Dashboard;

public class CANDevice extends Device implements Comparable<CANDevice> {
	public static enum JaguarType {
		BLACK_JAGUAR(2, "Black Jaguar", 107, Color.BLACK), 
		GRAY_JAGUAR(1, "Gray Jaguar", 101, new Color(0x5f5754)),
		UNK_JAGUAR(0, "Unk Jaguar", 0, Color.WHITE);
		public static JaguarType getTypeForID(int id) {
			for (JaguarType jt:values()) {
				if (jt.getID()==id) {
					return jt;
				}
			}
			return JaguarType.UNK_JAGUAR;
		}
		private Color fg;
		private int id;
		private int minVer;
		private String name;
		private JaguarType(int id, String name, int minVer, Color fg) {
			this.id = id;
			this.name = name;
			this.minVer = minVer;
			this.fg = fg;
		}
		public Color getForegroundColor() {
			return fg;
		}
		public int getID() {
			return id;
		}
		public int getMinimumFirmwareVersion() {
			return minVer;
		}
		public String toString() {
			return name;
		}
	}
	public static int getDeviceHeight() {
		return 80;
	}
	private int boardID = 0;
	private int firmware;
	private double inVolt = 0;
	private String name = "Unknown";
	private double outVolt = 0;
	private double temp = 0;
	private JaguarType type;
	private double value = 0;
	private boolean devel = false;
	public CANDevice(boolean dev, String name, int boardID, double value, double inVolt, double outVolt, double temp, int fw, JaguarType jt) {
		this.name = name;
		this.boardID = boardID;
		this.value = value;
		this.inVolt = inVolt;
		this.outVolt = outVolt;
		this.temp = temp*1.8+32.0; // Convert Celsius to Fahrenheit
		this.firmware = fw;
		this.type = jt;
		devel = dev;
		
		setSize(getDeviceWidth(), getDeviceHeight());
	}
	public int compareTo(CANDevice o) {
	     return getBoardID()-o.getBoardID();
	}
	public int getBoardID() {
		return boardID;
	}
	public String getName() {
		return name;
	}
	public int getSlot() {
		return boardID;
	}
	public void paintComponent(Graphics g) {
		int frame = Dashboard.getPaintFrame();
		boolean error = (value<-1 || value>1) && firmware<0 && type.equals(JaguarType.UNK_JAGUAR);
		if (error) {
			g.setColor(frame<4 ? Color.WHITE : Color.RED);
		} else {
			g.setColor(value<0?Color.RED.darker().darker().darker():value>0?Color.GREEN.darker().darker().darker():Color.YELLOW.darker().darker().darker().darker());
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(g.getColor().brighter());
		g.fillRect(1, 1, getWidth()-2, getHeight()-2);
		if (error) {
			g.setColor(frame<4 ? Color.RED : Color.WHITE);
		} else {
			g.setColor(Color.WHITE);
		}
		String str;
		FontMetrics fm = g.getFontMetrics();
		int y = fm.getHeight()-1;
		str = name;
		g.drawString(str, 5, y);
		str = ""+boardID;
		g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
		y += fm.getHeight()-1;
		if (error) {
			g.setColor(frame<4 ? Color.RED : Color.WHITE);
			g.setFont(g.getFont().deriveFont(20f));
			str = "COMM";
			fm = g.getFontMetrics();
			g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, (getHeight()-y)/3+fm.getHeight());
			y += fm.getHeight()-1;
			str = "FAULT!";
			g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y+fm.getHeight()/2);
		} else if (devel) {
			g.setColor(type.getForegroundColor());
			str = type.toString().substring(0,1).toUpperCase();
			g.drawString(str, getWidth()-5-fm.stringWidth(str), y);
			if (inVolt>=11.5 || frame<6) {
				g.setColor(inVolt>=12.0?Color.WHITE:inVolt>=11.5?Color.YELLOW.brighter():Color.RED);
				str = String.format("%.2fV", inVolt);
				g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
			}
			y += fm.getHeight()-1;
			g.setColor(Color.WHITE);
			str = String.format("%.2fV", outVolt);
			g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
			y += fm.getHeight()-1;
			str = String.format("%.2f\u2109", temp);
			g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
			y += fm.getHeight()-1;
			g.setColor(firmware>=type.getMinimumFirmwareVersion() ? Color.WHITE : Color.RED);
			if (!g.getColor().equals(Color.RED) || frame<6) {
				str = String.format("Firmware: %s", firmware);
				g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, y);
			}
		} else {
			g.setColor(frame<4 ? Color.RED : Color.WHITE);
			g.setFont(g.getFont().deriveFont(28f));
			fm = g.getFontMetrics();
			g.setColor(Color.WHITE);
			str = String.format("%.2fV", outVolt);
			g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, (getHeight()-y)/2+y);
			y += fm.getHeight()-1;
		}
	}
}
