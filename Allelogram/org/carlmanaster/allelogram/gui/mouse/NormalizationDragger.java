package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;
import java.util.List;

import org.carlmanaster.allelogram.gui.Chart;
import org.carlmanaster.allelogram.model.Genotype;

public class NormalizationDragger extends ClickerDragger {
	private final Chart chart;
	private final List<Genotype> genotypes;
	private double originalY;
	private double originalOffset;

	public NormalizationDragger(Chart chart, final List<Genotype> genotypes) {
		super();
		this.chart = chart;
		this.genotypes = genotypes;
		originalOffset = genotypes.get(0).getOffset();
	}
	
	public void mousePressed(MouseEvent e) {
		originalY = chart.toDataY(e.getPoint().y) - originalOffset;
	}
	
	public void mouseDragged(MouseEvent e) {
		double offset = chart.toDataY(e.getPoint().y) - originalY;
		List<Genotype> x = genotypes;
		for (Genotype genotype : x)
			genotype.offsetBy(offset);
		chart.repaint();
	}

}
