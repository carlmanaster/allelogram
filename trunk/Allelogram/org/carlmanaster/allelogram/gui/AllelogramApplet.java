package org.carlmanaster.allelogram.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.carlmanaster.allelogram.gui.mouse.AllelogramMouseDispatcher;
import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.BinGuesser;
import org.carlmanaster.allelogram.model.BinNamer;
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

public class AllelogramApplet extends Application {
	private static final String SETTINGS_FILE = "settings file";
	private Settings settings;
	private List<Genotype> genotypes;
	private final List<Allele> alleles = new ArrayList<Allele>();

	private Map<String, Bin> bins = new HashMap<String, Bin>();
	private final HashSet<Genotype> selection = new HashSet<Genotype>();
	private final Chart chart = new Chart(this);
	private GenotypeComparator comparator = null;
	private final MenuBarBuilder menuBarBuilder = new MenuBarBuilder(this);
	private Classifier sortClassifier;
	private String settingsFile;
	private final String dataFile;
	private Integer activeAllele = null;
	private JTextArea info;
	private BinNamer binNamer = BinNamer.size;

	public AllelogramApplet(String settingsFile, String dataFile) {
		this.settingsFile = settingsFile;
		this.dataFile = dataFile;
		makeDropTargetListener();
	}

	private void recallSettingsFile() {
		this.settingsFile = getPreferences().get(SETTINGS_FILE, null);
		if (settingsFile != null)
			readSettings(settingsFile);
	}

	private void rememberSettingsFile(String filename) {
		getPreferences().put(SETTINGS_FILE, filename);
	}

	private Preferences getPreferences() {
		return Preferences.userNodeForPackage(this.getClass());
	}

	private void makeDropTargetListener() {
		setDropTarget(new DropTarget(this, new DropListener()));
	}

	public void init() {
		super.init();
		setPreferredSize(new Dimension(700, 400));

		info = new JTextArea();
		info.setEditable(false);
		info.setPreferredSize(new Dimension(700, 40));
		info.setMaximumSize(new Dimension(700, 40));
		info.setMinimumSize(new Dimension(100, 40));
		info.setFocusable(false);

		AllelogramMouseDispatcher mouseDispatcher = new AllelogramMouseDispatcher(this, chart);
		chart.addMouseListener(mouseDispatcher);
		chart.addMouseMotionListener(mouseDispatcher);
		invokeWindowsWorkaroundForKeyListener();
		addKeyListener(new ArrowKeyListener());

		Container frame = getContentPane();
		frame.setLayout(new Layout());

		frame.add(chart);
		frame.add(info);
	}

	private void invokeWindowsWorkaroundForKeyListener() {
		setFocusable(true);
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	public static void main(String[] args) {
		// new AllelogramApplet("C:/temp/carl/vic_file_format",
		// "C:/temp/carl/vic_1.txt").run("Allelogram");
		//	new AllelogramApplet(
		//		"/Users/carlmanaster/Desktop/PM_file_format",
		//		"/Users/carlmanaster/Desktop/PM_data.txt").run("Allelogram");
		new AllelogramApplet(null, null).run("Allelogram");
	}

	protected void buildMenuBar() {
		menuBarBuilder.buildMenuBar();
	}

	private void readSettings(String settingsFile) {
		File file = FileUtil.chooseFile(settingsFile);
		if (file == null)
			return;
		try {
			rememberSettingsFile(file.toString());
			settings = new Settings(file);
			comparator = new GenotypeComparator(settings.getSortClassifier());
			menuBarBuilder.extendSortMenu();
			menuBarBuilder.extendColorMenu();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public void doOpen(String settingsFile, String dataFile) {
		recallSettingsFile();

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


	public void doSaveAs() {
		File file = FileUtil.pickNewFile();
		if (file == null)
			return;

		try {
			PrintWriter out = new PrintWriter(new FileWriter(file.getAbsolutePath()));

			for (String column : settings.getColumns())
				out.print(column + '\t');

			int ploidy = settings.getAlleleIndexes().length;
			for (int i = 0; i < ploidy; ++i)
				out.print("Normalized Size " + (i + 1) + '\t');
			for (int i = 0; i < ploidy; ++i)
				out.print("Call " + (i + 1) + '\t');

			out.println();

			for (Genotype genotype : genotypes) {
				for (String column : settings.getColumns())
					out.print(genotype.get(column) + '\t');

				List<Double> values = genotype.getAdjustedAlleleValues(ploidy);
				for (Double value : values)
					out.print(String.format("%.2f\t", value));
				for (Double value : values)
					out.print(call(value) + '\t');

				out.println();
			}

			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String call(Double value) {
		for (String key : bins.keySet())
			if (bins.get(key).contains(value))
				return key;
		return "";
	}

	private List<Genotype> findControlGenotypes() {
		List<Genotype> controls = Filter.in(settings.getControlSubject()).filtered(genotypes);
		Predicate<Genotype> excluded = new Predicate<Genotype>() {
			public boolean passes(Genotype genotype) {
				return genotype.isExcluded();
			}
		};
	return Filter.out(excluded).filtered(controls);
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
		List<Bin> list = new BinGuesser(unexcludedAlleles()).bestGuess();
		setBins(list);
	}

	public List<Allele> unexcludedAlleles() {
		Predicate<Allele> excluded = new Predicate<Allele>() {
			public boolean passes(Allele allele) {
				return allele.getGenotype().isExcluded();
			}
		};
		return Filter.out(excluded).filtered(alleles);
	}

	public void doGuessBySize() {
		String s = JOptionPane.showInputDialog("Size?");
		if (s == null)
			return;
		try {
			Double size = Double.parseDouble(s);
			List<Bin> list = new BinGuesser(alleles).guess(size);
			setBins(list);
		} catch (NumberFormatException e) {
			return;
		}
	}

	public void renameBins() {
		ArrayList<Bin> list = new ArrayList<Bin>();
		list.addAll(bins.values());
		setBins(list);
	}

	private void setBins(List<Bin> list) {
		Collections.sort(list);
		bins.clear();
		for (int i = 0; i < list.size(); ++i)
			bins.put(binNamer.getName(list.get(i), i), list.get(i));

		chart.setBins(bins);
		repaint();
	}

	public void doSort(Classifier classification) {
		sortBy(classification);
		repaint();
	}

	private void sortBy(Classifier classifier) {
		sortClassifier = classifier;
		comparator = classifier == null ? null : new GenotypeComparator(classifier);
		sortAlleles();
	}

	public void doColor(Classifier classification) {
		chart.setColorizer(new Colorizer(classification, genotypes));
		repaint();
	}

	private int getInfoHeight() {
		return settings == null ? 24 : (1 + settings.getInfoClassifiers().size()) * info.getFontMetrics(info.getFont()).getHeight();
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

	public void nameBinsBySize() {
		binNamer  = BinNamer.size;
		renameBins();
	}

	public void nameBinsByLetter() {
		binNamer  = BinNamer.alphabetic;
		renameBins();
	}

	public void nameBinsByIndex() {
		binNamer  = BinNamer.sequential;
		renameBins();
	}

	public void selectAllele(Allele allele, boolean commandKey, boolean optionKey) {
		selectGenotype(allele.getGenotype(), commandKey, optionKey);
		activeAllele = alleles.indexOf(allele);
	}

	public void selectNextAllele() {
		selectAllele(alleles.get(nextActiveAlleleIndex()), false, false);
	}

	private int nextActiveAlleleIndex() {
		if (chart.isShowingExcludedGenotypes())
			return nextAlleleIndex(activeAllele);
		int index = nextAlleleIndex(activeAllele);
		while (alleles.get(index).getGenotype().isExcluded())
			index = nextAlleleIndex(index);
		return index;
	}

	private int nextAlleleIndex(Integer index) {
		if (index == null)				return 0;
		if (index == alleles.size() - 1)	return 0;
		return index + 1;
	}

	public void selectPreviousAllele() {
		selectAllele(alleles.get(previousActiveAlleleIndex()), false, false);
	}

	private int previousActiveAlleleIndex() {
		if (chart.isShowingExcludedGenotypes())
			return previousAlleleIndex(activeAllele);
		int index = previousAlleleIndex(activeAllele);
		while (alleles.get(index).getGenotype().isExcluded())
			index = previousAlleleIndex(index);
		return index;
	}

	private int previousAlleleIndex(Integer index) {
		if (index == null)	return alleles.size() - 1;
		if (index == 0)		return alleles.size() - 1;
		return index - 1;
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

	private void showInfo(String s) {
		info.setText(s);
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


	public void toggleNormalization() {
		chart.toggleNormalization();
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

	private class DropListener extends DropTargetAdapter {
		@SuppressWarnings("unchecked")
		public void drop(DropTargetDropEvent event) {
			try {
				DataFlavor flavor = event.getCurrentDataFlavors()[0];
				if (DataFlavor.javaFileListFlavor.equals(flavor)) {
					event.acceptDrop(DnDConstants.ACTION_REFERENCE);
					List<File> list = (List<File>) event.getTransferable().getTransferData(flavor);
					File file = list.get(0);
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

	private class ArrowKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (!e.isActionKey()) return;
			//showInfo(Integer.toString(e.getKeyCode()));
			//JOptionPane.showMessageDialog(null,Integer.toString(e.getKeyCode()),"Test",JOptionPane.INFORMATION_MESSAGE);
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:	selectPreviousAllele();	break;
				case KeyEvent.VK_RIGHT:	selectNextAllele();		break;
			}
		}
	}

	private class Layout implements LayoutManager {
		public void layoutContainer(Container container) {
			Dimension size = container.getSize();
			int infoHeight = getInfoHeight();
			size.height = size.height - infoHeight;

			chart.setSize(size);
			chart.setLocation(0, 0);

			size.height = infoHeight;
			info.setSize(size);
			info.setLocation(0, chart.getHeight());
		}

		public void addLayoutComponent(String name, Component comp)	{}
		public Dimension minimumLayoutSize(Container parent)		{return null;}
		public Dimension preferredLayoutSize(Container parent)		{return null;}
		public void removeLayoutComponent(Component comp)			{}
	}

	public Classifier getSortClassifier() {
		return sortClassifier;
	}

	public List<Genotype> genotypesMatching(Genotype genotype) {
		Classifier sort = getSortClassifier();
		GenotypeClassificationPredicate predicate = new GenotypeClassificationPredicate(sort, genotype);
		return (Filter.in(predicate).filtered(genotypes));
	}

	public void saveBins() {
		File file = FileUtil.pickNewFile();
		if (file == null)
			return;

		try {
			PrintWriter out = new PrintWriter(new FileWriter(file.getAbsolutePath()));
			out.println("Bin\tLow\tHigh");
			SortedSet<String> keys = new TreeSet<String>(bins.keySet());
			for (String key : keys)
				out.println(key + "\t" + bins.get(key).toTabDelimitedString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void applyBins() {
		File file = FileUtil.pickFile();
		if (file == null) return;
		try {
			List<Bin> newBins = new ArrayList<Bin>();
			BufferedReader reader = FileUtil.makeReader(file);
			reader.readLine();	// skip the header row
			while (reader.ready()) {
				String[] items = reader.readLine().split("\t");
				newBins.add(new Bin(Double.parseDouble(items[1]), Double.parseDouble(items[2])));
			}
			setBins(newBins);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excludeGenotype() {
		boolean excluded = anySelectedGenotypesAreIncluded();
		for (Genotype genotype : selection)
			genotype.setExcluded(excluded);
		if (excluded && !chart.isShowingExcludedGenotypes())
			selection.clear();
		repaint();
	}

	private boolean anySelectedGenotypesAreIncluded() {
		for (Genotype genotype : selection)
			if (!genotype.isExcluded())
				return true;
		return false;
	}

	private boolean anySelectedGenotypesAreExcluded() {
		for (Genotype genotype : selection)
			if (genotype.isExcluded())
				return true;
		return false;
	}

	public void showExcludedGenotypes() {
		chart.toggleExcludedGenotypeDisplay();
		if (!chart.isShowingExcludedGenotypes() && anySelectedGenotypesAreExcluded())
			selection.clear();
		repaint();
	}

}
