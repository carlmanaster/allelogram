package org.carlmanaster.allelogram.gui.mouse;

import java.awt.event.MouseEvent;

import org.carlmanaster.allelogram.gui.AllelogramApplet;
import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.util.PlatformUtil;

public class AlleleClicker extends ClickerDragger {
	private final AllelogramApplet applet;
	private final Allele allele;

	public AlleleClicker(AllelogramApplet applet, Allele allele) {
		this.applet	= applet;
		this.allele	= allele;
	}

	public void mouseClicked(MouseEvent event) {
		boolean commandKey = PlatformUtil.isMac() ? event.isMetaDown() : event.isControlDown();
		boolean optionKey = event.isAltDown();
		applet.selectGenotype(allele.getGenotype(), commandKey, optionKey);
	}
}
