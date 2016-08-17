package org.aludratest.log4testing.engine;

import java.util.List;
import java.util.Map;

import org.aludratest.log4testing.TestStepFilter;

final class LogContext {

	private Map<String, String> abbreviations;

	private List<TestStepFilter> filters;

	public LogContext(Map<String, String> abbreviations, List<TestStepFilter> filters) {
		if (abbreviations == null) {
			throw new IllegalArgumentException("abbreviations must not be null");
		}
		if (filters == null) {
			throw new IllegalArgumentException("filters must not be null");
		}

		this.abbreviations = abbreviations;
		this.filters = filters;
	}

	public Map<String, String> getAbbreviations() {
		return abbreviations;
	}

	public List<TestStepFilter> getFilters() {
		return filters;
	}

}
