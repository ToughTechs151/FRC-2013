package org.nashua.tt151;

import org.nashua.tt151.libraries.Controller;

/**
 * This class holds all the constants for the robot. Examples include wiring information,
 * servo stop positions, and/or sensor parameters.
 * @author Brian Ashworth
 * @version 1.0
 */
public class RobotMap {
    /**
     * Logitech DualAction F310 Controller (D Mode)
     */
    public static abstract class DualAction {
        /**
         * Axis mappings for the controller
         */
        public static abstract class Axis extends Controller.DualAction.Axis {
            /**
             * Mappings for directional pad buttons. The parameters of the
             * mappings are {AXIS, VALUE}. If VALUE is positive, VALUE is the 
             * minimum value. If VALUE is negative, VALUE is the maximum value.
             */
            public static abstract class DPAD {
                /**
                 * This is the mapping for the directional pad pointing down.
                 */
                public static final double[] DPAD_DOWN = {DPAD_Y, 0.5};
                /**
                 * This is the mapping for the directional pad pointing left.
                 */
                public static final double[] DPAD_LEFT = {DPAD_X, -0.5};
                /**
                 * This is the mapping for the directional pad pointing right.
                 */
                public static final double[] DPAD_RIGHT = {DPAD_X, 0.5};
                /**
                 * This is the mapping for the directional pad pointing up.
                 */
                public static final double[] DPAD_UP = {DPAD_Y, -0.5};
            }
        }
        /**
         * Mappings for the buttons on the controller
         */
        public static abstract class Button extends Controller.DualAction.Button {}
    }
    
    public static abstract class Jaguar {
        public static final int SHOOTER_1 = 2;
        public static final int SHOOTER_2 = 3;
    }
    
    /**
     * Victor wiring diagram {DigitalModuleNumber, PWMOutSlotNumber}
     */
    public static abstract class Victor {
        /**
         * This is the mapping for the victor that controls the south motor in omni drive
         */
        public static final int[] SOUTH = {1,1};
        /**
         * This is the mapping for the victor that controls the west motor in omni drive
         */
        public static final int[] WEST = {1,3};
        /**
         * This is the mapping for the victor that controls the east motor in omni drive
         */
        public static final int[] EAST = {1,2};
        /**
         * This is the mapping for the victor that controls the north motor in omni drive
         */
        public static final int[] NORTH = {1,4};
    }
    
    /**
     * This class holds the wiring information and the stop locations for the servos
     */
    public static abstract class Servo {
        /**
         * This class holds the wiring information and stop location for the camera servo
         */
        public static abstract class Camera {
            /**
             * This wiring information for the Camera Servo
             */
            public static final int[] SLOT = {1, 8};
            /**
             * The lower limit of the servo
             */
            public static final double MINIMUM = 0.1000;
            /**
             * The upper limit of the servo
             */
            public static final double MAXIMUM = 0.4700;
            /**
             * The default location for the servo
             */
            public static final double DEFAULT = 0.3930;
        }
    }
    
    /**
     * This class contains the wiring information for the relays
     */
    public static abstract class Relay {
        /**
         * The wiring information for the relay controlling the LED
         */
        public static final int[] LED = {1,1};
    }
    
    /**
     * This class contains the wiring information for the analog sensors
     */
    public static abstract class Analog {
        /**
         * This class contains the information for the gyro
         */
        public static abstract class Gyro {
            /**
             * The slot number that the gyro is plugged into
             */
            public static final int SLOT = 1;
            /**
             * The sensitivity of the gyro (in volts per degree per second)
             */
            public static final double SENSITIVITY = 0.007;
            /**
             * The threshold value for position using the gyro
             */
            public static final int THRESHOLD = 1;
        }
    }
    
    /**
     * The wiring information and parameters for the encoder;
     */
    public static abstract class Encoder {
        /**
         * The wiring information and parameter for the encoder connected to the
         * left side of the drive train
         */
        public static abstract class Left {
            /**
             * The module number of the first PWM cable
             */
            public static final int MODULE_A = 1;
            /**
             * The slot number of the first PWM cable
             */
            public static final int SLOT_A = 1;
            /**
             * The module number of the second PWM cable
             */
            public static final int MODULE_B = 1;
            /**
             * The slot number of the second PWM cable
             */
            public static final int SLOT_B = 2;
            /**
             * The diameter of the wheel (in inches)
             */
            public static final double DIAMETER_OF_WHEEL = 6;
            /**
             * The gear ratio of the transmission
             */
            public static final double GEAR_RATIO = 4.67;
            /**
             * The scale factor for the encoder
             */
            public static final double SCALE_FACTOR = 2;
            /**
             * Whether of not to reverse the values of the encoder
             */
            public static final boolean REVERSED = true;
            /**
             * The resolution of the encoder
             */
            public static final double RESOLUTION = 250;
        }
    }
    /**
     * The mappings for the controls
     */
    public static abstract class Controls {
        /**
         * The mappings for the driver controller
         */
        public static abstract class Driver {
            /**
             * The joystick slot number for the controller
             */
            public static final int SLOT = 1;
            /**
             * The button mappings for the controller
             */
            public static abstract class Buttons {
                /**
                 * The mapping for turning off the dumper conveyor
                 */
                public static final int DUMPER_CONVEYOR_OFF = DualAction.Button.BACK;
                /**
                 * The mapping for rotating the robot counter clockwise
                 */
                public static final int ROTATE_COUNTER_CLOCKWISE = DualAction.Button.LEFT_BUMPER;
                /**
                 * The mapping for rotating the robot clockwise
                 */
                public static final int ROTATE_CLOCKWISE = DualAction.Button.RIGHT_BUMPER;
                /**
                 * The mapping for turning on the dumper conveyor
                 */
                public static final int DUMPER_CONVEYOR_ON = DualAction.Button.SELECT;
                /**
                 * The mapping for raising the climbing arm
                 */
                public static final int CLIMBING_ARM = DualAction.Button.Y;
            }
            /**
             * The axis mapping for the controller
             */
            public static abstract class Axis {
                /**
                 * The mapping for adjusting the dumper angle
                 */
                public static final int DUMPER_ANGLE = DualAction.Axis.LEFT_Y;
                /**
                 * The mapping for moving sideways
                 */
                public static final int DRIVE_SIDEWAYS = DualAction.Axis.RIGHT_X;
                /**
                 * The mapping for moving vertical
                 */
                public static final int DRIVE_VERTICAL = DualAction.Axis.RIGHT_Y;
            }
        }
        /**
         * The mappings for the shooter controller
         */
        public static abstract class Shooter {
            /**
             * The joystick slot number for the controller
             */
            public static final int SLOT = 2;
            /**
             * The button mappings for the controller
             */
            public static abstract class Buttons {
                /**
                 * The mapping for the first shooter present speed (Manual Shooting)
                 */
                public static final int SHOOTER_PRESET_1 = DualAction.Button.A;
                /**
                 * The mapping for the second shooter present speed (Manual Shooting)
                 */
                public static final int SHOOTER_PRESET_2 = DualAction.Button.B;
                /**
                 * The mapping for turning off the LED (This Will Force Manual Shooting)
                 */
                public static final int LED_OFF = DualAction.Button.BACK;
                /**
                 * The mapping for lowering the power of the shooter by one tenth of a volt (Manual Shooting)
                 */
                public static final int SHOOTER_MINUS_TENTH_VOLT = DualAction.Button.LEFT_BUMPER;
                /**
                 * The mapping for lowering the power of the shooter by one volt (Manual Shooting)
                 */
                public static final int SHOOTER_MINUS_ONE_VOLT = DualAction.Button.LEFT_TRIGGER;
                /**
                 * The mapping for raising the power of the shooter by one tenth of a volt (Manual Shooting)
                 */
                public static final int SHOOTER_PLUS_TENTH_VOLT = DualAction.Button.RIGHT_BUMPER;
                /**
                 * The mapping for raising the power of the shooter by one volt (Manual Shooting)
                 */
                public static final int SHOOTER_PLUS_ONE_VOLT = DualAction.Button.RIGHT_TRIGGER;
                /**
                 * The mapping for turning on the LED (This Will Allow For Either Shooting Mode)
                 */
                public static final int LED_ON = DualAction.Button.SELECT;
                /**
                 * The mapping for the third shooter present speed (Manual Shooting)
                 */
                public static final int SHOOTER_PRESET_3 = DualAction.Button.X;
                /**
                 * Feed a frisbee to the shooter (Manual Shooting)
                 */
                public static final int FEED_SHOOTER = DualAction.Button.Y;
            }
            /**
             * The axis mappings for the controller
             */
            public static abstract class Axis {
                /**
                 * Shoot at the bottom target (Automatic Shooting)
                 */
                public static final double[] SHOOT_AT_BOTTOM = DualAction.Axis.DPAD.DPAD_DOWN;
                /**
                 * Shoot at the middle left target (Automatic Shooting)
                 */
                public static final double[] SHOOT_AT_MIDDLE_LEFT = DualAction.Axis.DPAD.DPAD_LEFT;
                /**
                 * Shoot at the middle right target (Automatic Shooting)
                 */
                public static final double[] SHOOT_AT_MIDDLE_RIGHT = DualAction.Axis.DPAD.DPAD_RIGHT;
                /**
                 * Shoot at the top target (Automatic Shooting)
                 */
                public static final double[] SHOOT_AT_TOP = DualAction.Axis.DPAD.DPAD_UP;
                /**
                 * Manually adjust the camera angle
                 */
                public static final int CAMERA_ANGLE = DualAction.Axis.LEFT_Y;
            }
        }
    }
}
