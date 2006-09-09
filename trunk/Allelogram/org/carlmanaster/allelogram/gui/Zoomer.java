package org.carlmanaster.allelogram.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Zoomer extends MouseAdapter implements MouseMotionListener {
	private final Chart chart;
	private int start;
	private int y;
	
	public void mousePressed(MouseEvent event) {
		if (!chart.isZoomable())
			return;
		start = event.getPoint().y;
		y = start;
		chart.drawZoomLine(y);
	}
	
	public void mouseReleased(MouseEvent event) {
		if (!chart.isZoomable())
			return;
		chart.zoom(start, event.getPoint().y);
	}

	public Zoomer(Chart chart) {
		this.chart = chart;
	}

	public void mouseDragged(MouseEvent event) {
		if (!chart.isZoomable())
			return;
		drawLine();
		y = event.getPoint().y;
		drawLine();
	}

	private void drawLine() {
		if (y != start)
			chart.drawZoomLine(y);
	}

	public void mouseMoved(MouseEvent event) {
	}

}
