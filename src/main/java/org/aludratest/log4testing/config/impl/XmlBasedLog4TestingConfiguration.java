package org.aludratest.log4testing.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.aludratest.log4testing.config.AbbreviatorConfiguration;
import org.aludratest.log4testing.config.Log4TestingConfiguration;
import org.aludratest.log4testing.config.TestLogWriterConfiguration;
import org.aludratest.log4testing.config.TestStepFilterConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Configuration implementation which searches for a <code>log4testing.xml</code> in
 * <ul>
 * <li>manually specified locations, passed to the constructor</li>
 * <li>Context Classloader classpath's root</li>
 * <li>current classpath's root</li>
 * <li>current folder</li>
 * </ul>
 * in this order, and uses the contents of the first found file as configuration. If no file is found, only empty objects are
 * returned. <br>
 * <br>
 * As an additional feature, global properties declared in the XML file can be referenced from test writer properties, by using
 * the common <code>${myPropertyName}</code> pattern.
 * 
 * @author falbrech
 * 
 */
public final class XmlBasedLog4TestingConfiguration implements Log4TestingConfiguration {
	
	public static final String NAMESPACE = "http://aludratest.org/log4testing/1.0";

	private static final Logger LOG = LoggerFactory.getLogger(XmlBasedLog4TestingConfiguration.class);

	private static final String CONFIG_FILE_NAME = "log4testing.xml";

	private XmlConfiguration configuration;
	
	private Schema configurationSchema;

	public XmlBasedLog4TestingConfiguration(URL... additionalLocations) {
		loadSchema();
		configuration = scanForConfiguration(additionalLocations);
	}
	
	private void loadSchema() {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			configurationSchema = factory.newSchema(XmlBasedLog4TestingConfiguration.class
					.getResource("log4testingConfiguration.xsd"));
		}
		catch (SAXException e) {
			LOG.error("Could not read log4testingConfiguration.xsd schema", e);
			configurationSchema = null;
		}
	}

	private XmlConfiguration scanForConfiguration(URL... additionalLocations) {
		for (URL url : additionalLocations) {
			InputStream in = null;
			try {
				in = url.openStream();
				return readConfiguration(in);
			}
			catch (IOException e) {
				// ignore; try next location
				LOG.debug("No valid Log4Testing configuration found at " + url, e);
			}
			catch (SAXException e) {
				LOG.error("Log4Testing configuration file " + url + " is invalid", e);
			}
			catch (JAXBException e) {
				LOG.error("Log4Testing configuration file " + url + " is invalid", e);
			}
			finally {
				IOUtils.closeQuietly(in);
			}
		}

		// now the default locations
		ClassLoader[] clsToTest = new ClassLoader[] { Thread.currentThread().getContextClassLoader(),
				XmlBasedLog4TestingConfiguration.class.getClassLoader() };

		for (ClassLoader cl : clsToTest) {
			if (cl != null) {
				URL resource = cl.getResource(CONFIG_FILE_NAME);
				if (resource != null) {
					InputStream in = null;
					try {
						return readConfiguration(in = resource.openStream());
					}
					catch (IOException e) {
						LOG.debug("No valid Log4Testing configuration found at " + resource, e);
					}
					catch (SAXException e) {
						LOG.error("Log4Testing configuration file " + resource + " is invalid", e);
					}
					catch (JAXBException e) {
						LOG.error("Log4Testing configuration file " + resource + " is invalid", e);
					}
					finally {
						IOUtils.closeQuietly(in);
					}
				}
			}
		}

		// finally, current directory
		File file = new File(new File(System.getProperty("user.dir")), CONFIG_FILE_NAME);
		if (file.isFile()) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
				return readConfiguration(in);
			}
			catch (IOException e) {
				LOG.debug("No valid Log4Testing configuration found at " + file.getAbsolutePath(), e);
			}
			catch (SAXException e) {
				LOG.error("Log4Testing configuration file " + file.getAbsolutePath() + " is invalid", e);
			}
			catch (JAXBException e) {
				LOG.error("Log4Testing configuration file " + file.getAbsolutePath() + " is invalid", e);
			}
			finally {
				IOUtils.closeQuietly(in);
			}
		}

		// return empty config
		return new XmlConfiguration();
	}

	private XmlConfiguration readConfiguration(InputStream in) throws IOException, SAXException, JAXBException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(in);

			// validate document against schema
			if (configurationSchema != null) {
				configurationSchema.newValidator().validate(new DOMSource(doc));
			}

			JAXBContext context = JAXBContext.newInstance(XmlConfiguration.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (XmlConfiguration) unmarshaller.unmarshal(new DOMSource(doc.getDocumentElement()));
		}
		catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
	}

	@Override
	public AbbreviatorConfiguration getAbbreviatorConfiguration() {
		return configuration;
	}

	@Override
	public List<TestLogWriterConfiguration> getTestLogWriterConfigurations() {
		List<TestLogWriterConfiguration> result = new ArrayList<TestLogWriterConfiguration>();

		if (configuration.getWriters() != null) {
			for (XmlWriterConfiguration xwc : configuration.getWriters()) {
				result.add(new LogWriterConfigurationAdapter(xwc));
			}
		}

		return result;
	}

	@Override
	public Properties getGlobalProperties() {
		// TODO could be cached
		return toProperties(configuration.getProperties(), new Properties());
	}

	private static Properties toProperties(List<XmlProperty> xmlProperties, Properties globalProperties) {
		Properties properties = new Properties();
		if (xmlProperties == null) {
			return properties;
		}

		for (XmlProperty p : xmlProperties) {
			String key = p.getKey();
			String value = p.getValue();
			if (value != null) {
				value = StrSubstitutor.replace(value, globalProperties);
			}
			properties.setProperty(key, value);
		}

		return properties;
	}

	private class LogWriterConfigurationAdapter implements TestLogWriterConfiguration {

		private XmlWriterConfiguration config;

		public LogWriterConfigurationAdapter(XmlWriterConfiguration config) {
			this.config = config;
		}

		@Override
		public String getWriterClassName() {
			return config.getClassName();
		}

		@Override
		public Properties getWriterProperties() {
			Properties properties = new Properties();
			if (config.getProperties() == null) {
				return properties;
			}

			return toProperties(config.getProperties(), getGlobalProperties());
		}

		@Override
		public List<? extends TestStepFilterConfiguration> getTestStepFilters() {
			if (config.getTestStepFilters() == null) {
				return Collections.emptyList();
			}

			List<TestStepFilterConfiguration> result = new ArrayList<TestStepFilterConfiguration>();

			for (XmlTestStepFilterConfiguration sfc : config.getTestStepFilters()) {
				result.add(new TestStepFilterConfigurationAdapter(sfc));
			}

			return result;
		}
	}

	private class TestStepFilterConfigurationAdapter implements TestStepFilterConfiguration {

		private XmlTestStepFilterConfiguration config;

		public TestStepFilterConfigurationAdapter(XmlTestStepFilterConfiguration config) {
			this.config = config;
		}

		@Override
		public String getClassName() {
			return config.getClassName();
		}

		@Override
		public Properties getProperties() {
			return toProperties(config.getProperties(), getGlobalProperties());
		}

	}

}
