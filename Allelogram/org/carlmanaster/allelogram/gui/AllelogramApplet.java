package org.carlmanaster.allelogram.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.BinGuesser;
import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeClassificationPredicate;
import org.carlmanaster.allelogram.model.GenotypeComparator;
import org.carlmanaster.allelogram.model.GenotypeReader;
import org.carlmanaster.allelogram.model.Settings;
import org.carlmanaster.allelogram.util.FileUtil;
import org.carlmanaster.filter.Filter;

public class AllelogramApplet extends Application {
	
	private Settings settings;
	private List<Genotype> genotypes;
	private final List<Allele> alleles = new ArrayList<Allele>();
	private List<Bin> bins = new ArrayList<Bin>();
	private final HashSet<Genotype> selection = new HashSet<Genotype>();

	private final Chart chart = new Chart(this);
	private GenotypeComparator comparator = null; 

	private final MenuBarBuilder menuBarBuilder = new MenuBarBuilder(this);
	private Classification sortClassification;
	
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
			chart.setControlGenotypes(findControlGenotypes());
			getWindow().setTitle(file.getName());
			doSort(settings.getSortClassification());
			repaint();
		} catch (IOException e) {
		}
	}

	private List<Genotype> findControlGenotypes() {
		return Filter.in(settings.getControlSubject()).filtered(genotypes);
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
		sortBy(classification);
		repaint();
	}

	private void sortBy(Classification classification) {
		sortClassification = classification;
		comparator = classification == null ? null : new GenotypeComparator(classification);
		sortAlleles();
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

	public void selectGenotype(Genotype genotype, boolean commandKey, boolean optionKey) {
		if (optionKey && settings.getOptionClickClassification() != null) {
			Classification classification = settings.getOptionClickClassification();
			selectMatching(genotype, classification);
			return;
		}
		if (commandKey && settings.getCommandClickClassification() != null) {
			Classification classification = settings.getCommandClickClassification();
			selectMatching(genotype, classification);
			return;
		}
		System.err.println(settings.info(genotype));
		select(genotype);
	}

	private void selectMatching(Genotype genotype, Classification classification) {
		GenotypeClassificationPredicate predicate = new GenotypeClassificationPredicate(classification, genotype);
		selection.clear();
		selection.addAll(Filter.in(predicate).filtered(genotypes));
		repaint();
	}

	private void select(Genotype genotype) {
		selection.clear();
		selection.add(genotype);
		repaint();
	}

	public HashSet<Genotype> getSelection() {
		return selection;
	}

	public void doAutonormalize() {
		if (sortClassification == null)
			return;
		
		List<Genotype> controls = findControlGenotypes();
		if (controls.isEmpty())
			return;
		
		double average = averageValue(controls);
		
		HashSet<Vector<String>> set = getAllClassificationResults(sortClassification);
		for (Vector<String> vector : set) {
			try {
				GenotypeClassificationPredicate p = new GenotypeClassificationPredicate(sortClassification, vector);
				
				List<Genotype> theseControls = Filter.in(p.and(settings.getControlSubject())).filtered(genotypes);
				if (theseControls.isEmpty())
					continue;
				double offset = average - averageValue(theseControls);
				
				List<Genotype> thisGroup = Filter.in(p).filtered(genotypes);
				for (Genotype genotype : thisGroup) 
					genotype.offsetBy(offset);

			} catch (Exception e) {
			}
		}
		rescale();
		resort();
		repaint();
	}

	private HashSet<Vector<String>> getAllClassificationResults(Classification classification) {
		HashSet<Vector<String>> set = new HashSet<Vector<String>>();
		for (Genotype genotype : genotypes) 
			set.add(classification.classify(genotype));
		return set;
	}

	private double averageValue(List<Genotype> controls) {
		double sum = 0;
		for (Genotype genotype : controls)
			for (Double d : genotype.getRawAlleleValues(2))
				sum += d;
		return sum / (2 * controls.size());
	}

	public void doClearNormalization() {
		for (Genotype genotype : genotypes)
			genotype.clearOffset();
		rescale();
		resort();
		repaint();
	}

	private void resort() {
		sortBy(sortClassification);
	}

	private void rescale() {
		chart.adjustScales();
	}
	
}
