package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;

import org.carlmanaster.allelogram.gui.AllelogramApplet;
import org.carlmanaster.allelogram.gui.Chart;
import org.carlmanaster.allelogram.model.Allele;

public class AllelogramMouseDispatcher extends MouseDispatcher {
	private final AllelogramApplet applet;
	private final Chart chart;
	
	public AllelogramMouseDispatcher(AllelogramApplet applet, Chart chart) {
		this.applet	= applet;
		this.chart	= chart;
	}

	protected ClickerDragger findDelegate(MouseEvent event) {
		Allele allele = chart.alleleAt(event.getPoint());
		if (allele != null)
			return new AlleleClicker(applet, allele);
		if (true)
			return new Zoomer(chart);
		return doNothing();
	}

}
