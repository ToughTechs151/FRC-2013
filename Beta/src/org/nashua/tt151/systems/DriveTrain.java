package org.nashua.tt151.systems;

import java.io.IOException;
import edu.wpi.first.wpilibj.templates.Dash;
import edu.wpi.first.wpilibj.templates.RobotMap;
import org.nashua.tt151.libraries.Controller.DualAction;
import org.nashua.tt151.wrappers.Victor;

public final class DriveTrain extends Subsystem {
    private static final Victor north = new Victor(RobotMap.Victor.NORTH);
    private static final Victor east = new Victor(RobotMap.Victor.EAST);
    private static final Victor south = new Victor(RobotMap.Victor.SOUTH);
    private static final Victor west = new Victor(RobotMap.Victor.WEST);
    private static double multiplier = RobotMap.Drive.NORMAL;
    private static final DriveTrain instance = new DriveTrain();
    private static boolean autoMove = false;
    private static boolean enabled = false;
    private DriveTrain() {
    }
    public static DriveTrain getInstance() {
        return instance;
    }
    public void operatorControl(DualAction driver, DualAction shooter) {
        double horizontal = 0, vertical = 0;
        multiplier = RobotMap.Drive.NORMAL;
        if (driver.getRawButton(RobotMap.Controls.Driver.Buttons.SPEED_TURBO)) {
            multiplier = RobotMap.Drive.TURBO;
        }
        if (driver.getRawButton(RobotMap.Controls.Driver.Buttons.ROTATE_CLOCKWISE)) {
            set(multiplier, multiplier, multiplier, multiplier);
        } else if (driver.getRawButton(RobotMap.Controls.Driver.Buttons.ROTATE_COUNTER_CLOCKWISE)) {
            set(-multiplier, -multiplier, -multiplier, -multiplier);
        } else {
            horizontal = driver.getRawAxis(RobotMap.Controls.Driver.Axis.DRIVE_SIDEWAYS);
            vertical = driver.getRawAxis(RobotMap.Controls.Driver.Axis.DRIVE_VERTICAL);
            set(horizontal, vertical, -horizontal, -vertical);
        }
    }
    public void set(double nPower, double ePower, double sPower, double wPower) {
        north.set(nPower);
        east.set(ePower);
        south.set(sPower);
        west.set(wPower);
    }
    public void setAutoMove(boolean auto) {
        autoMove = auto;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void updateDashboard(Dash dash) throws IOException {
        dash.sendPWM((int)(south.get()*128+128), "South", south.getModuleNumber(), south.getChannel(), Dash.PWMType.VICTOR);
        dash.sendPWM((int)(west.get()*128+128), "West", west.getModuleNumber(), west.getChannel(), Dash.PWMType.VICTOR);
        dash.sendPWM((int)(east.get()*128+128), "East", east.getModuleNumber(), east.getChannel(), Dash.PWMType.VICTOR);
        dash.sendPWM((int)(north.get()*128+128), "North", north.getModuleNumber(), north.getChannel(), Dash.PWMType.VICTOR);
        dash.sendDriveMode(multiplier, autoMove, enabled);
    }

    public void initialization() {
    }

    public void test(DualAction driver, DualAction shooter) {
    }
    
}