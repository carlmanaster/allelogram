package org.carlmanaster.allelogram.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.swing.BoxLayout;
import org.carlmanaster.allelogram.gui.mouse.AllelogramMouseDispatcher;
import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.BinGuesser;
import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Classifier;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeClassificationPredicate;
import org.carlmanaster.allelogram.model.GenotypeComparator;
import org.carlmanaster.allelogram.model.GenotypeReader;
import org.carlmanaster.allelogram.model.Settings;
import org.carlmanaster.allelogram.util.FileUtil;
import org.carlmanaster.allelogram.util.PrintUtils;
import org.carlmanaster.filter.Filter;
import org.carlmanaster.predicate.Predicate;

public class AllelogramApplet extends Application implements DropTargetListener {
	private Settings settings;
	private List<Genotype> genotypes;
	private final List<Allele> alleles = new ArrayList<Allele>();
	private List<Bin> bins = new ArrayList<Bin>();
	private final HashSet<Genotype> selection = new HashSet<Genotype>();
	private final Chart chart = new Chart(this);
	private GenotypeComparator comparator = null;
	private final MenuBarBuilder menuBarBuilder = new MenuBarBuilder(this);
	private Classifier sortClassifier;
	private final String settingsFile;
	private final String dataFile;

	public AllelogramApplet(String settingsFile, String dataFile) {
		this.settingsFile = settingsFile;
		this.dataFile = dataFile;
		makeDropTargetListener();
	}

	private void makeDropTargetListener() {
		this.setDropTarget(new DropTarget(this, this));
	}

	public void init() {
		super.init();
		setPreferredSize(new Dimension(700, 400));
		getContentPane().setLayout(
				new Layout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(chart);
		AllelogramMouseDispatcher mouseDispatcher = new AllelogramMouseDispatcher(
				this, chart);
		chart.addMouseListener(mouseDispatcher);
		chart.addMouseMotionListener(mouseDispatcher);
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	public static void main(String[] args) {
		// new AllelogramApplet("C:/temp/carl/vic_file_format",
		// "C:/temp/carl/vic_1.txt").run("Allelogram");
		new AllelogramApplet(// null, null
				"/Users/carlmanaster/Desktop/PM_file_format",
				"/Users/carlmanaster/Desktop/PM_data.txt").run("Allelogram");
	}

	protected void buildMenuBar() {
		menuBarBuilder.buildMenuBar();
	}

	private void readSettings(String settingsFile) {
		File file = FileUtil.chooseFile(settingsFile);
		if (file == null)
			return;
		try {
			settings = new Settings(file);
			comparator = new GenotypeComparator(settings.getSortClassifier());
			menuBarBuilder.extendSortMenu();
			menuBarBuilder.extendColorMenu();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public void doOpen(String settingsFile, String dataFile) {
		if (settings == null)
			readSettings(settingsFile);
		File file = FileUtil.chooseFile(dataFile);
		if (file == null)
			return;
		GenotypeReader reader = new GenotypeReader(settings);
		try {
			genotypes = reader.readGenotypes(file);
			makeAlleles();
			doClearBins();
			chart.setAlleles(alleles);
			chart.setControlGenotypes(findControlGenotypes());
			getWindow().setTitle(file.getName());
			doSort(settings.getSortClassifier());
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
			alleles.addAll(getGenotypeAlleles(genotype));
		sortAlleles();
	}

	private List<Allele> getGenotypeAlleles(Genotype genotype) {
		return Filter.out(new Zero()).filtered(genotype.getAlleles());
	}

	private void sortAlleles() {
		Collections.sort(alleles, new AlleleComparator(comparator));
	}

	public void doGuess() {
		bins = new BinGuesser(alleles).bestGuess();
		chart.setBins(bins);
		repaint();
	}

	public void doSort(Classifier classification) {
		sortBy(classification);
		repaint();
	}

	private void sortBy(Classifier classification) {
		sortClassifier = classification;
		comparator = classification == null ? null : new GenotypeComparator(
				classification);
		sortAlleles();
	}

	public void doColor(Classifier classification) {
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
		readSettings(null);
	}

	public void doClearBins() {
		bins.clear();
		repaint();
	}

	public void selectGenotype(Genotype genotype, boolean commandKey,
			boolean optionKey) {
		if (optionKey && settings.getOptionClickClassifier() != null) {
			Classifier classification = settings.getOptionClickClassifier();
			selectMatching(genotype, classification);
			return;
		}
		if (commandKey && settings.getCommandClickClassifier() != null) {
			Classifier classification = settings.getCommandClickClassifier();
			selectMatching(genotype, classification);
			return;
		}
		
		select(genotype);
	}

	private void showGenotypeInfo(Genotype genotype) {
		showInfo(settings.info(genotype));
	}

	private void showInfo(String info) {
		System.err.println(info);
	}

	private void selectMatching(Genotype genotype, Classifier classification) {
		GenotypeClassificationPredicate predicate = new GenotypeClassificationPredicate(
				classification, genotype);
		selection.clear();
		selection.addAll(Filter.in(predicate).filtered(genotypes));
		showSelectionInfo();
		repaint();
	}

	private void showSelectionInfo() {
		showInfo(String.format("%1d genotypes selected", selection.size()));
	}

	private void select(Genotype genotype) {
		selection.clear();
		selection.add(genotype);
		showGenotypeInfo(genotype);
		repaint();
	}

	public HashSet<Genotype> getSelection() {
		return selection;
	}

	public void doAutonormalize() {
		if (sortClassifier == null)
			return;
		List<Genotype> controls = findControlGenotypes();
		if (controls.isEmpty())
			return;
		double average = averageValue(controls);
		for (Classification classification : getAllClassifications(sortClassifier)) {
			try {
				List<Genotype> theseGenotypes	= genotpesIn(classification);
				List<Genotype> theseControls	= controls(theseGenotypes);
				if (theseControls.isEmpty())
					continue;
				double offset = average - averageValue(theseControls);
				for (Genotype genotype : theseGenotypes)
					genotype.offsetBy(offset);
			} catch (Exception e) {
			}
		}
		rescale();
		resort();
		repaint();
	}

	private List<Genotype> controls(List<Genotype> these) {
		return Filter.in(settings.getControlSubject()).filtered(these);
	}

	private List<Genotype> genotpesIn(Classification classification) throws Exception {
		return Filter.in(new GenotypeClassificationPredicate(sortClassifier,classification)).filtered(genotypes);
	}

	private HashSet<Classification> getAllClassifications(Classifier classifier) {
		HashSet<Classification> set = new HashSet<Classification>();
		for (Genotype genotype : genotypes)
			set.add(classifier.classify(genotype));
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
		sortBy(sortClassifier);
	}

	private void rescale() {
		chart.adjustScales();
	}

	public void doPrint() {
		PrintUtils.printComponent(this);
	}

	protected void initFinal() {
		if (settingsFile != null && dataFile != null)
			doOpen(settingsFile, dataFile);
	}

	private static class Zero extends Predicate<Allele> {
		public boolean passes(Allele allele) {
			return allele.getRawValue() == 0;
		}
	}

	public void dragEnter(DropTargetDragEvent event)			{}
	public void dragOver(DropTargetDragEvent event)			{}
	public void dropActionChanged(DropTargetDragEvent event)	{}
	public void dragExit(DropTargetEvent event) 				{}

	public void drop(DropTargetDropEvent event) {
		try {
			DataFlavor flavor = event.getCurrentDataFlavors()[0];
			if (DataFlavor.javaFileListFlavor.equals(flavor)) {
				event.acceptDrop(DnDConstants.ACTION_REFERENCE);
				List list = (List) event.getTransferable().getTransferData(
						flavor);
				File file = (File) list.get(0);
				doOpen(null, file.getAbsolutePath());
				event.dropComplete(true);
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
