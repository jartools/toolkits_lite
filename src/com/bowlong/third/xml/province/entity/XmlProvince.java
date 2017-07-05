package com.bowlong.third.xml.province.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
/**
 * 省份
 * @author Canyon
 * @version createtime：2015年4月29日 上午00:10:41
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlProvince implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "ID")
	private int id;
	@XmlAttribute(name = "ProvinceName")
	private String provinceName;
	@XmlValue
	private String province;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "XmlProvince [id=" + id + ", provinceName=" + provinceName
				+ ", province=" + province + "]";
	}
}
