package org.cde.cdephone.service;

import java.util.List;
import java.util.Map;

import org.cde.domain.entity.EntityPhone;

public interface PhoneMatchingService {

	Map<String, String> match(List<EntityPhone> source, List<EntityPhone> target);

}
