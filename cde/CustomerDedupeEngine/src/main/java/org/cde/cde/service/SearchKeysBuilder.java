package org.cde.cde.service;

import java.util.List;

import org.cde.domain.entity.Entity;

public class SearchKeysBuilder {
	public static void  buildKeys(List<Entity> entities) {
		for (Entity entity : entities)
		{
			buildKeys(entity);
		}
	}
	
	public static void  buildKeys(Entity entity) {
		
	}

}
