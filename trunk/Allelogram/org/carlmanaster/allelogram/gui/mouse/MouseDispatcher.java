package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class MouseDispatcher implements MouseListener, MouseMotionListener {
	private ClickerDragger delegate;

	public void mousePressed(MouseEvent event) {
		delegate = findDelegate(event);
		delegate.mousePressed(event);
	}

	protected abstract ClickerDragger findDelegate(MouseEvent event);
	
	public void mouseClicked(MouseEvent event)		{delegate.mouseClicked(event);}
	public void mouseReleased(MouseEvent event)		{delegate.mouseReleased(event);}
	public void mouseDragged(MouseEvent event)		{delegate.mouseDragged(event);}

	public void mouseMoved(MouseEvent event)		{}
	public void mouseEntered(MouseEvent event)		{}
	public void mouseExited(MouseEvent event)		{}
	
	protected ClickerDragger doNothing() {
		return new ClickerDragger() {
			public void mouseMoved(MouseEvent event)	{}
			public void mouseDragged(MouseEvent event)	{}
		};
	}
}
