package com.telemetry.gui.device;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class SpeedDialPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4277454858501952039L;
	DefaultValueDataset speed_dataset;
	DialPlot speed_dial = new DialPlot();
	
	public SpeedDialPanel() {
		super(new BorderLayout());
		
		speed_dataset = new DefaultValueDataset(0D);
		
		speed_dial.setView(0.0D, 0.0D, 1.0D, 1.0D);
		speed_dial.setDataset(0, speed_dataset);
		
		StandardDialFrame standard_dial_frame = new StandardDialFrame();
		standard_dial_frame.setBackgroundPaint(Color.LIGHT_GRAY);
		standard_dial_frame.setForegroundPaint(Color.DARK_GRAY);
		speed_dial.setDialFrame(standard_dial_frame);
		
		GradientPaint gradient_paint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(120, 232, 255));
		DialBackground dial_background = new DialBackground(gradient_paint);
		dial_background.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		speed_dial.setBackground(dial_background);
		
		DialTextAnnotation speed_dial_annotation = new DialTextAnnotation("Speed");
		speed_dial_annotation.setFont(new Font("Dialog", 1, 14));
		speed_dial_annotation.setRadius(0.69999999999999999D);
		speed_dial.addLayer(speed_dial_annotation);
		
		StandardDialScale speed_scale = new StandardDialScale(0D, 80D, -120D, -300D, 10D, 1);
		speed_scale.setTickRadius(0.88D);
		speed_scale.setTickLabelOffset(0.169999999999999999D);
		speed_scale.setTickLabelFont(new Font("Dialog", 0, 14));
		speed_dial.addScale(0, speed_scale);
		
		speed_dial.mapDatasetToScale(1, 1);
		
		DialPointer.Pointer pointer = new DialPointer.Pointer(0);
		speed_dial.addPointer(pointer);

		DialCap dial_cap = new DialCap();
		dial_cap.setRadius(0.1000000000000000001D);
		speed_dial.setCap(dial_cap);
		
		JFreeChart speed_chart = new JFreeChart(speed_dial);
		speed_chart.setTitle("Speed Dial");
		ChartPanel speed_panel = new ChartPanel(speed_chart);
		speed_panel.setPreferredSize(new Dimension(200, 200));
		add(speed_panel);
	}
	
	public void updateDial(String speed) {
		double new_speed = Double.parseDouble(speed);
		if(new_speed != 0D) {
			speed_dataset.setValue(new_speed);
			validate();
			repaint();
		}
	}
	
	public void updateDial(HashMap<String, Double> motor_data) {
		double new_speed = motor_data.get("motor_speed");
		if(new_speed != 0D) {
			speed_dataset.setValue(new_speed);
			validate();
			repaint();
		}
	}
	private double abs(Double decimal) {
		if(decimal > 0)
			return decimal;
		else if(decimal < 0)
			return -decimal;
		else
			return decimal;
	}
}
