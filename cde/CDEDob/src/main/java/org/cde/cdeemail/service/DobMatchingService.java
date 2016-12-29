package org.cde.cdeemail.service;

import java.util.Map;

import org.cde.domain.entity.EntityDob;

public interface DobMatchingService {

	public Map<String, String> match(EntityDob source, EntityDob target);

}
