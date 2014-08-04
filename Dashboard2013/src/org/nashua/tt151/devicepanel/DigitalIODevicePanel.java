package org.nashua.tt151.devicepanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import org.nashua.tt151.devices.DigitalIODevice;
import org.nashua.tt151.devices.EmptyDevice;

public class DigitalIODevicePanel extends DevicePanel<DigitalIODevice> {
	private static final int SLOTS_PER_COLUMN = 20;
	
	public DigitalIODevicePanel(String name, int slots) {
		super(name, slots);
		clearDevices();
		setPreferredSize(new Dimension(DigitalIODevice.getDeviceWidth()*(slots/SLOTS_PER_COLUMN+(slots%SLOTS_PER_COLUMN==0?0:1))+4, 
				SLOTS_PER_COLUMN*DigitalIODevice.getDeviceHeight()+25));
		setSize(getPreferredSize());
		setBackground(Color.DARK_GRAY.darker());
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(getBackground().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getBackground());
		g.fillRect(2, 2, getWidth()-4, getHeight()-4);
		int x = 2;
		int y = 2;
		for (int i=0;i<getSlots();i++) {
			DigitalIODevice dev = getDevices().get(i); 
			if (dev==null) {
				EmptyDevice de = new EmptyDevice(i+1);
				de.paintComponent(g.create(x+de.getWidth()*((de.getSlot()-1)/SLOTS_PER_COLUMN), y+DigitalIODevice.getDeviceHeight()*((de.getSlot()-1)%SLOTS_PER_COLUMN), de.getWidth(), DigitalIODevice.getDeviceHeight()));
			} else {
				dev.paintComponent(g.create(x+dev.getWidth()*((dev.getSlot()-1)/SLOTS_PER_COLUMN), y+dev.getHeight()*((dev.getSlot()-1)%SLOTS_PER_COLUMN), dev.getWidth(), dev.getHeight()));
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(getName().toUpperCase(), getWidth()/2-g.getFontMetrics().stringWidth(getName().toUpperCase())/2, getHeight()-10);
	}
}
