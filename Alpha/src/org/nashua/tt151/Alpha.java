package org.nashua.tt151;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.SimpleRobot;
import java.io.IOException;
import org.nashua.tt151.RobotMap.Controls;
import org.nashua.tt151.libraries.Controller.DualAction;
import org.nashua.tt151.util.MathTools;
import org.nashua.tt151.wrappers.Jaguar;
import org.nashua.tt151.wrappers.Relay;
import org.nashua.tt151.wrappers.Servo;
import org.nashua.tt151.wrappers.Victor;

public class Alpha extends SimpleRobot {
    // Logitech DualAction F310 Controllers - D Mode
    DualAction driver = new DualAction(Controls.Driver.SLOT);
    DualAction shooter = new DualAction(Controls.Shooter.SLOT);
    // Speed Controllers
    Jaguar south = new Jaguar(RobotMap.Victor.SOUTH);
    Victor west = new Victor(RobotMap.Victor.WEST);
    Victor east = new Victor(RobotMap.Victor.EAST);
    Victor north = new Victor(RobotMap.Victor.NORTH);
    // CAN Jaguars
    CANJaguar shooter1, shooter2;
    // Relays
    Relay led = new Relay(RobotMap.Relay.LED, Direction.kForward);
    // Servos
    Servo cameraServo = new Servo(RobotMap.Servo.Camera.SLOT);
    double camServoPos = 0.0;
    // Dashboard
    Dash dash = null;
    private int failedCount;
    private boolean tryingToConnect = false;
    // CAN Jaguars
    CANJaguar jag2;
    CANJaguar jag3;
    public void autonomous() {
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
                        dash = new Dash("10.1.53.5", new Dash.ConnectionListener() {
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
                    System.err.println("Failed to connect to dashboard: "+ex.getMessage());
                }
                tryingToConnect = false;
            }
        }.start();
    }
    public void disabled() {
        if (dash==null && !tryingToConnect) {
            connectToDashboard();
        }
    }
    public void operatorControl() {
        camServoPos = RobotMap.Servo.Camera.DEFAULT;
        cameraServo.setPosition(camServoPos);
        while (isEnabled() && isOperatorControl()) {
            driver.queryButtons();
            shooter.queryButtons();
            
            // Arcade Drive For An Omni Drive Train
            if (driver.getRawButton(DualAction.Button.LEFT_BUMPER)) {
                west.set(0.75);
                east.set(-0.75);
                north.set(0);
                south.set(0);
            } else if (driver.getRawButton(DualAction.Button.RIGHT_BUMPER)) {
                west.set(-0.75);
                east.set(0.75);
                north.set(0);
                south.set(0);
            } else {
                double vertical = driver.getRawAxis(Controls.Driver.Axis.DRIVE_VERTICAL);
                double horizontal = driver.getRawAxis(Controls.Driver.Axis.DRIVE_SIDEWAYS);
                west.set(vertical);
                east.set(-vertical);
                north.set(horizontal);
                south.set(-horizontal);
                System.out.println("HS: "+MathTools.round(horizontal, 2)+" / VS: "+MathTools.round(vertical, 2));
            }
            
            try {
                jag2.setX(driver.getLeftY()*12);
                jag3.setX(driver.getLeftY()*12);
            } catch (Exception e) {
            }
            
            // LED Control
            if (driver.wasReleased(Controls.Shooter.Buttons.LED_ON)) {
                led.set(edu.wpi.first.wpilibj.Relay.Value.kOn);
            }
            if (driver.wasReleased(Controls.Shooter.Buttons.LED_OFF)) {
                led.set(edu.wpi.first.wpilibj.Relay.Value.kOff);
            }
            // Map Axis To Servo Range
            camServoPos = (shooter.getRawAxis(Controls.Shooter.Axis.CAMERA_ANGLE)+1)/2.0
                    *(RobotMap.Servo.Camera.MAXIMUM-RobotMap.Servo.Camera.MINIMUM)+RobotMap.Servo.Camera.MINIMUM;
            cameraServo.setPosition(camServoPos);
            if (driver.wasReleased(DualAction.Button.RIGHT_TRIGGER)) {
                alignServo(Target.HIGHEST);
            }
            if (driver.getPadY()<-0.5) {
                setDistance(Target.TOP, 60);
            } else if (driver.getPadX()<-0.5) {
                setDistance(Target.MIDDLE_LEFT, 60);
            } else if (driver.getPadX()>0.5) {
                setDistance(Target.MIDDLE_RIGHT, 60);
            } else if (driver.getPadY()>0.5) {
                setDistance(Target.BOTTOM, 60);
            }
        }
    }
    public void robotInit() {
        try {
            jag2 = new CANJaguar(2, CANJaguar.ControlMode.kVoltage);
            jag3 = new CANJaguar(3, CANJaguar.ControlMode.kVoltage);
        } catch (Exception e) {
        }
        connectToDashboard();
        failedCount = 0;
        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask(){
            public void run() {
                if (dash!=null) {
                    try {
                        dash.sendPWM((int)(south.get()*128+128), "South", south.getModuleNumber(), south.getChannel(), Dash.PWMType.JAGUAR);
                        dash.sendPWM((int)(west.get()*128+128), "West", west.getModuleNumber(), west.getChannel(), Dash.PWMType.JAGUAR);
                        dash.sendPWM((int)(east.get()*128+128), "East", east.getModuleNumber(), east.getChannel(), Dash.PWMType.JAGUAR);
                        dash.sendPWM((int)(north.get()*128+128), "North", north.getModuleNumber(), north.getChannel(), Dash.PWMType.JAGUAR);
                        
                        dash.sendRelay(led.get(), "LED Ring", RobotMap.Relay.LED[0], RobotMap.Relay.LED[1], led.getDirection());
                        
                        double angle = (RobotMap.Servo.Camera.DEFAULT-camServoPos)*180;
                        dash.sendPWM((int)(cameraServo.getPosition()*1000), "Camera Servo", cameraServo.getModuleNumber(), cameraServo.getChannel(), Dash.PWMType.SERVO);
                        dash.sendCameraAngle(angle);
                        
                        try {
                            dash.sendCAN(2, "CAN Jag 2", jag2.getX()/12.0, jag2.getBusVoltage(), jag2.getOutputVoltage(), jag2.getTemperature(), jag2.getFirmwareVersion(), jag2.getHardwareVersion());
                        } catch (Exception e) {
                            System.err.println("CAN 2 Failed");
                        }
                        
                        try {
                            dash.sendCAN(3, "CAN Jag 3", jag3.getX()/12.0, jag3.getBusVoltage(), jag3.getOutputVoltage(), jag3.getTemperature(), jag3.getFirmwareVersion(), jag3.getHardwareVersion());
                        } catch (Exception e) {
                            System.err.println("CAN 3 Failed");
                        }
                        
                        failedCount = 0;
                    } catch (IOException ex) {
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
        camServoPos=RobotMap.Servo.Camera.DEFAULT;
        cameraServo.setPosition(camServoPos);
    }
    public void alignServo(Target t) {
        try {
            double pos;
            while ((pos=dash.queryCameraAngle(t))!=0 && isEnabled()) {
                camServoPos += pos;
                cameraServo.setPosition(camServoPos);
            }
        } catch (Exception e) {

        }
    }
    public void alignRotation(Target t, double threshold) {
        try {
            double offset;
            while (Math.abs(offset=dash.queryPosition(t)[1])>threshold && isEnabled()) {
                double value = -Math.max(-0.25, Math.min(0.25, offset/16.0));
                west.set(value);
                east.set(value);
                north.set(0);
                south.set(0);
                Thread.sleep(10);
                west.set(0);
                east.set(0);
            }
        } catch (Exception e) {
        }
    }
    public void setDistance(Target t, double dist) {
        try {
            double[] position;
            alignServo(t);
            while (Math.abs(((position=dash.queryPosition(t))[0])-dist)>6
                    && isEnabled()) {
                double value = -Math.max(-0.5, Math.min(0.5, (position[0]-60)/30.0));
                if (value<0) {
                    value -= 0.25;
                } else if (value>0) {
                    value += 0.25;
                }
                west.set(value);
                east.set(-value);
                north.set(0);
                south.set(0);
                Thread.sleep(10);
                west.set(0);
                east.set(0);
                alignServo(t);
                alignRotation(t, 15);
            }
            alignRotation(t, 1);
            alignServo(t);
        } catch (Exception ex) {
        }
    }
}
