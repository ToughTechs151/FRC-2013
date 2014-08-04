package edu.wpi.first.wpilibj.templates;

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
        public static abstract class Axis extends Controller.DualAction.Axis {}
        /**
         * Mappings for the buttons on the controller
         */
        public static abstract class Button extends Controller.DualAction.Button {}
    }
    
    public static abstract class Drive {
        public static final double CREEP = 0.125;
        public static final double NORMAL = 0.25;
        public static final double TURBO = 0.50;
    }
    
    public static abstract class CANJaguar {
        public static final int SHOOTER_1 = 2;
        public static final int SHOOTER_2 = 5;
        public static final int ARM_LEFT = 9;
        public static final int ARM_RIGHT = 10;
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
        /**
         * This is the mapping for the victor that controls the shooter feeder
         */
        public static final int[] FEEDER = {1,9};
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
            public static final double MINIMUM = 0.0000;
            /**
             * The upper limit of the servo
             */
            public static final double MAXIMUM = 1.0000;
            /**
             * The default location for the servo
             */
            public static final double DEFAULT = 0.3050;
        }
    }
    
    public static abstract class Shooter {
        public static final double SHOOTER_PRESET_1M1 = 0;
        public static final double SHOOTER_PRESET_1M2 = 0;
        public static final double SHOOTER_PRESET_2M1 = 6.5;
        public static final double SHOOTER_PRESET_2M2 = 6.5;
        public static final double SHOOTER_PRESET_3M1 = 7.5;
        public static final double SHOOTER_PRESET_3M2 = 7.5;
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
    public static abstract class Analog {
        public static final int FEEDER_POT = 1;
    }
    public static abstract class Digital {
        public static final int FEEDER_FRONT_STOP = 1;
        public static final int FEEDER_BACK_STOP = 2;
    }
    public static abstract class Feeder {
        public static final long FRONT_TIMEOUT = 500;
        public static final long BACK_TIMEOUT = 750;
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
                 * The mapping for rotating the robot clockwise
                 */
                public static final int ROTATE_CLOCKWISE = RobotMap.DualAction.Button.RIGHT_BUMPER;
                /**
                 * The mapping for rotating the robot counter-clockwise
                 */
                public static final int ROTATE_COUNTER_CLOCKWISE = RobotMap.DualAction.Button.LEFT_BUMPER;
                /**
                 * The mapping for setting the speed to turbo
                 */
                public static final int SPEED_TURBO = RobotMap.DualAction.Button.RIGHT_TRIGGER;
            }
            /**
             * The axis mapping for the controller
             */
            public static abstract class Axis {
                /**
                 * The mapping for moving sideways
                 */
                public static final int DRIVE_SIDEWAYS = RobotMap.DualAction.Axis.RIGHT_X;
                /**
                 * The mapping for moving vertical
                 */
                public static final int DRIVE_VERTICAL = RobotMap.DualAction.Axis.RIGHT_Y;
                /**
                 * The mapping for the camera angle
                 */
                public static final int CAMERA_ANGLE = RobotMap.DualAction.Axis.DPAD_Y;
                public static final int FEEDER = RobotMap.DualAction.Axis.LEFT_Y;
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
                public static final int SHOOTER_PRESET_1 = RobotMap.DualAction.Button.A;
                /**
                 * The mapping for the second shooter present speed (Manual Shooting)
                 */
                public static final int SHOOTER_PRESET_2 = RobotMap.DualAction.Button.B;
                /**
                 * The mapping for turning off the LED (This Will Force Manual Shooting)
                 */
                public static final int LED_OFF = RobotMap.DualAction.Button.BACK;
                /**
                 * The mapping for lowering the power of the shooter by one tenth of a volt (Manual Shooting)
                 */
                public static final int SHOOTER_MINUS_TENTH_VOLT = RobotMap.DualAction.Button.LEFT_BUMPER;
                /**
                 * The mapping for lowering the power of the shooter by one volt (Manual Shooting)
                 */
                public static final int SHOOTER_MINUS_ONE_VOLT = RobotMap.DualAction.Button.LEFT_TRIGGER;
                /**
                 * The mapping for raising the power of the shooter by one tenth of a volt (Manual Shooting)
                 */
                public static final int SHOOTER_PLUS_TENTH_VOLT = RobotMap.DualAction.Button.RIGHT_BUMPER;
                /**
                 * The mapping for raising the power of the shooter by one volt (Manual Shooting)
                 */
                public static final int SHOOTER_PLUS_ONE_VOLT = RobotMap.DualAction.Button.RIGHT_TRIGGER;
                /**
                 * The mapping for turning on the LED (This Will Allow For Either Shooting Mode)
                 */
                public static final int LED_ON = RobotMap.DualAction.Button.SELECT;
                /**
                 * The mapping for the third shooter present speed (Manual Shooting)
                 */
                public static final int SHOOTER_PRESET_3 = RobotMap.DualAction.Button.X;
                /**
                 * Feed a frisbee to the shooter (Manual Shooting)
                 */
                public static final int FEED_SHOOTER = RobotMap.DualAction.Button.Y;
            }
            /**
             * The axis mappings for the controller
             */
            public static abstract class Axis {
                /**
                 * Shoot at the bottom target (Automatic Shooting)
                 */
                public static final int SHOOT_AT_BOTTOM = RobotMap.DualAction.Button.DPAD_DOWN;
                /**
                 * Shoot at the middle left target (Automatic Shooting)
                 */
                public static final int SHOOT_AT_MIDDLE_LEFT = RobotMap.DualAction.Button.DPAD_LEFT;
                /**
                 * Shoot at the middle right target (Automatic Shooting)
                 */
                public static final int SHOOT_AT_MIDDLE_RIGHT = RobotMap.DualAction.Button.DPAD_RIGHT;
                /**
                 * Shoot at the top target (Automatic Shooting)
                 */
                public static final int SHOOT_AT_TOP = RobotMap.DualAction.Button.DPAD_UP;
                /**
                 * Manually adjust the climbing arm (left)
                 */
                public static final int CLIMBING_ARM_LEFT = RobotMap.DualAction.Axis.LEFT_Y;
                /**
                 * Manually adjust the climbing arm (right)
                 */
                public static final int CLIMBING_ARM_RIGHT = RobotMap.DualAction.Axis.RIGHT_Y;
                /**
                 * The mapping for the camera angle
                 */
                public static final int CAMERA_ANGLE = RobotMap.DualAction.Axis.DPAD_Y;
            }
        }
    }
}
