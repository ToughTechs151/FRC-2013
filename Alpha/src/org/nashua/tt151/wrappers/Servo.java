package org.nashua.tt151.wrappers;

/**
 * The purpose of this class is to be a wrapper for the WPILibJ Servo class. This
 * class will at the functionality to pass a int array in the form {module, slot}.
 * @author Brian Ashworth
 * @version 1.0
 */
public class Servo extends edu.wpi.first.wpilibj.Servo {
    /**
     * Create a Servo object on the given slot on the default module
     * @param slot PWM Slot
     */
    public Servo(int slot) {
        super(slot);
    }
    /**
     * Create a Servo object on the given slot on the give module
     * @param module Digital Module Number
     * @param slot PWM Slot
     */
    public Servo(int module, int slot) {
        super(module, slot);
    }
    /**
     * Create a Servo object on the given slot on the given module
     * @param params Same as Servo(int module, int slot) but as a int array
     */
    public Servo(int[] params) {
        super(params!=null&&params.length>0?params[0]:2, params!=null&&params.length>1?params[1]:0);
    }
}
