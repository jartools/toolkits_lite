package com.bowlong.third.xml.province.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bowlong.io.FileRw;
import com.bowlong.lang.StrEx;
import com.bowlong.third.xml.jaxb.JaxbReadXml;
import com.bowlong.util.ListEx;

@XmlRootElement(name = "Districts")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDistricts implements Serializable {

	private static final long serialVersionUID = 1L;

	static final String filePath = "com/bowlong/third/xml/province/xmls/Districts.xml";

	@XmlElement(name = "District")
	private List<XmlDistrict> list;

	public List<XmlDistrict> getList() {
		return list;
	}

	public void setList(List<XmlDistrict> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("XmlDistricts [list=")
				.append("\n");
		if (list != null) {
			for (XmlDistrict item : list) {
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
		return path + pkg + "/Districts.xml";
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
	static public XmlDistricts getEnXmlDistricts(String path) throws Exception {
		if (StrEx.isEmptyTrim(path)) {
			path = getPath4StaticXml();
		}
		return JaxbReadXml.readXmlPath(XmlDistricts.class, path);
	}

	public static void main(String[] args) throws Exception {
		// XmlDistricts xmlEn = new XmlDistricts();
		// xmlEn = JaxbReadXml.readXmlPath(XmlDistricts.class,
		// xmlEn.getPath4Xml());
		// System.out.println(xmlEn.toString());
		XmlDistricts env = getEnXmlDistricts("");
		System.out.println(env.toString());
	}

	static public XmlDistricts xmlEnCache;

	/*** jar包下面编译取得文件流 **/
	static public InputStream getIns4StaticJar() throws Exception {
		return XmlDistricts.class.getClassLoader()
				.getResourceAsStream(filePath);
	}

	/*** jar包下面读取文件 **/
	static public XmlDistricts getEn4Jar(String path) throws Exception {
		if (StrEx.isEmptyTrim(path)) {
			InputStream ins = getIns4StaticJar();
			return JaxbReadXml.readConfigFromStream(XmlDistricts.class, ins);
		}
		return JaxbReadXml.readXmlPath(XmlDistricts.class, path);
	}

	/*** jar包下面读取文件 cache缓存对象 **/
	static public XmlDistricts getEn4JarCache(String path) {
		if (xmlEnCache == null) {
			try {
				xmlEnCache = getEn4Jar(path);
			} catch (Exception e) {
			}
		}
		return xmlEnCache;
	}

	/*** jar包下面读取文件list值 **/
	static public List<XmlDistrict> getList4Jar(String path, int citid) {
		List<XmlDistrict> result = new ArrayList<XmlDistrict>();
		try {
			XmlDistricts xml = getEn4JarCache(path);
			if (xml != null && !ListEx.isEmpty(xml.list)) {
				int len = xml.list.size();
				for (int i = 0; i < len; i++) {
					XmlDistrict tmp = xml.list.get(i);
					if (tmp == null || tmp.getCid() != citid)
						continue;
					result.add(tmp);
				}
			}
		} catch (Exception e) {
		}
		return result;
	}
}
