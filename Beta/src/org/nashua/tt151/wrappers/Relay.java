package org.nashua.tt151.wrappers;

/**
 * The purpose of this class is to be a wrapper for the WPILibJ Relay class. This
 * class will at the functionality to pass a int array in the form {module, slot}.
 * The class also adds conveniency constructors for setting the Direction.
 * @author Brian Ashworth
 * @version 2.0
 */
public class Relay extends edu.wpi.first.wpilibj.Relay {
    private Direction currentDirection = Direction.kBoth;
    /**
     * Create a new Relay on the given slot on the default module.
     * @param slot - Relay slot on digital sidecar
     */
    public Relay(int slot) {
        super(slot);
    }
    /**
     * Create a new Relay on the given slot on the default module. This also adds
     * the conveniency of setting the direction restriction.
     * @param slot - Relay slot on digital sidecar
     * @param dir - Direction restriction for the relay
     */
    public Relay(int slot, Direction dir) {
        super(slot);
        setDirection(dir);
    }
    /**
     * Create a new Relay on the given slot on the given module.
     * @param module - Module on the cRIO that the digital sidecar is in
     * @param slot - Relay slot on digital sidecar
     */
    public Relay(int module, int slot) {
        super(module, slot);
    }
    /**
     * Create a new Relay on the given slot on the given module. This also adds
     * the conveniency of setting the direction restriction.
     * @param module - Module on the cRIO that the digital sidecar is in
     * @param slot - Relay slot on digital sidecar
     * @param dir - Direction restriction for the relay
     */
    public Relay(int module, int slot, Direction dir) {
        super(module, slot);
        setDirection(dir);
    }
    /**
     * Create a new Relay on the given slot on the given module. 
     * @param params - Same as Relay(int module, int slot) but as a int array
     */
    public Relay(int[] params) {
        super(params!=null&&params.length>0?params[0]:2, params!=null&&params.length>1?params[1]:0);
    }
    /**
     * Create a new Relay on the given slot on the given module. This also adds
     * the conveniency of setting the direction restriction.
     * @param params - Same as Relay(int module, int slot) but as a int array
     * @param dir - Direction restriction for the relay
     */
    public Relay(int[] params, Direction dir) {
        super(params!=null&&params.length>0?params[0]:2, params!=null&&params.length>1?params[1]:0);
        setDirection(dir);
    }
    /**
     * Retrieves the current direction restrictions.
     * @return Direction restriction
     */
    public Direction getDirection() {
        return currentDirection;
    }
    /**
     * An override of edu.wpi.first.wpilibj.Relay.setDirection that allows for storing the direction restrictions.
     * @param d - Direction restriction to set
     * @see The edu.wpi.first.wpilibj.Relay.setDirection(edu.wpi.first.wpilibj.Relay.Direction) method for more information.
     */
    public final void setDirection(Direction d) {
        currentDirection = d;
        super.setDirection(d);
    }
}
