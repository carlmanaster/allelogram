package org.carlmanaster.allelogram.util;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

public class PrintUtils implements Printable
{
  private Component componentToBePrinted;
  /***********************************************************************/
  public static void printComponent(Component c)
  {
    printComponent(c, true);
  }
  /***********************************************************************/
  public static void printComponent(Component c, boolean prompt)
  {
    new PrintUtils(c).print(prompt);
  }
  /***********************************************************************/
  public PrintUtils(Component componentToBePrinted)
  {
    this.componentToBePrinted = componentToBePrinted;
  }
  /***********************************************************************/
  public void print(boolean prompt)
  {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    PageFormat format = printJob.defaultPage();
    Paper paper = format.getPaper();
    //paper.setImageableArea(18, 0, 180, 840);// Paper format for receipt printer
    format.setPaper(paper);
    printJob.setPrintable(this, format);
    if (!prompt || printJob.printDialog())
    {
      try
      {
        printJob.print();
      }
      catch (PrinterException pe)
      {
        System.out.println("Error printing: " + pe);
      }
    }
  }
  /***********************************************************************/
  public int print(Graphics g, PageFormat pageFormat, int pageIndex)
  {
    if (pageIndex > 0)
    {
      return (NO_SUCH_PAGE);
    }
    else
    {
      Graphics2D g2d = (Graphics2D) g;
      //			My_System.variable("width = " + pageFormat.getWidth());
      //      My_System.variable("image width = " + pageFormat.getImageableWidth());
      //			My_System.variable("getHeight() = " + pageFormat.getHeight());
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      disableDoubleBuffering(componentToBePrinted);
      componentToBePrinted.paint(g2d);
      enableDoubleBuffering(componentToBePrinted);
      return (PAGE_EXISTS);
    }
  }
  /***********************************************************************/
  public static void disableDoubleBuffering(Component c)
  {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(false);
  }
  /***********************************************************************/
  public static void enableDoubleBuffering(Component c)
  {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(true);
  }
  /***********************************************************************/
  /***********************************************************************/
}
