package org.cde.cdename.service;

import java.util.List;

import org.cde.domain.entity.EntityName;

public interface NameStandardizationService {

	public void standardize(EntityName name);

	public void standardize(List<EntityName> names);

}
