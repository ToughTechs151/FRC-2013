package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.io.BufferedWriter;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.Target;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import org.nashua.tt151.libraries.parsers.ProtocolParsing;
import org.nashua.tt151.util.MathTools;

/**
 * Communication with Dashboard utilizing the custom ProtocolParsing library.
 * @author Brian Ashworth
 * @version 1.1
 */
public class Dash {
    /**
     * Connection object that holds the connection to the dashboard.
     */
    private Connection con;
    /**
     * Connection from cRIO to laptop
     * @param host IP Address of the laptop
     * @param cl Listener for this connection
     * @throws IOException
     */
    public Dash(String host, ConnectionListener cl) throws IOException {
        con = new Connection(host, 1735, cl);
    }
    public void logMatchInfo() throws IOException {
        logMessage("MATCH:"+DriverStation.getInstance().getAlliance()+":"+DriverStation.getInstance().getLocation());
    }
    public void logHopperStatus(boolean jammed) throws IOException {
        logMessage("HOP"+(jammed?"JAM":"RST")+":"+DriverStation.getInstance().getMatchTime());
    }
    public void logMessage(String msg) throws IOException {
        con.send("ERR:"+msg);
    }
    public double queryCameraAngle(Target t) throws NumberFormatException, IOException {
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Query, 
                ProtocolParsing.Key.CamAngle, "", new String[]{""+t.getID()}));
        // Wait for result
        String[][] result = null;
        while ((result=con.getKeyFromQueue(ProtocolParsing.Key.CamAngle))==null) {
        }
        // Verify non null value and parse the double from the string
        if (result.length>1 && result[1].length>0) {
            return Double.parseDouble(result[1][0]);
        }
        // Return -1 on null result
        return -1;
    }
    public double[] queryPosition(Target t) throws NumberFormatException, IOException {
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Query, 
                ProtocolParsing.Key.Position, "", new String[]{""+t.getID()}));
        // Wait for result
        String[][] result = null;
        while ((result=con.getKeyFromQueue(ProtocolParsing.Key.Position))==null) {
        }
        // Verify non null value and parse the double from the string
        if (result.length>2 && result[2].length>0) {
            return new double[]{Double.parseDouble(result[1][0]), Double.parseDouble(result[2][0])};
        }
        // Return {-1,-1} on null result
        return new double[]{-1, -1};
    }
    /**
     * Query the dashboard for the speed (in ft/s) to set the shooter to
     * @param t Target ti get speed for
     * @return The speed (in ft/s) to set the shooter to or -1 on failure
     * @throws NumberFormatException
     * @throws IOException 
     */
    public double queryShooterSpeed(Target t) throws NumberFormatException, IOException {
        /* Send query to the dashboard
         * No Value
         * Arguments:
         *   Analog Device Type
         */
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Query, 
                ProtocolParsing.Key.ShooterSpeed, "", new String[]{""+t.getID()}));
        // Wait for result
        String[][] result = null;
        while ((result=con.getKeyFromQueue(ProtocolParsing.Key.ShooterSpeed))==null) {
        }
        // Verify non null value and parse the double from the string
        if (result.length>1 && result[1].length>0) {
            return Double.parseDouble(result[1][0]);
        }
        // Return -1 on null result
        return -1;
    }
    public void sendCAN(int boardID, String name, double value, double inVolt, double outVolt, double temp, int fw, int jagType) throws IOException {
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Send, ProtocolParsing.Key.CANValue, ""+MathTools.round(value, 2),
                new String[]{name,""+boardID,""+MathTools.round(inVolt, 2), ""+MathTools.round(outVolt, 2), ""+MathTools.round(temp, 2), ""+fw, ""+jagType}));
    }
    public void sendCANCommFault(int boardID, String name) throws IOException {
        sendCAN(boardID, name, -2.0, 0, 0, 0, -1, 0);
    }
    /**
     * Send the value of an analog sensor to the dashboard to display
     * @param value Value of the analog sensor
     * @param name Name of the sensor to display on the dashboard
     * @param slot PWM Slot on the analog module
     * @param at The analog device type (gyro, accelerometer, etc)
     * @throws IOException 
     */
    public void sendAnalog(int value, String name, int slot, AnalogType at) throws IOException {
        /* Send the analog value to the dashboard
         * Value: analog value
         * Arguments:
         *   Name
         *   Slot
         *   Analog Type
         */
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Send, 
                ProtocolParsing.Key.AnalogValue, ""+value, new String[]{name, ""+slot, at.toString()}));
    }
    /**
     * Send the camera angle to the dashboard for target calculations
     * @param value Camera angle
     * @throws IOException 
     */
    public void sendCameraAngle(double value) throws IOException {
        /* Send the camera angle to the dashboard
         * Value: degrees in the format ##.##
         * Arguments: None
         */
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Send, 
                ProtocolParsing.Key.CamAngle, ""+MathTools.round(value, 2), new String[]{"0"}));
    }
    /**
     * Send the PWM value to the dashboard for displaying
     * @param value Raw PWM Value (0 - 255)
     * @param name Name for the PWM Channel to display
     * @param mod Module Number (1 or 2)
     * @param slot Slot Number (1 through 10)
     * @param pt PWM Device Type (Jaguar, Victor, Servo, etc)
     * @throws IOException 
     */
    public void sendPWM(int value, String name, int mod, int slot, PWMType pt) throws IOException {
        /* Send the value to the dashboard
         * Value: Raw PWM Value (0 - 255)
         * Arguments:
         *   Name
         *   Module Slot (###)
         *   PWM Device Type
         */
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Send,
                ProtocolParsing.Key.PWMValue, ""+value, new String[]{name, mod+(slot<10?"0"+slot:""+slot), pt.toString()}));
    }
    /**
     * Send the value of the relay to the dashboard for displaying
     * @param v Relay Value
     * @param name Name of the relay to display
     * @param mod Module number (1 or 2)
     * @param slot Slot number (1 through 8)
     * @param dir Relay Direction
     * @throws IOException 
     */
    public void sendRelay(Relay.Value v, String name, int mod, int slot, Relay.Direction dir) throws IOException {
        /* Send the value to the dashboard
         * Value: PWM Value (0, 1 or -1)
         * Arguments:
         *   Name
         *   Module Slot (##)
         *   Direction (0, 1 or -1)
         */
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Send,
                ProtocolParsing.Key.RelayValue, ""+new int[]{0,1,1,-1}[v.value], 
                new String[]{name, ""+mod+slot, ""+new int[]{0,1,-1}[dir.value]}));
    }
    public void sendDriveMode(double speed, boolean auto, boolean enabled) throws IOException {
        if (!enabled) {
            con.send("D:D");
        } else if (auto) {
            con.send("D:A");
        } else if (speed==RobotMap.Drive.TURBO) {
            con.send("D:T");
        } else if (speed==RobotMap.Drive.CREEP) {
            con.send("D:C");
        } else {
            con.send("D:N");
        }
    }
    public void sendDigitalIO(boolean value, String name, int channel) throws IOException {
        con.send(ProtocolParsing.StringParser.createMessage(ProtocolParsing.Command.Send, ProtocolParsing.Key.DigitalIO, ""+value, new String[]{name, ""+channel}));
    }
    /**
     * Send a raw message to the dashboard
     * @param msg Message to send
     * @throws IOException 
     */
    public void sendRaw(String msg) throws IOException {
        con.send(msg);
    }
    /**
     * Analog Device Type
     * @author Brian Ashworth
     * @version 1.0
     */
    public static final class AnalogType {
        /**
         * Used to identify an accelerometer
         */
        public static final AnalogType ACCELEROMETER = new AnalogType('A');
        /**
         * Used to identify a gyro
         */
        public static final AnalogType GYRO = new AnalogType('G');
        /**
         * Used to identify an unknown analog value
         */
        public static final AnalogType UNKNOWN = new AnalogType('U');
        /**
         * Used to identify a potentiometer
         */
        public static final AnalogType POTENTIOMETER = new AnalogType('P');
        /**
         * Shorthand character that identifies the device type
         */
        public char shorthand;
        /**
         * An analog device identifier
         * @param shorthand Character that identifies the device type
         */
        private AnalogType(char shorthand) {
            this.shorthand = shorthand;
        }
        /**
         * Retrieves the shorthand character
         * @return shorthand character that identifies the device type
         */
        public char getShorthand() {
            return shorthand;
        }
        /**
         * Override of java.lang.Object.toString. Turns the object into a String
         * @return String representation of the object
         */
        public String toString() {
            return ""+shorthand;
        }
    }
    /**
     * PWM Device Identifier
     * @author Brian Ashworth
     * @version 1.0
     */
    public static final class PWMType {
        /**
         * Used to identify a jaguar
         */
        public static final PWMType JAGUAR = new PWMType('J');
        /**
         * Used to identify a servo
         */
        public static final PWMType SERVO = new PWMType('S');
        /**
         * Used to identify a talon
         */
        public static final PWMType TALON = new PWMType('T');
        /**
         * Used to identify an unknown PWM device
         */
        public static final PWMType UNKNOWN = new PWMType('U');
        /**
         * Used to identify a victor
         */
        public static final PWMType VICTOR = new PWMType('V');
        /**
         * Shorthand character that identifies a PWM device type
         */
        public char shorthand;
        /**
         * PWM Device Identifier
         * @param shorthand Character that represents the device type
         */
        private PWMType(char shorthand) {
            this.shorthand = shorthand;
        }
        /**
         * Retrieves the character that identifies the device type
         * @return The device type identifier
         */
        public char getShorthand() {
            return shorthand;
        }
        /**
         * Override of java.lang.Object.toString. Returns a string representation of the object.
         * @return String representation of the object.
         */
        public String toString() {
            return ""+shorthand;
        }
    }
    /**
     * The ConnectionListener interface has the method declarations for the 
     * methods that get called when an event happens on the communication
     * channel. The following events are currently supported: onConnect,
     * onDisconnect, and onDataReceived.
     * @author Brian Ashworth
     * @version 1.0
     */
    public static interface ConnectionListener {
        /**
         * This listener method gets called when a connection is made
         */
        public void onConnect();
        /**
         * This listener method gets called when a connection is lost
         */
        public void onDisconnect();
        /**
         * This listener method gets called when a message from the dashboard is received.
         * @param msg Message from the dashboard
         */
        public void onDataReceived(String msg);
    }
    /**
     * The Connection class establishes the communication channel from the robot
     * to the laptop (and vise versa).
     * @author Brian Ashworth
     * @version 1.0
     */
    private static class Connection implements ConnectionListener {
        /**
         * Listener for this connection
         */
        private ConnectionListener listener;
        /**
         * Socket connection between the laptop and the cRIO. This is the client side.
         */
        private SocketConnection socket;
        /**
         * Reader for the input stream. The input stream is to the cRIO
         */
        private InputStreamReader reader;
        /**
         * The Writer for the output stream. The output stream is from the cRIO.
         */
        private BufferedWriter writer;
        /**
         * Queue of queried items
         */
        private String[][][] queue = new String[0][0][0];
        /**
         * Establish a client connection from the cRIO to the dashboard
         * @param host IP Address of laptop
         * @param port Port that the dashboard is listening on
         * @param cl Listener for this connection
         * @throws IOException 
         */
        public Connection(String host, int port, ConnectionListener cl) throws IOException {
            // Set listener if null to this (dummy) or if not null to the listener
            listener = cl==null?this:cl;
            
            // Connect to server (in this case the dashboard on the laptop)
            socket = (SocketConnection) Connector.open("socket://"+host+":"+port);
            reader = new InputStreamReader(socket.openDataInputStream());
            writer = new BufferedWriter(new OutputStreamWriter(socket.openDataOutputStream()));
            
            // Listen for events every 10ms
            final java.util.Timer t = new java.util.Timer();
            t.schedule(new java.util.TimerTask(){
                public void run(){
                    if (socket==null) {
                        t.cancel();
                        return;
                    }
                    try {
                        if (reader.ready()) {
                            // Read length byte
                            int length = reader.read();
                            // Read for length bytes
                            int offset = 0;
                            char[] buffer = new char[length];
                            while (length!=0) {
                                int read = reader.read(buffer, offset, length);
                                offset+=read;
                                length-=read;
                                if (read==-1) {
                                    break;
                                }
                            }
                            // Turn buffer into String
                            String msg = String.valueOf(buffer);
                            // Check to see if disconnected
                            if (msg.equals("[Disconnected]")) {
                               socket = null;
                               reader = null;
                               writer = null;
                               listener.onDisconnect();
                            } else if (ProtocolParsing.StringParser.isReply(msg)) {
                                // Add reply to the queue
                                String key = ProtocolParsing.StringParser.getKey(msg).shorthand;
                                String value = ProtocolParsing.StringParser.getValue(msg);
                                String[] args = ProtocolParsing.StringParser.getArgs(msg);
                                updateQueue(key, value, args);
                            } else {
                                // Send to the connection listener
                                listener.onDataReceived(msg);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getClass()+": "+e.getMessage());
                    }
                }
            },1,10);
            
            listener.onConnect();
        }

        /**
         * Remove all null indices from queue and shrink down to size
         */
        private void cleanQueue() {
            // Remove null indices from queue
            int nc = 0;
            for (int i=0;i<queue.length;i++) {
                if (queue[i]==null) {
                    nc++;
                }
            }
            String[][][] newQueue = new String[queue.length-nc][3][0];
            int index = 0;
            for (int i=0;i<queue.length;i++) {
                if (queue[i]!=null) {
                    newQueue[index++] = queue[i];
                }
            }
        }
        /**
         * Retrieve a key from the queue
         * @param key Key to retrieve
         * @return The value (and args) that correspond to the key or null if not found
         */
        public String[][] getKeyFromQueue(ProtocolParsing.Key key) {
            return getKeyFromQueue(key.shorthand);
        }
        /**
         * Retrieve a key from the queue
         * @param key Key to retrieve
         * @return The value (and args) that correspond to the key or null if not found
         */
        public String[][] getKeyFromQueue(String key) {
            for (int i=0; i<queue.length; i++) {
                if (queue[i]!=null&&queue[i][0]!=null&&queue[i][0][0].equals(key)) {
                    String[][] value = queue[i];
                    queue[i] = null;
                    cleanQueue();
                    return value;
                }
            }
            return null;
        }
        /**
         * Dummy onConnect listener method
         */
        public void onConnect() {
        }

        /**
         * Dummy onDisconnect listener method
         */
        public void onDisconnect() {
        }

        /**
         * Dummy onDataReceived listener method
         * @param msg Message received
         */
        public void onDataReceived(String msg) {
        }
        
        /**
         * Send message to the dashboard
         * @param msg Message to send
         * @throws IOException 
         */
        public void send(String msg) throws IOException {
            writer.write(""+(char)msg.length());
            writer.write(msg);
            writer.flush();
        }
        
        /**
         * Add item to the queue
         * @param key Key of the queued item
         * @param val Value of the queued item
         * @param args Args of the queued item
         */
        private void updateQueue(String key, String val, String[] args) {
            String[][][] newQueue = new String[queue.length+1][3][0];
            System.arraycopy(queue, 0, newQueue, 0, queue.length);
            newQueue[queue.length] = new String[][]{new String[]{key}, new String[]{val}, args};
            queue = newQueue;
            cleanQueue();
        }
    }
}
