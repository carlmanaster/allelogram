package org.carlmanaster.allelogram.gui.tests;

import java.io.BufferedReader;
import java.io.File;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Settings;
import org.carlmanaster.allelogram.util.FileUtil;

public class ReadSettingsTest extends TestCase {
	public void testRead() throws Exception {
		File file = FileUtil.pickFile();
		if (file == null) return;
		
		BufferedReader reader = FileUtil.makeReader(file);
		Settings settings = new Settings(reader);
		System.err.println(settings.toString());
	}

}
