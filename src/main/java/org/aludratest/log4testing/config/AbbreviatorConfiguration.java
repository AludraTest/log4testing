package org.aludratest.log4testing.config;

import java.util.Map;

/**
 * Interface for the abbreviations configuration of a Log4Testing configuration. Abbreviations are completely defined by a Map of
 * String -> Replacement pairs.
 * 
 * @author falbrech
 *
 */
public interface AbbreviatorConfiguration {

	/**
	 * Abbreviations to use - a Map which keys are replaced - if found in several locations - with the according value.
	 * 
	 * @return Abbreviations map, maybe empty, but never <code>null</code>.
	 */
	Map<String, String> getAbbreviations();

}
