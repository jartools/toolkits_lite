/**
 * @author kimi
 * @dateTime 2013-4-28 下午4:09:13
 */
package com.bowlong.third.xml.jaxb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 响应MMIAP支付服务器bean
 * 
 * @author kimi
 * @dateTime 2013-4-28 下午4:09:13
 */
@XmlRootElement(name = "SyncAppOrderResp")
public class DemoXml implements Serializable {

	/**
	 * @author kimi
	 * @dateTime 2013-4-28 上午11:38:03
	 */
	private static final long serialVersionUID = 7123473855192948887L;

	private String MsgType;// 消息类型

	private String Version;// 版本号

	private Integer hRet;// 返回值

	public String getMsgType() {
		return MsgType;
	}

	@XmlElement(name = "MsgType")
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getVersion() {
		return Version;
	}

	@XmlElement(name = "Version")
	public void setVersion(String version) {
		Version = version;
	}

	public Integer gethRet() {
		return hRet;
	}

	@XmlElement(name = "hRet")
	public void sethRet(Integer hRet) {
		this.hRet = hRet;
	}

	@Override
	public String toString() {
		return "DemoXml [MsgType=" + getMsgType() + ", Version=" + getVersion()
				+ ", hRet=" + gethRet() + "]";
	}

	public static void main(String[] args) throws Exception {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><SyncAppOrderResp><MsgType>SyncAppOrderResp</MsgType><Version>1.0.0</Version><hRet>12</hRet></SyncAppOrderResp>";
		DemoXml v = JaxbReadXml.readXmlContext(DemoXml.class, xml);
		System.out.println(v.toString());
	}
}
