package org.aludratest.log4testing.config.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "writer")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlWriterConfiguration {

	@XmlElement(name = "class")
	private String className;

	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = XmlProperty.class)
	private List<XmlProperty> properties;

	@XmlElementWrapper(name = "testStepFilters")
	@XmlElement(name = "testStepFilter", type = XmlTestStepFilterConfiguration.class)
	private List<XmlTestStepFilterConfiguration> testStepFilters;

	public String getClassName() {
		return className;
	}

	public List<XmlProperty> getProperties() {
		return properties;
	}

	public List<XmlTestStepFilterConfiguration> getTestStepFilters() {
		return testStepFilters;
	}

}
