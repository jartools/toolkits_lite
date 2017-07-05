package com.bowlong.third.xml.province.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bowlong.io.FileRw;
import com.bowlong.lang.StrEx;
import com.bowlong.third.xml.jaxb.JaxbReadXml;

@XmlRootElement(name = "Provinces")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlProvinces implements Serializable {

	private static final long serialVersionUID = 1L;
	static final String filePath = "com/bowlong/third/xml/province/xmls/Provinces.xml";

	@XmlElement(name = "Province")
	private List<XmlProvince> list;

	public List<XmlProvince> getList() {
		return list;
	}

	public void setList(List<XmlProvince> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("XmlProvinces [list=")
				.append("\n");
		if (list != null) {
			for (XmlProvince item : list) {
				buff.append(item.toString()).append(",").append("\n");
			}
		}
		buff.append("]");
		return buff.toString();
	}

	/*** 工程模式下面编译取得路径 **/
	public String getPath4Xml() {
		String pkg = this.getClass().getPackage().getName();
		String path = this.getClass().getClassLoader().getResource("")
				.getPath();
		pkg = StrEx.package2Path(pkg);
		pkg = pkg.replace("entity", "xmls");
		return path + pkg + "/Provinces.xml";
	}

	/*** 工程模式下面编译取得路径 **/
	static public String getPath4StaticXml() throws Exception {
		String path = "bin/" + filePath;
		File f = FileRw.openFile(path);
		return f.getCanonicalPath();
	}

	public String getXmls() throws Exception {
		String path = getPath4Xml();
		return FileRw.readText(path);
	}

	/*** 工程模式下面读取文件 **/
	static public XmlProvinces getEnXmlProvinces(String path) throws Exception {
		if (StrEx.isEmptyTrim(path)) {
			path = getPath4StaticXml();
		}
		return JaxbReadXml.readXmlPath(XmlProvinces.class, path);
	}

	public static void main(String[] args) throws Exception {
		// XmlProvinces xmlEn = new XmlProvinces();
		// System.out.println(xmlEn.getPath4Xml());
		// System.out.println(xmlEn.toString());
		XmlProvinces env = getEnXmlProvinces("");
		System.out.println(env.toString());
	}

	static public XmlProvinces xmlEnCache;

	/*** jar包下面编译取得文件流 **/
	static public InputStream getIns4StaticJar() throws Exception {
		return XmlProvinces.class.getClassLoader()
				.getResourceAsStream(filePath);
	}

	/*** jar包下面读取文件 **/
	static public XmlProvinces getEn4Jar(String path) throws Exception {
		if (StrEx.isEmptyTrim(path)) {
			InputStream ins = getIns4StaticJar();
			return JaxbReadXml.readConfigFromStream(XmlProvinces.class, ins);
		}
		return JaxbReadXml.readXmlPath(XmlProvinces.class, path);
	}

	/*** jar包下面读取文件 cache缓存对象 **/
	static public XmlProvinces getEn4JarCache(String path) {
		if (xmlEnCache == null) {
			try {
				xmlEnCache = getEn4Jar(path);
			} catch (Exception e) {
			}
		}
		return xmlEnCache;
	}
}
