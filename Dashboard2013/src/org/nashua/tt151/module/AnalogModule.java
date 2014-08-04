package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.nashua.tt151.devicepanel.DevicePanel;
import org.nashua.tt151.devices.AnalogDevice;

public class AnalogModule extends JPanel {
	private DevicePanel<AnalogDevice> analog = new DevicePanel<AnalogDevice>("", 8);
	private String name;
	public AnalogModule(String name) {
		this.name = name;
		setPreferredSize(new Dimension(analog.getWidth()+4, analog.getHeight() + 25));
		setSize(getPreferredSize());
	}
	public void clearAnalogDevice(int slot) {
		analog.clearDevice(slot);
	}
	public void clearAnalogDevices() {
		analog.clearDevices();
	}
	public AnalogDevice getAnalogDevice(int slot) {
		return analog.getDevice(slot);
	}
	public void paintComponent(Graphics g){
		g.setColor(Color.DARK_GRAY.darker().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		analog.paintComponent(g.create(2, 2, analog.getWidth(), analog.getHeight()));
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(name.toUpperCase(), getWidth()/2-g.getFontMetrics().stringWidth(name.toUpperCase())/2, getHeight()-10);
	}
	public void registerAnalogDevice(AnalogDevice an) {
		analog.registerDevice(an);
	}
}
