package org.nashua.tt151.systems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.io.IOException;
import edu.wpi.first.wpilibj.templates.Dash;
import edu.wpi.first.wpilibj.templates.RobotMap;
import org.nashua.tt151.libraries.Controller.DualAction;

public final class ClimbingArm extends Subsystem {
    private static CANJaguar armL, armR;
    private static final ClimbingArm instance = new ClimbingArm();
    private ClimbingArm() {
        try {
            armL = new CANJaguar(RobotMap.CANJaguar.ARM_LEFT, CANJaguar.ControlMode.kPercentVbus);
            armR = new CANJaguar(RobotMap.CANJaguar.ARM_RIGHT, CANJaguar.ControlMode.kPercentVbus);
        } catch (CANTimeoutException ex) {
        }
    }
    public static ClimbingArm getInstance() {
        return instance;
    }
    public void operatorControl(DualAction driver, DualAction shooter) {
        setVoltage(shooter.getRawAxis(RobotMap.Controls.Shooter.Axis.CLIMBING_ARM_LEFT),
                shooter.getRawAxis(RobotMap.Controls.Shooter.Axis.CLIMBING_ARM_RIGHT));
    }
    private void setVoltage(double voltsLeft, double voltsRight) {
        try {
            armL.setX(voltsLeft);
            armR.setX(-voltsRight);
        } catch (Exception e) {
        }
    }
    public void updateDashboard(Dash dash) throws IOException {
        try {
            dash.sendCAN(RobotMap.CANJaguar.ARM_LEFT, "Left Arm", armL.getPosition(), armL.getBusVoltage(), 
                armL.getOutputVoltage(), armL.getTemperature(), armL.getFirmwareVersion(), armL.getHardwareVersion());
        } catch (CANTimeoutException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.ARM_LEFT, "Left Arm");
        } catch (NullPointerException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.ARM_LEFT, "Left Arm");
        } 
        try {
            dash.sendCAN(RobotMap.CANJaguar.ARM_RIGHT, "Right Arm", armR.getPosition(), armR.getBusVoltage(), 
                armR.getOutputVoltage(), armR.getTemperature(), armR.getFirmwareVersion(), armR.getHardwareVersion());
        } catch (CANTimeoutException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.ARM_RIGHT, "Right Arm");
        } catch (NullPointerException ex) {
            dash.sendCANCommFault(RobotMap.CANJaguar.ARM_RIGHT, "Right Arm");
        }
    }

    public void initialization() {
    }

    public void test(DualAction driver, DualAction shooter) {
    }
}
