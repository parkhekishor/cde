package org.cde.cdescore.score;

import java.util.List;
import java.util.Map;

import org.cde.domain.entity.Entity;
import org.cde.domain.entity.EntityAddress;
import org.cde.domain.entity.EntityDob;
import org.cde.domain.entity.EntityEmail;
import org.cde.domain.entity.EntityId;
import org.cde.domain.entity.EntityName;
import org.cde.domain.entity.EntityPhone;

public class EntityScoreService {
	

	public List<Entity> scoreCandidate(Entity source, List<Entity> candidates) {
		for (Entity candidate : candidates) {
			Map<String, String> matchResult = match(source, candidate);
			candidate.setMatchResult(matchResult);
		}
		return null;
	}

	public Map<String, String> match(Entity source, Entity candidate) {
		List<EntityAddress> sourceAddresses = source.getAddresses();
		List<EntityEmail> sourceEmails = source.getEmails();
		List<EntityName> sourceNames = source.getNames();
		List<EntityPhone> sourcePhones = source.getPhones();
		EntityDob sourceDob = source.getDob();
		List<EntityId> sourceIds = source.getIds();

		List<EntityAddress> candidateAddresses = candidate.getAddresses();
		List<EntityEmail> candidateEmails = candidate.getEmails();
		List<EntityName> candidateNames = candidate.getNames();
		List<EntityPhone> candidatePhones = candidate.getPhones();
		EntityDob candidateDob = candidate.getDob();
		List<EntityId> candidateIds = candidate.getIds();

		
		
		return null;
	}

}
