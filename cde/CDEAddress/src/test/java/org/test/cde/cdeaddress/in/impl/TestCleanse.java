package org.test.cde.cdeaddress.in.impl;

import org.cde.cdeaddress.AddressCleansingService;
import org.cde.cdeaddress.in.impl.AddressCleansingServiceImpl;
import org.cde.cdecommons.utilities.LookUpService;
import org.cde.cdedomain.exceptions.CDEGenricException;

import junit.framework.TestCase;

public class TestCleanse extends TestCase {
	public void testCleanseAddress() {
		LookUpService.storeCleansingTokens("/in/impl/conf/CleansingTokensNew.txt");
		String address = "BILL UNIT IIND A SHOP N- KE PASS FRONT OF BEHIND NR P L NO STREET EXTN A1STB A-NOB AC\\OB";
		AddressCleansingService cleanseService = new AddressCleansingServiceImpl();

		try {
			System.out.println(address);
			String cleanse = cleanseService.cleanse(address);
			System.out.println(cleanse);
		} catch (CDEGenricException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
