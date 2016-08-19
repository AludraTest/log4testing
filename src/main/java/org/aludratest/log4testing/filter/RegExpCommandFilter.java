package org.aludratest.log4testing.filter;

import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.aludratest.log4testing.TestStepFilter;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;

/**
 * A filter checking the <code>command</code> attribute of test steps to match a given regular expression.
 * 
 * @author falbrech
 *
 */
public class RegExpCommandFilter implements TestStepFilter {

	private String commandRegexp;

	private Pattern compiledCommandRegexp;

	@Override
	public void init(Properties properties) throws InvalidConfigurationException {
		commandRegexp = properties.getProperty("commandRegexp");
		// null or empty is okay - allow everything. But otherwise, RegExp must be valid
		if (commandRegexp != null && commandRegexp.trim().length() > 0) {
			try {
				compiledCommandRegexp = Pattern.compile(commandRegexp);
			}
			catch (PatternSyntaxException e) {
				throw new InvalidConfigurationException("Invalid RegExp for commandRegexp setting", e);
			}
		}
	}

	@Override
	public boolean filter(TestStepLog testStep) {
		if (compiledCommandRegexp != null) {
			return testStep.getCommand() != null && compiledCommandRegexp.matcher(testStep.getCommand()).matches();
		}

		return true;
	}

}
