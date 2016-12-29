package org.cde.domain.entity;

import java.util.List;

public class EntityAddressStd {
	private String aptNo_;
	private String buildingNo_;
	private String streetName_;
	private List<EntityAddressStreet> streets_;
	private String state_;
	private String city_;
	private String zip_;
	private String addressType_;
	
	
	public String getAptNo() {
		return aptNo_;
	}
	public void setAptNo(String aptNo) {
		aptNo_ = aptNo;
	}
	public String getBuildingNo() {
		return buildingNo_;
	}
	public void setBuildingNo(String buildingNo) {
		buildingNo_ = buildingNo;
	}
	public String getStreetName() {
		return streetName_;
	}
	public void setStreetName(String streetName) {
		streetName_ = streetName;
	}
	public List<EntityAddressStreet> getStreets() {
		return streets_;
	}
	public void setStreets(List<EntityAddressStreet> streets) {
		streets_ = streets;
	}
	public String getState() {
		return state_;
	}
	public void setState(String state) {
		state_ = state;
	}
	public String getCity() {
		return city_;
	}
	public void setCity(String city) {
		city_ = city;
	}
	public String getZip() {
		return zip_;
	}
	public void setZip(String zip) {
		zip_ = zip;
	}
	public String getAddressType() {
		return addressType_;
	}
	public void setAddressType(String addressType) {
		addressType_ = addressType;
	}
	
	

}
