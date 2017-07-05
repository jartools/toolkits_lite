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

@XmlRootElement(name = "Cities")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCities implements Serializable {

	private static final long serialVersionUID = 1L;

	static final String filePath = "com/bowlong/third/xml/province/xmls/Cities.xml";

	@XmlElement(name = "City")
	private List<XmlCity> list;

	public List<XmlCity> getList() {
		return list;
	}

	public void setList(List<XmlCity> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer("XmlCities [list=").append("\n");
		if (list != null) {
			for (XmlCity item : list) {
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
		return path + pkg + "/Cities.xml";
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
	static public XmlCities getEnXmlCities(String path) throws Exception {
		if (StrEx.isEmptyTrim(path)) {
			path = getPath4StaticXml();
		}
		return JaxbReadXml.readXmlPath(XmlCities.class, path);
	}

	public static void main(String[] args) throws Exception {
		// XmlCities xmlEn = new XmlCities();
		// xmlEn = JaxbReadXml.readXmlPath(XmlCities.class,
		// xmlEn.getPath4Xml());
		// System.out.println(xmlEn.toString());
		XmlCities env = getEnXmlCities("");
		System.out.println(env.toString());
	}

	static public XmlCities xmlEnCache;

	/*** jar包下面编译取得文件流 **/
	static public InputStream getIns4StaticJar() throws Exception {
		return XmlCities.class.getClassLoader().getResourceAsStream(filePath);
	}

	/*** jar包下面读取文件 **/
	static public XmlCities getEn4Jar(String path) throws Exception {
		if (StrEx.isEmptyTrim(path)) {
			InputStream ins = getIns4StaticJar();
			return JaxbReadXml.readConfigFromStream(XmlCities.class, ins);
		}
		return JaxbReadXml.readXmlPath(XmlCities.class, path);
	}

	/*** jar包下面读取文件 cache缓存对象 **/
	static public XmlCities getEn4JarCache(String path) {
		if (xmlEnCache == null) {
			try {
				xmlEnCache = getEn4Jar(path);
			} catch (Exception e) {
			}
		}
		return xmlEnCache;
	}

	/*** jar包下面读取文件list值 **/
	static public List<XmlCity> getList4Jar(String path, int pid) {
		List<XmlCity> result = new ArrayList<XmlCity>();
		try {
			XmlCities xml = getEn4JarCache(path);
			if (xml != null && !ListEx.isEmpty(xml.list)) {
				int len = xml.list.size();
				for (int i = 0; i < len; i++) {
					XmlCity tmp = xml.list.get(i);
					if (tmp == null || tmp.getPid() != pid)
						continue;
					result.add(tmp);
				}
			}
		} catch (Exception e) {
		}
		return result;
	}
}
