package org.aludratest.log4testing.config;

import java.util.List;
import java.util.Properties;

/**
 * Interface for the complete Log4Testing configuration. The configuration consists of three main parts:
 * <ul>
 * <li>Global Properties</li>
 * <li>The list of Writer configurations</li>
 * <li>The abbreviations configuration</li>
 * </ul>
 * 
 * @author falbrech
 *
 */
public interface Log4TestingConfiguration {
	
	/**
	 * Returns the abbreviations configuration for this Log4Testing configuration.
	 * 
	 * @return The abbreviations configuration for this Log4Testing configuration, maybe without any abbreviations, but never
	 *         <code>null</code>.
	 */
	AbbreviatorConfiguration getAbbreviatorConfiguration();

	/**
	 * Returns the list of writer configurations for this Log4Testing configuration.
	 * 
	 * @return The list of writer configurations for this Log4Testing configuration, maybe an empty list, but never
	 *         <code>null</code>.
	 */
	List<? extends TestLogWriterConfiguration> getTestLogWriterConfigurations();

	/**
	 * Returns the global properties set in this Log4Testing configuration, which may be referenced in Writer or Filter
	 * properties.
	 * 
	 * @return The global properties set in this Log4Testing configuration, maybe an empty object, but never <code>null</code>.
	 */
	Properties getGlobalProperties();

}
