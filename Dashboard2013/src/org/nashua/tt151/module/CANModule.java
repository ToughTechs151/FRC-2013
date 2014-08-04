package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.nashua.tt151.devicepanel.CANDevicePanel;
import org.nashua.tt151.devices.CANDevice;

public class CANModule extends JPanel {
	private CANDevicePanel canDevices;
	private String name;
	public CANModule(String name) {
		this(name, 4);
	}
	public CANModule(String name, int devices) {
		this.name = name;
		canDevices = new CANDevicePanel("Black Jag Serial", devices);
		setPreferredSize(new Dimension(canDevices.getWidth()+4, canDevices.getHeight() + 25));
		setSize(getPreferredSize());
	}
	public void clearCANDevice(int boardID) {
		canDevices.clearDevice(boardID);
	}
	public void clearCANDevices() {
		canDevices.clearDevices();
	}
	public CANDevice getCANDevice(int boardID) {
		return canDevices.getDevice(boardID);
	}
	public void paintComponent(Graphics g){
		g.setColor(Color.DARK_GRAY.darker().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		canDevices.paintComponent(g.create(2, 2, canDevices.getWidth(), canDevices.getHeight()));
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(name.toUpperCase(), getWidth()/2-g.getFontMetrics().stringWidth(name.toUpperCase())/2, getHeight()-10);
	}
	public void registerCANDevice(CANDevice cd) {
		canDevices.registerDevice(cd);
	}
}
