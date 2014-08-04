package edu.wpi.first.wpilibj.templates;

/**
 * This class stores all the information on the various targets.
 * @author Brian Ashworth
 * @version 1.0
 */
public final class Target {
    /**
     * This is a fake target that allows for identifying the lowest visible target.
     */
    public static final Target LOWEST = new Target(0, 0, 0, 0);
    /**
     * This is the bottom target. The opening is 29"x24" and it is 19" off the ground.
     */
    public static final Target BOTTOM = new Target(1, 29, 24, 19);
    /**
     * This is the middle right target. The opening is 54"x21" and it is 88 5/8" off the ground.
     */
    public static final Target MIDDLE_RIGHT = new Target(2, 54, 21, 88.625);
    /**
     * This is the middle left target. The opening is 54"x21" and it is 88 5/8" off the ground.
     */
    public static final Target MIDDLE_LEFT = new Target(3, 54, 21, 88.625);
    /**
     * This is the top target. The opening is 54"x12" and it is 104 1/8" off the ground.
     */
    public static final Target TOP = new Target(4, 54, 12, 104.125);
    /**
     * This is a fake target that allows for identifying the highest visible target.
     */
    public static final Target HIGHEST = new Target(5, 0, 0, 0);
    
    private int id;
    private double slotWidth;
    private double slotHeight;
    private double heightToSlot;
    /**
     * The Target class is used for identifying the targets on the field and their properties.
     * @param id - Target ID
     * @param sw - Width of slot in inches
     * @param sh - Height of slot in inches
     * @param hts - Height to slot in inches
     */
    private Target(int id, double sw, double sh, double hts) {
        this.id = id;
        this.slotWidth = sw;
        this.slotHeight = sh;
        this.heightToSlot = hts;
    }
    /**
     * Retrieves the height to the center of the target
     * @return The height to the center of the target in inches
     */
    public double getHeightToCenter() {
        return getHeightToSlot()+getSlotHeight()/2.0;
    }
    /**
     * Retrieves the height to the slot
     * @return The height to the slot in inches
     */
    public double getHeightToSlot() {
        return heightToSlot;
    }
    /**
     * Retrieves the ID of the target. This is only used for communication
     * between the cRIO and the dashboard on the laptop.
     * @return Target ID
     */
    public int getID() {
        return id;
    }
    /**
     * Retrieves the height of the slot
     * @return The height of the slot in inches
     */
    public double getSlotHeight() {
        return slotHeight;
    }
    /**
     * Retrieves the width of the slot
     * @return The width of the slot in inches
     */
    public double getSlotWidth() {
        return slotWidth;
    }
    /**
     * Override of java.lang.Object.toString. A textual representation of the Target object.
     * @return A textual representation of the Target object
     */
    public String toString() {
        return "org.nashua.tt151.Target[id="+getID()+", slotWidth="+getSlotWidth()+", slotHeight="+getSlotHeight()
                +", heightToSlot="+getHeightToSlot()+", heightToCenter="+getHeightToCenter()+"]";
    }
}