package com.abc.huoyun.model;


import java.io.Serializable;

public class Truck  implements Serializable {
	
	static public final int TRUCK_ORG_ID = 0;
	
	private int truckId;
	private int typeId;
	private int lengthId;
	private int weightId;
	private int statusId;
	public int getAuditStatusId() {
		return auditStatusId;
	}

	public void setAuditStatusId(int auditStatusId) {
		this.auditStatusId = auditStatusId;
	}
	private int auditStatusId;

	private String lincenPlateNumber;
	
	private String  photoImageUrl;
	private String licenseImageUrl;
	
	private int userId;
	
	protected int getUserId() {
		return userId;
	}

	protected void setUserId(int userId) {
		this.userId = userId;
	}

	public Truck(){
		this.truckId = TRUCK_ORG_ID;
		this.lengthId = TRUCK_ORG_ID;
		this.statusId = TRUCK_ORG_ID;
		this.weightId = TRUCK_ORG_ID;
		this.auditStatusId = TRUCK_ORG_ID;
	}
	
	public Truck(int truckId) {
		this.truckId = truckId;
	}
 	
	public int getTruckId() {
		return truckId;
	}
	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getLengthId() {
		return lengthId;
	}
	public void setLengthId(int lengthId) {
		this.lengthId = lengthId;
	}
	public int getWeightId() {
		return weightId;
	}
	public void setWeightId(int weightId) {
		this.weightId = weightId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getLincenPlateNumber() {
		return lincenPlateNumber;
	}
	public void setLincenPlateNumber(String lincenPlateNumber) {
		this.lincenPlateNumber = lincenPlateNumber;
	}
	public String getPhotoImageUrl() {
		return photoImageUrl;
	}
	public void setPhotoImageUrl(String photoImageUrl) {
		this.photoImageUrl = photoImageUrl;
	}
	public String getLicenseImageUrl() {
		return licenseImageUrl;
	}
	public void setLicenseImageUrl(String licenseImageUrl) {
		this.licenseImageUrl = licenseImageUrl;
	}
	
	
}
