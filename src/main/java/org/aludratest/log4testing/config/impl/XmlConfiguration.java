package org.aludratest.log4testing.config.impl;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.aludratest.log4testing.config.AbbreviatorConfiguration;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlConfiguration implements AbbreviatorConfiguration {

	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = XmlProperty.class)
	private List<XmlProperty> properties;

	@XmlElementWrapper(name = "abbreviations")
	@XmlElement(name = "abbreviation", type = XmlAbbreviatorConfiguration.class)
	private List<XmlAbbreviatorConfiguration> abbreviations;

	@XmlElementWrapper(name = "writers")
	@XmlElement(name = "writer", type = XmlWriterConfiguration.class)
	private List<XmlWriterConfiguration> writers;

	@Override
	public Map<String, String> getAbbreviations() {
		return new AbbreviationsMap();
	}

	public List<XmlProperty> getProperties() {
		return properties;
	}

	public List<XmlWriterConfiguration> getWriters() {
		return writers;
	}

	private class AbbreviationsMap extends AbstractMap<String, String> {
		@Override
		public Set<Entry<String, String>> entrySet() {
			return Collections.unmodifiableSet(new HashSet<Entry<String, String>>(abbreviations));
		}

		@Override
		public String get(Object key) {
			if (key == null) {
				return null;
			}

			for (XmlAbbreviatorConfiguration config : abbreviations) {
				if (key.equals(config.getText())) {
					return config.getReplacement();
				}
			}

			return null;
		}
	}

}
