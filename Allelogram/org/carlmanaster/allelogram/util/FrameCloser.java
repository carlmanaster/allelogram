package org.carlmanaster.allelogram.util;


public class FrameCloser 
  extends java.awt.event.WindowAdapter
{
  /***********************************************************************/

  public void windowClosing(java.awt.event.WindowEvent e) 
  {
		System.exit(0);
	}
  /***********************************************************************/
  
	public void windowClosed(java.awt.event.WindowEvent e) 
  {
    System.exit(0);
  }	
  /***********************************************************************/
}