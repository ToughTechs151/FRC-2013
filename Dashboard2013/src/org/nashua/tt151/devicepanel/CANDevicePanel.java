package org.nashua.tt151.devicepanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import org.nashua.tt151.devices.CANDevice;

public class CANDevicePanel extends DevicePanel<CANDevice> {
	private static final int SLOTS_PER_COLUMN = 5;
	
	public CANDevicePanel(String name, int slots) {
		super(name, slots);
		setPreferredSize(new Dimension(CANDevice.getDeviceWidth()*(slots/SLOTS_PER_COLUMN+(slots%SLOTS_PER_COLUMN==0?0:1))+4, 
				SLOTS_PER_COLUMN*CANDevice.getDeviceHeight()+25));
		setSize(getPreferredSize());
	}
	public void clearDevice(int boardID) {
		ArrayList<CANDevice> devs = getDevices();
		for (int i=0; i<devs.size(); i++) {
			if (devs.get(i).getBoardID()==boardID) {
				devs.remove(i);
				i--;
			}
		}
	}
	public void clearDevices() {
		getDevices().clear();
	}
	
	public CANDevice getDevice(int boardID) {
		for (CANDevice dev:getDevices()) {
			if (dev.getBoardID()==boardID) {
				return dev;
			}
		}
		return null;
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY.darker().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.DARK_GRAY.darker());
		g.fillRect(2, 2, getWidth()-4, getHeight()-4);
		int x = 2;
		int y = 2;
		for (CANDevice dev:getDevices()) {
			if (dev==null) continue;
			dev.paintComponent(g.create(x, y, dev.getWidth(), dev.getHeight()));
			y += dev.getHeight();
		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(getName().toUpperCase(), getWidth()/2-g.getFontMetrics().stringWidth(getName().toUpperCase())/2, getHeight()-10);
	}
	public void registerDevice(CANDevice dev) {
		clearDevice(dev.getBoardID());
		getDevices().add(dev);
		Collections.sort(getDevices());
	}
}
