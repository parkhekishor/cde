package org.cde.cdeaddress;

import org.cde.cdedomain.exceptions.CDEGenricException;

public interface AddressCleansingService {

	public String cleanse(String rec) throws CDEGenricException;

}
