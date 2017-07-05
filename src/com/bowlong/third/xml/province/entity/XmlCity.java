package com.bowlong.third.xml.province.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * 城市
 * @author Canyon
 * @version createtime：2015年4月29日 上午00:10:41
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCity implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "ID")
	private int id;
	@XmlAttribute(name = "CityName")
	private String cityName;
	@XmlAttribute(name = "PID")
	private int pid;
	@XmlAttribute(name = "ZipCode")
	private String zipCode;
	@XmlValue
	private String city;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "XmlCity [id=" + id + ", cityName=" + cityName + ", pid=" + pid
				+ ", zipCode=" + zipCode + ", city=" + city + "]";
	}
}
