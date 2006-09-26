package org.carlmanaster.allelogram.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {

	public static File pickFile() {
		FileDialog picker = new FileDialog(new Frame(), "", FileDialog.LOAD);
		picker.setVisible(true);
		try {
			File file = new File(picker.getDirectory(), picker.getFile());
			System.err.println(file.getAbsolutePath());
			return file;
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	public static BufferedReader makeReader(File file) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}

	public static String readAll(BufferedReader reader) throws IOException {
		StringBuffer sb = new StringBuffer();
		while (reader.ready())
			sb.append(reader.readLine() + '\n');
		reader.close();
		return sb.toString();
	}

	public static File chooseFile(String settingsFile) {
		return (settingsFile != null) ? new File(settingsFile) : pickFile();
	}

}
