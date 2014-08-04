package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import java.io.IOException;
import org.nashua.tt151.libraries.Controller.DualAction;
import org.nashua.tt151.systems.ClimbingArm;
import org.nashua.tt151.systems.DriveTrain;
import org.nashua.tt151.systems.Hopper;
import org.nashua.tt151.systems.Shooter;

public class Beta extends SimpleRobot {
    // Logitech DualAction F310 - D Mode
    DualAction driver = new DualAction(1,0.1);
    DualAction shooter = new DualAction(2,0.1);
    // Dashboard
    Dash dash = null;
    private int failedCount;
    private boolean tryingToConnect = false;
    //
    public void autonomous() {
        DriveTrain.getInstance().initialization();
        ClimbingArm.getInstance().initialization();
        Hopper.getInstance().initialization();
        Shooter.getInstance().initialization();
        DriveTrain.getInstance().setEnabled(true);
        DriveTrain.getInstance().setAutoMove(true);
        Shooter.getInstance().shoot(7.5);
        DriveTrain.getInstance().setAutoMove(true);
    }
    private void connectToDashboard() {
        if (tryingToConnect) {
            return;
        }
        tryingToConnect = true;
        new Thread() {
            public void run() {
                try {
                    if (tryingToConnect && dash==null) {
                        dash = new Dash("10.1.51.5", new Dash.ConnectionListener() {
                            public void onConnect() {
                                System.out.println("[Connected]");
                            }
                            public void onDisconnect() {
                                System.out.println("[Disconnected]");
                                dash = null;
                            }
                            public void onDataReceived(String msg) {
                                System.out.println("[MSG "+msg+"]");
                            }
                        });
                    }
                } catch (IOException ex) {
                    System.err.println("[ERR Failed to connect to dashboard: "+ex.getMessage()+"]");
                }
                tryingToConnect = false;
            }
        }.start();
    }
    public void disabled() {
        DriveTrain.getInstance().setEnabled(false);
        if (dash==null && !tryingToConnect) {
            connectToDashboard();
        }
    }
    public void robotInit() {
        connectToDashboard();
        try {
            dash.logMatchInfo();
        } catch (Exception e) {
        }
        failedCount = 0;
        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask(){
            public void run() {
                if (dash!=null) {
                    try {
                        DriveTrain.getInstance().updateDashboard(dash);
                        Shooter.getInstance().updateDashboard(dash);
                        ClimbingArm.getInstance().updateDashboard(dash);
                        Hopper.getInstance().updateDashboard(dash);
                        
                        failedCount = 0;
                    } catch (Exception ex) {
                        System.err.println("Failed to update dashboard: "+ex.getMessage());
                        failedCount++;
                        if (failedCount<=10) {
                            failedCount = 0;
                            dash = null;
                            connectToDashboard();
                        }
                    }
                } else {
                    failedCount = 0;
                    connectToDashboard();
                }
            }
        }, 1, 100);
    }
    public void operatorControl() {
        DriveTrain.getInstance().initialization();
        ClimbingArm.getInstance().initialization();
        Hopper.getInstance().initialization();
        Shooter.getInstance().initialization();
        DriveTrain.getInstance().setEnabled(true);
        Shooter.getInstance().resetServo();
        while (isEnabled() && isOperatorControl()) {
            driver.queryButtons();
            shooter.queryButtons();
            
            /************************** DRIVER CONTROLS ****************************/
            DriveTrain.getInstance().operatorControl(driver, shooter);
            ClimbingArm.getInstance().operatorControl(driver, shooter);
            
            /************************** SHOOTER CONTROLS ****************************/
            Shooter.getInstance().operatorControl(driver, shooter);
            Hopper.getInstance().operatorControl(driver, shooter);
        }
    }
    public void test() {
        Shooter.getInstance().resetServo();
        while (isEnabled() && isTest()) {
            driver.queryButtons();
            shooter.queryButtons();
            
            /************************** DRIVER CONTROLS ****************************/
            DriveTrain.getInstance().test(driver, shooter);
            ClimbingArm.getInstance().test(driver, shooter);
            
            /************************** SHOOTER CONTROLS ****************************/
            Shooter.getInstance().test(driver, shooter);
            Hopper.getInstance().test(driver, shooter);
        }
    }
}
