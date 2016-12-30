package org.cde.cdeaddress.in.impl;

import java.util.List;

import org.cde.cdeaddress.AddressCleansingService;
import org.cde.cdeaddress.AddressStandardizationService;
import org.cde.cdedomain.exceptions.CDEGenricException;
import org.cde.domain.entity.EntityAddress;
import org.cde.domain.entity.EntityAddressRaw;

public class AddressStandardizationServiceImpl implements AddressStandardizationService {
	AddressCleansingServiceImpl addressCleansingService = new AddressCleansingServiceImpl();

	public void standardize(List<EntityAddress> addresses) {
		for (EntityAddress entityAddress : addresses) {

		}
	}

	public void standardize(EntityAddress address) throws CDEGenricException {

		AddressCleansingService cleasningService = new AddressCleansingServiceImpl();
		EntityAddressRaw addressRaw = address.getAddressRaw();
		String line1 = cleasningService.cleanse(addressRaw.getLine1());
		String line2 = cleasningService.cleanse(addressRaw.getLine2());
		String line3 = cleasningService.cleanse(addressRaw.getLine3());
		String line4 = cleasningService.cleanse(addressRaw.getLine4());
		String line5 = cleasningService.cleanse(addressRaw.getLine5());
		String state = cleasningService.cleanse(addressRaw.getState());
		String city = cleasningService.cleanse(addressRaw.getCity());
		String zip = cleasningService.cleanse(addressRaw.getZip());

	}
}
