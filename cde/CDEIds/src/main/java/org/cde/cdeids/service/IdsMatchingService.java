package org.cde.cdeids.service;

import java.util.List;
import java.util.Map;

import org.cde.domain.entity.EntityId;

public interface IdsMatchingService {

	public Map<String, String> match(List<EntityId> source, List<EntityId> target);

}
