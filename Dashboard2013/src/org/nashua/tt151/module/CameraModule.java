package org.nashua.tt151.module;

import ipcapture.IPCapture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.nashua.tt151.Dashboard;
import org.nashua.tt151.imaging.LED;
import org.nashua.tt151.imaging.Processor;
import org.nashua.tt151.imaging.Target;

import processing.core.PApplet;

public class CameraModule extends JPanel {
	private static final double CAMERA_HEIGHT = 10.25; // Inches
	private IPCapture cam;
	private double cameraAngle = 0;
	private boolean devBuild = false;
	private BufferedImage display = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
	private LED led = LED.RED;
	private Processor p;
	private Image raw;
	public CameraModule(String ip, boolean dev) {
		devBuild = dev;
		cam = new IPCapture(new PApplet(), "http://"+ip+"/mjpg/video.mjpg", "FRC", "FRC");
		cam.init(320, 240, IPCapture.RGB);
		cam.start();
		new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
			public void run() {
				try {
					if (!cam.isAlive()) {
						cam.start();
					} else if (cam.isAvailable()) {
						try {
							update();
						} catch (Exception e) {
						}
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}, 1, 33);
		if (devBuild) {
			setSize(640, 300);
			setPreferredSize(new Dimension(640, 300));
		} else {
			setSize(320, 300);
			setPreferredSize(new Dimension(320, 300));
		}
		if (devBuild) {
			addMouseMotionListener(new MouseMotionListener() {
				public void mouseMoved(MouseEvent arg0) {
					try {
						Color c = new Color(display.getRGB(arg0.getX(), arg0.getY()));
						float[] hsv = Processor.rgbToHSB(c.getRed(), c.getGreen(), c.getBlue());
						Dashboard.frame.setTitle(String.format("RGB(%s, %s, %s) HSV(%.f, %.f, %.f)", c.getRed(), c.getGreen(), c.getBlue(), hsv[0]*255, hsv[1]*255, hsv[2]*255));
					} catch (Exception e){
					}
				}
				public void mouseDragged(MouseEvent arg0) {
				}
			});
		}
	}
	public double getCameraAngle() {
		return cameraAngle;
	}
	public LED getLED() {
		return led;
	}
	public Processor getProcessor() {
		return p;
	}
	public void paintComponent(Graphics g) {
		g.drawImage(display, 0, 0, 320, 240, 0, 0, 320, 240, null);
		if (devBuild) {
			if (p!=null && p.mask!=null) {
				g.drawImage(p.mask, 320, 0, 640, 240, 0, 0, 320, 240, null);
			} else {
				g.setColor(Color.BLACK);
				g.fillRect(320, 0, 640, 240);
			}
		}
		g.setFont(new Font("Arial", Font.PLAIN, 48));
		g.setColor(Color.LIGHT_GRAY);
		Target t = new Target(0, 0, 0, 0, 0, 0);
		try {
			t = p.targets.get(0);
		} catch (Exception e) {
		}
		String str = "";
		if (t.slot.equals(Target.Slot.TOP)) {
			str = String.format("%s.f' %.f\"", t.distance/12, t.distance%12);
		}
		g.drawString(str, 160-g.getFontMetrics().stringWidth(str)/2, 290);
	}
	public void setCameraAngle(double cameraAngle) {
		this.cameraAngle = cameraAngle;
	}
	public void setLED(LED led) {
		this.led = led;
	}
	public void update() {
		cam.read();
		raw = cam.getImage();
		p = new Processor(raw, raw.getWidth(null), raw.getHeight(null), cameraAngle, CAMERA_HEIGHT, led, devBuild);
		display = new BufferedImage(raw.getWidth(null), raw.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = display.getGraphics();
		g.drawImage(p.overlay, 0, 0, null);
	}
}
