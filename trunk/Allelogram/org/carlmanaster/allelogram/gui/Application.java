package org.carlmanaster.allelogram.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.MenuBar;
import javax.swing.JApplet;
import org.carlmanaster.allelogram.util.FrameCloser;

public abstract class Application extends JApplet
{
  private final MenuBar menubar = new MenuBar();
  private Frame         window;
  protected void run(String title)
  {
    window = new Frame(title);
    window.addWindowListener(new FrameCloser());
    init();
    window.add(this, BorderLayout.CENTER);
    window.pack();
    window.setVisible(true);
    buildMenuBar();
    window.setMenuBar(menubar);
    initFinal();
  }
  protected abstract void initFinal();
  protected abstract void buildMenuBar();
  public MenuBar getMenuBar()
  {
    return menubar;
  }
  protected Frame getWindow()
  {
    return window;
  }
}
