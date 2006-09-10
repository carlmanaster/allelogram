package org.carlmanaster.allelogram.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.Genotype;

public class Chart extends JPanel {
	private static final Color BIN_COLOR = Color.CYAN;
	private List<Allele> alleles;
	private HashSet<Allele> controlAlleles = new HashSet<Allele>();
	private List<Bin> bins;
	private Scale xScale;
	private Scale yScale;
	private Colorizer colorizer = new Colorizer();
	private final HashSet<Genotype> selection;
	private boolean autoscale = true;
	
	public Chart(AllelogramApplet applet) {
		setBackground(Color.WHITE);
		selection = applet.getSelection();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		hiliteAlleles(g);
		paintBins(g);
		paintAlleles(g);
		emphasizeControlAlleles(g);
	}
	
	public Dimension getMinimumSize()		{return new Dimension(100, 100);}
	public Dimension getMaximumSize()		{return new Dimension(2000, 2000);}
	public Dimension getPreferredSize()	{return new Dimension(500, 500);}
	
	public void setSize(Dimension dimension) {
		super.setSize(dimension);
		makeScales();
	}

	private void paintBins(Graphics g) {
		if (bins == null)
			return;
		g.setColor(BIN_COLOR);
		for (Bin bin : bins)
			paintBin(g, bin);
	}

	private void paintBin(Graphics g, Bin bin) {
		drawHorizontalLine(g, yScale.toScreen(bin.getLow()));
		drawHorizontalLine(g, yScale.toScreen(bin.getHigh()));
	}
	
	private void drawHorizontalLine(Graphics g, int y) {
		g.drawLine(0, y, getWidth(), y);
	}

	private void hiliteAlleles(Graphics g) {
		if (alleles == null)
			return;
		
		g.setColor(Color.YELLOW);

		for (Genotype genotype : selection) 
			hiliteAlleles(g, genotype.getAlleles());
	}

	private void hiliteAlleles(Graphics g, ArrayList<Allele> selectedAlleles) {
		for (Allele allele : selectedAlleles)
			hiliteAllele(g, allele);
	}

	private void hiliteAllele(Graphics g, Allele allele) {
		Point p = alleleLocation(allele);
		g.fillRect(p.x-4, p.y-4, 10, 10);
	}

	private void paintAlleles(Graphics g) {
		if (alleles == null)
			return;
		g.setColor(Color.BLUE);
		for (int i = 0; i < alleles.size(); ++i)
			paintAllele(g, alleles.get(i), i);
	}

	private void paintAllele(Graphics g, Allele allele, int i) {
		g.setColor(colorizer.colorFor(allele));
		Point p = alleleLocation(allele, i);
		g.fillRect(p.x, p.y, 2, 2);
	}

	private void emphasizeControlAlleles(Graphics g) {
		g.setColor(Color.BLACK);
		for (Allele allele : controlAlleles)
			emphasizeControlAllele(g, allele);
	}

	private void emphasizeControlAllele(Graphics g, Allele allele) {
		Point p = alleleLocation(allele);
		g.drawLine(p.x - 5, p.y, p.x + 5, p.y);
	}

	private Point alleleLocation(Allele allele, int i) {
		int x = xScale.toScreen(i);
		int y = yScale.toScreen(allele.getAdjustedValue());
		return new Point(x, y);
	}

	private Point alleleLocation(Allele allele) {
		return alleleLocation(allele, alleles.indexOf(allele));
	}

	public void setAlleles(List<Allele> alleles)	{
		this.alleles = alleles;
		makeScales();
	}
	
	private void makeScales() {
		if (alleles == null)
			return;
		try {
			xScale = new Scale(0, alleles.size(), 0, getWidth());
			fitYScaleToData();
		} catch (Exception e) {
		}
		
	}

	private void fitYScaleToData() {
		if (!autoscale)
			return;
		try {
			yScale = new Scale(min(), max(), getHeight(), 0);
		} catch (Exception e) {
		}
	}
	
	public void adjustScales() {
		fitYScaleToData();
	}

	private double min() {
		double min = Double.MAX_VALUE;
		for (Allele allele : alleles) 
			min = Math.min(min, allele.getAdjustedValue());
		return min;
	}

	private double max() {
		double max = 0;
		for (Allele allele : alleles) 
			max = Math.max(max, allele.getAdjustedValue());
		return max;
	}

	public void setBins(List<Bin> bins)			{this.bins = bins;}

	public void setColorizer(Colorizer colorizer) {
		this.colorizer  = colorizer;
	}

	public Allele alleleAt(Point point) {
		if (alleles == null)
			return null;
		for (int i = 0; i < alleles.size(); ++i) {
			Allele allele = alleles.get(i);
			Point p = alleleLocation(allele, i);
			if (p.distance(point) < 4)
				return allele;
		}
		return null;
	}

	public void hiliteGenotype(Genotype genotype) {
		selection.clear();
		selection.add(genotype);
	}

	public void setControlGenotypes(List<Genotype> controlGenotypes) {
		controlAlleles.clear();
		for (Genotype genotype : controlGenotypes)
			controlAlleles.addAll(genotype.getAlleles());
	}

	public void drawZoomLine(int y) {
		
		Graphics g = getGraphics();
		g.setXORMode(getBackground());
		g.setColor(Color.BLACK);
		drawHorizontalLine(g, y);
		g.setPaintMode();
	}

	public void zoom(int start, int end) {
		autoscale = start > end;
		if (start > end) {
			fitYScaleToData();
			repaint();
			return;
		} else {
			try {
				yScale = new Scale(yScale.toData(end), yScale.toData(start), getHeight(), 0);
			} catch (Exception e) {
			}
			repaint();
		}
	}
}
