package org.aludratest.log4testing.engine;

import java.util.Map;

import org.aludratest.log4testing.NamedTestLogElement;

abstract class FilteringNamedTestLogElement<T extends NamedTestLogElement> extends FilteringTestLogElement<T>
		implements
		NamedTestLogElement {

	protected FilteringNamedTestLogElement(T delegate, LogContext logContext) {
		super(delegate, logContext);
	}

	@Override
	public final String getName() {
		// here, perform the replacement
		String name = getDelegate().getName();
		for (Map.Entry<String, String> entry : getLogContext().getAbbreviations().entrySet()) {
			name = name.replace(entry.getKey(), entry.getValue());
		}
		return name;
	}

}
