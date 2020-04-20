package com.bowlong.third.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.bowlong.lang.NumEx;

@SuppressWarnings("unchecked")
public class XMLEx {

	public static final Document createDocument() {
		Document doc = DocumentHelper.createDocument();
		return doc;
	}

	public static final Element createRootElement(String name) {
		Element e = DocumentHelper.createElement(name);
		return e;
	}

	public static final Element createRootElement(Document doc, String name) {
		Element e = createRootElement(name);
		doc.setRootElement(e);
		return e;
	}

	public static final Attribute createAttribute(Element e, String name, String value) {
		Attribute a = DocumentHelper.createAttribute(e, name, value);
		return a;
	}

	public static final Document parse(String xml) throws DocumentException {
		return parse(new StringReader(xml));
	}

	public static final Document parse(Reader reader) throws DocumentException {
		SAXReader sr = new SAXReader();
		Document doc = sr.read(reader);
		return doc;
	}

	public static final Document parse(File f) throws DocumentException {
		SAXReader sr = new SAXReader();
		Document doc = sr.read(f);
		return doc;
	}

	public static final Document parse(InputStream in) throws DocumentException {
		SAXReader sr = new SAXReader();
		Document doc = sr.read(in);
		return doc;
	}

	public static final Document parse4Path(String path) throws DocumentException {
		return parse(new File(path));
	}

	public static final Element getRoot(String xml) throws DocumentException {
		Document doc = parse(xml);
		return getRoot(doc);
	}

	public static final Element getRoot(File f) throws DocumentException {
		Document doc = parse(f);
		return getRoot(doc);
	}

	public static final Element getRoot(Reader reader) throws DocumentException {
		Document doc = parse(reader);
		return getRoot(doc);
	}

	public static final Element getRoot(Document doc) {
		return doc.getRootElement();
	}

	public static final Element getRoot4Path(String path) throws DocumentException {
		Document doc = parse4Path(path);
		return getRoot(doc);
	}

	public static final Element getRoot(Element e,String name) {
		return e.element(name);
	}

	public static final Element element(Element e,String name) {
		Element e1 = e.element(name);
		return e1;
	}

	public static final List<Element> elements(Element e) {
		List<Element> e1 = ((List<Element>) e.elements());
		return e1;
	}

	public static final List<Element> elements(Element e,String name) {
		List<Element> e1 = ((List<Element>) e.elements(name));
		return e1;
	}

	public static final Element addElement(Element e,String name) {
		return e.addElement(name);
	}

	public static final Element setText(Element e,String text) {
		e.setText(text);
		return e;
	}

	public static final Element setBool(Element e,boolean value) {
		return setText(e, String.valueOf(value));
	}

	public static final Element setByte(Element e,byte value) {
		return setText(e, String.valueOf(value));
	}

	public static final Element setShort(Element e,short value) {
		return setText(e, String.valueOf(value));
	}

	public static final Element setInt(Element e,int value) {
		return setText(e, String.valueOf(value));
	}

	public static final Element setLong(Element e,long value) {
		return setText(e, String.valueOf(value));
	}

	public static final Element setFloat(Element e,float value) {
		return setText(e, String.valueOf(value));
	}

	public static final Element setDouble(Element e,double value) {
		return setText(e, String.valueOf(value));
	}

	public static final Attribute setText(Attribute a,String value) {
		a.setValue(value);
		return a;
	}

	public static final Attribute setBool(Attribute a,boolean value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Attribute setByte(Attribute a,byte value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Attribute setShort(Attribute a,short value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Attribute setInt(Attribute a,int value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Attribute setLong(Attribute a,long value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Attribute setFloat(Attribute a,float value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Attribute setDouble(Attribute a,double value) {
		a.setValue(String.valueOf(value));
		return a;
	}

	public static final Element addAttribute(Element e,String name,String value) {
		return e.addAttribute(name, value);
	}

	public static final Element addAttribute(Element e,String name,boolean value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static final Element addAttribute(Element e,String name,byte value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static Element addAttribute(Element e,String name,short value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static final Element addAttribute(Element e,String name,int value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static final Element addAttribute(Element e,String name,long value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static final Element addAttribute(Element e,String name,float value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static final Element addAttribute(Element e,String name,double value) {
		return addAttribute(e, name, String.valueOf(value));
	}

	public static final Attribute attribute(Element e,String name) {
		return e.attribute(name);
	}

	public static final String getText(Element e) {
		if (e == null)
			return "";
		return e.getText();
	}

	public static final String getTextTrim(Element e) {
		if (e == null)
			return "";
		return e.getTextTrim();
	}

	public static final boolean getBool(Element e) {
		return NumEx.stringToBool(getTextTrim(e));
	}

	public static final byte getByte(Element e) {
		return NumEx.stringToByte(getTextTrim(e));
	}

	public static final short getShort(Element e) {
		return NumEx.stringToShort(getTextTrim(e));
	}

	public static final int getInt(Element e) {
		return NumEx.stringToInt(getTextTrim(e));
	}

	public static final long getLong(Element e) {
		return NumEx.stringToLong(getTextTrim(e));
	}

	public static final float getFloat(Element e) {
		return NumEx.stringToFloat(getTextTrim(e));
	}

	public static final double getDouble(Element e) {
		return NumEx.stringToDouble(getTextTrim(e));
	}

	public static final String getText(Attribute a) {
		if (a == null)
			return "";
		return a.getText();
	}

	public static final String getTextTrim(Attribute a) {
		if (a == null)
			return "";
		return a.getText().trim();
	}

	public static final boolean getBool(Attribute e) {
		return NumEx.stringToBool(getTextTrim(e));
	}

	public static final byte getByte(Attribute e) {
		return NumEx.stringToByte(getTextTrim(e));
	}

	public static final short getShort(Attribute e) {
		return NumEx.stringToShort(getTextTrim(e));
	}

	public static final int getInt(Attribute e) {
		return NumEx.stringToInt(getTextTrim(e));
	}

	public static final long getLong(Attribute e) {
		return NumEx.stringToLong(getTextTrim(e));
	}

	public static final float getFloat(Attribute e) {
		return NumEx.stringToFloat(getTextTrim(e));
	}

	public static final double getDouble(Attribute e) {
		return NumEx.stringToDouble(getTextTrim(e));
	}

	public static final void writeTo(Document doc, OutputStream out) throws IOException {
		writePrettyTo(doc, out);
	}

	public static final void writePrettyTo(Document doc, OutputStream out) throws IOException {
		// 美化格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter wr = new XMLWriter(out, format);
		wr.write(doc);
	}

	public static final void writeCompactTo(Document doc, OutputStream out) throws IOException {
		// 缩减格式
		OutputFormat format = OutputFormat.createCompactFormat();
		format.setEncoding("UTF-8");
		XMLWriter wr = new XMLWriter(out, format);
		wr.write(doc);
	}

	public static final String XML_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public static void main(String[] args) throws DocumentException, IOException {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><hRet>0</hRet><status>1800</status><transIDO>12345678901234567</transIDO><versionId>100</versionId><userId>12345678</userId><cpServiceId>120123002000</cpServiceId><consumeCode>120123002001</consumeCode><cpParam>0000000000000000</cpParam></request>";
		Document doc = parse(str);
		writePrettyTo(doc, System.out);
		// System.out.println(doc.asXML());
	}

}
