package org.cde.domain.entity;

import java.util.List;
import java.util.Map;

public class Entity {

	private Long entityId_;
	private Long batchId_;
	private String sourceId_;
	private String  sourceType_;

	private Map<String, String> metadata_;
	private List<EntityName> names_;
	private List<Entity> relations_;
	private List<EntityAddress> addresses_;
	private List<EntityPhone> phones_;
	private List<EntityId> ids_;
	private List<EntityEmail> emails_;
	private EntityDob dob_;
	
	
	private Map<String, String> searchKeys_;
	private Map<String,String> matchResult_;
	
	
	
	public Map<String, String> getMatchResult() {
		return matchResult_;
	}
	public void setMatchResult(Map<String, String> matchResult) {
		matchResult_ = matchResult;
	}
	public String getSourceId() {
		return sourceId_;
	}
	public void setSourceId(String sourceId) {
		sourceId_ = sourceId;
	}
	public String getSourceType() {
		return sourceType_;
	}
	public void setSourceType(String sourceType) {
		sourceType_ = sourceType;
	}
	public EntityDob getDob() {
		return dob_;
	}
	public void setDob(EntityDob dob) {
		dob_ = dob;
	}
	public Long getEntityId() {
		return entityId_;
	}
	public void setEntityId(Long entityId) {
		entityId_ = entityId;
	}
	public Long getBatchId() {
		return batchId_;
	}
	public void setBatchId(Long batchId) {
		batchId_ = batchId;
	}
	public Map<String, String> getMetadata() {
		return metadata_;
	}
	public void setMetadata(Map<String, String> metadata) {
		metadata_ = metadata;
	}
	public List<EntityName> getNames() {
		return names_;
	}
	public void setNames(List<EntityName> names) {
		names_ = names;
	}
	public List<Entity> getRelations() {
		return relations_;
	}
	public void setRelations(List<Entity> relations) {
		relations_ = relations;
	}
	public List<EntityAddress> getAddresses() {
		return addresses_;
	}
	public void setAddresses(List<EntityAddress> addresses) {
		addresses_ = addresses;
	}
	
	
	public List<EntityPhone> getPhones() {
		return phones_;
	}
	public void setPhones(List<EntityPhone> phones) {
		phones_ = phones;
	}
	public List<EntityId> getIds() {
		return ids_;
	}
	public void setIds(List<EntityId> ids) {
		ids_ = ids;
	}
	public List<EntityEmail> getEmails() {
		return emails_;
	}
	public void setEmails(List<EntityEmail> emails) {
		emails_ = emails;
	}
	public Map<String, String> getSearchKeys() {
		return searchKeys_;
	}
	public void setSearchKeys(Map<String, String> searchKeys) {
		searchKeys_ = searchKeys;
	}

	
	
	
}
