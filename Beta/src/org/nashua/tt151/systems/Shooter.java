package org.nashua.tt151.systems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Dash;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.RobotMap.Controls;
import java.io.IOException;
import org.nashua.tt151.libraries.Controller.DualAction;
import org.nashua.tt151.wrappers.Relay;
import org.nashua.tt151.wrappers.Servo;

public final class Shooter extends Subsystem {
    private static final Relay led = new Relay(RobotMap.Relay.LED, Relay.Direction.kForward);
    private static final Servo cameraServo = new Servo(RobotMap.Servo.Camera.SLOT);
    private static double camServoPos = 0.0;
    private static final Shooter instance = new Shooter();
    private static CANJaguar shooter1, shooter2;
    private double volt1 = 0, volt2 = 0;
    private Shooter() {
        try {
            shooter1 = new CANJaguar(RobotMap.CANJaguar.SHOOTER_1, edu.wpi.first.wpilibj.CANJaguar.ControlMode.kVoltage);
            shooter2 = new CANJaguar(RobotMap.CANJaguar.SHOOTER_2, edu.wpi.first.wpilibj.CANJaguar.ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
        }
        resetServo();
    }
    public static Shooter getInstance() {
        return instance;
    }
    public double getServo() {
        return cameraServo.getPosition();
    }
    public void operatorControl(DualAction driver, DualAction shooter) {
        // LED
        if (shooter.getRawButton(RobotMap.Controls.Shooter.Buttons.LED_ON)) {
            led.set(edu.wpi.first.wpilibj.Relay.Value.kOn);
        } else if (shooter.getRawButton(RobotMap.Controls.Shooter.Buttons.LED_OFF)) {
            led.set(edu.wpi.first.wpilibj.Relay.Value.kOff);
        }

        // Servo
        camServoPos += driver.getRawAxis(RobotMap.DualAction.Axis.DPAD_Y)*0.01;
        camServoPos += driver.getRawAxis(RobotMap.DualAction.Axis.DPAD_X)*0.001;
        camServoPos += shooter.getRawAxis(RobotMap.DualAction.Axis.DPAD_Y)*0.01;
        camServoPos += shooter.getRawAxis(RobotMap.DualAction.Axis.DPAD_X)*0.001;
        camServoPos = Math.max(0, Math.min(1, camServoPos));
        cameraServo.setPosition(camServoPos);
        
        // Manual Shooter Code (MSC)
        // MSC: Presets
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_PRESET_1)) {
            // Preset 1
            volt1 = RobotMap.Shooter.SHOOTER_PRESET_1M1;
            volt2 = RobotMap.Shooter.SHOOTER_PRESET_1M2;
        }
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_PRESET_2)) {
            // Preset 2
            volt1 = RobotMap.Shooter.SHOOTER_PRESET_2M1;
            volt2 = RobotMap.Shooter.SHOOTER_PRESET_2M2;
        }
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_PRESET_3)) {
            // Preset 3
            volt1 = RobotMap.Shooter.SHOOTER_PRESET_3M1;
            volt2 = RobotMap.Shooter.SHOOTER_PRESET_3M2;
        }
        // MSC: Increment/Decrement Control
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_PLUS_ONE_VOLT)) {
            // Increment by 1V
            volt1++;
            volt2++;
        }
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_MINUS_ONE_VOLT)) {
            // Decrement by 1V
            volt1--;
            volt2--;
        }
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_PLUS_TENTH_VOLT)) {
            // Increment by 0.1V
            volt1 += 0.1;
            volt2 += 0.1;
        }
        if (shooter.wasReleased(Controls.Shooter.Buttons.SHOOTER_MINUS_TENTH_VOLT)) {
            // Decrement by 0.1V
            volt1 -= 0.1;
            volt2 -= 0.1;
        }
        setShooter(volt1, volt2);
        
    }
    public void resetServo() {
        setServo(RobotMap.Servo.Camera.DEFAULT);
    }
    public void setServo(double pos) {
        camServoPos = pos;
        cameraServo.setPosition(pos);
    }
    public void setShooter(double volt1, double volt2) {
        // Make sure max and min are not exceeded
        volt1 = Math.min(12, Math.max(0, volt1));
        volt2 = Math.min(12, Math.max(0, volt2));
        // Set voltage
        try {
            shooter1.setX(volt1);
            shooter2.setX(volt2);
        } catch (CANTimeoutException ex) {
        } catch (Exception e) {
        }
    }
    public void shoot(double voltage) {
        try {
            DriveTrain.getInstance().setAutoMove(true);
            if (voltage!=0) {
                if (shooter1.getOutputVoltage()!=voltage || 
                        shooter2.getOutputVoltage()!=voltage) {
                    setShooter(voltage, voltage);
                    Thread.sleep(4000);
                }
                for (int i=0; i<4; i++) {
                    Hopper.getInstance().feed();
                    Thread.sleep(100);
                }
            }
            setShooter(0, 0);
            DriveTrain.getInstance().setAutoMove(false);
        } catch (InterruptedException ex) {
        } catch (IOException ex) {
        } catch (NullPointerException ex) {
        }
    }
    public void updateDashboard(Dash dash) throws IOException {
        dash.sendRelay(led.get(), "LED Ring", RobotMap.Relay.LED[0], RobotMap.Relay.LED[1], led.getDirection());
        double angle = (-(RobotMap.Servo.Camera.DEFAULT-camServoPos)*180)-6.70;
        dash.sendPWM((int)(cameraServo.getPosition()*1000), "Camera Servo", cameraServo.getModuleNumber(),
                cameraServo.getChannel(), Dash.PWMType.SERVO);
        dash.sendCameraAngle(angle);
        try {
            dash.sendCAN(RobotMap.CANJaguar.SHOOTER_1, "Shooter 1", shooter1.getPosition(), shooter1.getBusVoltage(), 
                shooter1.getOutputVoltage(), shooter1.getTemperature(), shooter1.getFirmwareVersion(), shooter1.getHardwareVersion());
        } catch (CANTimeoutException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.SHOOTER_1, "Shooter 1");
        } catch (NullPointerException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.SHOOTER_1, "Shooter 1");
        } 
        try {
            dash.sendCAN(RobotMap.CANJaguar.SHOOTER_2, "Shooter 2", shooter2.getPosition(), shooter2.getBusVoltage(), 
                shooter2.getOutputVoltage(), shooter2.getTemperature(), shooter2.getFirmwareVersion(), shooter2.getHardwareVersion());
        } catch (CANTimeoutException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.SHOOTER_2, "Shooter 2");
        } catch (NullPointerException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.SHOOTER_2, "Shooter 2");
        }
    }

    public void initialization() {
        camServoPos = 0.5;
        cameraServo.set(0.5);
    }

    public void test(DualAction driver, DualAction shooter) {
    }
}
