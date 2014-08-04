package org.nashua.tt151.module;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nashua.tt151.Dashboard;
import org.nashua.tt151.imaging.LED;

public class LEDModule extends JPanel {
	private JSpinner bMax;
	private JSpinner bMin;
	private JCheckBox bRev;
	private JSpinner gMax;
	private JSpinner gMin;
	private JCheckBox gRev;
	private JSpinner hMax;
	private JSpinner hMin;
	private JCheckBox hRev;
	private JSpinner rMax;
	private JSpinner rMin;
	private JCheckBox rRev;
	private JButton saveButton;
	private JSpinner sMax;
	private JSpinner sMin;
	private JCheckBox sRev;
	private JSpinner vMax;
	private JSpinner vMin;
	private JCheckBox vRev;

	public LEDModule(boolean devBuild) {
		super(new GridBagLayout());
		setBackground(Dashboard.panel.getBackground());
		// LED Label
		JLabel lLab = new JLabel("LED: ");
		lLab.setForeground(Color.LIGHT_GRAY);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		add(lLab, gbc);
		
		// LED Combo
		final JComboBox<LED> leds = new JComboBox<LED>(LED.values());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.gridwidth = 7;
		leds.setSelectedItem(Dashboard.camera.getLED());
		leds.setFocusable(false);
		add(leds, gbc);
		
		// Hue
		JLabel hLab = new JLabel("Hue: ");
		hLab.setForeground(Color.LIGHT_GRAY);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(hLab, gbc);
		}
		hMin = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getHueMinimum(), 0, 255, 1));
		hMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setHueMinimum(((SpinnerNumberModel)hMin.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		if (devBuild) {
			add(hMin, gbc);
		}
		hMax = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getHueMaximum(), 0, 255, 1));
		hMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setHueMaximum(((SpinnerNumberModel)hMax.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		if (devBuild) {
			add(hMax, gbc);
		}
		hRev = new JCheckBox();
		hRev.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Dashboard.camera.getLED().setHueReversed(hRev.isSelected());
			}
		});
		hRev.setBackground(Dashboard.panel.getBackground());
		hRev.setSelected(Dashboard.camera.getLED().isHueReversed());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(hRev, gbc);
		}
		// Red
		JLabel rLab = new JLabel(" Red: ");
		rLab.setForeground(Color.LIGHT_GRAY);
		gbc.gridx++;
		if (devBuild) {
			add(rLab, gbc);
		}
		rMin = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getRedMinimum(), 0, 255, 1));
		rMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setRedMinimum(((SpinnerNumberModel)rMin.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		if (devBuild) {
			add(rMin, gbc);
		}
		rMax = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getRedMaximum(), 0, 255, 1));
		rMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setRedMaximum(((SpinnerNumberModel)rMax.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		if (devBuild) {
			add(rMax, gbc);
		}
		rRev = new JCheckBox();
		rRev.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Dashboard.camera.getLED().setRedReversed(rRev.isSelected());
			}
		});
		rRev.setBackground(Dashboard.panel.getBackground());
		rRev.setSelected(Dashboard.camera.getLED().isRedReversed());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(rRev, gbc);
		}
		// Sat
		JLabel sLab = new JLabel("Sat: ");
		sLab.setForeground(Color.LIGHT_GRAY);
		gbc.gridx = 0;
		gbc.gridy++;
		if (devBuild) {
			add(sLab, gbc);
		}
		sMin = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getSaturationMinimum(), 0, 255, 1));
		sMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setSaturationMinimum(((SpinnerNumberModel)sMin.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		if (devBuild) {
			add(sMin, gbc);
		}
		sMax = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getSaturationMaximum(), 0, 255, 1));
		sMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setSaturationMaximum(((SpinnerNumberModel)sMax.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		if (devBuild) {
			add(sMax, gbc);
		}
		sRev = new JCheckBox();
		sRev.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Dashboard.camera.getLED().setSaturationReversed(sRev.isSelected());
			}
		});
		sRev.setBackground(Dashboard.panel.getBackground());
		sRev.setSelected(Dashboard.camera.getLED().isSaturationReversed());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(sRev, gbc);
		}
		// Green
		JLabel gLab = new JLabel(" Grn: ");
		gLab.setForeground(Color.LIGHT_GRAY);
		gbc.gridx++;
		if (devBuild) {
			add(gLab, gbc);
		}
		gMin = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getGreenMinimum(), 0, 255, 1));
		gMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setGreenMinimum(((SpinnerNumberModel)gMin.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		if (devBuild) {
			add(gMin, gbc);
		}
		gMax = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getGreenMaximum(), 0, 255, 1));
		gMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setGreenMaximum(((SpinnerNumberModel)gMax.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		if (devBuild) {
			add(gMax, gbc);
		}
		gRev = new JCheckBox();
		gRev.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Dashboard.camera.getLED().setGreenReversed(gRev.isSelected());
			}
		});
		gRev.setBackground(Dashboard.panel.getBackground());
		gRev.setSelected(Dashboard.camera.getLED().isGreenReversed());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(gRev, gbc);
		}
		// Value
		JLabel vLab = new JLabel("Val: ");
		vLab.setForeground(Color.LIGHT_GRAY);
		gbc.gridx = 0;
		gbc.gridy++;
		if (devBuild) {
			add(vLab, gbc);
		}
		vMin = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getValueMinimum(), 0, 255, 1));
		vMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setValueMinimum(((SpinnerNumberModel)vMin.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		if (devBuild) {
			add(vMin, gbc);
		}
		vMax = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getValueMaximum(), 0, 255, 1));
		vMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setValueMaximum(((SpinnerNumberModel)vMax.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		if (devBuild) {
			add(vMax, gbc);
		}
		vRev = new JCheckBox();
		vRev.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Dashboard.camera.getLED().setValueReversed(vRev.isSelected());
			}
		});
		vRev.setBackground(Dashboard.panel.getBackground());
		vRev.setSelected(Dashboard.camera.getLED().isValueReversed());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(vRev, gbc);
		}
		// Blue
		JLabel bLab = new JLabel(" Blue: ");
		bLab.setForeground(Color.LIGHT_GRAY);
		gbc.gridx++;
		if (devBuild) {
			add(bLab, gbc);
		}
		bMin = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getBlueMinimum(), 0, 255, 1));
		bMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setBlueMinimum(((SpinnerNumberModel)bMin.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		if (devBuild) {
			add(bMin, gbc);
		}
		bMax = new JSpinner(new SpinnerNumberModel(Dashboard.camera.getLED().getBlueMaximum(), 0, 255, 1));
		bMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Dashboard.camera.getLED().setBlueMaximum(((SpinnerNumberModel)bMax.getModel()).getNumber().intValue());
			}
		});
		gbc.gridx++;
		if (devBuild) {
			add(bMax, gbc);
		}
		bRev = new JCheckBox();
		bRev.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Dashboard.camera.getLED().setBlueReversed(bRev.isSelected());
			}
		});
		bRev.setBackground(Dashboard.panel.getBackground());
		bRev.setSelected(Dashboard.camera.getLED().isBlueReversed());
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		if (devBuild) {
			add(bRev, gbc);
		}
		
		saveButton = new JButton("Save");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.gridwidth = 8;
		if (devBuild) {
			add(saveButton, gbc);
		}
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LED.saveConfig();
			}
		});
		
		leds.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Dashboard.camera.setLED(leds.getItemAt(leds.getSelectedIndex()));
				hMin.setValue(new Integer(Dashboard.camera.getLED().getHueMinimum()));
				hMax.setValue(new Integer(Dashboard.camera.getLED().getHueMaximum()));
				hRev.setSelected(Dashboard.camera.getLED().isHueReversed());
				rMin.setValue(new Integer(Dashboard.camera.getLED().getRedMinimum()));
				rMax.setValue(new Integer(Dashboard.camera.getLED().getRedMaximum()));
				rRev.setSelected(Dashboard.camera.getLED().isRedReversed());
				sMin.setValue(new Integer(Dashboard.camera.getLED().getSaturationMinimum()));
				sMax.setValue(new Integer(Dashboard.camera.getLED().getSaturationMaximum()));
				sRev.setSelected(Dashboard.camera.getLED().isSaturationReversed());
				gMin.setValue(new Integer(Dashboard.camera.getLED().getGreenMinimum()));
				gMax.setValue(new Integer(Dashboard.camera.getLED().getGreenMaximum()));
				gRev.setSelected(Dashboard.camera.getLED().isGreenReversed());
				vMin.setValue(new Integer(Dashboard.camera.getLED().getValueMinimum()));
				vMax.setValue(new Integer(Dashboard.camera.getLED().getValueMaximum()));
				vRev.setSelected(Dashboard.camera.getLED().isValueReversed());
				bMin.setValue(new Integer(Dashboard.camera.getLED().getBlueMinimum()));
				bMax.setValue(new Integer(Dashboard.camera.getLED().getBlueMaximum()));
				bRev.setSelected(Dashboard.camera.getLED().isBlueReversed());
			}
		});
	}
}
