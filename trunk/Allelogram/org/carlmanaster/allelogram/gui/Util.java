package org.carlmanaster.allelogram.gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public class Util {
	public static File pickFile() {
		FileDialog picker = new FileDialog(new Frame(), "", FileDialog.LOAD);
		picker.setVisible(true);
		try {
			return new File(picker.getDirectory(), picker.getFile());
		} catch (RuntimeException e) {
			return null;
		}
	}
}
