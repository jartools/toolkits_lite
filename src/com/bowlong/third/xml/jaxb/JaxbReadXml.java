package com.bowlong.third.xml.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.bowlong.io.FileRw;

/**
 * 可以根据xml文件读取成相应的类 <br/>
 * 根据相应的类生成xml文件
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings({ "unchecked" })
public class JaxbReadXml {

	// ////////////////// xml 转为对象 //////////////////

	static public final <T> T readXmlPath(Class<T> clazz, String path) throws JAXBException {
		return readXmlFile(clazz, new File(path));
	}

	static public final <T> T readXmlFile(Class<T> clazz, File file) throws JAXBException {
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			return (T) u.unmarshal(file);
		} catch (JAXBException e) {
			throw e;
		}
	}

	static public final <T> T readXmlContext(Class<T> clazz, String context) throws JAXBException {
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			return (T) u.unmarshal(new StringReader(context));
		} catch (JAXBException e) {
			throw e;
		}
	}

	static public final <T> T readConfig(Class<T> clazz, String config, Object... arguments)
			throws IOException, JAXBException {
		InputStream is = null;
		try {
			if (arguments.length > 0) {
				config = MessageFormat.format(config, arguments);
			}
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			is = new FileInputStream(config);
			return (T) u.unmarshal(is);
		} catch (IOException e) {
			throw e;
		} catch (JAXBException e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	static public final <T> T readConfigFromStream(Class<T> clazz, InputStream dataStream) throws JAXBException {
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			return (T) u.unmarshal(dataStream);
		} catch (JAXBException e) {
			throw e;
		}
	}

	////////////////// 对象 转为xml //////////////////

	static public final <T> void writeObj(T obj, String xmlPath) throws JAXBException {
		File f = FileRw.getFile(xmlPath);
		writeObj(obj, f);
	}

	static public final <T> void writeObj(T obj, File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = jc.createMarshaller();
		marshaller.marshal(obj, file);
	}

	static public final <T> byte[] getBytes(T obj, String xmlPath) throws JAXBException {
		File f = FileRw.getFile(xmlPath);
		writeObj(obj, f);
		byte[] bes = FileRw.readBytes(f);
		if (bes == null)
			bes = new byte[0];
		return bes;
	}

	static public final <T> String getString(T obj, String xmlPath) throws JAXBException {
		byte[] bes = getBytes(obj, xmlPath);
		return new String(bes);
	}
}