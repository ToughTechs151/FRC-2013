package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.nashua.tt151.ui.SlimWindow;

public class BandwidthMonitor extends SlimWindow {
	private static final Sigar s = new Sigar();
	private static double bytes = 0;
	private static double current = 0;
	private static double average = 0;
	private static double[] averageBuffer= new double[20];
	private static double averageTemp = 0.0;
	private static int count = 0;
	private static double peak = 0;
	private static int indices = 0;
	public BandwidthMonitor() {
		super("Bandwidth Monitor");
		new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
			public void run() {
				try {
					for (String ifc:s.getNetInterfaceList()) {
						NetInterfaceConfig nic = s.getNetInterfaceConfig(ifc);
						if (!nic.getAddress().equals("0.0.0.0") && !nic.getType().contains("Loopback")) {
							NetInterfaceStat nis = s.getNetInterfaceStat(ifc);
							double nBytes = nis.getTxBytes()+nis.getRxBytes(); 
							if (bytes>0) {
								current = (nBytes-bytes)*8/1024.0/1024.0;
								if (count == averageBuffer.length-1){
									averageBuffer[count] = current;
									count = 0;
								} else {
									averageBuffer[count] = current;
									count++;
								}
								indices = 0;
								for (int i =0; i < averageBuffer.length; i++){
									averageTemp+=averageBuffer[i];
									if (averageBuffer[i]>0.0) {
										indices++;
									}
								}
								average = averageTemp/indices;
								averageTemp = 0;
								peak = Math.max(current, peak);
							}
							bytes = nBytes;
						}
					}
				} catch (SigarException e) {
					e.printStackTrace();
				}
			}
		}, 1, 1000);
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.setColor(Color.DARK_GRAY.darker().darker().darker());
				g.fillRect(0, 0, getWidth(), getHeight());
				
				g.setFont(g.getFont().deriveFont(24f));
				FontMetrics fm = g.getFontMetrics();
				String str;
				
				g.setColor(current<4 ? Color.GREEN.darker().darker().darker() : current<6 ? Color.YELLOW.darker().darker().darker().darker().darker() : current<7 ? Color.RED.darker().darker().darker() : Color.MAGENTA);
				g.fillRect(0,1,getWidth(),getHeight()/3-2);
				g.setColor(Color.LIGHT_GRAY);
				str = String.format("Current: %.02f Mbps", current);
				g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, (getHeight()/3+2)/2+fm.getHeight()/4);
				
				g.setColor(average<4 ? Color.GREEN.darker().darker().darker() : average<6 ? Color.YELLOW.darker().darker().darker().darker().darker() : average<7 ? Color.RED.darker().darker().darker() : Color.MAGENTA);
				g.fillRect(0,getHeight()/3+1,getWidth(),getHeight()/3-2);
				g.setColor(Color.LIGHT_GRAY);
				str = String.format("Average: %.02f Mbps", average);
				g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, getHeight()/3+1+(getHeight()/3+2)/2+fm.getHeight()/4);
				
				g.setColor(peak<4 ? Color.GREEN.darker().darker().darker() : peak<6 ? Color.YELLOW.darker().darker().darker().darker().darker() : peak<7 ? Color.RED.darker().darker().darker() : Color.MAGENTA);
				g.fillRect(0,getHeight()/3*2+1,getWidth(),getHeight()/3-2);
				g.setColor(Color.LIGHT_GRAY);
				str = String.format("Peak: %.02f Mbps", peak);
				g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, getHeight()/3*2+1+(getHeight()/3+2)/2+fm.getHeight()/4);
				
			}
		};
		setSize(300, 150);
		panel.setBounds(5,20,300,150);
		add(panel);
		setVisible(true);
		new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
			public void run() {
				repaint();
			}
		}, 1, 100);
	}
	public static void main(String[] args) {
		new BandwidthMonitor();
	}
}