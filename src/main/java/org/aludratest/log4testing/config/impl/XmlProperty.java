package org.aludratest.log4testing.config.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlProperty {

	@XmlElement
	private String key;

	@XmlElement
	private String value;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
