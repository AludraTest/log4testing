package org.aludratest.log4testing.config.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.aludratest.log4testing.engine.MockTestLogWriter;
import org.junit.Test;

/**
 * Tests the XmlBasedLog4TestingConfiguration. This includes checking for reading in default locations, specifying alternative
 * URLs (including possibly invalid ones), and handling invalid formats.
 * 
 * @author falbrech
 * 
 */
public class XmlBasedLog4TestingConfigurationTest {

	@Test
	public void testDefaultRead() {
		// should read src/test/java/log4testing.xml
		XmlBasedLog4TestingConfiguration configuration = new XmlBasedLog4TestingConfiguration();

		assertFalse(configuration.getGlobalProperties().isEmpty());
		assertFalse(configuration.getAbbreviatorConfiguration().getAbbreviations().isEmpty());
		assertFalse(configuration.getTestLogWriterConfigurations().isEmpty());

		assertEquals(MockTestLogWriter.class.getName(), configuration.getTestLogWriterConfigurations().get(0)
				.getWriterClassName());
	}

	@Test
	public void testDirectRead() {
		URL resource = getClass().getClassLoader().getResource("log4testing_empty.xml");

		XmlBasedLog4TestingConfiguration configuration = new XmlBasedLog4TestingConfiguration(resource);

		assertNotNull(configuration.getGlobalProperties());
		assertTrue(configuration.getGlobalProperties().isEmpty());
	}

	@Test
	public void testSkipInvalidURLs() throws MalformedURLException {
		URL invalid = new File("/jsdfd32947328sdnsd").toURI().toURL();
		URL resource = getClass().getClassLoader().getResource("log4testing_empty.xml");

		// invalid URL must not throw any exception (but could be logged)
		XmlBasedLog4TestingConfiguration configuration = new XmlBasedLog4TestingConfiguration(invalid, resource);

		assertNotNull(configuration.getGlobalProperties());
		assertTrue(configuration.getGlobalProperties().isEmpty());
	}

	@Test
	public void testInvalidConfigurationFallbackToDefault() {
		URL resource = getClass().getClassLoader().getResource("log4testing_invalid.xml");

		// invalid file must not throw any exception (but could be logged); should skip file and use default one.
		XmlBasedLog4TestingConfiguration configuration = new XmlBasedLog4TestingConfiguration(resource);

		assertNotNull(configuration.getGlobalProperties());
		assertFalse(configuration.getGlobalProperties().isEmpty());
		assertEquals(MockTestLogWriter.class.getName(), configuration.getTestLogWriterConfigurations().get(0)
				.getWriterClassName());
	}

}
