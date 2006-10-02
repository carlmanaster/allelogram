package org.carlmanaster.allelogram.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameCloser extends WindowAdapter {
	public void windowClosing(WindowEvent e)	{exit();}
	public void windowClosed(WindowEvent e)	{exit();}
	private void exit()						{System.exit(0);}
}