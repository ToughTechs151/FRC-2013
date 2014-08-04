package org.nashua.tt151.wrappers;

/**
 * The purpose of this class is to be a wrapper for the WPILibJ Jaguar class. This
 * class will at the functionality to pass a int array in the form {module, slot}.
 * @author Brian Ashworth
 * @version 1.0
 */
public class Jaguar extends edu.wpi.first.wpilibj.Jaguar {
    /**
     * Create a Jaguar object on the given slot on the default module
     * @param slot PWM Slot
     */
    public Jaguar(int slot) {
        super(slot);
    }
    /**
     * Create a Jaguar object on the given slot on the give module
     * @param module Digital Module Number
     * @param slot PWM Slot
     */
    public Jaguar(int module, int slot) {
        super(module, slot);
    }
    /**
     * Create a Jaguar object on the given slot on the given module
     * @param params Same as Jaguar(int module, int slot) but as a int array
     */
    public Jaguar(int[] params) {
        super(params!=null&&params.length>0?params[0]:2, params!=null&&params.length>1?params[1]:0);
    }
}
