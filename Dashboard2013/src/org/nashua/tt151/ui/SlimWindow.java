package org.nashua.tt151.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JWindow;

public class SlimWindow extends JWindow {
	private BufferedImage img;
	private Point p;
	public SlimWindow(String title) {
		this(title, Color.DARK_GRAY.darker().darker().darker().darker().darker(), Color.LIGHT_GRAY);
	}
	public SlimWindow(String title, Color bg, Color fg) {
		setName(title);
		setBackground(bg);
		setForeground(fg);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getPoint().y<20) {
					p = e.getPoint();
				}
			}
			public void mouseReleased(MouseEvent e) {
				p = null;
				if (new Rectangle(getWidth()-19, 3, 14, 14).contains(e.getPoint())) {
					setVisible(false);
				}
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent arg0) {}
			public void mouseDragged(MouseEvent arg0) {
				if (p!=null) {
					setLocation(arg0.getLocationOnScreen().x-p.x, arg0.getLocationOnScreen().y-p.y);
				}
			}
		});
		setLayout(null);
	}
	public void paint(Graphics g) {
		BufferedImage cImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		super.paint(cImg.getGraphics());
		img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics bg = img.getGraphics();
		bg.setColor(getBackground().brighter().brighter().brighter());
		bg.fillRect(0, 0, getWidth(), getHeight());
		bg.setColor(getBackground());
		bg.fillRect(1, 1, getWidth()-2, getHeight()-2);
		bg.setColor(getBackground().brighter().brighter().brighter());
		bg.fillRect(4, 19, getWidth()-8, getHeight()-23);
		bg.setColor(getForeground());
		bg.setFont(bg.getFont().deriveFont(Font.BOLD));
		bg.drawString(getName(), 5, 14);
		bg.setColor(Color.RED);
		bg.fillRect(getWidth()-19, 3, 14, 14);
		bg.setColor(Color.WHITE);
		bg.drawString("\u2718", getWidth()-18, 15);
		bg.setFont(bg.getFont().deriveFont(Font.PLAIN));
		bg.drawImage(cImg, 5, 20, getWidth()-5, getHeight()-5, 5, 20, getWidth()-5, getHeight()-5, null);
		g.drawImage(img, 0, 0, null);
	}
	public void setSize(int w, int h) {
		super.setSize(w+10, h+25);
	}
}
