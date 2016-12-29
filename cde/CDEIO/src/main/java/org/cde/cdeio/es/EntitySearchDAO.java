package org.cde.cdeio.es;

import java.util.List;

import org.cde.domain.entity.Entity;

public class EntitySearchDAO {
	public void indexEntities(List<Entity> entities) {
		for (Entity entity : entities) {
			indexEntity(entity);
		}
	}

	public void indexEntity(Entity entity) {

	}
}
