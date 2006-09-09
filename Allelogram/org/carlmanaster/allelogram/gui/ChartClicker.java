package org.carlmanaster.allelogram.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.carlmanaster.allelogram.model.Allele;

public class ChartClicker extends MouseAdapter {

	private final AllelogramApplet applet;
	private final Chart chart;
	private Allele allele = null;

	public ChartClicker(AllelogramApplet applet, Chart chart) {
		this.applet = applet;
		this.chart = chart;
	}
	
	public void mousePressed(MouseEvent event) {
		allele = chart.alleleAt(event.getPoint());
		if (allele != null)
			chart.rejectZooms();
	}

	public void mouseClicked(MouseEvent event) {
		if (allele == null)
			return;
		
		boolean commandKey = isMac() ? event.isMetaDown() : event.isControlDown();
		boolean optionKey = event.isAltDown();
		
		applet.selectGenotype(allele.getGenotype(), commandKey, optionKey);
		chart.acceptZooms();
	}

	private static boolean isMac() {
		return System.getProperty("os.name").startsWith("Mac");
	}

}
