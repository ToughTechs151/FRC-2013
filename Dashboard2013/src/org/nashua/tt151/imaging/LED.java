package org.nashua.tt151.imaging;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;


public final class LED {
	private static final Properties CFG = new Properties();
	private static final File CFG_FILE = new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath()+File.separator+"leds.cfg");
	public static final LED AMBER = new LED("Amber", /*R*/200, 255, false, /*G*/0, 255, false, /*B*/0, 255, false, 
			/*H*/0, 255, false, /*S*/0, 255, false, /*V*/0, 255, false); 
	public static final LED BLUE = new LED("Blue", /*R*/0, 255, false, /*G*/0, 255, false, /*B*/230, 255, false,
			/*H*/125, 160, false, /*S*/0, 255, false, /*V*/0, 255, false);
	public static final LED GREEN = new LED("Green", /*R*/0, 50, false, /*G*/0, 255, false, /*B*/58, 255, false,
			/*H**/0, 255, false, /*S*/0, 255, false, /*V*/0, 255, false);
	public static final LED RED = new LED("Red", /*R*/200, 255, false, /*G*/0, 255, false, /*B*/0, 255, false,
			/*H*/110, 190, true, /*S*/0, 255, false, /*V*/0, 255, false);
	public static LED get(String color) {
		for (LED l:values()) {
			if (l.toString().equalsIgnoreCase(color)) {
				return l;
			}
		}
		return RED;
	}
	public static void saveConfig() {
		CFG.clear();
		for (LED l:values()) {
			// RED
			CFG.setProperty(String.format("%s.red.min", l.toString()), ""+l.getRedMinimum());
			CFG.setProperty(String.format("%s.red.max", l.toString()), ""+l.getRedMaximum());
			CFG.setProperty(String.format("%s.red.rev", l.toString()), ""+l.isRedReversed());
			// GREEN
			CFG.setProperty(String.format("%s.green.min", l.toString()), ""+l.getGreenMinimum());
			CFG.setProperty(String.format("%s.green.max", l.toString()), ""+l.getGreenMaximum());
			CFG.setProperty(String.format("%s.green.rev", l.toString()), ""+l.isGreenReversed());
			// BLUE
			CFG.setProperty(String.format("%s.blue.min", l.toString()), ""+l.getBlueMinimum());
			CFG.setProperty(String.format("%s.blue.max", l.toString()), ""+l.getBlueMaximum());
			CFG.setProperty(String.format("%s.blue.rev", l.toString()), ""+l.isBlueReversed());
			// HUE
			CFG.setProperty(String.format("%s.hue.min", l.toString()), ""+l.getHueMinimum());
			CFG.setProperty(String.format("%s.hue.max", l.toString()), ""+l.getHueMaximum());
			CFG.setProperty(String.format("%s.hue.rev", l.toString()), ""+l.isHueReversed());
			// SATURATION
			CFG.setProperty(String.format("%s.saturation.min", l.toString()), ""+l.getSaturationMinimum());
			CFG.setProperty(String.format("%s.saturation.max", l.toString()), ""+l.getSaturationMaximum());
			CFG.setProperty(String.format("%s.saturation.rev", l.toString()), ""+l.isSaturationReversed());
			// VALUE
			CFG.setProperty(String.format("%s.value.min", l.toString()), ""+l.getValueMinimum());
			CFG.setProperty(String.format("%s.value.max", l.toString()), ""+l.getValueMaximum());
			CFG.setProperty(String.format("%s.value.rev", l.toString()), ""+l.isValueReversed());
		}
		try {
			CFG.store(new FileWriter(CFG_FILE), "");
		} catch (IOException e) {
		}
	}
	public static LED[] values() {
		return new LED[]{AMBER, BLUE, GREEN, RED};
	}
	private int bMax;
	private int bMin;
	private boolean bRev;
	private int gMax;
	private int gMin;
	private boolean gRev;
	private int hMax;
	private int hMin;
	private boolean hRev;
	private String name;
	private int rMax;
	private int rMin;
	private boolean rRev;
	private int sMax;
	private int sMin;
	private boolean sRev;
	private int vMax;
	private int vMin;
	private boolean vRev;
	public LED(String name, int rMin, int rMax, boolean rRev, int gMin, int gMax, boolean gRev, int bMin, int bMax, boolean bRev,
			int hMin, int hMax, boolean hRev, int sMin, int sMax, boolean sRev, int vMin, int vMax, boolean vRev) {
		/* If config file has not been loaded, load it */
		if (CFG.isEmpty() && CFG_FILE.exists()) {
			try {
				CFG.load(new FileReader(CFG_FILE));
			} catch (Exception e) {
			}
		}
		// Set Name
		this.name = name;
		/* Structure for loading values
		 * If the configuration has the property, read the value
		 * If not, set the given default value and store in running configuration
		 */
		// Set Red Minimum
		if (CFG.containsKey(String.format("%s.red.min", name))) {
			this.rMin = Integer.parseInt(CFG.getProperty(String.format("%s.red.min", name)));
		} else {
			this.rMin = rMin;
			CFG.setProperty(String.format("%s.red.min", name), ""+this.rMin);
		}
		// Set Red Maximum
		if (CFG.containsKey(String.format("%s.red.max", name))) {
			this.rMax = Integer.parseInt(CFG.getProperty(String.format("%s.red.max", name)));
		} else {
			this.rMax = rMax;
			CFG.setProperty(String.format("%s.red.max", name), ""+this.rMax);
		}
		// Set Red Reversal
		if (CFG.containsKey(String.format("%s.red.rev", name))) {
			this.rRev = Boolean.parseBoolean(CFG.getProperty(String.format("%s.red.rev", name)));
		} else {
			this.rRev = rRev;
			CFG.setProperty(String.format("%s.red.rev", name), ""+this.rRev);
		}
		// Set Green Minimum
		if (CFG.containsKey(String.format("%s.green.min", name))) {
			this.gMin = Integer.parseInt(CFG.getProperty(String.format("%s.green.min", name)));
		} else {
			this.gMin = gMin;
			CFG.setProperty(String.format("%s.green.min", name), ""+this.gMin);
		}
		// Set Green Maximum
		if (CFG.containsKey(String.format("%s.green.max", name))) {
			this.gMax = Integer.parseInt(CFG.getProperty(String.format("%s.green.max", name)));
		} else {
			this.gMax = gMax;
			CFG.setProperty(String.format("%s.green.max", name), ""+this.gMax);
		}
		// Set Green Reversal
		if (CFG.containsKey(String.format("%s.green.rev", name))) {
			this.gRev = Boolean.parseBoolean(CFG.getProperty(String.format("%s.green.rev", name)));
		} else {
			this.gRev = gRev;
			CFG.setProperty(String.format("%s.green.rev", name), ""+this.gRev);
		}
		// Set Blue Minimum
		if (CFG.containsKey(String.format("%s.blue.min", name))) {
			this.bMin = Integer.parseInt(CFG.getProperty(String.format("%s.blue.min", name)));
		} else {
			this.bMin = bMin;
			CFG.setProperty(String.format("%s.blue.min", name), ""+this.bMin);
		}
		// Set Blue Maximum
		if (CFG.containsKey(String.format("%s.blue.max", name))) {
			this.bMax = Integer.parseInt(CFG.getProperty(String.format("%s.blue.max", name)));
		} else {
			this.bMax = bMax;
			CFG.setProperty(String.format("%s.blue.max", name), ""+this.bMax);
		}
		// Set Blue Reversal
		if (CFG.containsKey(String.format("%s.blue.rev", name))) {
			this.bRev = Boolean.parseBoolean(CFG.getProperty(String.format("%s.blue.rev", name)));
		} else {
			this.bRev = bRev;
			CFG.setProperty(String.format("%s.blue.rev", name), ""+this.bRev);
		}
		// Set Hue Minimum
		if (CFG.containsKey(String.format("%s.hue.min", name))) {
			this.hMin = Integer.parseInt(CFG.getProperty(String.format("%s.hue.min", name)));
		} else {
			this.hMin = hMin;
			CFG.setProperty(String.format("%s.hue.min", name), ""+this.hMin);
		}
		// Set Hue Maximum
		if (CFG.containsKey(String.format("%s.hue.max", name))) {
			this.hMax = Integer.parseInt(CFG.getProperty(String.format("%s.hue.max", name)));
		} else {
			this.hMax = hMax;
			CFG.setProperty(String.format("%s.hue.max", name), ""+this.hMax);
		}
		// Set Hue Reversal
		if (CFG.containsKey(String.format("%s.hue.rev", name))) {
			this.hRev = Boolean.parseBoolean(CFG.getProperty(String.format("%s.hue.rev", name)));
		} else {
			this.hRev = hRev;
			CFG.setProperty(String.format("%s.hue.rev", name), ""+this.hRev);
		}
		// Set Saturation Minimum
		if (CFG.containsKey(String.format("%s.saturation.min", name))) {
			this.sMin = Integer.parseInt(CFG.getProperty(String.format("%s.saturation.min", name)));
		} else {
			this.sMin = sMin;
			CFG.setProperty(String.format("%s.saturation.min", name), ""+this.sMin);
		}
		// Set Saturation Minimum
		if (CFG.containsKey(String.format("%s.saturation.max", name))) {
			this.sMax = Integer.parseInt(CFG.getProperty(String.format("%s.saturation.max", name)));
		} else {
			this.sMax = sMax;
			CFG.setProperty(String.format("%s.saturation.max", name), ""+this.sMax);
		}
		// Set Saturation Reversal
		if (CFG.containsKey(String.format("%s.saturation.rev", name))) {
			this.sRev = Boolean.parseBoolean(CFG.getProperty(String.format("%s.saturation.rev", name)));
		} else {
			this.sRev = sRev;
			CFG.setProperty(String.format("%s.saturation.rev", name), ""+this.sRev);
		}
		// Set Value Minimum
		if (CFG.containsKey(String.format("%s.value.min", name))) {
			this.vMin = Integer.parseInt(CFG.getProperty(String.format("%s.value.min", name)));
		} else {
			this.vMin = vMin;
			CFG.setProperty(String.format("%s.value.min", name), ""+this.vMin);
		}
		// Set Value Maximum
		if (CFG.containsKey(String.format("%s.value.max", name))) {
			this.vMax = Integer.parseInt(CFG.getProperty(String.format("%s.value.max", name)));
		} else {
			this.vMax = vMax;
			CFG.setProperty(String.format("%s.value.max", name), ""+this.vMax);
		}
		// Set Value Reversal
		if (CFG.containsKey(String.format("%s.value.rev", name))) {
			this.vRev = Boolean.parseBoolean(CFG.getProperty(String.format("%s.value.rev", name)));
		} else {
			this.vRev = vRev;
			CFG.setProperty(String.format("%s.value.rev", name), ""+this.vRev);
		}
	}
	public int getBlueMaximum() {
		return bMax;
	}
	public int getBlueMinimum() {
		return bMin;
	}
	public int getGreenMaximum() {
		return gMax;
	}
	public int getGreenMinimum() {
		return gMin;
	}
	public int getHueMaximum() {
		return hMax;
	}
	public int getHueMinimum() {
		return hMin;
	}
	public int getRedMaximum() {
		return rMax;
	}
	public int getRedMinimum() {
		return rMin;
	}
	public int getSaturationMaximum() {
		return sMax;
	}
	public int getSaturationMinimum() {
		return sMin;
	}
	public int getValueMaximum() {
		return vMax;
	}
	public int getValueMinimum() {
		return vMin;
	}
	public boolean isBlueReversed() {
		return bRev;
	}
	public boolean isGreenReversed() {
		return gRev;
	}
	public boolean isHueReversed() {
		return hRev;
	}
	public boolean isRedReversed() {
		return rRev;
	}
	public boolean isSaturationReversed() {
		return sRev;
	}
	public boolean isValueReversed() {
		return vRev;
	}
	public void setBlueMaximum(int blue) {
		bMax = blue;
	}
	public void setBlueMinimum(int blue) {
		bMin = blue;
	}
	public void setBlueReversed(boolean rev) {
		bRev = rev;
	}
	public void setGreenMaximum(int green) {
		gMax = green;
	}
	public void setGreenMinimum(int green) {
		gMin = green;
	}
	public void setGreenReversed(boolean rev) {
		gRev = rev;
	}
	public void setHueMaximum(int hue) {
		hMax = hue;
	}
	public void setHueMinimum(int hue) {
		hMin = hue;
	}
	public void setHueReversed(boolean rev) {
		hRev = rev;
	}
	public void setRedMaximum(int red) {
		rMax = red;
	}
	public void setRedMinimum(int red) {
		rMin = red;
	}
	public void setRedReversed(boolean rev) {
		rRev = rev;
	}
	public void setSaturationMaximum(int sat) {
		sMax = sat;
	}
	public void setSaturationMinimum(int sat) {
		sMin = sat;
	}
	public void setSaturationReversed(boolean rev) {
		sRev = rev;
	}
	public void setValueMaximum(int val) {
		vMax = val;
	}
	public void setValueMinimum(int val) {
		vMin = val;
	}
	public void setValueReversed(boolean val) {
		vRev = val;
	}
	public boolean testPixel(int r, int g, int b, int h, int s, int v) {
		if ((!rRev && r<=rMax && r>=rMin) || (rRev && (r>=rMax || r<=rMin))) {
			if ((!gRev && g<=gMax && g>=gMin) || (gRev && (g>=gMax || g<=gMin))) {
				if ((!bRev && b<=bMax && b>=bMin) || (bRev && (b>=bMax || b<=bMin))) {
					if ((!hRev && h<=hMax && h>=hMin) || (hRev && (h>=hMax || h<=hMin))) {
						if ((!sRev && s<=sMax && s>=sMin) || (sRev && (s>=sMax || s<=sMin))) {
							if ((!vRev && v<=vMax && v>=vMin) || (vRev && (v>=vMax || v<=vMin))) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	public String toString() {
		return name;
	}
}
