package org.cde.cdeids.service;

import java.util.List;

import org.cde.domain.entity.EntityId;

public interface IdsStandardizationService {

	void standardize(List<EntityId> ids);

}
