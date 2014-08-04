package org.nashua.tt151.imaging;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

public class Target {
	public enum Slot {
		BOTTOM(1, 29, 24, 19, 7.5),
		HIGHEST(5, 0, 0, 0, 7.5),
		LOWEST(0, 0, 0, 0, 7.5),
		MIDDLE(2, 54, 21, 88.625, 7.5),
		MIDDLE_LEFT(3, 54, 21, 88.625, 7.5),
		MIDDLE_RIGHT(2, 54, 21, 88.625, 7.5),
		TOP(4, 54, 12, 104.125, 7.5),
		UNKNOWN(-1, 0, 0, 0, 7.5); 
		public static Slot getBestFit(Target t) {
			Slot s = Slot.UNKNOWN;
			double perc_error = 10;
			for (Slot sl:new Slot[]{Slot.TOP, Slot.MIDDLE, Slot.BOTTOM}) {
				if (Slot.isValid(t.w, t.h, sl)) {
					double per_error = t.ratio/sl.getRatio();
					if (Math.abs(1-per_error)<Math.abs(1-perc_error)) {
						perc_error = per_error;
						s = sl;
					}
				}
			}
			return s;
		}
		public static Slot getTargetWithID(int id) {
			for (Slot s:Slot.values()) {
				if (s.equals(MIDDLE) || s.getID()==-1) {
					continue;
				}
				if (s.getID()==id) {
					return s;
				}
			}
			return Slot.UNKNOWN;
		}
		public static boolean isValid(double tw, double th) {
			return isValid(tw, th, TOP) || isValid(tw, th, MIDDLE) || isValid(tw, th, BOTTOM);
		}
		public static boolean isValid(double tw, double th, Slot s) {
			double r = tw * 1.0 / th;
			return (r>=s.getMinRatio() && tw>=s.getMinWidth() && th>=s.getMaxHeight()) ||
					(r<=s.getMaxRatio() && tw<=s.getMaxWidth() && th<=s.getMaxHeight());
		}
		private double heightToSlot;
		private int id;
		private double slotHeight;
		private double slotWidth;
		private double volts;
		private Slot(int id, double sw, double sh, double hts, double volts) {
			this.id = id;
			this.slotWidth = sw;
			this.slotHeight = sh;
			this.heightToSlot = hts;
			this.volts = volts;
		}
		public double getHeightToCenter() {
			return getHeightToSlot()+getSlotHeight()/2;
		}
		public double getHeightToSlot() {
			return heightToSlot;
		}
		public int getID() {
			return id;
		}
		public double getMaxHeight() {
			return getMaxRatio()*getSlotHeight();
		}
		public double getMaxRatio() {
			return getRatio()*1.3;
		}
		public double getMaxWidth() {
			return getMaxRatio()*getSlotWidth();
		}
		public double getMinHeight(){
			return getMinRatio()*getSlotHeight();
		}
		public double getMinRatio() {
			return getRatio()*0.7;
		}
		public double getMinWidth(){
			return getMinRatio()*getSlotWidth();
		}
		public double getRatio() {
			return slotWidth/slotHeight;
		}
		public double getSlotHeight() {
			return slotHeight;
		}
		public double getSlotWidth() {
			return slotWidth;
		}
		public double getVolts() {
			return volts;
		}
 		public String toString(){
			return name().startsWith("MIDDLE_")?"M"+name().split("_")[1].substring(0,1):name().substring(0,3);
		}
	}
	private String uuid;
	public double ratio;
	public Slot slot;
	public double x,y,w,h,cx,cy,distance,offset,hypot;
	public Target(double x, double y, double w, double h, double cx, double cy) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.cx=cx;
		this.cy=cy;
		this.distance=0;
		this.offset=0;
		ratio=w*1.0/h;
		uuid = UUID.randomUUID().toString();
		slot = Slot.getBestFit(this);
	}
	public boolean contains(int[] point) {
		return contains(new Point(point[0],point[1]));
	}
	public boolean contains(Point point) {
		return point.x>=x&&point.x<=x+w&&point.y>=y&&point.y<=y+h;
	}
	public boolean equals(Object other) {
		return other instanceof Target ? ((Target)other).getUUID().equals(getUUID()) : false;
	}
	public String getUUID() {
		return uuid;
	}
	public boolean intersects(Target t) {
		return new Rectangle2D.Double(x, y, w, h).intersects(t.x, t.y, t.w, t.h);
	}
	public String toString() {
		return String.format("Target[x=%s,y=%s,w=%s,h=%s,slot=%s,uuid=%s]",x,y,w,h,slot,uuid);
	}
}
