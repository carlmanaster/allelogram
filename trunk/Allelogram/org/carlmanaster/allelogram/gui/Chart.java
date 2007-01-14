package org.carlmanaster.allelogram.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Classifier;
import org.carlmanaster.allelogram.model.Genotype;

public class Chart extends JPanel {
	private static final Color BIN_COLOR = Color.CYAN;

	private static final Color NORMALIZATION_COLOR = new Color(204, 255, 255);

	private List<Allele> alleles;
	private HashSet<Allele> controlAlleles = new HashSet<Allele>();
	private Map<String, Bin> bins;
	private int axisWidth = 30;
	private Scale xScale;
	private Scale yScale;
	private Colorizer colorizer = new Colorizer();
	private final HashSet<Genotype> selection;
	private boolean autoscale = true;
	private boolean showingNormalization = false;
	private final AllelogramApplet applet;

	public Chart(AllelogramApplet applet) {
		this.applet = applet;
		setBackground(Color.WHITE);
		selection = applet.getSelection();
	}
	
	public void toggleNormalization() {
		showingNormalization = !showingNormalization;
		repaint();
	}
	
	public boolean isShowingNormalization() {return showingNormalization;}

	public void paint(Graphics g) {
		super.paint(g);
		paintAxis(g);
		paintNormalization(g);
		hiliteAlleles(g);
		paintBins(g);
		paintAlleles(g);
		emphasizeControlAlleles(g);
	}

	private void paintAxis(Graphics g) {
		if (yScale == null)
			return;
		int h = getHeight();
		g.setColor(Color.white);
		g.fillRect(0, 0, axisWidth, h);
		g.setColor(Color.black);
		drawVerticalLine(g, axisWidth);
		final int letterHeight = g.getFontMetrics().getAscent();
		final int fontWidth = g.getFontMetrics().charWidth('0');
		for (int i = (int) yScale.toData(h) % 5 * 5; i < yScale.toData(0); i += 5) {
			int y = yScale.toScreen(i);
			g.drawString("" + i, 0, y + letterHeight / 2);
			g.drawLine(3 * fontWidth + 2, y, axisWidth, y);
		}

	}

	public Dimension getMinimumSize() {
		return new Dimension(100, 100);
	}

	public Dimension getMaximumSize() {
		return new Dimension(2000, 2000);
	}

	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}

	public void setSize(Dimension dimension) {
		super.setSize(dimension);
		makeScales();
	}

	private void paintNormalization(Graphics g) {
		if (!showingNormalization)
			return;
		final Classifier sort = applet.getSortClassifier();
		if (sort == null)
			return;
		
		for (Normalization normalization : makeNormalizations(sort))
			paintNormalization(g, normalization);
	}

	private ArrayList<Normalization> makeNormalizations(final Classifier sort) {
		Classification lastClassification = null;
		int start = 0;
		int end = 0;
		double offset = 0.0;
		ArrayList<Normalization> normalizations = new ArrayList<Normalization>();
		for (int i = 0; i < alleles.size(); ++i) {
			final Allele allele = alleles.get(i);
			int h = alleleLocation(allele, i).x;
			Classification classification = sort.classify(allele.getGenotype());

			if (classification.equals(lastClassification) && i < alleles.size() - 1) {
				end = h;
			} else {
				if (lastClassification != null) {
					normalizations.add(new Normalization(start, end, offset));
				}
				lastClassification = classification;
				start = h;
				offset = allele.getOffset();
			}
		}
		return normalizations;
	}

	private void paintNormalization(Graphics g, Normalization normalization) {
		double margin = (max() - min()) / 5;
		double yLow = min() + margin;
		double yHigh = max() - margin;
		yLow	+= normalization.offset;
		yHigh	+= normalization.offset;
		int vLow = yScale.toScreen(yHigh);
		int vHigh = yScale.toScreen(yLow);
		
		g.setColor(NORMALIZATION_COLOR);
		g.fillRect(normalization.start, vLow, normalization.width, vHigh - vLow);
	}

	public static class Normalization {
		private final int start;
		private final double offset;
		private final int width;

		public Normalization(int start, int end, double offset) {
			this.start = start;
			this.offset = offset;
			this.width = end - start + 1;
		}

	}

	private void paintBins(Graphics g) {
		if (bins == null)
			return;
		g.setColor(BIN_COLOR);
		for (String name : bins.keySet())
			paintBin(g, bins.get(name), name);
	}

	private void paintBin(Graphics g, Bin bin, String name) {
		drawHorizontalLine(g, yScale.toScreen(bin.getLow()));
		drawHorizontalLine(g, yScale.toScreen(bin.getHigh()));
		
		int x = axisWidth + 10;
		int y = yScale.toScreen((bin.getHigh() + bin.getLow()) / 2);
		g.drawString(name, x, y);
	}

	private void drawVerticalLine(Graphics g, int x) {
		g.drawLine(x, 0, x, getHeight());
	}
	
	private void drawHorizontalLine(Graphics g, int y) {
		g.drawLine(axisWidth, y, getWidth(), y);
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
		g.fillRect(p.x - 4, p.y - 4, 10, 10);
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

	public void setAlleles(List<Allele> alleles) {
		this.alleles = alleles;
		autoscale = true;
		makeScales();
	}

	private void makeScales() {
		if (alleles == null)
			return;
		try {
			xScale = new Scale(0, alleles.size(), axisWidth, getWidth());
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

	public void setBins(Map<String, Bin> bins) {
		this.bins = bins;
	}

	public void setColorizer(Colorizer colorizer) {
		this.colorizer = colorizer;
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
		} else {
			try {
				yScale = new Scale(yScale.toData(end), yScale.toData(start), getHeight(), 0);
			} catch (Exception e) {
			}
		}
		repaint();
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

	public Allele closestAllele(int x) {
		Allele closest = null;
		int distance = Integer.MAX_VALUE;
		if (alleles == null)
			return null;
		for (int i = 0; i < alleles.size(); ++i) {
			Allele allele = alleles.get(i);
			Point p = alleleLocation(allele, i);
			int d = Math.abs(p.x - x);
			if (d < distance) {
				distance = d;
				closest = allele;
			}
		}
		return closest;
	}
	
	public BinBoundary binBoundaryAt(int v) {
		if (bins == null)
			return null;
		Bin above = null;
		Bin below = null;
		int boundaryV = 0;
		for (Bin bin : bins.values())
			if (Math.abs(yScale.toScreen(bin.getLow()) - v) < 4) {
				below = bin;
				boundaryV = yScale.toScreen(bin.getLow());
			}
		for (Bin bin : bins.values())
			if (Math.abs(yScale.toScreen(bin.getHigh()) - v) < 4) {
				above = bin;
				boundaryV = yScale.toScreen(bin.getHigh());
			}
		if (above == null && below == null)
			return null;
		return new BinBoundary(above, below, boundaryV);
	}

	public void drawBinLine(int y) {
		Graphics g = getGraphics();
		g.setXORMode(getBackground());
		g.setColor(BIN_COLOR);
		drawHorizontalLine(g, y);
		g.setPaintMode();
	}

	public double toDataY(int v) {
		return yScale.toData(v);
	}


}
