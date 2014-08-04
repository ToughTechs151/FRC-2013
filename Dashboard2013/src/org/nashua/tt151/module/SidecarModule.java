package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.nashua.tt151.devicepanel.DevicePanel;
import org.nashua.tt151.devicepanel.DigitalIODevicePanel;
import org.nashua.tt151.devices.DigitalIODevice;
import org.nashua.tt151.devices.PWMOutDevice;
import org.nashua.tt151.devices.RelayDevice;

public class SidecarModule extends JPanel {
	private DigitalIODevicePanel digIO = new DigitalIODevicePanel("Digital I/O", 14);
	private String name;
	private DevicePanel<PWMOutDevice> pwmOut = new DevicePanel<PWMOutDevice>("PWM Out", 10);
	private DevicePanel<RelayDevice> relays = new DevicePanel<RelayDevice>("Relays", 8);
	public SidecarModule(String name) {
		this.name = name;
		setPreferredSize(new Dimension(pwmOut.getWidth()+digIO.getWidth()+relays.getWidth()+4, digIO.getHeight() + 25));
		setSize(getPreferredSize());
	}
	public void clearDigitalIODevice(int slot) {
		digIO.clearDevice(slot);
	}
	public void clearDigitalIODevices() {
		digIO.clearDevices();
	}
	public void clearPWMOutDevice(int slot) {
		pwmOut.clearDevice(slot);
	}
	public void clearPWMOutDevices() {
		pwmOut.clearDevices();
	}
	public void clearRelayDevice(int slot) {
		relays.clearDevice(slot);
	}
	public void clearRelayDevices() {
		relays.clearDevices();
	}
	public DigitalIODevice getDigitalIODevice(int slot) {
		return digIO.getDevice(slot);
	}
	public PWMOutDevice getPWMOutDevice(int slot) {
		return pwmOut.getDevice(slot);
	}
	public RelayDevice getRelayDevice(int slot) {
		return relays.getDevice(slot);
	}
	public void paintComponent(Graphics g){
		g.setColor(Color.DARK_GRAY.darker().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		pwmOut.paintComponent(g.create(2, 2, pwmOut.getWidth(), pwmOut.getHeight()));
		digIO.paintComponent(g.create(2+pwmOut.getWidth(), 2, digIO.getWidth(), digIO.getHeight()));
		relays.paintComponent(g.create(2+pwmOut.getWidth()+digIO.getWidth(), 2, relays.getWidth(), relays.getHeight()));
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(name.toUpperCase(), getWidth()/2-g.getFontMetrics().stringWidth(name.toUpperCase())/2, getHeight()-10);
	}
	public void registerDigitalIODevice(DigitalIODevice dig) {
		digIO.registerDevice(dig);
	}
	public void registerPWMOutDevice(PWMOutDevice pwm) {
		pwmOut.registerDevice(pwm);
	}
	public void registerRelayDevice(RelayDevice relay) {
		relays.registerDevice(relay);
	}
}
