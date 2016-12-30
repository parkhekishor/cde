package org.cde.cdeaddress;

import java.util.List;

import org.cde.cdedomain.exceptions.CDEGenricException;
import org.cde.domain.entity.EntityAddress;

public interface AddressStandardizationService {

	void standardize(List<EntityAddress> addresses);

	void standardize(EntityAddress address) throws CDEGenricException;

}
