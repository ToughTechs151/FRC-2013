package org.nashua.tt151;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.nashua.tt151.ServerConnection.ConnectionListener;
import org.nashua.tt151.devices.AnalogDevice;
import org.nashua.tt151.devices.AnalogDevice.AnalogType;
import org.nashua.tt151.devices.CANDevice;
import org.nashua.tt151.devices.CANDevice.JaguarType;
import org.nashua.tt151.devices.DigitalIODevice;
import org.nashua.tt151.devices.PWMOutDevice;
import org.nashua.tt151.devices.PWMOutDevice.PWMType;
import org.nashua.tt151.devices.RelayDevice;
import org.nashua.tt151.devices.RelayDevice.Direction;
import org.nashua.tt151.devices.RelayDevice.Value;
import org.nashua.tt151.imaging.Target;
import org.nashua.tt151.imaging.Target.Slot;
import org.nashua.tt151.libraries.parsers.ProtocolParsing.Command;
import org.nashua.tt151.libraries.parsers.ProtocolParsing.FastParser;
import org.nashua.tt151.libraries.parsers.ProtocolParsing.Key;
import org.nashua.tt151.libraries.parsers.ProtocolParsing.StringParser;
import org.nashua.tt151.module.AnalogModule;
import org.nashua.tt151.module.BandwidthMonitor;
import org.nashua.tt151.module.CANModule;
import org.nashua.tt151.module.CameraModule;
import org.nashua.tt151.module.LEDModule;
import org.nashua.tt151.module.SidecarModule;
import org.nashua.tt151.module.SpeedModule;
import org.nashua.tt151.module.SpeedModule.Mode;
import org.nashua.tt151.module.StatusModule;
import org.nashua.tt151.module.StatusModule.Status;
import org.nashua.tt151.ui.SlimFrame;

public class Dashboard {
	private static AnalogModule analogModule = new AnalogModule(
			"Slot 1 - Analog");
	private static CANModule can = new CANModule("CAN Devices");
	public static SlimFrame frame;
	private static StatusModule state = new StatusModule();
	private static int paintFrame = 0;
	private static SidecarModule sidecar1 = new SidecarModule("Slot 2 - DS 1");
	public static CameraModule camera;
	public static final File LOG_FILE = new File(FileSystemView
			.getFileSystemView().getHomeDirectory().getPath()
			+ File.separator
			+ "Dash2013LogFiles"
			+ File.separator
			+ System.currentTimeMillis() + ".log");
	public static JPanel panel;
	private static final JTextPane log = new JTextPane();
	private static final SpeedModule speed = new SpeedModule();

	public static int getPaintFrame() {
		return paintFrame;
	}

	public static void main(String[] args) {
		new Dashboard(args);
	}

	private JLabel camAngleLabel;
	private ServerConnection server;
	private PrintWriter writer = null;
	private BandwidthMonitor bwmonitor;
	private boolean devBuild;

	public Dashboard(String[] args) {
		if (!LOG_FILE.exists()) {
			try {
				LOG_FILE.getParentFile().mkdirs();
				LOG_FILE.createNewFile();
				writer = new PrintWriter(new FileWriter(LOG_FILE));
			} catch (IOException e) {
			}
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		devBuild = false;
		String ip = "10.1.51.11";
		int port = 1735;
		if (args != null) {
			if (args.length > 0) {
				for (String arg : args) {
					if (arg.equals("--dev")) {
						devBuild = true;
					} else if (arg.startsWith("--camIP=")) {
						ip = arg.substring(arg.indexOf("=") + 1).trim();
						System.out.println("Camera IP Address: " + ip);
					} else if (arg.startsWith("--port=")) {
						port = Integer.parseInt(arg.substring(
								arg.indexOf("=") + 1).trim());
						System.out.println("Port " + port);
					}
				}
			}
		}
		camera = new CameraModule(ip, devBuild);
		/************************ Create Window *****************************/
		frame = new SlimFrame("FRC 151 - Tough Techs - Dashboard 2013"
				+ (devBuild ? " (Development)" : ""));
		panel = new JPanel(null);
		if (devBuild) {
			frame.setBackground(Color.RED.darker().darker().darker().darker());
			frame.setForeground(Color.WHITE);
			frame.setIconImage(new ImageIcon(Dashboard.class
					.getResource("/dev.png")).getImage());
		} else {
			frame.setIconImage(new ImageIcon(Dashboard.class
					.getResource("/dash.png")).getImage());
		}
		panel.setBackground(Color.DARK_GRAY.darker().darker().darker());
		frame.setResizable(false);

		/*********************** Add To Window ******************************/
		int x = 0;
		int y = 0;
		// Analog
		analogModule.setLocation(x, y);
		panel.add(analogModule);
		x += analogModule.getWidth() + 2;
		// Sidecar 1
		sidecar1.setLocation(x, y);
		panel.add(sidecar1);
		x += sidecar1.getWidth() + 2;
		// CAN
		can.setLocation(x, y);
		panel.add(can);
		x += can.getWidth() + 2;
		// Camera
		camera.setLocation(x, y);
		panel.add(camera);
		y += camera.getHeight() + 2;
		// Camera Angle Label
		camAngleLabel = new JLabel("Camera Angle: 0\u00b0", JLabel.CENTER);
		camAngleLabel.setForeground(Color.LIGHT_GRAY);
		camAngleLabel.setBounds(x, y, 320, 12);
		panel.add(camAngleLabel);
		y += camAngleLabel.getHeight() + 2;
		// LED
		LEDModule ledPanel = new LEDModule(devBuild);
		ledPanel.setBounds(x, y, 320, devBuild?105:25);
		panel.add(ledPanel);
		y += ledPanel.getHeight() + 2;
		// Logo
		if (!devBuild) {
			ImageIcon logo = new ImageIcon(
					Dashboard.class.getResource("/ttlogo.png"));
			JLabel lab = new JLabel(logo);
			lab.setSize(new Dimension(logo.getIconWidth(), logo.getIconHeight()));
			lab.setLocation(x, y);
			panel.add(lab);
			y += lab.getHeight() + 2;
		} else {
			y += camera.getHeight() + 2;
		}
		x += camera.getWidth() + 2;
		if (!devBuild) {
			// Driver Controls
			y = 0;
			ImageIcon dControls = new ImageIcon(
					Dashboard.class.getResource("/driver.png"));
			JLabel dConLab = new JLabel(dControls);
			dConLab.setSize(new Dimension(dControls.getIconWidth(), dControls
					.getIconHeight()));
			dConLab.setLocation(x, y);
			panel.add(dConLab);
			y += 242;
			// Drive Mode
			speed.setBounds(x, y - 42, dConLab.getWidth(), 40);
			panel.add(speed);
			// Shooter Controls
			ImageIcon sControls = new ImageIcon(
					Dashboard.class.getResource("/shooter.png"));
			JLabel sConLab = new JLabel(sControls);
			sConLab.setSize(new Dimension(sControls.getIconWidth(), sControls
					.getIconHeight()));
			sConLab.setLocation(x, y);
			panel.add(sConLab);
			x += sConLab.getWidth() + 2;
			y += sConLab.getHeight() + 2;
		} else {
			JScrollPane srpane = new JScrollPane(log);
			srpane.setBorder(null);
			log.setEditable(false);
			log.setBackground(Color.DARK_GRAY.darker().darker().darker()
					.darker());
			log.setForeground(Color.LIGHT_GRAY);
			srpane.setBounds(x - 322, 242, 320, 200);
			panel.add(srpane);
		}
		// Status
		state.setBounds(0, 450, x, 40);
		panel.add(state);

		/************************ Show Window *******************************/
		if (x > 2) {
			x -= 2;
		}
		panel.setBounds(5, 20, x, 490);
		frame.add(panel);
		frame.setSize(x, 490);
		frame.setVisible(true);

		new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
			public void run() {
				frame.repaint();
				paintFrame = (paintFrame + 1) % 8;
			}
		}, 1, 100);

		try {
			server = new ServerConnection(port, new ConnectionListener() {
				public void onConnect(Socket s) {
					System.out.println("[Connected to " + s + "]");
					analogModule.clearAnalogDevices();
					sidecar1.clearPWMOutDevices();
					sidecar1.clearDigitalIODevices();
					sidecar1.clearRelayDevices();
					can.clearCANDevices();
					DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
					String time = df.format(Calendar.getInstance().getTime());
					try {
						writer.println("[Connected to " + s + " at "+time+"]");
					} catch (Exception e) {
					}
					log.setText("[Connected at "+time+"]\n" + log.getText());
					state.setStatus(Status.CONNECTED);
				}

				public void onDataReceived(Socket s, String msg) {
					try {
						Command command = FastParser.getCommand(msg);

						if (msg.startsWith("ERR:")) {
							String[] message = msg.split(":");
							if (message.length > 1) {
								if (message[1].equals("HOPJAM")) {
									// Hopper Jammed
									state.setStatus(Status.HOPPER_JAM);
								} else if (message[1].equals("HOPRST")) {
									// Hopper Good
									state.setStatus(Status.CONNECTED);
								}
							}
							try {
								writer.println(msg);
							} catch (Exception e) {
							}
							log.setText(msg + "\n" + log.getText());
						} else if (msg.startsWith("D:")) {
							if (msg.charAt(2) == 'T') {
								speed.setMode(Mode.TURBO);
							} else if (msg.charAt(2) == 'C') {
								speed.setMode(Mode.CREEP);
							} else if (msg.charAt(2) == 'D') {
								speed.setMode(Mode.DISABLED);
							} else if (msg.charAt(2) == 'A') {
								speed.setMode(Mode.AUTOMATED);
							} else {
								speed.setMode(Mode.NORMAL);
							}
						} else if (StringParser.isSend(msg)) {
							//System.out.println(msg);
							Key key = StringParser.getKey(msg);
							String val = StringParser.getValue(msg);
							String[] args = StringParser.getArgs(msg);
							if (key.equals(Key.AnalogValue)) {
								analogModule.registerAnalogDevice(new AnalogDevice(
										Integer.parseInt(val), args[0], Integer
												.parseInt(args[1]), AnalogType
												.getFromShorthand(args[2]
														.charAt(0))));
							} else if (key.equals(Key.PWMValue)) {
								sidecar1.registerPWMOutDevice(new PWMOutDevice(
										Integer.parseInt(val), args[0],
										Integer.parseInt(args[1].substring(1)),
										PWMType.getFromShorthand(args[2]
												.charAt(0))));
							} else if (key.equals(Key.RelayValue)) {
								sidecar1.registerRelayDevice(new RelayDevice(
										Value.getValueForID(Integer
												.parseInt(val)), args[0],
										Integer.parseInt(args[1].substring(1)),
										Direction.getDirectionForID(Integer
												.parseInt(args[2]))));
							} else if (key.equals(Key.CANValue)) {
								can.registerCANDevice(new CANDevice(devBuild, args[0],
										Integer.parseInt(args[1]), Double
												.parseDouble(val), Double
												.parseDouble(args[2]), Double
												.parseDouble(args[3]), Double
												.parseDouble(args[4]), Integer
												.parseInt(args[5]), JaguarType
												.getTypeForID(Integer
														.parseInt(args[6]))));
							} else if (key.equals(Key.DigitalIO)) {
								sidecar1.registerDigitalIODevice(new DigitalIODevice(
										Boolean.parseBoolean(val), args[0],
										Integer.parseInt(args[1])));
							} else if (key.equals(Key.CamAngle)) {
								camera.setCameraAngle(Double.parseDouble(val));
								camAngleLabel.setText(String.format(
										"Camera Angle: %.4f\u00b0",
										camera.getCameraAngle()));
							}
						} else if (StringParser.isQuery(msg)) {
							if (StringParser.getKey(msg).equals(Key.CamAngle)) {
								Slot slot = Target.Slot.getTargetWithID(Integer
										.parseInt(StringParser.getArgs(msg)[0]));
								Target targ = getTarget(slot);
								if (targ != null
										&& Math.abs(targ.y
												+ targ.h
												/ 2.0
												- camera.getProcessor().overlay
														.getHeight() / 2) > 2) {
									server.send(StringParser
											.createMessage(
													Command.Reply,
													Key.CamAngle,
													""
															+ (-0.0001 * (targ.y
																	+ targ.h
																	/ 2.0 - camera
																	.getProcessor().overlay
																	.getHeight() / 2)),
													new String[] { "0" }));
								} else {
									server.send(StringParser.createMessage(
											Command.Reply, Key.CamAngle,
											"0.00", new String[] { "0" }));
								}
							} else if (StringParser.getKey(msg).equals(
									Key.Position)) {
								Slot slot = Target.Slot.getTargetWithID(Integer
										.parseInt(StringParser.getArgs(msg)[0]));
								Target targ = getTarget(slot);
								if (targ != null) {
									server.send(StringParser.createMessage(
											Command.Reply, Key.Position, ""
													+ targ.distance,
											new String[] { "" + targ.offset }));
								} else {
									server.send(StringParser.createMessage(
											Command.Reply, Key.Position, "-1",
											new String[] { "0" }));
								}
							} else if (StringParser.getKey(msg).equals(
									Key.ShooterSpeed)) {
								Slot slot = Target.Slot.getTargetWithID(Integer
										.parseInt(StringParser.getArgs(msg)[0]));
								Target targ = getTarget(slot);
								if (targ != null) {
									server.send(StringParser.createMessage(
											Command.Reply, Key.ShooterSpeed, ""
													+ targ.slot.getVolts(),
											new String[] {"0"}));
								} else {
									server.send(StringParser.createMessage(
											Command.Reply, Key.ShooterSpeed, "0",
											new String[] { "0" }));
								}
							}
						}
					} catch (Exception e) {
					}

				}

				public void onDisconnect(Socket s) {
					System.out.println("[Disconnected from " + s + "]");
					analogModule.clearAnalogDevices();
					sidecar1.clearPWMOutDevices();
					sidecar1.clearDigitalIODevices();
					sidecar1.clearRelayDevices();
					can.clearCANDevices();
					DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
					String time = df.format(Calendar.getInstance().getTime());
					try {
						writer.println("[Disconnected to " + s + " at "+time+"]");
					} catch (Exception e) {
					}
					log.setText("[Disconnected at "+time+"]\n" + log.getText());
					state.setStatus(Status.DISCONNECTED);
				}
			});
		} catch (Exception e) {
			frame.setVisible(false);
			JOptionPane.showMessageDialog(null, "Port " + port
					+ " already in use. Dash is shutting down.");
			System.exit(0);
		}
		bwmonitor = new BandwidthMonitor();
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		bwmonitor.setLocation(
				maxBounds.x + maxBounds.width - bwmonitor.getWidth(),
				maxBounds.y + maxBounds.height - bwmonitor.getHeight());
		frame.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_B) {
					bwmonitor.setVisible(true);
				}
			}
		});
		//bwmonitor.setAlwaysOnTop(true);
	}

	private Target getTarget(Slot slot) {
		ArrayList<Target> t = camera.getProcessor().targets;
		Target targ = null;
		if (slot.equals(Slot.UNKNOWN) || t.size() == 0) {

		} else if (slot.equals(Slot.LOWEST)) {
			targ = t.get(0);
		} else if (slot.equals(Slot.BOTTOM)) {
			if (t.get(0).slot.equals(Slot.BOTTOM)) {
				targ = t.get(0);
			}
		} else if (slot.equals(Slot.MIDDLE_LEFT)) {
			if (t.get(0).slot.equals(Slot.MIDDLE)) {
				targ = t.get(0);
			} else if (t.size() > 1 && t.get(1).slot.equals(Slot.MIDDLE)) {
				targ = t.get(1);
			}
		} else if (slot.equals(Slot.MIDDLE_RIGHT)) {
			if (t.size() > 1 && t.get(1).slot.equals(Slot.MIDDLE)) {
				targ = t.get(1);
			} else if (t.size() > 2 && t.get(2).slot.equals(Slot.MIDDLE)) {
				targ = t.get(2);
			}
		} else if (slot.equals(Slot.TOP)) {
			for (int i = t.size() - 1; i > -1; i--) {
				if (t.get(i).slot.equals(Slot.TOP)) {
					targ = t.get(i);
					break;
				}
			}
		} else if (slot.equals(Slot.HIGHEST)) {
			targ = t.get(t.size() - 1);
		}
		return targ;
	}
}
