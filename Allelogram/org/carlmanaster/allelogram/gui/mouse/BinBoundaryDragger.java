package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;

import org.carlmanaster.allelogram.gui.AllelogramApplet;
import org.carlmanaster.allelogram.gui.BinBoundary;
import org.carlmanaster.allelogram.gui.Chart;

public class BinBoundaryDragger extends ClickerDragger{
	private final AllelogramApplet applet;
	private final Chart chart;
	private final BinBoundary boundary;
	private int y = -1;
	
	public BinBoundaryDragger(AllelogramApplet applet, Chart chart, BinBoundary boundary) {
		this.applet = applet;
		this.chart = chart;
		this.boundary = boundary;
	}

	public void mousePressed(MouseEvent event) {
		drawBinLine(boundary.getV());
		drawOldAndNewLines(event);
	}

	public void mouseReleased(MouseEvent event) {
		drawOldAndNewLines(event);
		boundary.set(chart.toDataY(y));
		applet.renameBins();
		chart.repaint();
	}

	public void mouseDragged(MouseEvent event) {
		drawOldAndNewLines(event);
	}

	private void drawOldAndNewLines(MouseEvent event) {
		drawBinLine(y);
		y = event.getPoint().y;
		drawBinLine(y);
	}

	private void drawBinLine(int v) {
		chart.drawBinLine(v);
	}
	
}
