package org.aludratest.log4testing.config.impl;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "abbreviation")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAbbreviatorConfiguration implements Map.Entry<String, String> {

	@XmlElement
	private String text;

	@XmlElement
	private String replacement;

	public String getText() {
		return text;
	}

	public String getReplacement() {
		return replacement;
	}

	@Override
	public String getKey() {
		return text;
	}

	@Override
	public String getValue() {
		return replacement;
	}

	@Override
	public String setValue(String value) {
		throw new UnsupportedOperationException();
	}

}
