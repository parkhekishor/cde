package org.cde.cdename.service.in.impl;

import java.util.List;

import org.cde.cdename.service.NameStandardizationService;
import org.cde.domain.entity.EntityName;
import org.cde.domain.entity.EntityNameRaw;

public class NameStandardizationServiceImpl implements  NameStandardizationService{
	public void standardize(List<EntityName> names) {
		for (EntityName entityName : names)
		{
			
		}
	}

	public void standardize(EntityName name) {
		EntityNameRaw nameRaw = name.getNameRaw();
		
		
	}
}
