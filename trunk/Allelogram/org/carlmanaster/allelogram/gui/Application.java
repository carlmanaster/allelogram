package org.carlmanaster.allelogram.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.MenuBar;

import javax.swing.JApplet;

public abstract class Application extends JApplet {
	private final MenuBar menubar = new MenuBar();
	private Frame window;
	

	protected void run(String title) {
		window = new Frame(title);
		init();	
		window.add(this, BorderLayout.CENTER);
		window.pack();
		window.setVisible(true);
	    buildMenuBar();
	    window.setMenuBar(menubar);
	}

	protected abstract void buildMenuBar();

	public MenuBar getMenuBar() {return menubar;}
	protected Frame getWindow()	{return window;}

}
