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
			return new File(picker.getDirectory(), picker.getFile());
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

}
