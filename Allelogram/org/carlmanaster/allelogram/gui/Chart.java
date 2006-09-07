package org.carlmanaster.allelogram.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;

public class Chart extends JPanel {
	private static final Color BIN_COLOR = Color.CYAN;
	private List<Allele> alleles;
	private List<Bin> bins;
	private Scale xScale;
	private Scale yScale;
	private Colorizer colorizer = new Colorizer();
	
	public Chart() {
		setBackground(Color.WHITE);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		paintBins(g);
		paintAlleles(g);
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

	private void paintAlleles(Graphics g) {
		if (alleles == null)
			return;
		g.setColor(Color.BLUE);
		for (int i = 0; i < alleles.size(); ++i)
			paintAllele(g, alleles.get(i), i);
	}

	private void paintAllele(Graphics g, Allele allele, int i) {
		g.setColor(colorizer.colorFor(allele));
		int x = xScale.toScreen(i);
		int y = yScale.toScreen(allele.getAdjustedValue());
		g.fillRect(x, y, 2, 2);
	}

	public void setAlleles(List<Allele> alleles)	{
		this.alleles = alleles;
		makeScales();
	}
	
	private void makeScales() {
		if (alleles == null)
			return;
		double min = min();
		double max = max();
		try {
			xScale = new Scale(0, alleles.size(), 0, getWidth());
			yScale = new Scale(min, max, getHeight(), 0);
		} catch (Exception e) {
		}
		
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
}
