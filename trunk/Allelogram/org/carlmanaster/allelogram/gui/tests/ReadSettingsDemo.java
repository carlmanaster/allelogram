package org.carlmanaster.allelogram.gui.tests;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeReader;
import org.carlmanaster.allelogram.model.Settings;
import org.carlmanaster.allelogram.util.FileUtil;

public class ReadSettingsDemo extends TestCase {
	
	public void testRead() throws Exception {
		File file = FileUtil.pickFile();
		if (file == null) return;
		
		Settings settings = new Settings(file);
		
		file = FileUtil.pickFile();
		if (file == null) return;
		
		GenotypeReader reader = new GenotypeReader(settings); 
		List<Genotype> genotypes = reader.readGenotypes(file);

		for (Genotype genotype : genotypes)
			System.err.println(genotype.getAlleles().toString() + " " + genotype.isHomozygous());
	}


}
