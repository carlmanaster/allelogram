package org.carlmanaster.allelogram.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.BinGuesser;
import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeComparator;
import org.carlmanaster.allelogram.model.GenotypeReader;
import org.carlmanaster.allelogram.model.Settings;
import org.carlmanaster.allelogram.util.FileUtil;

public class AllelogramApplet extends Application {
	
	private Settings settings;
	private List<Genotype> genotypes;
	private final List<Allele> alleles = new ArrayList<Allele>();
	private List<Bin> bins = new ArrayList<Bin>();
	
	private final Chart chart = new Chart();
	private GenotypeComparator comparator = null; 

	private final MenuBarBuilder menuBarBuilder = new MenuBarBuilder(this);
	
	public void init() {
		super.init();
		setPreferredSize(new Dimension(300, 300));
		
		getContentPane().setLayout(new Layout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(chart);
		ChartClicker clicker = new ChartClicker(this, chart);
		chart.addMouseListener(clicker);
		chart.addMouseMotionListener(clicker);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	public static void main(String[] args) {
		new AllelogramApplet().run("Allelogram");
	  }

	protected void buildMenuBar() {
		menuBarBuilder.buildMenuBar();
	}
	
	private void readSettings() {
		File file = FileUtil.pickFile();
		if (file == null) return;
		
		try {
			settings = new Settings(file);
			comparator = new GenotypeComparator(settings.getSortClassification());
			menuBarBuilder.extendSortMenu();
			menuBarBuilder.extendColorMenu();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public void doOpen() {
		if (settings == null)
			readSettings();
		
		File file = FileUtil.pickFile();		
		if (file == null) return;
		
		GenotypeReader reader = new GenotypeReader(settings); 
		try {
			genotypes = reader.readGenotypes(file);
			makeAlleles();
			chart.setAlleles(alleles);
			getWindow().setTitle(file.getName());
			repaint();
		} catch (IOException e) {
		}
	}

	private void makeAlleles() {
		alleles.clear();
		for (Genotype genotype : genotypes)
			alleles.addAll(genotype.getAlleles());
		sortAlleles();
	}

	private void sortAlleles() {
		Collections.sort(alleles, new AlleleComparator(comparator));
	}

	public void doGuess() {
		bins = new BinGuesser(alleles).guess(2);
		chart.setBins(bins);
		repaint();
	}

	public void doSort(Classification classification) {
		comparator = classification == null ? null : new GenotypeComparator(classification);
		sortAlleles();
		repaint();
	}

	public void doColor(Classification classification) {
		chart.setColorizer(new Colorizer(classification, genotypes));
		repaint();
	}


	private class Layout extends BoxLayout {

		public Layout(Container container, int i) {
			super(container, i);
		}
		
		public void layoutContainer(Container container) {
			super.layoutContainer(container);
			chart.setSize(container.getSize());
		}
		
	}


	public Settings getSettings() {
		return settings;
	}

	public void doPickFileFormat() {
		readSettings();		
	}

	public void doClearBins() {
		bins.clear();
		repaint();
	}

	public void selectGenotype(Genotype genotype) {
		System.err.println(settings.info(genotype));
		chart.hiliteGenotype(genotype);
		repaint();
	}
	
}
