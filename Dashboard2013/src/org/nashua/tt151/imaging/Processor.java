package org.nashua.tt151.imaging;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.nashua.tt151.imaging.Target.Slot;

/**
 * Image Processor - Detects 2013 Targets
 * @author Brian Ashworth
 * @version 1.0
 */
public class Processor {
	/**
	 * The image data to use while processing
	 */
	private int[][] imgData;
	/**
	 * The image data after processing. This requires the imgData to mask loop in the constructor to be uncommented
	 */
	public BufferedImage mask;
	/**
	 * The raw image with the target information overlayed
	 */
	public BufferedImage overlay;
	/**
	 * List of all the targets that were found
	 */
	public ArrayList<Target> targets;
	/**
	 * Process the image for the 2013 Vision Targets
	 * @param img Raw camera feed
	 * @param width Width of the camera feed
	 * @param height Height of the camera feed
	 * @param cameraAngle Angle of the camera - used for distance
	 * @param led 
	 */
	public Processor(Image img, int width, int height, double cameraAngle, double cameraHeight, LED led, boolean devBuild) {
		// Create overlay and mask copies of img
		overlay = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		overlay.getGraphics().drawImage(img, 0, 0, null);
		mask = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		mask.getGraphics().drawImage(img, 0, 0, null);
		// Remove all pixels that are not in a given threshold
		thresholdFilter(width, height, led);
		// Remove all pixels that are not dense enough
		nearestNeighbor(width, height);
		// Remove all "noise" pixels that are not long/wide enough
		consecutiveLineTest(width, height);
		// Search for all possible targets
		targets = findTargets(width, height);
		// Check for overlapping targets (the image is searched left to right and up to down)
		if (targets.size()>1) {
			ArrayList<Target> ts = new ArrayList<Target>();
			for (int i=0; i<targets.size(); i++) {
				Target t = targets.get(i);
				boolean overlap = false;
				// Only check previous targets. Otherwise all copies will be removed
				for (int j=0; j<i; j++) {
					Target t2 = targets.get(j);
					if (t2.intersects(t)) {
						overlap = true;
						break;
					}
				}
				// Add overlapping target to remove list
				if (overlap) {
					ts.add(t);
				}
			}
			// Remove all overlapping targets
			targets.removeAll(ts);
 		}
		for (int i=0; i<targets.size()-1; i++) {
			for (int j=i+1; j<targets.size(); j++) {
				if (targets.get(i).slot.getID()>targets.get(j).slot.getID()) {
					targets.add(i, targets.remove(j));
				} else if (targets.get(i).slot.getID()==targets.get(j).slot.getID()) {
					if (targets.get(i).cx>targets.get(j).cx) {
						targets.add(i, targets.remove(j));
					}
				}
			}
		}
		// Paint imgData to mask
		if (devBuild) {
			for (int r = 0; r < height; r++) {
				for (int c = 0; c < width; c++) {
					mask.setRGB(c, r, imgData[r][c]);
				}
			}
		}
		// Paint targets and their information onto the images
		Graphics mg = mask.getGraphics();
		Graphics og = overlay.getGraphics();
		for (int i=0;i<targets.size();i++) {
			Target t = targets.get(i);
			if (t==null) continue;
			// Compute distance, hypot, and offset
			if (cameraAngle!=0) {
				t.distance = Math.abs((t.slot.getHeightToCenter()-cameraHeight)/Math.tan(Math.toRadians(cameraAngle)));
				t.hypot = Math.sqrt(Math.pow(t.distance,2)+Math.pow(t.slot.getHeightToCenter(),2));
			} else {
				t.distance = -1;
				t.hypot = -1;
			}
			t.offset = (t.x+t.w/2.0)-160.0;
			if (t.slot.equals(Slot.TOP)) {
				mg.setColor(t.distance<192 ? Color.BLUE : t.distance>240 ? Color.RED : Color.GREEN);
				og.setColor(t.distance<192 ? Color.BLUE : t.distance>240 ? Color.RED : Color.GREEN);
			} else if (t.slot.equals(Slot.MIDDLE)) {
				mg.setColor(t.distance<113 ? Color.BLUE : t.distance>118 ? Color.RED : Color.GREEN);
				og.setColor(t.distance<113 ? Color.BLUE : t.distance>118 ? Color.RED : Color.GREEN);
			} else {
				mg.setColor(Color.YELLOW);
				og.setColor(Color.YELLOW);
			}
			// Paint target
			mg.fillRect((int)t.x,(int)t.y,(int)t.w,(int)t.h);
			og.fillRect((int)t.x,(int)t.y,(int)t.w,(int)t.h);
			// Set up graphics
			String st = "";
			int w = 0;
			mg.setColor(Color.GRAY);
			og.setColor(Color.WHITE);
			mg.setFont(mg.getFont().deriveFont(12f));
			og.setFont(og.getFont().deriveFont(12f));
			// Paint Slot String
			w = og.getFontMetrics().stringWidth(""+t.slot);
			mg.drawString(""+t.slot,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2+6));
			og.drawString(""+t.slot,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2+6));
			// Paint distance in feet and inches
			st = String.format("%s' %.1f\"",(int)(t.distance/12),t.distance%12);
			w = og.getFontMetrics().stringWidth(st);
			//mg.drawString(st,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2-5));
			og.drawString(st,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2-5));
			// Paint size in pixels
			st = String.format("%s x %s", t.w, t.h);
			w = og.getFontMetrics().stringWidth(st);
			mg.drawString(st,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2-5));
			// Paint offset in feet and inches
			/*st = String.format("%s' %.1f\"", (int)(t.offset/12), t.offset%12);
			w = mg.getFontMetrics().stringWidth(st);
			mg.drawString(st,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2+mg.getFontMetrics().getHeight()));
			og.drawString(st,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2+og.getFontMetrics().getHeight()));*/
			st = String.format("%s px", t.offset);
			w = mg.getFontMetrics().stringWidth(st);
			mg.drawString(st,(int)(t.x+t.w/2-w/2),(int)(t.y+t.h/2+mg.getFontMetrics().getHeight()));
		}
		mg.fillRect(0, 119, 320, 2);
		mg.fillRect(159, 0, 2, 240);
		og.fillRect(0, 119, 320, 2);
		og.fillRect(159, 0, 2, 240);
		// Add timestamps
		mg.setColor(Color.MAGENTA);
		og.setColor(Color.MAGENTA);
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		String s = df.format(Calendar.getInstance().getTime());
		mg.drawString(s, width-mg.getFontMetrics().stringWidth(s)-5, height-mg.getFontMetrics().getHeight()+10);
		og.drawString(s, width-og.getFontMetrics().stringWidth(s)-5, height-og.getFontMetrics().getHeight()+10);
	}
	
	/**
	 * Verify that the borders are long enough
	 * @param width Width of the buffer
	 * @param height Height of the buffer
	 */
	private void consecutiveLineTest(int width, int height) {
		// Check widths
		for (int y = 0; y < height; y++) {
			int start = -1;
			for (int x = 0; x < width; x++) {
				if (imgData[y][x] == 0) {
					if (start == -1) {
						start = x;
					}
				} else if (start != -1) {
					// Verify that the width is at least 23 pixels
					if (x - start > 23) {
						for (int c = start; c < x; c++) {
							imgData[y][c] = 0xFF00;
						}
					}
					start = -1;
				}
			}
		}
		// Check heights
		for (int x = 0; x < width; x++) {
			int start = -1;
			for (int y = 0; y < height; y++) {
				if (imgData[y][x] != 0xFFFFFF) {
					if (start == -1) {
						start = y;
					}
				} else if (start != -1) {
					for (int r = start; r < y; r++) {
						// Verify that the height is at least 17 pixels
						imgData[r][x] = (y - start > 17 || imgData[r][x] == 0xFF00) ? 0
								: 0xFFFFFF;
					}
					start = -1;
				}
			}
		}
	}

	/**
	 * Search for the target given a center point. The algorithm looks from the center out to the edges
	 * @param width Width of the buffer
	 * @param height Height of the buffer
	 * @param cx Center X location
	 * @param cy Center Y location
	 * @return The target that was found or null
	 */
	private Target findRect(int width, int height, int cx, int cy) {
		try {
			// Center is black. Gap not found
			if (imgData[cy][cx] == 0)
				return null;
			// Set Upper Y and Down Y to Center Y
			int UY = cy;
			int DY = cy;
			// Go up until black
			while (UY > 0 && imgData[UY][cx] != 0)
				UY--;
			// Go down until black
			while (DY < height && imgData[DY][cx] != 0)
				DY++;
			// Find average width
			int tw = 0, ax = 0;
			for (int y = UY; y < DY; y++) {
				int LX = cx, RX = cx;
				// Go left until black
				while (LX > 0 && imgData[cy][LX] != 0)
					LX--;
				// Go right until black
				while (RX < width && imgData[cy][RX] != 0)
					RX++;
				// Add width and left x to the average values 
				tw += RX - LX + 1;
				ax += LX;
			}
			// Compute average width
			tw /= DY - UY;
			ax /= DY - UY;
			// Computer height
			int th = DY - UY;
			// Return target if valid
			if (Target.Slot.isValid(tw, th))
				return new Target(ax, UY, tw, th, cx, cy);
		} catch (ArithmeticException e) {
		}
		// No valid target found
		return null;
	}

	/**
	 * Find the targets. The algorithm looks left to right and top to bottom.
	 * @param width Width of the buffer
	 * @param height Height of the buffer
	 * @return Targets found
	 */
	private ArrayList<Target> findTargets(int width, int height) {
		ArrayList<Target> targets = new ArrayList<Target>();
		int cx = 0; // X Sum; Used to find center
		int cy = 0; // Y Sum; Used to find center
		int cnt = 0; // Amount of pixels in the sums; Used to find center
		// Look for targets vertically
		for (int y = 0; y < height; y++) {
			int rc = 0; // Amount of pixels found in the row; Used to know when to stop
			for (int x = 0; x < width; x++) {
				// If the pixel is black, add the x to the x sum, y to the y sum, and increment cnt and rc.
				if (imgData[y][x] == 0) {
					cx = cx + x;
					cy = cy + y;
					cnt++;
					rc++;
				}
			}
			if (rc==0 && cnt>0) {
				// Bottom of target reached. Verify that the target has enough area.
				if (cnt>200) {
					// Compute center
					cx = cx/cnt;
					cy = cy/cnt;
					// Search for the target
					Target t = findRect(width, height, cx, cy);
					if (t != null)
						targets.add(t);
				}
				// Reset variables
				cx = 0;
				cy = 0;
				cnt = 0;
			}
		}
		// Reset variables
		cx = 0;
		cy = 0;
		cnt = 0;
		// Search for targets horizontally
		for (int x = 0; x < width; x++) {
			int cc = 0; // Column Count; Used to identify when to stop
			for (int y = 0; y < height; y++) {
				// If the pixel is black, add the x to the x sum, y to the y sum, and increment cnt and cc.
				if (imgData[y][x] == 0) {
					cx = cx + x;
					cy = cy + y;
					cnt++;
					cc++;
				}
			}
			if (cc==0 && cnt>0) {
				// Right side of target found. Verify the area is dense enough
				if (cnt>175) {
					// Calculate the center
					cx = cx/cnt;
					cy = cy/cnt;
					// Search for the target
					Target t = findRect(width, height, cx, cy);
					if (t != null)
						targets.add(t);
				}
				// Reset the variables
				cx = 0;
				cy = 0;
				cnt = 0;
			}
		}
		return targets;
	}

	/**
	 * Check the density of a 5x5 area. The density must be 10 of the 25 pixels (40%)
	 * @param width Width of the buffer
	 * @param height Height of the buffer
	 */
	private void nearestNeighbor(int width, int height) {
		int w = 2; // Area radius
		int t = 10; // Threshold
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int cnt = 0;
				for (int r = y - w; r < y + w; r++) {
					for (int c = x - w; c < x + w; c++) {
						if (imgData[y][x] == 0)
							cnt++;
					}
				}
				// Verify density is over 10 out of 25 (40%) in the 5x5 area
				imgData[y][x] = cnt >= t ? 0xFFFFFF : 0;
			}
		}
	}

	/**
	 * Threshold filter. Verify that the values are within the RGB and HSV restrictions.
	 * This filter also converts the image to the buffer
	 * @param width Width of the image
	 * @param height Height of the image
	 */
	private void thresholdFilter(int width, int height, LED led) {
		// Create imgData buffer
		imgData = new int[height][width];
		// Check each pixels
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				// Get RGB value
				int rgb = mask.getRGB(c, r);
				// Extract each component using bitwise operators
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = (rgb) & 0xFF;
				// Convert to HSB (also known as HSV)
				float[] hsb = rgbToHSB(red / 255f, green / 255f, blue / 255f);
				// Check threshold
				if (!led.testPixel(red, green, blue, (int)(hsb[0]*255), (int)(hsb[1]*255), (int)(hsb[2]*255)))
					imgData[r][c] = 0;
				else
					imgData[r][c] = 0xFFFFFF;
			}
		}
	}

	/**
	 * Convert RGB to HSB
	 * @param r Red value (0 to 1)
	 * @param g Green value (0 to 1)
	 * @param b Blue value (0 to 1)
	 * @return HSB
	 */
	public static float[] rgbToHSB(float r, float g, float b) {
		float[] hsv = new float[3];
		float min, max, delta;
		min = r < g ? r < b ? r : b : g < b ? g : b;
		max = r > g ? r > b ? r : b : g > b ? g : b;
		if (max == 0)
			return hsv;
		hsv[2] = max;
		delta = max - min;
		hsv[1] = delta / max;
		hsv[0] = r == max ? (g - b) / delta : g == max ? 2 + (b - r) / delta
				: 4 + (r - g) / delta;
		hsv[0] = hsv[0] * 60;
		if (hsv[0] < 0)
			hsv[0] = hsv[0] + 360;
		hsv[0] = hsv[0] / 360;
		hsv[2] /= 255.0;
		return hsv;
	}
}
