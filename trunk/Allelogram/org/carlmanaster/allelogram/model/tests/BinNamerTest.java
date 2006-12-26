package org.carlmanaster.allelogram.model.tests;

import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.BinNamer;

import junit.framework.TestCase;

public class BinNamerTest extends TestCase {
	
	public void testSequential() throws Exception {
		BinNamer sequential = BinNamer.sequential;
		Bin bin = new Bin(145.2, 147.2);
		assertEquals("1", sequential.getName(bin, 0));
	}
	
	public void testAlphabetic() throws Exception {
		BinNamer alphabetic = BinNamer.alphabetic;
		Bin bin = new Bin(145.2, 147.2);
		assertEquals("A", alphabetic.getName(bin, 0));
		assertEquals("Z", alphabetic.getName(bin, 25));
		assertEquals("27", alphabetic.getName(bin, 26));
	}
	
	public void testSize() throws Exception {
		BinNamer size = BinNamer.size;
		Bin bin = new Bin(145.2, 147.2);
		assertEquals("146", size.getName(bin, 0));
	}
}
