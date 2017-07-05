package com.bowlong.third.xml.province.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * 区县
 * @author Canyon
 * @version createtime：2015年4月29日 上午00:10:41
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDistrict implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "ID")
	private int id;
	@XmlAttribute(name = "DistrictName")
	private String districtName;
	@XmlAttribute(name = "CID")
	private int cid;
	@XmlValue
	private String district;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return "XmlDistrict [id=" + id + ", districtName=" + districtName
				+ ", cid=" + cid + ", district=" + district + "]";
	}

}
