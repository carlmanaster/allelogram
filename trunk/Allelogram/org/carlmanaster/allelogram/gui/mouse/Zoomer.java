package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;

import org.carlmanaster.allelogram.gui.Chart;

public class Zoomer extends ClickerDragger {
	private final Chart chart;
	private int start;
	private int y;
	
	public Zoomer(Chart chart) {
		this.chart = chart;
	}
	
	public void mousePressed(MouseEvent event) {
		start = event.getPoint().y;
		y = start;
		chart.drawZoomLine(y);
	}
	
	public void mouseReleased(MouseEvent event) {
		chart.zoom(start, event.getPoint().y);
	}

	public void mouseDragged(MouseEvent event) {
		drawLine();
		y = event.getPoint().y;
		drawLine();
	}

	private void drawLine() {
		if (y != start)
			chart.drawZoomLine(y);
	}

}
