package org.aludratest.log4testing.config;

import java.util.HashMap;
import java.util.Map;

public class MockAbbreviatorConfiguration implements AbbreviatorConfiguration {

	private Map<String, String> abbreviations = new HashMap<String, String>();

	@Override
	public Map<String, String> getAbbreviations() {
		return abbreviations;
	}

}
