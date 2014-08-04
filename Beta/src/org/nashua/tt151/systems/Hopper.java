package org.nashua.tt151.systems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.templates.Dash;
import edu.wpi.first.wpilibj.templates.Dash.PWMType;
import edu.wpi.first.wpilibj.templates.RobotMap;
import java.io.IOException;
import org.nashua.tt151.libraries.Controller.DualAction;
import org.nashua.tt151.wrappers.Victor;

public class Hopper extends Subsystem {
    private static final Victor feeder = new Victor(RobotMap.Victor.FEEDER);
    private static final DigitalInput backLimSw = new DigitalInput(RobotMap.Digital.FEEDER_BACK_STOP);
    private static final DigitalInput frontLimSw = new DigitalInput(RobotMap.Digital.FEEDER_FRONT_STOP);
    private static final AnalogChannel pot = new AnalogChannel(RobotMap.Analog.FEEDER_POT);
    private static int start = 0;
    private static boolean jammed = false;
    private static final Hopper INSTANCE = new Hopper();
    private static boolean lastJammed = false;
    private Hopper() {
    }
    public static Hopper getInstance() {
        return INSTANCE;
    }
    public void feed() {
        long st = System.currentTimeMillis();
        while (!frontLimSw.get() && System.currentTimeMillis()-st<RobotMap.Feeder.FRONT_TIMEOUT) {
            feeder.set(-.25);
        }
        st = System.currentTimeMillis();
        while (!backLimSw.get() && System.currentTimeMillis()-st<RobotMap.Feeder.BACK_TIMEOUT) {
            feeder.set(.25);
        }
        feeder.set(0);
        lastJammed = jammed;
        jammed = !backLimSw.get();
    }
    public void operatorControl(DualAction driver, DualAction shooter) {
        if (shooter.wasReleased(RobotMap.Controls.Shooter.Buttons.FEED_SHOOTER)) {
            feed();
        }
        feeder.set(driver.getRawAxis(RobotMap.Controls.Driver.Axis.FEEDER)*0.25);
    }
    public void updateDashboard(Dash dash) throws IOException {
        dash.sendPWM((int)(feeder.get()*128+128), "Feeder", feeder.getModuleNumber(), feeder.getChannel(), PWMType.VICTOR);
        dash.sendDigitalIO(frontLimSw.get(), "Feeder Front", frontLimSw.getChannel());
        dash.sendDigitalIO(backLimSw.get(), "Feeder Back", backLimSw.getChannel());
        dash.sendAnalog(pot.getAverageValue(), "Feeder Pot", pot.getChannel(), Dash.AnalogType.POTENTIOMETER);
        if (jammed!=lastJammed) {
            dash.logHopperStatus(jammed);
        }
    }

    public void initialization() {
        long st = System.currentTimeMillis();
        while (!backLimSw.get() && System.currentTimeMillis()-st<RobotMap.Feeder.BACK_TIMEOUT) {
            feeder.set(.25);
        }
        feeder.set(0);
    }

    public void test(DualAction driver, DualAction shooter) {
        feeder.set(-shooter.getRightY());
    }
    
}
