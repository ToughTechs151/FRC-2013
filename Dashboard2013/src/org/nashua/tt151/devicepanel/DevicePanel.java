package org.nashua.tt151.devicepanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.UIManager;

import org.nashua.tt151.devices.Device;
import org.nashua.tt151.devices.EmptyDevice;

public class DevicePanel<E extends Device> extends JPanel {
	private static final int SLOTS_PER_COLUMN = 10;
	private ArrayList<E> devices = new ArrayList<E>();
	private String name;
	private int slots;
	
	public DevicePanel(String name, int slots) {
		this.name = name;
		this.slots = slots;
		clearDevices();
		setPreferredSize(new Dimension(Device.getDeviceWidth()*(slots/SLOTS_PER_COLUMN+(slots%SLOTS_PER_COLUMN==0?0:1))+4, SLOTS_PER_COLUMN*Device.getDeviceHeight()+25));
		setSize(getPreferredSize());
		setBackground(Color.DARK_GRAY.darker());
	}
	
	public void clearDevice(int slot) {
		if (devices.size()<slot) {
			devices.add(null);
		} else {
			devices.set(slot-1, null);
		}
	}

	public void clearDevices() {
		for (int i=0; i<slots; i++) {
			clearDevice(i+1);
		}
	}

	public E getDevice(int i) {
		return devices.get(i-1);
	}

	public ArrayList<E> getDevices() {
		return devices;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSlots() {
		return slots;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(getBackground().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getBackground());
		g.fillRect(2, 2, getWidth()-4, getHeight()-4);
		int x = 2;
		int y = 2;
		for (int i=0;i<slots;i++) {
			E dev = devices.get(i); 
			if (dev==null) {
				EmptyDevice de = new EmptyDevice(i+1);
				de.paintComponent(g.create(x+de.getWidth()*((de.getSlot()-1)/SLOTS_PER_COLUMN), y+de.getHeight()*((de.getSlot()-1)%SLOTS_PER_COLUMN), de.getWidth(), de.getHeight()));
			} else {
				dev.paintComponent(g.create(x+dev.getWidth()*((dev.getSlot()-1)/SLOTS_PER_COLUMN), y+dev.getHeight()*((dev.getSlot()-1)%SLOTS_PER_COLUMN), dev.getWidth(), dev.getHeight()));
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(name.toUpperCase(), getWidth()/2-g.getFontMetrics().stringWidth(name.toUpperCase())/2, getHeight()-SLOTS_PER_COLUMN);
	}
	
	public void registerDevice(E dev) {
		devices.remove(dev.getSlot()-1);
		devices.add(dev.getSlot()-1, dev);
	}
}
