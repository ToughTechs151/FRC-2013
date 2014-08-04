package org.nashua.tt151.wrappers;

/**
 * The purpose of this class is to be a wrapper for the WPILibJ Victor class. This
 * class will at the functionality to pass a int array in the form {module, slot}.
 * @author Brian Ashworth
 * @version 1.0
 */
public class Victor extends edu.wpi.first.wpilibj.Victor {
    /**
     * Create a Victor object on the given slot on the default module
     * @param slot PWM Slot
     */
    public Victor(int slot) {
        super(slot);
    }
    /**
     * Create a Victor object on the given slot on the give module
     * @param module Digital Module Number
     * @param slot PWM Slot
     */
    public Victor(int module, int slot) {
        super(module, slot);
    }
    /**
     * Create a Victor object on the given slot on the given module
     * @param params Same as Victor(int module, int slot) but as a int array
     */
    public Victor(int[] params) {
        super(params!=null&&params.length>0?params[0]:2, params!=null&&params.length>1?params[1]:0);
    }
}
