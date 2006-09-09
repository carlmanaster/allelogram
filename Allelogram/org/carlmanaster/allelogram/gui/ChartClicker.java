package org.carlmanaster.allelogram.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.carlmanaster.allelogram.model.Allele;

public class ChartClicker extends MouseAdapter implements MouseListener,
		MouseMotionListener {

	private final AllelogramApplet applet;
	private final Chart chart;

	public ChartClicker(AllelogramApplet applet, Chart chart) {
		this.applet = applet;
		this.chart = chart;
	}

	public void mouseClicked(MouseEvent event) {
		Allele allele = chart.alleleAt(event.getPoint());
		if (allele == null)
			return;
		
		boolean commandKey = isMac() ? event.isMetaDown() : event.isControlDown();
		boolean optionKey = event.isAltDown();
		
		applet.selectGenotype(allele.getGenotype(), commandKey, optionKey);
	}

	private static boolean isMac() {
		return System.getProperty("os.name").startsWith("Mac");
	}
	
	public void mouseDragged(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}

}