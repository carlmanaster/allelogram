package org.carlmanaster.allelogram.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

public class PrintUtils implements Printable {
	private Component printee;

	public static void printComponent(Component c) {
		printComponent(c, true);
	}

	private static void printComponent(Component c, boolean prompt) {
		new PrintUtils(c).print(prompt);
	}

	private PrintUtils(Component componentToBePrinted) {
		this.printee = componentToBePrinted;
	}

	private void print(boolean prompt) {
		PrinterJob	printJob	= PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (!prompt || printJob.printDialog()) {
			try {
				printJob.print();
			} catch (PrinterException pe) {
				System.out.println("Error printing: " + pe);
			}
		}
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0)
			return (NO_SUCH_PAGE);
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		disableDoubleBuffering(printee);
		printee.paint(g2d);
		enableDoubleBuffering(printee);
		return (PAGE_EXISTS);
	}
	
	private static void disableDoubleBuffering(Component c)	{setDoubleBuffering(c, false);}
	private static void enableDoubleBuffering(Component c)	{setDoubleBuffering(c, true);}
	private static void setDoubleBuffering(Component c, boolean value) {
		RepaintManager.currentManager(c).setDoubleBufferingEnabled(value);
	}
}
