package org.carlmanaster.allelogram.gui.tests;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.gui.Scale;

public class ScaleTest extends TestCase {

	private Scale scale;

	protected void setUp() throws Exception {
		scale = new Scale(1.0, 11.0, 0, 1000);
	}
	
	public void testToScreen() throws Exception {
		assertEquals(0, scale.toScreen(1.0));
		assertEquals(50, scale.toScreen(1.5));
		assertEquals(100, scale.toScreen(2.0));
	}

	public void testToData() throws Exception {
		assertEquals(1.0, scale.toData(0));
		assertEquals(1.5, scale.toData(50));
		assertEquals(2.0, scale.toData(100));
	}
	
	public void testMany() throws Exception {
		for (int i = 0; i < 1000; ++i) {
			double x = scale.toData(i);
			assertEquals(x, scale.toData(scale.toScreen(x)), 0.02);
		}
	}

}
