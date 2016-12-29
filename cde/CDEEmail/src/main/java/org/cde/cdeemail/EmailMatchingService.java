package org.cde.cdeemail;

import java.util.List;
import java.util.Map;

import org.cde.domain.entity.EntityEmail;

public interface EmailMatchingService {

	public Map<String, String> match(List<EntityEmail> source, List<EntityEmail> target);

}
