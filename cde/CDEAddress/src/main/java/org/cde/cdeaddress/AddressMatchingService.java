package org.cde.cdeaddress;

import java.util.List;
import java.util.Map;

import org.cde.domain.entity.EntityAddress;

public interface AddressMatchingService {

	Map<String, String> match(List<EntityAddress> source, List<EntityAddress> target);

}
