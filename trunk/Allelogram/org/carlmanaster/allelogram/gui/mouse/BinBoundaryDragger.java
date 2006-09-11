package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;

import org.carlmanaster.allelogram.gui.BinBoundary;
import org.carlmanaster.allelogram.gui.Chart;

public class BinBoundaryDragger extends ClickerDragger{

	private final Chart chart;
	private final BinBoundary boundary;
	private int y = -1;
	
	public BinBoundaryDragger(Chart chart, BinBoundary boundary) {
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
